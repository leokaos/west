package org.west.componentes.tabelafilter;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JCheckBox;
import org.jfree.ui.tabbedui.VerticalLayout;

public class PanelFilter extends javax.swing.JPanel {

    private List selecionados = new ArrayList();
    private JCheckBox boxTodos;

    public PanelFilter(List lista,List selec) {
        initComponents();

        VerticalLayout layout = new VerticalLayout();
        this.setBorder(null);
        this.setLayout(layout);
        int total = 24;

        boxTodos = new JCheckBox("Todos");
        boxTodos.setOpaque(false);

        boxTodos.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                selecionados = null;
            }
        });

        this.add(boxTodos);

        this.selecionados = (selec == null)? new ArrayList() : selec;

        for (Iterator it = lista.iterator(); it.hasNext();) {
            final Object obj = it.next();
             
            final JCheckBox box = new JCheckBox((obj.toString().isEmpty())?"(Vazio)":obj.toString());
            box.setOpaque(false);
            box.setFont(box.getFont().deriveFont(Font.PLAIN));

            if (selec != null && selec.contains(obj))
                box.setSelected(true);

            box.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (selecionados == null)
                        selecionados = new ArrayList();

                    boxTodos.setSelected(false);

                    if (selecionados.contains(obj))
                        selecionados.remove(obj);
                    else
                        selecionados.add(obj);
                }
            });

            add(box);
        }

        this.setPreferredSize(new Dimension(0,total * (lista.size() + 1)));
    }

    public List getSelecionados(){
        return selecionados;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(null);
        setPreferredSize(new java.awt.Dimension(0, 100));
        setLayout(new java.awt.GridBagLayout());
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}