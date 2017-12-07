package org.west.formulario.imoveis;

import java.util.Arrays;
import java.util.List;
import org.west.componentes.PanelAnuncio;
import org.west.entidades.Anuncio;
import org.west.entidades.AnuncioDAO;
import org.west.entidades.Imovel;

public class FrmInfoAnuncios extends javax.swing.JDialog {
    
    private static final long serialVersionUID = -6710701963070602140L;

    private Imovel imovel;
    private List<Anuncio> listaAnuncios;

    public FrmInfoAnuncios(java.awt.Frame parent, boolean modal, Imovel imovel) {
        super(parent, modal);
        this.imovel = imovel;

        initComponents();

        setSize(800, 600);
        setLocationRelativeTo(null);

        Anuncio[] anuncios = AnuncioDAO.listAnuncioByQuery("exige_informacao = true", "nome desc");

        listaAnuncios = Arrays.asList(anuncios);

        for (Anuncio anuncio : listaAnuncios) {
            jTabbedPane1.addTab(anuncio.getNome(), new PanelAnuncio(imovel, anuncio));
        }
    }

    public void setAnuncioInicial(Anuncio anuncio) {
        jTabbedPane1.setSelectedIndex(listaAnuncios.indexOf(anuncio));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jTabbedPane1 = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sistema West - Anúncio: " + String.valueOf(imovel.getReferencia()));
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTabbedPane1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}