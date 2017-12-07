package org.west.formulario.imoveis;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Arrays;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import org.swingBean.actions.ApplicationAction;
import org.swingBean.descriptor.BeanTableModel;
import org.swingBean.descriptor.GenericFieldDescriptor;
import org.swingBean.descriptor.TableFieldDescriptor;
import org.swingBean.descriptor.XMLDescriptorFactory;
import org.swingBean.gui.JActButton;
import org.swingBean.gui.JBeanPanel;
import org.swingBean.gui.JBeanTable;
import org.west.entidades.Anuncio;
import org.west.entidades.AnuncioDAO;

public class PanelGenenciarAnuncios extends javax.swing.JPanel {

    public PanelGenenciarAnuncios() {
        initComponents();

        setBorder(new EmptyBorder(10, 10, 10, 10));

        GridBagConstraints cons = new GridBagConstraints();
        cons.insets = new Insets(5, 5, 5, 5);
        cons.weightx = 1;
        cons.weighty = 1;
        cons.fill = GridBagConstraints.BOTH;

        cons.gridx = 0;
        cons.gridy = 0;

        final TableFieldDescriptor tableFieldDescriptor = XMLDescriptorFactory.getTableFieldDescriptor(Anuncio.class, "org/west/xml/anuncioCadastroTable.xml", "anuncioCadastroTable");

        final BeanTableModel<Anuncio> model = new BeanTableModel<Anuncio>(tableFieldDescriptor);
        final JBeanTable tableAnuncio = new JBeanTable(model);

        add(new JScrollPane(tableAnuncio), cons);

        cons.gridy = 1;
        cons.weighty = 0.25;

        final GenericFieldDescriptor descriptor = XMLDescriptorFactory.getFieldDescriptor(Anuncio.class, "org/west/xml/anuncioCadastroForm.xml", "anuncioCadastroForm");

        final JBeanPanel<Anuncio> formAnuncio = new JBeanPanel<Anuncio>(Anuncio.class, "Informações de Anúncio", descriptor);

        add(formAnuncio, cons);

        cons.gridy = 2;
        cons.weighty = 0;

        final JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));

        panelBotoes.add(new JActButton("Salvar", new ApplicationAction() {

            @Override
            public void execute() {
                Anuncio anuncio = new Anuncio();

                formAnuncio.populateBean(anuncio);

                AnuncioDAO.save(anuncio);

                formAnuncio.cleanForm();
                model.addBean(anuncio);
            }
        }));

        panelBotoes.add(new JActButton("Limpar", new ApplicationAction() {

            @Override
            public void execute() {
                formAnuncio.cleanForm();
            }
        }));

        add(panelBotoes, cons);

        tableAnuncio.addDoubleClickAction(new ApplicationAction() {

            @Override
            public void execute() {
                Anuncio anuncio = model.getBeanAt(tableAnuncio.getSelectedRow());

                formAnuncio.setBean(anuncio);
            }
        });

        model.setBeanList(Arrays.asList(AnuncioDAO.listAnuncioByQuery("nome is not null", "nome")));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.GridBagLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
