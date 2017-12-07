package org.west.converters;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.hibernate.criterion.Restrictions;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.west.entidades.Imovel;
import org.west.entidades.ImovelCriteria;
import org.west.exception.ExportException;
import org.west.exception.GeracaoException;

/**
 * Classe que controla o exporte para o 123i. Todo imóvel na base de dados pode
 * ser anunciado em diversos meios. Um deles é o 123i, que possui um sistema
 * automatizado de inserção e modificação de anuncios, através de um Web
 * Service. Esta classe tem por finalidade varrer a base de dados, converte-los
 * para o formato adequado e gerar o arquivo XML adequado para então envia-lo
 * via Web Service.
 *
 * @author WestGuerra Ltda.
 */
public final class I123Control extends ControlExport {

    /**
     * Objeto do tipo {@link Document} que conterá o nó inicial do XML.
     */
    private Document document;
    /**
     * Arquivo que receberá o contéudo do {@link #document}.
     */
    private File arquivoXML;
    /**
     * Contador para controle do exporte.
     */
    private Integer count = 0;

    /**
     * Construtor padrão. Este contrutor cria a lista de imóveis que será
     * enviada ao 123i, através do método {@link #getListImoveis()}. Inicializa
     * o {@link #document} para receber os demais nós.
     */
    public I123Control() {
        this.listaImovel = getListImoveis();

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            document = builder.newDocument();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Transforma cada imóvel no formato XML e adicionado ao {@link #document}.
     */
    @Override
    public void gerarExport() throws GeracaoException {
        fireExportInit("Inicializando...", count++);

        arquivoXML = new File("export123i.xml");
        I123Converter converter = new I123Converter(document);

        Element ini = getHeader();

        document.appendChild(ini);

        Element header = (Element) ini.getLastChild();

        for (Imovel imovel : listaImovel) {
            header.appendChild((Element) converter.toFormato(imovel));
            fireExporMove("Convertendo Imovel " + imovel.getReferencia() + "...", count++);
        }

        try {
            DOMSource domSource = new DOMSource(document);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            StringWriter sw = new StringWriter();
            StreamResult sr = new StreamResult(sw);
            transformer.transform(domSource, sr);

            OutputStream OS = (OutputStream) new FileOutputStream(arquivoXML);
            OutputStreamWriter OSW = new OutputStreamWriter(OS, "UTF-8");
            PrintWriter Print = new PrintWriter(OSW);
            Print.println(sw.toString());
            Print.close();
            OSW.close();
            OS.close();

        } catch (Exception ex) {
            throw new GeracaoException("Erro ao gerar:", ex);
        }

    }

    /**
     * Retorna a lista de imóveis que será enviada para o ZAP.
     *
     * @return listaImoveis : lista de imóveis para conversão.
     */
    @Override
    protected List<Imovel> getListImoveis() {
        List<Imovel> listaImoveis;

        ImovelCriteria criteria = new ImovelCriteria();
        criteria.createAnuncioCriteria().add(Restrictions.eq("nome", "123i"));

        listaImoveis = criteria.list();

        return listaImoveis;
    }

    /**
     * Retorna o cabeçalho necessário para o arquivo XML ser enviado via Web
     * Service. Para o envio via Web Service, é necessário um cabeçalho para que
     * seja reconhecido como documento de exporte. Adiciona as tags
     * <i>"Carga"</i> e <i>"Imoveis"</i>.
     *
     * @return inicial : Retorna o elemento que contém o root do XML.
     */
    public Element getHeader() {
        Element inicial = document.createElement("Carga");
        Element header = document.createElement("Imoveis");
        inicial.appendChild(header);

        return inicial;
    }

    /**
     * Cria a conexão com o Web Service e envia via Web Service. Para futuras
     * atualizações será necessário implementar um conjunto de arquivos de
     * propriedades, contendo várias informações de acesso, como servidor de
     * MySQL interno e externo, usuário e senha do ZAP, entre outros, utilizando
     * inclusive JMX.
     */
    @Override
    public void enviarExport() throws ExportException {
        fireExporFinish("Enviando...", count++);

        try {
            FTPClient ftp = new FTPClient();

            ftp.connect("ftp.westguerra.com.br");

            if (FTPReply.isPositiveCompletion(ftp.getReplyCode())) {

                if (ftp.login("westguerra", "w3s4t5")) {

                    ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                    ftp.changeWorkingDirectory("/Web/");
                    ftp.storeFile(arquivoXML.getName(), new FileInputStream(arquivoXML));
                }
            } else {
                ftp.disconnect();
            }

        } catch (Exception ex) {
            throw new ExportException("west.export.enviarXml.error", ex);
        }
    }
}