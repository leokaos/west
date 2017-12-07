package org.west.formulario.corretor.actions;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.swingBean.actions.ApplicationAction;
import org.swingBean.descriptor.BeanTableModel;
import org.swingBean.gui.JBeanPanel;
import org.swingBean.gui.JBeanTable;
import org.west.componentes.DesktopSession;
import org.west.entidades.Cliente;
import org.west.entidades.ClienteCriteria;
import org.west.entidades.Imobiliaria;
import org.west.entidades.ImobiliariaCriteria;
import org.west.entidades.Usuario;
import org.west.formulario.corretor.ClienteFiltro;
import org.west.utilitarios.RenderImob;

public class ActionBuscaCliente extends ApplicationAction {

    private final JBeanPanel<ClienteFiltro> formBuscaCliente;
    private final BeanTableModel<Cliente> modelo;
    private final JBeanTable tabela;

    public ActionBuscaCliente(JBeanPanel<ClienteFiltro> formBuscaCliente, BeanTableModel<Cliente> modelo, JBeanTable tabela) {
        this.formBuscaCliente = formBuscaCliente;
        this.modelo = modelo;
        this.tabela = tabela;
    }

    @Override
    public void execute() {
        ClienteFiltro filtro = getFiltro();
        Usuario usuario = (Usuario) DesktopSession.getInstance().getObject("usuario");

        ImobiliariaCriteria imobCriteria = new ImobiliariaCriteria();
        ClienteCriteria clienteCriteria = imobCriteria.createClienteCriteria();

        if (!usuario.isSupervisor() && !usuario.isCoordenador()) {
            imobCriteria.createUsuarioCriteria().add(Restrictions.ilike("nome", usuario.getNome(), MatchMode.ANYWHERE));
        }

        if (filtro.hasCodigoCliente()) {
            clienteCriteria.add(Restrictions.eq("codigo", filtro.getCodigo().longValue()));
        } else {

            imobCriteria.add(Restrictions.between("dataEntrada", filtro.getInicio(), filtro.getFim()));

            if (filtro.hasImovel()) {
                imobCriteria.add(Restrictions.ilike("imovel", filtro.getImovel(), MatchMode.ANYWHERE));
            }

            if (filtro.hasNomeCliente()) {
                clienteCriteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
            }

            if (filtro.hasTelefone()) {
                clienteCriteria.createAlias("telefones", "tel", JoinType.LEFT_OUTER_JOIN);

                clienteCriteria.add(Restrictions.or(Restrictions.ilike("telefone", filtro.getTelefone(), MatchMode.ANYWHERE),
                        Restrictions.ilike("tel.telefone", filtro.getTelefone(), MatchMode.ANYWHERE)));
            }

            if (filtro.hasTipoAcompanhado()) {
                imobCriteria.add(Restrictions.eq("acompanhado", filtro.getTipoAcompanhado().getValue()));
            }

            if (filtro.hasHistorico()) {
                imobCriteria.createHistoricoCriteria().add(Restrictions.ilike("descricao", filtro.getHistorico(), MatchMode.ANYWHERE));
            }
        }

        List<Imobiliaria> listaImob = new ArrayList<Imobiliaria>();
        List<Cliente> listaCli = new ArrayList<Cliente>();

        for (Imobiliaria imob : imobCriteria.listImobiliarias()) {

            if (!listaCli.contains(imob.getCliente())) {
                listaCli.add(imob.getCliente());
                listaImob.add(imob);
            }
        }

        RenderImob render = new RenderImob(listaImob);
        modelo.setBeanList(listaCli);

        for (int x = 0; x < tabela.getColumnModel().getColumnCount(); ++x) {
            tabela.getColumnModel().getColumn(x).setCellRenderer(render);
        }
    }

    private ClienteFiltro getFiltro() {
        ClienteFiltro filtro = new ClienteFiltro();
        formBuscaCliente.populateBean(filtro);
        return filtro;
    }
}
