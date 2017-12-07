package org.west.componentes;

import org.west.entidades.Bairro;
import org.west.entidades.Imovel;
import org.west.entidades.ImovelCriteria;
import org.west.entidades.Tipo;
import org.west.formulario.imoveis.BuscaImovel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.swingBean.actions.ApplicationAction;
import org.swingBean.descriptor.GenericFieldDescriptor;
import org.swingBean.descriptor.XMLDescriptorFactory;
import org.swingBean.gui.JActButton;
import org.swingBean.gui.JBeanPanel;
import org.west.entidades.Foto;
import org.west.entidades.Medidas;
import org.west.entidades.MedidasCriteria;
import org.west.entidades.WestPersistentManager;
import org.west.utilitarios.Util;

import static org.west.utilitarios.ValidadorUtil.*;

/**
 * Classe de interface que controlará a exibição da listagem de imóveis e de
 * busca de imóveis.
 *
 * @author West Guerra Ltda.
 */
public class PanelImovel extends javax.swing.JPanel {

    /**
     * Componente responsável por exibir detalhadamente o imóvel.
     */
    private PanelDetalhesImovel detalhes;
    /**
     * Exibi a listagem de imóveis.
     */
    private PanelListaImovel listagemImo;

    /**
     * Construtor padrão.
     */
    public PanelImovel() {
        initComponents();

        //panel de detalhes ABA 1 - PanelDetalhesImovels
        drawDetalhes();

        //panel de busca na ABA 2 - lado esquerdo - apenas um JBeanPanel
        drawBusca();
        drawResultados();

        listagemImo.addPropertyChangeListener("selecionados", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                jTabbedPane1.setSelectedIndex(1);
                getDetalhes().setLista(getListagemImo().getSelecionados());
                WestPersistentManager.clear();
            }
        });

        listagemImo.addPropertyChangeListener("listagem", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                jTabbedPane1.setSelectedIndex(0);
            }
        });

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jTabbedPane1 = new javax.swing.JTabbedPane();
        panelBuscar = new javax.swing.JPanel();
        divisor = new javax.swing.JSplitPane();
        panelEditar = new javax.swing.JPanel();

        setLayout(new java.awt.GridBagLayout());

        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        panelBuscar.setLayout(new java.awt.BorderLayout());

        divisor.setDividerLocation(375);
        divisor.setOneTouchExpandable(true);
        panelBuscar.add(divisor, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("", new javax.swing.ImageIcon(getClass().getResource("/org/west/imagens/lupa.png")), panelBuscar); // NOI18N

        panelEditar.setLayout(new java.awt.BorderLayout());
        jTabbedPane1.addTab("", new javax.swing.ImageIcon(getClass().getResource("/org/west/imagens/casa.png")), panelEditar); // NOI18N

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jTabbedPane1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSplitPane divisor;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel panelBuscar;
    private javax.swing.JPanel panelEditar;
    // End of variables declaration//GEN-END:variables

    /**
     * Carrega a aba de edição e inclusão de imóveis.
     */
    private void drawDetalhes() {
        detalhes = new PanelDetalhesImovel();
        panelEditar.add(getDetalhes(), BorderLayout.CENTER);
    }

    /**
     * Cria o componente responsável por fazer a busca de imóveis para adicionar
     * em {@link #listagemImo}.
     */
    private void drawBusca() {
        JPanel panel = new JPanel(new BorderLayout());

        GenericFieldDescriptor descriptor =
                XMLDescriptorFactory.getFieldDescriptor(BuscaImovel.class, "org/west/xml/buscaImovel.xml", "buscaImovel");
        final JBeanPanel<BuscaImovel> formEstrutura = new JBeanPanel<BuscaImovel>(BuscaImovel.class, descriptor);
        formEstrutura.setBorder(new EmptyBorder(5, 5, 5, 5));

        formEstrutura.setPropertyValue("dataInicial", null);
        formEstrutura.setPropertyValue("dataFinal", null);
        formEstrutura.setPropertyValue("capInicial", null);
        formEstrutura.setPropertyValue("capFinal", null);

        panel.add(formEstrutura, BorderLayout.CENTER);
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));

        panelBotoes.add(new JActButton("Buscar", new ApplicationAction() {

            @Override
            public void execute() {
                BuscaImovel busca = new BuscaImovel();
                formEstrutura.populateBean(busca);
                efetuarBusca(busca);
            }
        }));

        panelBotoes.add(new JActButton("Limpar", new ApplicationAction() {

            @Override
            public void execute() {
                formEstrutura.cleanForm();
                formEstrutura.setPropertyValue("dataInicial", null);
                formEstrutura.setPropertyValue("dataFinal", null);
                formEstrutura.setPropertyValue("capInicial", null);
                formEstrutura.setPropertyValue("capFinal", null);
            }
        }));

        panel.add(panelBotoes, BorderLayout.SOUTH);

        divisor.setLeftComponent(panel);
    }

    /**
     * Após determinar os parâmetros de busca, este método é chamado para montar
     * a query que será enviada, através do método
     * {@link #montaQuery(org.west.formulario.imoveis.BuscaImovel)}, e com os
     * resultados exibir em {@link #listagemImo}.
     *
     * @param busca Um Bean contendo os parâmetros de busca.
     */
    private void efetuarBusca(BuscaImovel busca) {
        WestPersistentManager.clear();

        final ImovelCriteria criteria = montaQuery(busca);

        if (criteria != null) {

            getListagemImo().getPanelResultado().removeAll();
            getListagemImo().getPanelResultado().revalidate();
            getListagemImo().getPanelResultado().repaint();

            Util.load(getListagemImo().getPanelResultado());

            new Thread(new Runnable() {

                @Override
                public void run() {
                    List lista = criteria.list();
                    getListagemImo().setListagem(lista);
                    Util.unload(getListagemImo().getPanelResultado());
                }
            }).start();
        }
    }

    /**
     * Método responsável por extrair os parâmetros de busca e criar uma
     * {@link ImovelCriteria}.
     *
     * @param busca Um bean contendo os parâmetros da busca.
     * @return imovelCriteria : Criteria montada.
     */
    private ImovelCriteria montaQuery(BuscaImovel busca) {
        ImovelCriteria imovelCriteria = null;

        try {
            imovelCriteria = new ImovelCriteria(WestPersistentManager.getSession().createCriteria(Imovel.class, "ref"));

            if (!busca.getReferencia().isEmpty()) {
                String ref = busca.getReferencia();

                if (isNumber(ref)) {
                    imovelCriteria.add(Restrictions.eq("referencia", new Long(busca.getReferencia())));
                } else if (ref.indexOf("-") > 0) {
                    String[] valores = ref.split("-");
                    imovelCriteria.add(Restrictions.between("referencia", new Long(valores[0]), new Long(valores[1])));
                } else if (ref.indexOf(";") > 0) {
                    String valores[] = ref.split(";");
                    List<Long> refes = new ArrayList<Long>();

                    for (String valor : valores) {
                        if (isNumber(valor)) {
                            refes.add(new Long(valor));
                        }
                    }

                    imovelCriteria.add(Restrictions.in("referencia", refes));
                } else {
                    imovelCriteria = null;
                    JOptionPane.showMessageDialog(null, "Preenchimento incorreto do campo referência!");
                }
            } else {

                if (busca.getAreaPrivativa() != null) {
                    imovelCriteria = addIntervalRestriction((Object[]) busca.getAreaPrivativa(), imovelCriteria, "privativa", "DOUBLE");
                }

                if (busca.getDorms() != null) {
                    imovelCriteria = addIntervalRestriction((Object[]) busca.getDorms(), imovelCriteria, "dorms", "INTEGER");
                }

                if (busca.getSuites() != null) {
                    imovelCriteria = addIntervalRestriction((Object[]) busca.getSuites(), imovelCriteria, "suites", "INTEGER");
                }

                if (busca.getGaragens() != null) {
                    imovelCriteria = addIntervalRestriction((Object[]) busca.getGaragens(), imovelCriteria, "garagens", "INTEGER");
                }

                if (busca.getValor() != null) {
                    imovelCriteria = addIntervalRestriction((Object[]) busca.getValor(), imovelCriteria, "valor", "DOUBLE");
                }

                if (!busca.getProprietario().isEmpty()) {
                    imovelCriteria.add(Restrictions.or(Restrictions.ilike("proprietarios", busca.getProprietario(), MatchMode.ANYWHERE),
                            Restrictions.ilike("proprietarios1", busca.getProprietario(), MatchMode.ANYWHERE)));
                }

                if (((Object[]) busca.getAnuncios()).length > 0) {
                    Object dados[] = (Object[]) busca.getAnuncios();
                    List lista = new ArrayList();
                    for (Object obj : dados) {
                        lista.add(obj.toString());
                    }

                    imovelCriteria.createAnuncioCriteria().add(Restrictions.in("nome", lista));
                }

                if (((Object[]) busca.getTipos()).length > 0) {
                    List lista = new ArrayList();
                    for (Object object : ((Object[]) busca.getTipos())) {
                        Tipo tipo = (Tipo) object;
                        lista.add(tipo.getTipo());
                    }
                    imovelCriteria.createTipoCriteria().add(Restrictions.in("tipo", lista.toArray()));
                }

                if (((Object[]) busca.getStatus()).length > 0) {
                    imovelCriteria.add(Restrictions.in("status", (Object[]) busca.getStatus()));
                }

                if (busca.getDestaque() != null) {
                    imovelCriteria.add(Restrictions.eq("destaque", new Integer(busca.getDestaque())));
                }

                //Data atualização
                if (busca.getDataInicial() != null && busca.getDataFinal() == null) {
                    imovelCriteria.add(Restrictions.ge("atualizado", Util.corrigirDate(busca.getDataInicial(), Util.INICIO)));
                }

                if (busca.getDataInicial() == null && busca.getDataFinal() != null) {
                    imovelCriteria.add(Restrictions.le("atualizado", Util.corrigirDate(busca.getDataInicial(), Util.FIM)));
                }

                if (busca.getDataInicial() != null && busca.getDataFinal() != null) {
                    imovelCriteria.add(Restrictions.between("atualizado", Util.corrigirDate(busca.getDataInicial(), Util.INICIO), Util.corrigirDate(busca.getDataFinal(), Util.FIM)));
                }

                //Data Captação
                if (busca.getCapInicial() != null && busca.getCapFinal() == null) {
                    imovelCriteria.add(Restrictions.ge("captacao", busca.getCapInicial()));
                }

                if (busca.getCapInicial() == null && busca.getCapFinal() != null) {
                    imovelCriteria.add(Restrictions.le("captacao", busca.getCapInicial()));
                }

                if (busca.getCapInicial() != null && busca.getCapFinal() != null) {
                    imovelCriteria.add(Restrictions.between("captacao", busca.getCapInicial(), busca.getCapFinal()));
                }

                //Corretor
                if (!busca.getCorretor().isEmpty()) {
                    imovelCriteria.createUsuarioCriteria().add(Restrictions.ilike("nome", busca.getCorretor(), MatchMode.ANYWHERE));
                }

                //Bairros
                if (((Object[]) busca.getBairro()).length > 0) {
                    List lista = new ArrayList();
                    for (Object object : ((Object[]) busca.getBairro())) {
                        Bairro bairro = (Bairro) object;
                        lista.add(bairro.getNome());
                    }
                    imovelCriteria.createBairroCriteria().add(Restrictions.in("nome", lista.toArray()));
                }

                if (!busca.getEndereco().isEmpty()) {
                    int index = busca.getEndereco().indexOf(",");
                    String numeroStr = busca.getEndereco().substring(index + 1);

                    if (index > -1 && isNumber(numeroStr)) {
                        Long numero = new Long(numeroStr);

                        imovelCriteria.createNumeroCriteria().add(Restrictions.eq("numero", numero));
                        busca.setEndereco(busca.getEndereco().substring(0, index));
                    }

                    imovelCriteria.createCepCriteria().add(Restrictions.ilike("descricao", busca.getEndereco(), MatchMode.ANYWHERE));
                }

                if (busca.getComFotos() && !busca.getSemFotos()) {
                    imovelCriteria.createFotoCriteria().add(Restrictions.gt("codigo", new Long(0)));
                }

                if (!busca.getComFotos() && busca.getSemFotos()) {
                    DetachedCriteria inner = DetachedCriteria.forClass(Foto.class, "inner");
                    inner.setProjection(Projections.rowCount());
                    inner.add(Restrictions.eqProperty("inner.imovel", "ref.referencia"));

                    imovelCriteria.add(Subqueries.eq(0l, inner));
                }

                if (busca.getVago()) {
                    imovelCriteria.add(Restrictions.eq("vago", busca.getVago()));
                }

                if (busca.getTamanho() != null && !busca.getTamanho().isEmpty()) {
                    imovelCriteria.add(Restrictions.ilike("medidas", busca.getTamanho(), MatchMode.START));
                }

                addRestrictionsMedidas(busca.getMedidas(), imovelCriteria);

                if (busca.getDivulgar()) {
                    imovelCriteria.add(Restrictions.eq("divulgar", busca.getDivulgar()));
                }

                if (busca.getDescricao() != null && !busca.getDescricao().isEmpty()) {
                    imovelCriteria.add(Restrictions.ilike("descricao", busca.getDescricao(), MatchMode.ANYWHERE));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (imovelCriteria != null) {
            imovelCriteria.addOrder(Order.asc("valor"));
            imovelCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        }

        return imovelCriteria;
    }

    /**
     * Pelo fato de existirem vários campos do tipo {@link WrapperInterval},
     * este método recebe os parâmetros e adiciona na criteria enviada.
     *
     * @param dados Valores da busca.
     * @param criteria Criteria à qual será adicionada a {@link Restrictions}.
     * @param property Nome da propriedade para adicionar a restrição.
     * @param type "DOUBLE" ou "INT", apenas para validação do Hibernate.
     * @return criteria : Criteria devidamente preenchida.
     */
    public ImovelCriteria addIntervalRestriction(Object[] dados, ImovelCriteria criteria, String property, String type) {
        String min = "";
        String max = "";

        if (dados[0] != null) {
            min = dados[0].toString();
        }

        if (dados[1] != null) {
            max = dados[1].toString();
        }

        if (min.isEmpty() && !max.isEmpty()) {
            if (type.equals("DOUBLE")) {
                criteria.add(Restrictions.le(property, new Double(max)));
            } else {
                criteria.add(Restrictions.le(property, new Integer(max)));
            }
        }

        if (!min.isEmpty() && max.isEmpty()) {
            if (type.equals("DOUBLE")) {
                criteria.add(Restrictions.ge(property, new Double(min)));
            } else {
                criteria.add(Restrictions.ge(property, new Integer(min)));
            }
        }

        if (!min.isEmpty() && !max.isEmpty()) {
            if (type.equals("DOUBLE")) {
                criteria.add(Restrictions.between(property, new Double(min), new Double(max)));
            } else {
                criteria.add(Restrictions.between(property, new Integer(min), new Integer(max)));
            }
        }

        return criteria;
    }

    /**
     * Cria o componente {@link #listagemImo} e adiciona no layout.
     */
    private void drawResultados() {
        listagemImo = new PanelListaImovel();
        divisor.setRightComponent(getListagemImo());
    }

    /**
     * Dado um {@link Imovel}, exibe esse imóvel em detalhes.
     *
     * @param imovel Imóvel a ser exibido em detalhes.
     */
    public void exibeDetalhes(Imovel imovel) {
        List<Imovel> lista = new ArrayList<Imovel>();
        lista.add(imovel);
        exibeDetalhes(lista);
    }

    /**
     * Dado uma lista de {@link Imovel}, exibe os detalhes da lista.
     *
     * @param lista Lista a ser exibida em detalhes.
     */
    public void exibeDetalhes(List<Imovel> lista) {
        getDetalhes().setLista(lista);
        jTabbedPane1.setSelectedIndex(1);

        firePropertyChange("listagem", new ArrayList<Imovel>(), lista);
    }

    /**
     * Verifica se uma {@link String} contém apenas números.
     *
     * @param str String a ser verificada.
     * @return true : Apenas se str contiver apenas números.
     */
    private boolean isNumber(String str) {
        boolean retorno = true;
        for (int x = 0; x < str.length(); x++) {
            if (!Character.isDigit(str.charAt(x))) {
                retorno = false;
            }
        }
        return retorno;
    }

    /**
     * Recupera o componente {@link PanelDetalhesImovel}.
     *
     * @return detalhes : Componente {@link PanelDetalhesImovel}.
     */
    public PanelDetalhesImovel getDetalhes() {
        return detalhes;
    }

    /**
     * Recupera o componente {@link PanelListaImovel}.
     *
     * @return listagemImo : Componente {@link PanelListaImovel}.
     */
    public PanelListaImovel getListagemImo() {
        return listagemImo;
    }

    private void addRestrictionsMedidas(Medidas medidas, ImovelCriteria imovelCriteria) {
        if (!medidas.isVazio()) {
            MedidasCriteria medidasCriteria = imovelCriteria.createMedidasCriteria();

            if (isNotEmpty(medidas.getFrente())) {
                medidasCriteria.add(Restrictions.ge("frente", medidas.getFrente()));
            }

            if (isNotEmpty(medidas.getEsquerda())) {
                medidasCriteria.add(Restrictions.ge("esquerda", medidas.getEsquerda()));
            }

            if (isNotEmpty(medidas.getTraseira())) {
                medidasCriteria.add(Restrictions.ge("traseira", medidas.getTraseira()));
            }

            if (isNotEmpty(medidas.getDireita())) {
                medidasCriteria.add(Restrictions.ge("direita", medidas.getDireita()));
            }
        }
    }
}