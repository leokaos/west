package org.west.utilitarios;

import org.west.entidades.Recado;
import java.awt.Color;
import java.awt.Component;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class RenderRecados extends DefaultTableCellRenderer {

    private List<Recado> lista;

    public RenderRecados(List<Recado> lista) {
        this.lista = lista;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        Recado recado = (Recado) this.lista.get(row);
        Color red = new Color(251, 120, 120);

        if (recado.getLeitura() == null) {
            setBackground(red);
        } else {
            setBackground(Color.white);
        }
        return this;
    }
}
