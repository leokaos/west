package org.west.converters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
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
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.west.entidades.Anuncio;
import org.west.entidades.AnuncioDAO;
import org.west.entidades.Imovel;
import org.west.entidades.ImovelCriteria;
import org.west.entidades.ImovelDAO;
import org.west.entidades.WestPersistentManager;
import org.west.exception.ExportException;
import org.west.exception.GeracaoException;
import org.west.utilitarios.Util;

/**
 * Classe que controla o exporte para o VivaReal. Todo imóvel na base de dados
 * pode ser anunciado em diversos meios. Um deles é o VivaReal, que possui um
 * sistema automatizado de inserção e modificação de anuncios, através de um Web
 * Service. Esta classe tem por finalidade varrer a base de dados, converte-los
 * para o formato adequado e gerar o arquivo XML adequado para então envia-lo
 * via Web Service.
 *
 * @author WestGuerra Ltda.
 */
public final class VivaRealControl extends ControlExport {

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
     * Nome do arquivo que será gerado ao finalizar o export.
     */
    private String fileName = "exportVivaReal.xml";
    /**
     * Data mínima de atualização para envio para o Viva Real.
     */
    private Date dataCorte;

    /**
     * Construtor padrão. Este contrutor cria a lista de imóveis que será
     * enviada ao VivaReal, através do método {@link #getListImoveis()}.
     * Inicializa o {@link #document} para receber os demais nós.
     */
    public VivaRealControl(Date dataCorte) {
        this.dataCorte = dataCorte;
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

        excluirAnteriores();

        arquivoXML = new File(fileName);
        VivaRealConverter converter = new VivaRealConverter(document);

        Element ini = getHeader();

        document.appendChild(ini);

        Element header = (Element) ini.getLastChild();
        Anuncio anuncio = AnuncioDAO.load("Viva Real");

        for (Imovel imovel : listaImovel) {

            ImovelDAO.lock(imovel);
            imovel.getAnuncios().add(anuncio);
            ImovelDAO.save(imovel);

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

    /**
     * Retorna a lista de imóveis que será enviada para o VivaReal.
     *
     * @return listaImoveis : lista de imóveis para conversão.
     */
    @Override
    protected List<Imovel> getListImoveis() {
        List<Imovel> listaImoveis;

        ImovelCriteria criteria = new ImovelCriteria();
        criteria.add(Restrictions.eq("status", "Ativo"));
        criteria.add(Restrictions.ge("atualizado", Util.corrigirDate(dataCorte, Util.INICIO)));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

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
     * MySQL interno e externo, usuário e senha do VivaReal, entre outros,
     * utilizando inclusive JMX.
     */
    @Override
    public void enviarExport() throws ExportException {
        fireExporFinish("Enviando...", count++);

        try {
            FTPClient ftp = new FTPClient();

            ftp.connect("gestion.vivareal.com");

            if (FTPReply.isPositiveCompletion(ftp.getReplyCode())) {

                if (ftp.login("westguerra", "lokit357")) {

                    ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                    ftp.storeFile(fileName, new FileInputStream(fileName));
                }
            } else {
                ftp.disconnect();
            }

        } catch (Exception ex) {
            throw new ExportException("west.export.enviarXml.error", ex);
        }
    }

    /**
     * Exclui os registros referente aos imóveis anteriormente exportados para o
     * site.
     */
    private void excluirAnteriores() {
        Session session = WestPersistentManager.getSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createSQLQuery("delete from tab_imovel_anuncio where anuncio='Viva Real'");
        query.executeUpdate();
        session.flush();
        tx.commit();
    }
}