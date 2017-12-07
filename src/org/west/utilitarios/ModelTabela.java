package org.west.utilitarios;

import javax.swing.table.DefaultTableModel;

public class ModelTabela extends DefaultTableModel {

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
