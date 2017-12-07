package org.west.formulario.portaria;

import org.west.componentes.DesktopSession;
import org.west.utilitarios.Util;
import org.west.entidades.FrequenciaCriteria;
import org.west.entidades.Portaria;
import org.west.entidades.PortariaCriteria;
import org.west.entidades.Usuario;
import org.west.entidades.WestPersistentManager;
import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.swingBean.descriptor.BeanTableModel;
import org.swingBean.descriptor.TableFieldDescriptor;
import org.swingBean.descriptor.XMLDescriptorFactory;
import org.swingBean.gui.JBeanTable;
import org.west.entidades.Cartao;
import org.west.entidades.CartaoDAO;
import org.west.entidades.Frequencia;
import org.west.entidades.FrequenciaDAO;

public class FrmFrequencia extends javax.swing.JFrame {

    private Usuario usuario;
    private TableFieldDescriptor descriptor;
    private BeanTableModel model;
    private JBeanTable tabela;

    public FrmFrequencia() {
        initComponents();
        setLocationRelativeTo(null);
        this.usuario = (Usuario) DesktopSession.getInstance().getObject("usuario");

        descriptor = XMLDescriptorFactory.getTableFieldDescriptor(Portaria.class, "org/west/xml/portariaTabelaFrequencia.xml", "Frequencia");
        model = new BeanTableModel(descriptor);
        tabela = new JBeanTable(model);
        tabela.enableHeaderOrdering();

        jPanel1.add(new JScrollPane(tabela), BorderLayout.CENTER);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date ultima = getUltimaData();
        lblData.setText(format.format(ultima));

        model.setBeanList(getListaPortaria(false));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblData = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        dateValida = new com.toedter.calendar.JDateChooser();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        radioGeral = new javax.swing.JRadioButton();
        radioPessoal = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sistema West - Controle de Frequência");
        setMinimumSize(new java.awt.Dimension(700, 600));
        setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Verdana", 1, 14));
        jLabel1.setText("Controle de Frequência");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel1, gridBagConstraints);

        jLabel2.setText("Ultima data feita:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel2, gridBagConstraints);

        lblData.setFont(new java.awt.Font("Tahoma", 1, 11));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 7, 5);
        getContentPane().add(lblData, gridBagConstraints);

        jLabel3.setText("Próxima data válida:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel3, gridBagConstraints);

        dateValida.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 60;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(dateValida, gridBagConstraints);

        jButton1.setText("Cancelar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jButton1, gridBagConstraints);

        jButton2.setText("Gravar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jButton2, gridBagConstraints);

        jPanel1.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jPanel1, gridBagConstraints);

        buttonGroup1.add(radioGeral);
        radioGeral.setText("Todas");
        radioGeral.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                radioGeralItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        getContentPane().add(radioGeral, gridBagConstraints);

        buttonGroup1.add(radioPessoal);
        radioPessoal.setSelected(true);
        radioPessoal.setText("Pessoais");
        radioPessoal.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                radioPessoalItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        getContentPane().add(radioPessoal, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (dateValida.getDate() != null) {

            new Thread(new Runnable() {

                @Override
                public void run() {
                    jButton2.setEnabled(false);
                    List<Portaria> lista = new ArrayList<Portaria>();

                    int[] linhas = tabela.getSelectedRows();

                    for (int x = 0; x < linhas.length; x++) {
                        Portaria portaria = (Portaria) model.getBeanAt(linhas[x]);
                        lista.add(portaria);
                    }

                    int op = JOptionPane.showConfirmDialog(null, getListaFormatada(lista), "Confirmar", JOptionPane.OK_CANCEL_OPTION);

                    if (op == 0) {

                        boolean salvo = false;

                        WestPersistentManager.clear();

                        try {

                            for (Iterator<Portaria> it = lista.iterator(); it.hasNext();) {
                                Portaria portaria = it.next();

                                Frequencia frequencia = new Frequencia();
                                frequencia.setDataVisita(dateValida.getDate());
                                frequencia.setPortaria(portaria);
                                frequencia.setUsuario(usuario);

                                salvo = FrequenciaDAO.save(frequencia);

                                if (!salvo) {
                                    break;
                                }
                            }

                            if (salvo) {
                                Cartao cartao = usuario.getCartao();

                                if (cartao == null) {
                                    cartao = new Cartao();
                                    cartao.setQuantidade(0L);
                                    cartao.setUsuario(usuario);
                                    CartaoDAO.save(cartao);
                                }

                                Long quantidade = cartao.getQuantidade();
                                quantidade -= lista.size() * 10;
                                cartao.setQuantidade(quantidade);

                                if (!CartaoDAO.save(cartao)) {
                                    JOptionPane.showMessageDialog(null, "Erro ao atualizar o estoque de cartões!");
                                } else {
                                    JOptionPane.showMessageDialog(null, "Registros salvos com sucesso!");
                                    dispose();
                                }
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Erro ao salvar frequência!");
                        }

                        jButton2.setEnabled(true);
                    } else {
                        jButton2.setEnabled(true);
                    }
                }
            }).start();

        } else {
            JOptionPane.showMessageDialog(null, "Selecione a data de relatório!");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void radioPessoalItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_radioPessoalItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            model.setBeanList(getListaPortaria(false));
        }
    }//GEN-LAST:event_radioPessoalItemStateChanged

    private void radioGeralItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_radioGeralItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            model.setBeanList(getListaPortaria(true));
        }
    }//GEN-LAST:event_radioGeralItemStateChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private com.toedter.calendar.JDateChooser dateValida;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblData;
    private javax.swing.JRadioButton radioGeral;
    private javax.swing.JRadioButton radioPessoal;
    // End of variables declaration//GEN-END:variables

    private Date getUltimaData() {
        Date data = Util.addDias(new Date(), -1);

        try {
            FrequenciaCriteria criteria = new FrequenciaCriteria();

            ProjectionList projections = Projections.projectionList();
            projections.add(Projections.max("dataVisita"));

            criteria.add(Restrictions.eq("usuario", usuario));
            criteria.setProjection(projections);

            if ((Date) criteria.uniqueResult() != null) {
                data = (Date) criteria.uniqueResult();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return data;
    }

    private List getListaPortaria(boolean geral) {
        List lista = new ArrayList();

        try {
            WestPersistentManager.clear();

            PortariaCriteria criteria = new PortariaCriteria();

            if (!geral) {
                criteria.add(Restrictions.eq("usuario", DesktopSession.getInstance().getObject("usuario")));
            }

            lista = criteria.list();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return lista;
    }

    private String getListaFormatada(List<Portaria> lista) {
        StringBuilder builder = new StringBuilder();

        builder.append("CONFIRA AS PORTARIAS SELECIONADAS: \n\n");

        for (Portaria portaria : lista) {
            builder.append(portaria.toString()).append("\n");
        }

        return builder.toString();
    }
}