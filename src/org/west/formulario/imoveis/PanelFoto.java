package org.west.formulario.imoveis;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import org.west.entidades.Foto;
import org.west.entidades.FotoDAO;
import org.west.utilitarios.Util;

public class PanelFoto extends javax.swing.JPanel {
    
    private Color colorOver = new Color(244, 192, 96);
    private Color colorNone = new Color(238,238,238);
    private Color colorPrincipal = new Color(176, 214, 241);
    
    private Foto foto;    
    private Boolean clicado = false;
    private Integer tamanhoPadrao = 100;
    private String pasta;

    public PanelFoto(Foto ft) {
        initComponents();
        
        this.foto = ft;
        
        if (foto.isPrincipal())
            setBackground(colorPrincipal);
        
        MouseAdapter listener = new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                
                if (!foto.isPrincipal())
                    setBackground(colorOver);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                
                if (!foto.isPrincipal() && !clicado)
                    setBackground(colorNone);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setClicado((Boolean) true);
                firePropertyChange("clicado",false,true);
            }
        };
        
        this.addMouseListener(listener);
        this.img.addMouseListener(listener);
        
        boxSite.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                foto.setSite(boxSite.isSelected());
                FotoDAO.save(foto);
            }
        });
        
        boxSite.setSelected(foto.isSite());
        
        pasta = Util.getPropriedade("west.foto_dir");
        ImageIcon icon = new ImageIcon(pasta + "\\" + foto.getCaminho());
        
        Double proporcao = new Double((double) icon.getIconWidth() / icon.getIconHeight());
        Integer largura = 0;
        Integer altura = 0;

        if (icon.getIconWidth() > icon.getIconHeight()) {
            largura = tamanhoPadrao;
            altura = new Double(largura / proporcao).intValue();
        } else {
            altura = tamanhoPadrao;
            largura = new Double(altura * proporcao).intValue();
        }
        
        img.setIcon(new ImageIcon(icon.getImage().getScaledInstance(largura, altura, Image.SCALE_SMOOTH)));
        
        //ALTERAR ISSO DEPOIS!!!!!!
        //img.setToolTipText(foto.getDescricao());
        
        img.setToolTipText(foto.getCaminho());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        boxSite = new javax.swing.JCheckBox();
        img = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(100, 100));
        setMinimumSize(new java.awt.Dimension(100, 100));
        setPreferredSize(new java.awt.Dimension(100, 100));
        setLayout(new java.awt.GridBagLayout());

        boxSite.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(boxSite, gridBagConstraints);

        img.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        img.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(img, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox boxSite;
    private javax.swing.JLabel img;
    // End of variables declaration//GEN-END:variables

    public Boolean isClicado() {
        return clicado;
    }

    public void setClicado(Boolean clicado) {
        this.clicado = clicado;
        
        if (!foto.isPrincipal()){
            if (this.clicado)
                setBackground(colorOver);
            else
                setBackground(colorNone);
        }
    }
    
    public Image getImagem(){
        return new ImageIcon(pasta + "\\" + foto.getCaminho()).getImage();
    }
    
    public Foto getFoto(){
        return this.foto;
    }
    
    public void setPrincipal(Boolean principal){
        foto.setPrincipal(principal);
        setBackground(colorPrincipal);
    }
}