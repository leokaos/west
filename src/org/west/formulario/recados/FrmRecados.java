package org.west.formulario.recados;

import org.west.componentes.JMSManager;
import org.west.entidades.Recado;
import org.west.entidades.RecadoDAO;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.swingBean.actions.ActionChainFactory;
import org.swingBean.actions.ApplicationAction;
import org.swingBean.actions.ValidationAction;
import org.swingBean.descriptor.BeanTableModel;
import org.swingBean.descriptor.GenericFieldDescriptor;
import org.swingBean.descriptor.TableFieldDescriptor;
import org.swingBean.descriptor.XMLDescriptorFactory;
import org.swingBean.gui.JActButton;
import org.swingBean.gui.JBeanPanel;
import org.swingBean.gui.JBeanTable;
import org.west.entidades.Usuario;
import org.west.entidades.UsuarioDAO;

public class FrmRecados extends javax.swing.JFrame {

    private final TableFieldDescriptor descriptorTable;
    private final BeanTableModel<Recado> modeloRecado;
    private final JBeanTable tabelaRecado;
    private final GenericFieldDescriptor descriptorForm;
    private final JBeanPanel<Recado> panelRecado;
    private List<Recado> listaRecados;

    public FrmRecados() {
        initComponents();
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints cons = new GridBagConstraints();
        cons.insets = new Insets(10, 10, 10, 10);
        cons.weighty = 1;
        cons.weightx = 1;
        cons.fill = 1;

        cons.gridx = 0;
        cons.gridy = 0;
        cons.ipadx = 200;
        cons.gridheight = 2;

        this.descriptorTable = XMLDescriptorFactory.getTableFieldDescriptor(Recado.class, "org/west/xml/recadoTabelaRecepcao.xml", "tabelaRecado");
        this.modeloRecado = new BeanTableModel(this.descriptorTable);
        this.tabelaRecado = new JBeanTable(this.modeloRecado);

        getContentPane().add(new JScrollPane(this.tabelaRecado), cons);

        this.tabelaRecado.addDoubleClickAction(new ApplicationAction() {

            @Override
            public void execute() {
                panelRecado.setBean(modeloRecado.getBeanAt(tabelaRecado.getSelectedRow()));
            }
        });
        this.listaRecados = this.modeloRecado.getSynchronizedList();

        this.listaRecados = Arrays.asList(RecadoDAO.listRecadoByQuery("codigo > 0", "entrada desc"));

        this.modeloRecado.setBeanList(this.listaRecados);

        cons.gridx = 1;
        cons.ipadx = 0;
        cons.gridheight = 1;
        cons.ipadx = 0;
        cons.weightx = 0.5;

        this.descriptorForm = XMLDescriptorFactory.getFieldDescriptor(Recado.class, "org/west/xml/recadoFormRecepcao.xml", "recado");
        this.panelRecado = new JBeanPanel(Recado.class,"Recados", this.descriptorForm);

        getContentPane().add(this.panelRecado, cons);

        JPanel panelBotoes = new JPanel(new FlowLayout(2, 10, 5));

        ApplicationAction gravaRecado = new ApplicationAction() {

            @Override
            public void execute() {
                Recado recado = new Recado();
                panelRecado.populateBean(recado);
                recado.setEntrada(new Date());

                RecadoDAO.save(recado);
                
                if (recado.getUsuario().getCodigo() == 25){
                    Usuario usuario = UsuarioDAO.get(64l);
                    Recado copia = new Recado();
                    copia.setDescricao("Para Maurício: " + recado.getDescricao());
                    copia.setEntrada(recado.getEntrada());
                    copia.setUsuario(usuario);

                    RecadoDAO.save(copia);
                    
                    JMSManager.enviarObjectMensagem(null, usuario.getNome(), "recado");
                    
                    modeloRecado.addBean(copia);
                }

                panelRecado.cleanForm();

                JMSManager.enviarObjectMensagem(null, recado.getUsuario().getNome(), "recado");

                modeloRecado.addBean(recado);
            }
        };
        ValidationAction validaImob = new ValidationAction(new JBeanPanel[]{this.panelRecado});

        ApplicationAction valida = ActionChainFactory.createChainActions(new ApplicationAction[]{validaImob, gravaRecado});

        JActButton botaoGravaRecado = new JActButton("Gravar", valida);

        JActButton botaoLimpar = new JActButton("Limpar", new ApplicationAction() {

            @Override
            public void execute() {
                panelRecado.cleanForm();
            }
        });
        panelBotoes.add(botaoGravaRecado);
        panelBotoes.add(botaoLimpar);

        cons.gridx = 1;
        cons.gridy = 1;
        cons.weighty = 0;
        cons.fill = 2;

        getContentPane().add(panelBotoes, cons);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sistema West - Anotar Recados");
        setMinimumSize(new java.awt.Dimension(400, 300));
        setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 700, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}