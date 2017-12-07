package org.west.formulario.portaria;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.hibernate.criterion.Restrictions;
import org.swingBean.actions.ActionChainFactory;
import org.swingBean.actions.ApplicationAction;
import org.swingBean.actions.ValidationAction;
import org.swingBean.descriptor.GenericFieldDescriptor;
import org.swingBean.descriptor.XMLDescriptorFactory;
import org.swingBean.gui.JActButton;
import org.west.entidades.Planta;
import org.west.entidades.Portaria;
import org.swingBean.gui.JBeanPanel;
import org.west.entidades.Cep;
import org.west.entidades.Lazer;
import org.west.entidades.PlantaDAO;
import org.west.entidades.PortariaCriteria;
import org.west.entidades.PortariaDAO;

public class FrmPortaria extends javax.swing.JDialog {

    private JBeanPanel<Portaria> formPortaria;
    private JBeanPanel<Planta> formPlantas;
    private JBeanPanel<Portaria> formPortariaLazer;
    private Portaria portaria;

    public FrmPortaria(java.awt.Frame parent, boolean modal, Portaria port) {
        super(parent, modal);
        initComponents();

        this.portaria = port;

        setSize(800, 550);
        setLocationRelativeTo(null);

        GridBagConstraints cons = new GridBagConstraints();
        cons.insets = new Insets(5, 5, 5, 5);
        cons.fill = GridBagConstraints.BOTH;
        cons.gridx = 0;

        GenericFieldDescriptor descriptor = XMLDescriptorFactory.getFieldDescriptor(Portaria.class, "org/west/xml/portariaFormIncluir.xml", "portariaFormIncluir");
        formPortaria = new JBeanPanel<Portaria>(Portaria.class, "Informações da Portaria", descriptor);

        cons.gridy = 0;
        add(formPortaria, cons);

        descriptor = XMLDescriptorFactory.getFieldDescriptor(Portaria.class, "org/west/xml/portariaLazeres.xml", "portariaLazeres");
        formPortariaLazer = new JBeanPanel<Portaria>(Portaria.class, "Lazeres da Portaria", descriptor);
        formPortariaLazer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelLazeres.add(formPortariaLazer, BorderLayout.CENTER);

        descriptor = XMLDescriptorFactory.getFieldDescriptor(Planta.class, "org/west/xml/plantaForm.xml", "plantaForm");
        formPlantas = new JBeanPanel<Planta>(Planta.class, "Informações de Plantas", descriptor);
        ((TitledBorder) formPlantas.getBorder()).setBorder(new EmptyBorder(5, 5, 5, 5));

        panelPlantas.add(formPlantas, BorderLayout.CENTER);

        listaPlanta.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (listaPlanta.getSelectedIndex() > -1) {
                    formPlantas.setBean((Planta) listaPlanta.getModel().getElementAt(listaPlanta.getSelectedIndex()));
                }
            }
        });

        //botoes da planta
        ApplicationAction actionGravaPlanta = new ApplicationAction() {
            @Override
            public void execute() {
                Planta planta = new Planta();
                formPlantas.populateBean(planta);

                if (portaria.getCodigo() == null) {
                    JOptionPane.showMessageDialog(null, "Portaria não foi salva!");
                } else {
                    planta.setPortaria(portaria);
                    if (PlantaDAO.save(planta)) {
                        JOptionPane.showMessageDialog(null, "Planta salva com sucesso!");
                        carregaPlantas();
                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao salvar planta!");
                    }
                }
            }
        };

        ValidationAction validaAction = new ValidationAction(formPlantas);

        JActButton btnGravaPlanta = new JActButton("", ActionChainFactory.createChainActions(validaAction, actionGravaPlanta));
        btnGravaPlanta.setIcon(new ImageIcon(getClass().getResource("/org/west/imagens/save.png")));
        btnGravaPlanta.setToolTipText("Gravar planta");

        JActButton btnDeletaPlanta = new JActButton("", new ApplicationAction() {
            @Override
            public void execute() {
                if (listaPlanta.getSelectedValues().length > 0) {

                    for (Object obj : listaPlanta.getSelectedValues()) {
                        Planta planta = (Planta) obj;
                        PlantaDAO.delete(planta);
                    }

                    carregaPlantas();
                } else {
                    JOptionPane.showMessageDialog(null, "Não foi selecionado nenhuma planta!");
                }
            }
        });

        btnDeletaPlanta.setIcon(new ImageIcon(getClass().getResource("/org/west/imagens/cancel.png")));
        btnDeletaPlanta.setToolTipText("Deletar planta");

        JActButton btnLimpaPlanta = new JActButton("", new ApplicationAction() {
            @Override
            public void execute() {
                formPlantas.cleanForm();
            }
        });
        btnLimpaPlanta.setIcon(new ImageIcon(getClass().getResource("/org/west/imagens/limpar.png")));
        btnLimpaPlanta.setToolTipText("Limpar dados");

        cons = new GridBagConstraints();
        cons.insets = new Insets(7, 7, 7, 7);
        cons.gridx = 0;
        cons.weightx = 1;
        cons.weighty = 1;

        cons.gridy = 0;
        cons.anchor = GridBagConstraints.PAGE_END;
        panelBotoesPlanta.add(btnGravaPlanta, cons);
        cons.gridy = 1;
        cons.weighty = 0;
        panelBotoesPlanta.add(btnDeletaPlanta, cons);
        cons.gridy = 2;
        cons.weighty = 1;
        cons.anchor = GridBagConstraints.PAGE_START;
        panelBotoesPlanta.add(btnLimpaPlanta, cons);

        if (portaria != null) {
            carregaPlantas();
            carregaPortaria();
        } else {
            portaria = new Portaria();
            portaria.setPrioridade(false);
        }

        ((JTextField) formPortaria.getComponent("numero")).addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                Cep cep = (Cep) formPortaria.getPropertyValue("cep");

                try {
                    PortariaCriteria criteria = new PortariaCriteria();
                    criteria.add(Restrictions.eq("numero", formPortaria.getPropertyValue("numero").toString()));
                    criteria.createCepCriteria().
                            add(Restrictions.eq("descricao", cep.getDescricao())).add(Restrictions.eq("tipo", cep.getTipo()));

                    if (criteria.list().size() < 0) {
                        JOptionPane.showMessageDialog(null, "Portaria já existe!");
                        formPortaria.cleanForm();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        ((JPanel) formPortaria.getComponent("cep")).getComponent(0).addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);

                Cep cep = (Cep) formPortaria.getPropertyValue("cep");

                if (cep != null) {
                    formPortaria.setPropertyValue("endereco", cep.getTipo() + " " + cep.getDescricao());
                } else {
                    formPortaria.setPropertyValue("endereco", "");
                }
            }
        });

        formPortaria.getComponent("endereco").addPropertyChangeListener("selecionado", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Cep cep = (Cep) formPortaria.getPropertyValue("endereco");
                formPortaria.setPropertyValue("endereco", cep.toString());
                formPortaria.setPropertyValue("cep", cep);
            }
        });

        //botoes da portaria
        ApplicationAction actionGravaPortaria = new ApplicationAction() {
            @Override
            public void execute() {

                try {
                    formPortaria.populateBean(portaria);
                    portaria.setLazer(getLazeres());

                    if (portaria.isNovo() && hasPortaria(portaria)) {
                        JOptionPane.showMessageDialog(null, "Portaria já existe!", "Erro", JOptionPane.ERROR_MESSAGE);
                    } else {
                        if (PortariaDAO.save(portaria)) {
                            JOptionPane.showMessageDialog(null, "Portaria salva com sucesso!");
                            PortariaDAO.refresh(portaria);
                            formPortaria.setBean(portaria);
                        } else {
                            JOptionPane.showMessageDialog(null, "Erro ao salvar portaria!");
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            private boolean hasPortaria(Portaria portaria) {
                PortariaCriteria criteria = new PortariaCriteria();

                criteria.add(Restrictions.eq("numero", portaria.getNumero()));
                criteria.createCepCriteria().add(Restrictions.eq("descricao", portaria.getCep().getDescricao()));

                if (!criteria.listPortarias().isEmpty()) {
                    return true;
                }

                return false;
            }
        };

        ValidationAction validaActionPortaria = new ValidationAction(formPortaria);

        JActButton btnGravarPortaria = new JActButton("Gravar", ActionChainFactory.createChainActions(validaActionPortaria, actionGravaPortaria));

        panelBotoes.add(btnGravarPortaria);

        JActButton btnLimparPortaria = new JActButton("Limpar", new ApplicationAction() {
            @Override
            public void execute() {
                formPortaria.cleanForm();
                formPlantas.cleanForm();
                formPortariaLazer.cleanForm();
                listaPlanta.setModel(new DefaultListModel());
                portaria = new Portaria();
            }
        });

        panelBotoes.add(btnLimparPortaria);
    }

    private void carregaPlantas() {
        PortariaDAO.refresh(portaria);

        DefaultListModel model = new DefaultListModel();

        if (portaria.getCodigo() != null) {
            for (Planta planta : portaria.getPlantas()) {
                model.addElement(planta);
            }
        }

        listaPlanta.setModel(model);
    }

    private void carregaPortaria() {
        PortariaDAO.refresh(portaria);
        formPortaria.setBean(portaria);
        formPortariaLazer.setBean(portaria);
    }

    private Set<Lazer> getLazeres() {
        Set<Lazer> lazer = new HashSet<Lazer>();

        for (Object obj : (Collection) formPortariaLazer.getPropertyValue("lazer")) {
            lazer.add((Lazer) obj);
        }

        return lazer;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jTabbedPane1 = new javax.swing.JTabbedPane();
        panelPlantas = new javax.swing.JPanel();
        panelBotoesPlanta = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listaPlanta = new javax.swing.JList();
        panelLazeres = new javax.swing.JPanel();
        panelBotoes = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sistema West");
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panelPlantas.setLayout(new java.awt.BorderLayout());

        panelBotoesPlanta.setLayout(new java.awt.GridBagLayout());
        panelPlantas.add(panelBotoesPlanta, java.awt.BorderLayout.LINE_END);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setViewportView(listaPlanta);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel2.add(jScrollPane1, gridBagConstraints);

        panelPlantas.add(jPanel2, java.awt.BorderLayout.LINE_START);

        jTabbedPane1.addTab("Plantas", panelPlantas);

        panelLazeres.setLayout(new java.awt.BorderLayout());
        jTabbedPane1.addTab("Lazeres", panelLazeres);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTabbedPane1, gridBagConstraints);

        panelBotoes.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(panelBotoes, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JList listaPlanta;
    private javax.swing.JPanel panelBotoes;
    private javax.swing.JPanel panelBotoesPlanta;
    private javax.swing.JPanel panelLazeres;
    private javax.swing.JPanel panelPlantas;
    // End of variables declaration//GEN-END:variables
}