package org.west.utilitarios;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class RenderLancamentos extends DefaultTableCellRenderer{

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (table.getColumnName(column).equals("Saida"))
            comp.setForeground(new Color(200,0,0));
        else
            if (table.getColumnName(column).equals("Entrada"))
                comp.setForeground(new Color(0,200,0));
            else
                comp.setForeground(Color.black);

        return comp;
    }
}