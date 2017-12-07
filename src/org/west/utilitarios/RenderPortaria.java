package org.west.utilitarios;

import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class RenderPortaria extends DefaultTableCellRenderer {

    public RenderPortaria() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        Component comp = this;

        if (table.getColumnModel().getColumn(column).getHeaderValue().toString().equals("Frequencia")) {
            JCheckBox novo = new JCheckBox();
            novo.setOpaque(true);
            novo.setFocusable(true);
            novo.setEnabled(true);
            comp = (Component) novo;
        }

        if (isSelected) {
            comp.setForeground(table.getSelectionForeground());
            comp.setBackground(table.getSelectionBackground());
        } else {
            comp.setForeground(table.getForeground());
            comp.setBackground(table.getBackground());
        }

        return comp;
    }
}
