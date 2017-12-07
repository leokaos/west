package org.west.utilitarios;

import org.west.entidades.Servico;
import org.west.entidades.Tarefa;
import java.awt.Color;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.west.entidades.ServicoDAO;

public class RenderServicos extends DefaultTableCellRenderer {

    private List<Servico> lista;

    public RenderServicos(List<Servico> lista) {
        this.lista = lista;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

        String nome = table.getColumnModel().getColumn(col).getHeaderValue().toString();
        String[] nomes = {"Urgente", "Proposta", "Financiamento", "Assessoria", "Contratos", "Certidões", "Consignado", "Seguros"};

        if (nome.equals("Tipo")) {
            JLabel label = (JLabel) comp;
            label.setText(nomes[new java.lang.Integer(value.toString().trim()).intValue() - 1]);
        }

        Integer id = new Integer(table.getValueAt(row, table.getColumnModel().getColumnIndex("Nº")).toString());
        Servico servico = getServico(id);

        if (nome.equals("OK")) {
            String[] status = {"Normal", "Concluido", "Cancelado"};
            JLabel novo = (JLabel) comp;
            novo.setText(status[servico.getConcluido()]);
        }

        if (value instanceof Date) {
            JLabel label = (JLabel) comp;
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            label.setText(format.format((Date) value));
        }

        comp.setBackground(Color.white);

        if (servico.getUltimaPrevisaoTermino() != null && servico.getUltimaPrevisaoTermino().before(new Date())) {
            comp.setBackground(new Color(251, 120, 120));
        } else {
            if (servico.isInativo()) {
                comp.setBackground(new Color(159, 95, 159));
            } else {
                if (isServicoAtransando(servico)) {
                    comp.setBackground(new Color(242, 245, 113));
                }
            }
        }

        if (nome.equals("Nº")) {
            JLabel label = (JLabel) comp;
            label.setIcon(new ImageIcon(super.getClass().getResource("/org/west/imagens/tipo" + servico.getTipoServico().getIcone() + ".png")));
            label.setBackground(Color.white);
            label.setToolTipText(servico.getTipoServico().getDescricao());
        } else {
            if (comp.getClass() != JCheckBox.class) {
                JLabel label = (JLabel) comp;
                label.setIcon(null);
                label.setToolTipText(null);
            }
        }

        JComponent componente = (JComponent) comp;
        componente.setOpaque(true);

        return comp;
    }

    private Servico getServico(Integer id) {
        Servico servico = null;

        for (Iterator<Servico> it = lista.iterator(); it.hasNext();) {
            Servico servico1 = it.next();
            if (servico1.getCodigo() == id.longValue()) {
                servico = servico1;
                break;
            }
        }

        return servico;
    }

    private boolean isServicoAtransando(Servico servico) {
        Boolean atrasado = Boolean.FALSE;
        ServicoDAO.lock(servico);

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