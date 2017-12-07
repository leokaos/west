package org.west.componentes.tabelafilter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

public class HeaderRender extends DefaultTableCellRenderer{

    private Map<Integer,Integer> mapaOrder;
    private Map<Integer,List<String>> mapaFiltro;

    public HeaderRender(Map<Integer, Integer> mapa1,Map<Integer,List<String>> mapa2) {
        this.mapaOrder = mapa1;
        this.mapaFiltro = mapa2;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel order = new JLabel();
        JLabel filter = new JLabel();
        JLabel texto = new JLabel();

        filter.setHorizontalAlignment(SwingConstants.CENTER);
        order.setHorizontalAlignment(SwingConstants.CENTER);
        texto.setHorizontalAlignment(SwingConstants.CENTER);

        if (mapaOrder.containsKey(column)){
            Integer status = mapaOrder.get(column);

            if (status == 0)
                order.setIcon(new ImageIcon(getClass().getResource("/org/west/imagens/setaBaixo.png")));
            else
                order.setIcon(new ImageIcon(getClass().getResource("/org/west/imagens/setaCima.png")));
        }
        else
            order.setIcon(null);

        order.setPreferredSize(new Dimension(16,0));
        panel.add(order,BorderLayout.WEST);

        if (mapaFiltro.containsKey(column))
            filter.setIcon(new ImageIcon(getClass().getResource("/org/west/imagens/filter.png")));
        else
            filter.setIcon(null);

        filter.setPreferredSize(new Dimension(16,0));
        panel.add(filter,BorderLayout.EAST);
        
        texto.setHorizontalAlignment(JLabel.CENTER);
        texto.setFont(texto.getFont().deriveFont(Font.BOLD));
        
        if (column == 1)
           texto.setIcon(new ImageIcon(getClass().getResource("/org/west/imagens/exclamation.png")));
        else
           texto.setText(value.toString());

        panel.add(texto,BorderLayout.CENTER);

        Border headerBorder = UIManager.getBorder("TableHeader.cellBorder");
        
        panel.setBackground(new Color(242,241,240));
        panel.setPreferredSize(new Dimension(0, 30));
        panel.setBorder(headerBorder);
        panel.revalidate();
        panel.repaint();

        return panel;
    }
}