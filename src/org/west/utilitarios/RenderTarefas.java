package org.west.utilitarios;

import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.west.entidades.Tarefa;

public class RenderTarefas extends DefaultTableCellRenderer {

    private List<Tarefa> lista;

    public RenderTarefas(List<Tarefa> lista) {
        this.lista = lista;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        Tarefa tarefa = (Tarefa) this.lista.get(row);

        if (value instanceof Date) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            setText(format.format((Date) value));
        }

        setBackground(tarefa.getStatus().getColor());

        return this;
    }
}
