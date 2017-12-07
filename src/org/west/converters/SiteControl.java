package org.west.converters;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.west.entidades.Anuncio;
import org.west.entidades.AnuncioDAO;
import org.west.entidades.Imovel;
import org.west.entidades.ImovelCriteria;
import org.west.entidades.ImovelDAO;
import org.west.entidades.WestPersistentManager;
import org.west.exception.ExportException;
import org.west.exception.GeracaoException;
import org.west.exception.WestException;
import org.west.utilitarios.Util;

/**
 * Classe responsável por controlar o exporte para o site. O sistema atual tem
 * integração total com o site da empresa. Para que haja uma sincronização da
 * base de dados com a base do site, esta classe converte o imóvel para o
 * formato do banco externo, deleta os dados da base externa e insere os dados
 * atualizados.
 *
 * @author West Guerra Ltda.
 */
public final class SiteControl extends ControlExport {

    private List<String> listaStatement;
    private SiteConverter converter;
    private Date dataLimite;
    private Integer count;

    /**
     * Construtor que recebe uma {@link Date} que delimita a data mínima de
     * atualização. Para futuras atualizações será necessário implementar um
     * conjunto de arquivos de propriedades, contendo várias informações de
     * acesso, como servidor de MySQL interno e externo, usuário e senha do ZAP,
     * entre outros, utilizando inclusive JMX.
     *
     * @param dataLimite data mínima de atualização.
     */
    public SiteControl(Date dataLimite) {
        this.dataLimite = dataLimite;
        this.listaImovel = getListImoveis();
        this.listaStatement = new ArrayList<String>();
        this.count = 0;

        converter = new SiteConverter();
    }

    /**
     * Transforma a lista de imóvies no formato adequado. Todos os imóveis
     * marcados como "Ativo" seŕa enviado ao site. Para cada imóvel é criado um
     * {@link PreparedStatement} e colocado em {@link #listaStatement}, para
     * depois serem executados.
     */
    @Override
    public void gerarExport() throws GeracaoException {
        fireExportInit("Iniciando...", count++);

        excluirAnteriores();

        Anuncio anuncio = AnuncioDAO.load("Site");
        AnuncioDAO.lock(anuncio);

        for (Imovel imovel : listaImovel) {

            ImovelDAO.lock(imovel);
            imovel.getAnuncios().add(anuncio);
            ImovelDAO.save(imovel);

            listaStatement.add((String) converter.toFormato(imovel));
            fireExporMove("Exportando imovel " + imovel.getReferencia(), count++);
        }
    }

    /**
     * Executa todos {@link PreparedStatement} armazenados em
     * {@link #listaStatement}. Primeiramente é deletada toda a base previamente
     * cadastrada, para depois incluir todos os dados novos na base.
     */
    @Override
    public void enviarExport() throws ExportException {
        try {
            deletarAntigos();

            Connection con = createConnection();

            for (String str : listaStatement) {

                if (count % 100 == 0 && con != null) {
                    fecharConexao(con);
                    con = createConnection();
                }

                if (str != null) {
                    con.prepareStatement(str).executeUpdate();
                }

                fireExporMove("Enviando...", count++);
            }
        } catch (Exception ex) {
            throw new ExportException("west.export.enviarSql.error", ex);
        }
    }

    private Connection createConnection() throws WestException {
        try {

            Class.forName("com.mysql.jdbc.Driver");

            return DriverManager.getConnection("jdbc:mysql://187.18.58.105:3306/westguerra", "root", "w3s4t5g6");

        } catch (Exception ex) {
            throw new WestException("Não foi possível se conectar com o servidor!");
        }
    }

    private void deletarAntigos() throws WestException {
        Connection conn = null;

        try {

            conn = createConnection();

            conn.prepareStatement("DELETE FROM tab_imovel").executeUpdate();

        } catch (SQLException ex) {
            throw new WestException("Erro ao executar exclusão!", ex);
        } finally {
            fecharConexao(conn);
        }
    }

    private void fecharConexao(Connection conn) throws WestException {
        try {
            conn.close();
        } catch (SQLException ex) {
            throw new WestException("Não foi possível fechar a conexão!", ex);
        }
    }

    /**
     * Inclui na {@link #listaImovel} todos os imóveis marcados como "Ativo" na
     * base.
     *
     * @return listaImo : lista de imóveis a serem enviados ao site.
     */
    @Override
    protected List<Imovel> getListImoveis() {
        List<Imovel> listaImo = new ArrayList<Imovel>();

        try {
            ImovelCriteria criteria = new ImovelCriteria();
            criteria.add(Restrictions.eq("status", "Ativo"));
            criteria.add(Restrictions.ge("atualizado", Util.corrigirDate(dataLimite, Util.INICIO)));
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

            listaImo = criteria.list();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return listaImo;
    }

    private void excluirAnteriores() {
        Session session = WestPersistentManager.getSession();
        session.clear();
        Transaction tx = session.beginTransaction();
        Query query = session.createSQLQuery("delete from tab_imovel_anuncio where anuncio='Site'");
        query.executeUpdate();
        session.flush();
        tx.commit();
    }
}