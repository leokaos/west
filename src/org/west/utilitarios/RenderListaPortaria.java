package org.west.utilitarios;

import org.west.componentes.JLabelProgress;
import org.west.entidades.Frequencia;
import org.west.entidades.Usuario;
import java.awt.Color;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

public class RenderListaPortaria extends DefaultTableCellRenderer{

    private String busca;

    public RenderListaPortaria(String busca) {
        this.busca = busca;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value == null)
            value = "";
        
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        label.setIcon(null);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        
        if (column != 0)
            label.setToolTipText("Endereço: " + table.getValueAt(row, 2).toString() + ", Nº " + table.getValueAt(row, 3));
        else
            label.setToolTipText(null);

        if (busca != null && ! busca.isEmpty() && value != null){
            String str = value.toString().trim();
            busca = busca.toLowerCase();

            if (str.toLowerCase().indexOf(busca) > -1){
                String valor;
                int index = str.toLowerCase().indexOf(busca);

                StringBuilder sb = new StringBuilder();
                sb.append("<html><head></head><body>");
                sb.append(str.substring(0, index));
                sb.append("<span style='background-color:yellow;'>");
                sb.append(str.substring(index,index + busca.length()));
                sb.append("</span>");
                sb.append(str.substring(index + busca.length()));
                sb.append("</body></html>");

                valor = sb.toString();

                label.setText(valor);
            }
        }
        
        if (column == 1){
            label.setText("");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            
            if ( (Boolean) value)
                label.setIcon(new ImageIcon(getClass().getResource("/org/west/imagens/exclamation.png")));
            else
                label.setIcon(null);
        }
       
        if (table.getColumnName(column).equals("Imóveis")) {
            JLabelProgress labelProgress = new JLabelProgress(value.toString(), SwingConstants.CENTER);
            String[] valores = value.toString().split("/");
            labelProgress.setMaximo(new Integer(valores[0].trim()));
            labelProgress.setMinimo(new Integer(valores[1].trim()));
            labelProgress.setProgressColor(new Color(178, 253, 214));
            label = labelProgress;
        }

        if (table.getColumnName(column).equals("Retornos")){
            JLabelProgress labelProgress = new JLabelProgress(value.toString(), SwingConstants.CENTER);
            String[] valores = value.toString().split("/");
            labelProgress.setMaximo(new Integer(valores[0].trim()));
            labelProgress.setMinimo(new Integer(valores[1].trim()));            
            labelProgress.setProgressColor(new Color(253,167,100));
            label = labelProgress;
        }
        
        if (isSelected) {
            label.setForeground(table.getSelectionForeground());
            label.setBackground(table.getSelectionBackground());
        } else {
            label.setForeground(table.getForeground());
            label.setBackground(table.getBackground());
        }

        label.setFont(table.getFont());

        if (hasFocus) {
            Border border = null;
            if (isSelected) {
                border = UIManager.getBorder("Table.focusSelectedCellHighlightBorder");
            }
            if (border == null) {
                border = UIManager.getBorder("Table.focusCellHighlightBorder");
            }
           label.setBorder(border);

            if (!isSelected && table.isCellEditable(row, column)) {
                Color col;
                col = UIManager.getColor("Table.focusCellForeground");
                if (col != null) {
                    label.setForeground(col);
                }
                col = UIManager.getColor("Table.focusCellBackground");
                if (col != null) {
                    label.setBackground(col);
                }
            }
        } else {
            label.setBorder(new EmptyBorder(1, 1, 1, 1));
        }
        
        if (table.getColumnName(column).equals("Visita") && value instanceof Frequencia){
            Frequencia frequencia = (Frequencia) value;
            
            int coluna = table.getColumnModel().getColumnIndex("Corretor");
            String corretor = table.getValueAt(row, coluna).toString();    
            Usuario usuario = frequencia.getUsuario();
            
            if (!corretor.equals(usuario.getNome())) {
                String[] rgb = usuario.getCor().split(",");

                label.setBackground(new Color(new Integer(rgb[0]),new Integer(rgb[1]),new Integer(rgb[2])));
                label.setToolTipText(usuario.getNome());
            }
        }


        return label;
    }
}