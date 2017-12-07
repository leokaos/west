package org.west.formulario.imoveis;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.swing.border.EmptyBorder;
import org.west.entidades.Imovel;
import org.swingBean.descriptor.GenericFieldDescriptor;
import org.swingBean.descriptor.XMLDescriptorFactory;
import org.swingBean.gui.JBeanPanel;
import org.west.entidades.Usuario;

public class FrmUsuarioImovel extends javax.swing.JDialog {

    private JBeanPanel<Imovel> panel;
    private Set<Usuario> usuarios;
    
    public FrmUsuarioImovel(Frame frame,boolean modal,Imovel imovel) {
        super(frame,modal);
        
        initComponents();
        setLocationRelativeTo(null);
               
        GenericFieldDescriptor descriptor = XMLDescriptorFactory.getFieldDescriptor(Imovel.class, "org/west/xml/imovelFormUsuario.xml", "imovelFormUsuario");
        panel = new JBeanPanel<Imovel>(Imovel.class,"Usuários Captadores", descriptor);
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        add(panel,BorderLayout.CENTER);

        if (imovel != null){            
            try{
                usuarios = imovel.getUsuarios();
                usuarios.size();
                
                panel.setBean(imovel);
            }
            catch(Exception ex){ex.printStackTrace();}            
        }
    }

    public Set<Usuario> getUsuarios(){
        usuarios = new HashSet<Usuario>((Collection) panel.getPropertyValue("usuarios"));
        return usuarios;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sistema West - Usuários");
        setMinimumSize(new java.awt.Dimension(350, 260));
        setResizable(false);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}