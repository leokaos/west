package org.west.componentes;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.border.EmptyBorder;
import net.java.balloontip.BalloonTip;
import org.swingBean.descriptor.GenericFieldDescriptor;
import org.swingBean.descriptor.XMLDescriptorFactory;
import org.swingBean.gui.JBeanPanel;
import org.west.entidades.Imovel;
import org.west.utilitarios.ToolTipBalloonStyle;

public class TipImovel extends BalloonTip{

    private Imovel imovel;
    
    public TipImovel(JComponent attachedComponent, String text,Imovel imovel) {
        super(attachedComponent, text);
        this.imovel = imovel;
              
        configurarVisu();
    }
    
    private void configurarVisu() {
        setStyle(new ToolTipBalloonStyle(new Color(236, 241, 140), new Color(161, 241, 140)));
        
        setVisible(false);
        setSize(700, 400);
        
        GridBagConstraints cons = new GridBagConstraints();
        cons.insets = new Insets(5, 5, 5, 5);
        cons.gridx = 0;
        cons.gridy = 0;
        cons.fill = GridBagConstraints.BOTH;
        cons.weightx = 1;
        cons.weighty = 1;
        cons.gridwidth = 2;

        PanelVisuImovel visu = new PanelVisuImovel(imovel);
        visu.setBorder(new EmptyBorder(5, 5, 5, 5));
        visu.setOpaque(false);
        this.add(visu,cons);
        
        cons.gridy = 2;
        cons.gridwidth = 1;
        cons.weightx = 0;
        cons.weighty = 0;
        
        PanelEstatistica panelEstatistica = new PanelEstatistica(imovel);
        panelEstatistica.setOpaque(false);
        this.add(panelEstatistica,cons);
        
        cons.gridx = 1;
        GenericFieldDescriptor descriptor = XMLDescriptorFactory.getFieldDescriptor(Imovel.class, "org/west/xml/anuncioImovelForm.xml", "anuncioImovelForm");
        JBeanPanel<Imovel> formAnuncio = new JBeanPanel<Imovel>(Imovel.class, "Anunciado em", descriptor);
        formAnuncio.setOpaque(false);
        setOpaqueForAll(formAnuncio);
        this.add(formAnuncio, cons);          
    }
    
    private void setOpaqueForAll(JComponent parent){        
        for(Component comp : parent.getComponents()){
            if (comp instanceof JComponent){
                JComponent comps = (JComponent) comp;
                comps.setOpaque(false);  
                if (comps.getComponents().length > 0)
                    setOpaqueForAll(comps);
            }
        }
    }
}