package org.west.converters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
public final class ImovelWebControl extends ControlExport {

    private Document document;
    private Integer count = 0;
    private File arquivoXML;

    public ImovelWebControl() {
        this.listaImovel = getListImoveis();

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            document = builder.newDocument();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void gerarExport() throws GeracaoException {
        fireExportInit("Inicializando...", count++);

        arquivoXML = new File("iw_ofertas.xml");
        ImovelWebConverter converter = new ImovelWebConverter(document);

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
            transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            StringWriter sw = new StringWriter();
            StreamResult sr = new StreamResult(sw);
            transformer.transform(domSource, sr);

            OutputStream OS = (OutputStream) new FileOutputStream(arquivoXML);
            OutputStreamWriter OSW = new OutputStreamWriter(OS, "ISO-8859-1");
            PrintWriter Print = new PrintWriter(OSW);
            Print.println(sw.toString());
            Print.close();
            OSW.close();
            OS.close();

        } catch (Exception ex) {
            throw new GeracaoException("Erro ao gerar:", ex);
        }

    }

    @Override
    public void enviarExport() throws ExportException {
        fireExporFinish("Enviando...", count++);

        try {
            FTPClient ftp = new FTPClient();

            ftp.connect("carga.imovelweb.com.br");

            if (FTPReply.isPositiveCompletion(ftp.getReplyCode())) {

                if (ftp.login("usr459306", "1548018933")) {

//                    ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
//                    ftp.storeFile(arquivoXML.getName(), new FileInputStream(arquivoXML));
                }
            } else {
                ftp.disconnect();
            }

        } catch (Exception ex) {
            throw new ExportException("west.export.enviarXml.error", ex);
        }
    }

    @Override
    protected List<Imovel> getListImoveis() {
        List<Imovel> listaImoveis;

        ImovelCriteria criteria = new ImovelCriteria();
        criteria.createAnuncioCriteria().add(Restrictions.eq("nome", "Imóvel Web"));

        listaImoveis = criteria.list();

        return listaImoveis;
    }

    public Element getHeader() {
        Element inicial = document.createElement("Carga");
        inicial.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        inicial.setAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");

        Element header = document.createElement("Imoveis");
        inicial.appendChild(header);

        return inicial;
    }
}