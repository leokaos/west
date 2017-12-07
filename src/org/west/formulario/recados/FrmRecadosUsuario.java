package org.west.formulario.recados;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Arrays;
import java.util.Date;
import org.west.utilitarios.RenderRecados;
import org.west.entidades.Recado;
import org.west.entidades.Usuario;
import org.west.formulario.FrmMain;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.swingBean.actions.ApplicationAction;
import org.swingBean.descriptor.BeanTableModel;
import org.swingBean.descriptor.GenericFieldDescriptor;
import org.swingBean.descriptor.TableFieldDescriptor;
import org.swingBean.descriptor.XMLDescriptorFactory;
import org.swingBean.gui.JActButton;
import org.swingBean.gui.JBeanPanel;
import org.swingBean.gui.JBeanTable;
import org.west.componentes.DesktopSession;
import org.west.entidades.RecadoDAO;
import org.west.entidades.WestPersistentManager;

public class FrmRecadosUsuario extends JDialog {

    private TableFieldDescriptor descriptorTable;
    private BeanTableModel<Recado> modeloRecado;
    private JBeanTable tabelaRecado;
    private GenericFieldDescriptor descriptorForm;
    private JBeanPanel<Recado> formRecado;
    private RenderRecados render;
    private List<Recado> listaRecados;
    private Usuario usuario;
    private Recado recadoAtual;

    public FrmRecadosUsuario(final FrmMain frame) {
        super((JFrame)frame,true);
        
        initComponents();
        
        WestPersistentManager.clear();
        
        setSize(600,400);
        setLocationRelativeTo(null);
        
        this.usuario = (Usuario) DesktopSession.getInstance().getObject("usuario");
        this.recadoAtual = null;
        
        GridBagConstraints cons = new GridBagConstraints();
        cons.insets = new Insets(5, 5, 5, 5); 
        cons.weightx = 1;
        cons.weighty = 1;
        cons.fill = GridBagConstraints.BOTH;
              
        this.descriptorTable = XMLDescriptorFactory.getTableFieldDescriptor(Recado.class, "org/west/xml/recadoTabelaCorretor.xml", "tabelarecadocorretor");
        this.modeloRecado = new BeanTableModel(this.descriptorTable);
        this.tabelaRecado = new JBeanTable(this.modeloRecado);
        
        modeloRecado.setBeanList(Arrays.asList(RecadoDAO.listRecadoByQuery("usuario=" + this.usuario.getCodigo(), "entrada desc")));
        
        cons.gridx = 0;
        cons.gridy = 1;
        cons.gridheight = 2;        
        this.add(new JScrollPane(tabelaRecado),cons);
        
        this.render = new RenderRecados(modeloRecado.getCompleteList());
        this.tabelaRecado.getColumnModel().getColumn(0).setCellRenderer(this.render);
        
        this.tabelaRecado.addDoubleClickAction(new ApplicationAction() {

            @Override
            public void execute() {
                Recado recado = (Recado) modeloRecado.getBeanAt(tabelaRecado.getSelectedRow());
                formRecado.setBean(recado);
                recadoAtual = recado;
            }
        });
        
        this.descriptorForm = XMLDescriptorFactory.getFieldDescriptor(Recado.class, "org/west/xml/recadoFormCorretor.xml", "recadocorretor");
        this.formRecado = new JBeanPanel(Recado.class,"Recados de " + usuario.getNome(), this.descriptorForm);  
        
        cons.gridx = 1;
        cons.gridy = 1;
        cons.gridheight = 1;
        cons.weightx = 0.2;
        this.add(formRecado,cons);
        
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        
        JActButton lerRecado = new JActButton("   Lido   ", new ApplicationAction() {

            @Override
            public void execute() {
                if (recadoAtual == null)
                    JOptionPane.showMessageDialog(null, "Não foi selecionado um recado!", "Erro", JOptionPane.ERROR_MESSAGE);
                else{
                    recadoAtual.setLeitura(new Date());
                    
                    if (RecadoDAO.save(recadoAtual)){
                        frame.verificaRecados();
                        modeloRecado.setBeanList(Arrays.asList(RecadoDAO.listRecadoByQuery("usuario=" + usuario.getCodigo(), "entrada desc")));
                        JOptionPane.showMessageDialog(null, "Recado salvo!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else
                        JOptionPane.showMessageDialog(null, "Erro ao salvar recado!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JActButton cancelar = new JActButton("Cancelar", new ApplicationAction() {

            @Override
            public void execute() {
                dispose();
            }
        });
        
        panelBotoes.add(lerRecado);
        panelBotoes.add(cancelar);        
        
        cons.gridx = 1;
        cons.gridy = 2;
        cons.weighty = 0;
        cons.fill = GridBagConstraints.HORIZONTAL;
        this.add(panelBotoes,cons);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sistema West - Recados");
        setMinimumSize(new java.awt.Dimension(600, 400));
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Ubuntu", 1, 16)); // NOI18N
        jLabel1.setText("Recados");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}