package org.west.utilitarios;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import org.west.entidades.Tarefa;

public class RenderAviso extends DefaultListCellRenderer {

    private List<Tarefa> lista;

    public RenderAviso(List<Tarefa> lista) {
        this.lista = lista;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(list.getWidth(), 30));
        panel.setBackground(Color.white);

        Tarefa tarefa = lista.get(index);

        JLabel label = new JLabel();
        label.setIcon(new ImageIcon(super.getClass().getResource("/org/west/imagens/tipo" + tarefa.getServico().getTipoServico().getIcone() + ".png")));
        label.setBackground(Color.white);

        panel.add(label, BorderLayout.WEST);

        panel.add(new JLabel(tarefa.getNome()), BorderLayout.CENTER);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm   ");
        panel.add(new JLabel(format.format(tarefa.getAviso())), BorderLayout.EAST);

        return panel;
    }
}