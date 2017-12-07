package org.west.converters;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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
 * Classe que controla o exporte para o ZAP. Todo imóvel na base de dados pode
 * ser anunciado em diversos meios. Um deles é o ZAP, que possui um sistema
 * automatizado de inserção e modificação de anuncios, através de um Web
 * Service. Esta classe tem por finalidade varrer a base de dados, converte-los
 * para o formato adequado e gerar o arquivo XML adequado para então envia-lo
 * via Web Service.
 *
 * @author WestGuerra Ltda.
 */
public final class ZapControl extends ControlExport {

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
     * enviada ao ZAP, através do método {@link #getListImoveis()}. Inicializa o
     * {@link #document} para receber os demais nós.
     */
    public ZapControl() {
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

        arquivoXML = new File("exportZAP.xml");
        ZapConverter converter = new ZapConverter(document);

        Element ini = getHeader();

        document.appendChild(ini);

        Element header = (Element) ini.getLastChild();

        for (Imovel imovel : listaImovel) {
            header.appendChild((Element) converter.toFormato(imovel));
            fireExporMove("Convertendo Imovel " + imovel.getReferencia() + "...", count++);
        }

        try {
            XMLSerializer serializer = new XMLSerializer();

            OutputFormat format = new OutputFormat(document, "ISO-8859-1", true);
            format.setLineSeparator(System.getProperty("line.separator"));
            format.setIndent(4);

            serializer.setOutputFormat(format);

            FileWriter f = new FileWriter(arquivoXML);

            serializer.setOutputCharStream(f);
            serializer.serialize(document);

            f.close();

        } catch (Exception ex) {
            throw new GeracaoException("west.export.gerarXml.error", ex);
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
        criteria.createAnuncioCriteria().add(Restrictions.eq("nome", "Zap"));

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

        String SOAP_ACTION = "http://zap.com.br/webservices/AtualizarArquivo";
        String METHOD_NAME = "AtualizarArquivo";
        String NAMESPACE = "http://zap.com.br/webservices/";
        String URL = "http://ws.zap.com.br/EnvArqSenha.asmx";

        try {
            SoapObject soapOut = new SoapObject(NAMESPACE, METHOD_NAME);
            soapOut.addProperty("plngCodCliente", "903167");
            soapOut.addProperty("pstrSenhaCliente", "0542258");
            soapOut.addProperty("bytArquivo", getArrayArquivo());

            SoapSerializationEnvelope env = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            env.dotNet = true;
            env.setOutputSoapObject(soapOut);

            new MarshalBase64().register(env);
            env.encodingStyle = SoapEnvelope.ENC;

            HttpTransportSE httpTransport = new HttpTransportSE(URL);
            httpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
            httpTransport.call(SOAP_ACTION, env);

        } catch (Exception ex) {
            throw new ExportException("west.export.enviarXml.error", ex);
        }
    }

    /**
     * Para o envio do arquivo é necessário converter o arquivo em um array de
     * byte.
     *
     * @return bytes : Arquivo convertido em bytes.
     */
    private byte[] getArrayArquivo() {
        try {
            InputStream is = new FileInputStream(arquivoXML);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return buffer;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}