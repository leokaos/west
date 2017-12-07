package org.west.componentes;

import java.awt.BorderLayout;
import java.awt.Color;
import org.west.entidades.Imovel;
import org.west.entidades.WestPersistentManager;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import org.hibernate.SQLQuery;
import org.swingBean.descriptor.look.LookProvider;
import org.west.utilitarios.Util;

public class PanelEstatistica extends javax.swing.JPanel {

    private Imovel imovel;

    public PanelEstatistica(Imovel imovel) {
        initComponents();

        this.imovel = imovel;

        if (this.imovel != null)
            drawEstatisticas();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Retorno de Ficha", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12), new java.awt.Color(0, 70, 213))); // NOI18N
        setMaximumSize(new java.awt.Dimension(150, 180));
        setMinimumSize(new java.awt.Dimension(150, 180));
        setPreferredSize(new java.awt.Dimension(150, 180));
        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    private void drawEstatisticas() {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        String inicio = format.format(Util.corrigirDate(Util.addDias(new Date(), -30),Util.INICIO));
        String fim = format.format(Util.corrigirDate(new Date(),Util.FIM));

        String query = "SELECT I.anuncio,count(I.anuncio),I.veiculo,count(I.veiculo)," +
                       "sum(case when I.dataEntrada BETWEEN '" + inicio + "' AND '" + fim + "' then 1 else 0 end)" +
                       " FROM tab_imobiliaria I WHERE I.imovel = " + getImovel().getReferencia() + " GROUP BY I.anuncio;";
                      
        try{
            SQLQuery comando = WestPersistentManager.getSession().createSQLQuery(query);
            
            List listaValores = comando.list();          
            
            if (!listaValores.isEmpty()) { 
                
                setLayout(new GridBagLayout());
                GridBagConstraints cons = new GridBagConstraints();
                cons.insets = new Insets(2, 2, 2, 2);
                cons.fill = GridBagConstraints.BOTH;
                cons.ipady = 10;

                int gridy = 0;                
                
                for (Object object : listaValores) {
                    Object[] valores = (Object[]) object;
                    
                    JLabel titulo = new JLabel();
                    JLabelProgress label = new JLabelProgress("", SwingConstants.CENTER);
                    label.setProgressColor(new Color(83,190,127));
                    label.setOpaque(false);
                    titulo.setFont(LookProvider.getLook().getTextFont());
                    
                    if (valores[0] == null){
                        titulo.setText(valores[2].toString() + ": ");
                        label.setText(valores[3].toString() + " / " + valores[4].toString());
                        label.setMaximo(new Integer(valores[3].toString()));
                        label.setMinimo(new Integer(valores[4].toString()));
                    }     
                    else{
                        titulo.setText(valores[0].toString() + ": ");
                        label.setText(valores[1].toString() + " / " + valores[4].toString());
                        label.setMaximo(new Integer(valores[1].toString()));
                        label.setMinimo(new Integer(valores[4].toString()));                        
                    }
                    
                    cons.gridy = gridy;
                    
                    cons.gridx = 0;    
                    cons.weightx = 0;
                    add(titulo,cons);
                    
                    cons.gridx = 1;
                    cons.weightx = 1;
                    add(label,cons);
                    
                    gridy++;
                }                
            }
            else{
                setLayout(new BorderLayout());
                add(new JLabel("Imóvel sem retorno!",SwingConstants.CENTER),BorderLayout.CENTER);
            }
        }
        catch(Exception ex){ex.printStackTrace();}
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    public Imovel getImovel() {
        return imovel;
    }

    public void setImovel(Imovel imovel) {
        this.imovel = imovel;
        cleanPanel();
        drawEstatisticas();
        repaint();
    }

    public void cleanPanel(){
        removeAll();
        revalidate();
        repaint();
    }
}