package org.west.utilitarios;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Date;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.swingBean.descriptor.composite.SingleBean;
import org.west.componentes.DesktopSession;
import org.west.entidades.Servico;
import org.west.entidades.ServicoDAO;
import org.west.entidades.Tarefa;
import org.west.entidades.Usuario;

public class RenderTree extends DefaultTreeCellRenderer {

    private Usuario usuario;

    public RenderTree(List lista) {
        this.usuario = (Usuario) DesktopSession.getInstance().getObject("usuario");
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        Font normal = new Font("verdana", 0, 12);
        Font token = new Font("verdana", Font.BOLD, 12);

        setFont(normal);
        setOpaque(false);

        if (value.toString().equals("Serviços")) {
            setIcon(new ImageIcon(super.getClass().getResource("/org/west/imagens/pilha.png")));
        } else if (leaf) {
            Tarefa tarefa = (Tarefa) ((SingleBean) value).getBean();
            if (tarefa != null) {
                if (tarefa.getTerminado()) {
                    setIcon(new ImageIcon(super.getClass().getResource("/org/west/imagens/ok.png")));
                } else {
                    setIcon(new ImageIcon(super.getClass().getResource("/org/west/imagens/aberta.png")));
                }
            }
        } else {
            Servico servico = getServico(value.toString());

            setOpaque(true);

            if (servico.getUltimaPrevisaoTermino() != null && servico.getUltimaPrevisaoTermino().before(new Date())) {
                setBackground(new Color(251, 120, 120));
            } else {
                if (servico.isInativo()) {
                    setBackground(new Color(159, 95, 159));
                } else {
                    if (isServicoAtransando(servico)) {
                        setBackground(new Color(242, 245, 113));
                    }
                }
            }

            if (servico != null) {

                switch (servico.getConcluido()) {
                    case 0: {
                        setIcon(new ImageIcon(super.getClass().getResource("/org/west/imagens/tipo" + servico.getTipoServico().getIcone() + ".png")));
                        break;
                    }
                    case 2: {
                        setIcon(new ImageIcon(super.getClass().getResource("/org/west/imagens/cancelado.png")));
                        break;
                    }
                    case 1: {
                        setIcon(new ImageIcon(super.getClass().getResource("/org/west/imagens/concluido.png")));
                        break;
                    }
                }

                if (usuario.equals(servico.getToken())) {
                    setFont(token);
                }
            }
        }

        return this;
    }

    private Servico getServico(String busca) {
        Servico retorno = null;

        Long codigo = new Long(busca.substring(0, busca.indexOf("-")).trim());
        retorno = ServicoDAO.load(codigo);

        return retorno;
    }

    private boolean isServicoAtransando(Servico servico) {
        Boolean atrasado = Boolean.FALSE;

        for (Tarefa tarefa : servico.getTarefas()) {

            double relacao = 0.0;
            relacao = (new Date()).getTime() - tarefa.getDataInicio().getTime();
            relacao /= tarefa.getPrevisaoTermino().getTime() - tarefa.getDataInicio().getTime();

            if (relacao > 0.7) {
                atrasado = Boolean.TRUE;
                break;
            }
        }

        return atrasado;
    }
}
