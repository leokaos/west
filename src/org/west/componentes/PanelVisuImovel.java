package org.west.componentes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import javax.swing.JComponent;
import org.swingBean.descriptor.GenericFieldDescriptor;
import org.swingBean.descriptor.XMLDescriptorFactory;
import org.swingBean.gui.JBeanPanel;
import org.west.entidades.Imovel;
import org.west.entidades.ImovelDAO;
import org.west.entidades.Numero;
import org.west.formulario.imoveis.FrmFotos;
import org.west.utilitarios.ValidadorUtil;
import static org.west.utilitarios.ValidadorUtil.isNotEmpty;
import static org.west.utilitarios.ValidadorUtil.isNotNull;

public class PanelVisuImovel extends javax.swing.JPanel {

    private Imovel imovel;
    private JBeanPanel formSlide;
    private Integer posicaoFoto;

    public PanelVisuImovel(Imovel bean) {
        initComponents();
        this.imovel = bean;

        ImovelDAO.lock(imovel);

        GenericFieldDescriptor descriptor = XMLDescriptorFactory.getFieldDescriptor(Imovel.class, "org/west/xml/imovelFormSlide.xml", "slide" + imovel.getReferencia());
        formSlide = new JBeanPanel(Imovel.class, descriptor);
        formSlide.setOpaque(false);
        formSlide.setBorder(null);

        formSlide.getComponent("fotos").addPropertyChangeListener("posicao", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                posicaoFoto = (Integer) evt.getNewValue();
            }
        });

        formSlide.getComponent("fotos").addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    FrmFotos frm = new FrmFotos(imovel);
                    frm.setPositionInicial(posicaoFoto);
                    frm.setVisible(true);
                }
            }
        });

        setOpaqueForAll(formSlide);

        panelSlideShow.add(formSlide, BorderLayout.CENTER);

        exibeImovel();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (formSlide.getPropertyValue("fotos") == null) {
            ImovelDAO.lock(imovel);
            formSlide.setBean(imovel);
        }
    }

    private void exibeImovel() {
        selecionado.setText(String.valueOf(getImovel().getReferencia()));
        lblTipo.setText(getImovel().getTipo().getTipo());
        lblLocali.setText(getImovel().getBairro().toString());

        lblEndereco.setText(getImovel().getCep().getTipo() + " " + getImovel().getCep().getDescricao() + " " + getTextoNumero(getImovel()));

        if (getImovel().getApto() != null && !getImovel().getApto().isEmpty() && !getImovel().getApto().equals("0")) {
            lblEndereco.setText(lblEndereco.getText() + ", Apto " + getImovel().getApto());
        }

        if (getImovel().getBloco() != null && !getImovel().getBloco().isEmpty() && !getImovel().getBloco().equals("0")) {
            lblEndereco.setText(lblEndereco.getText() + ", Bloco " + getImovel().getBloco());
        }

        lblDorms.setText("Dorms:     " + getImovel().getDorms());
        lblGars.setText("Vagas:      " + getImovel().getGaragens());
        lblSuites.setText("Suítes:    " + getImovel().getSuites());
        lblArea.setText(getTextoArea(getImovel()) + " m²");
        lblBanheiros.setText("Banheiros: " + getImovel().getBanheiros());
        lblSalas.setText("Salas:     " + getImovel().getSalas());

        final DecimalFormat formato = new DecimalFormat("#,###.00");
        lblPreco.setText("Valor: " + formato.format(getImovel().getValor()));

        lblStatus.setText("Status: " + getImovel().getStatus());
        lblStatus.setForeground(getColor(imovel.getStatus()));

        lblPortaria.setText(getLabelEdificio(imovel));

        if (!imovel.getStatus().equals("Ativo")) {
            lblPreco.setText("");

            lblPreco.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    lblPreco.setText("Valor: " + formato.format(getImovel().getValor()));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    lblPreco.setText("");
                }
            });
        }
    }

    private String getTextoArea(Imovel imovel) {
        if (imovel.getTipo().isColetivo()) {
            return ("Área Privativa: " + imovel.getPrivativa());
        } else {
            return ("Área Construida: " + imovel.getConstruido());
        }
    }

    private String getLabelEdificio(Imovel imovel) {
        StringBuilder builder = new StringBuilder();

        if (isNotNull(imovel.getPortaria())) {
            builder.append("Edifício: ").append(imovel.getPortaria().getEdificio());
        } else if (isNotEmpty(imovel.getEdificio())) {
            builder.append("Edifício: ").append(imovel.getEdificio());
        }

        return builder.toString();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panelSlideShow = new javax.swing.JPanel();
        selecionado = new javax.swing.JCheckBox();
        selecionado.setOpaque(false);
        lblTipo = new javax.swing.JLabel();
        lblLocali = new javax.swing.JLabel();
        lblEndereco = new javax.swing.JLabel();
        lblPreco = new javax.swing.JLabel();
        lblDorms = new javax.swing.JLabel();
        lblGars = new javax.swing.JLabel();
        lblSalas = new javax.swing.JLabel();
        lblSuites = new javax.swing.JLabel();
        lblBanheiros = new javax.swing.JLabel();
        lblArea = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        lblPortaria = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(0, 0));
        setPreferredSize(new java.awt.Dimension(0, 150));
        setLayout(new java.awt.GridBagLayout());

        panelSlideShow.setMaximumSize(new java.awt.Dimension(100, 2147483647));
        panelSlideShow.setMinimumSize(new java.awt.Dimension(100, 110));
        panelSlideShow.setOpaque(false);
        panelSlideShow.setPreferredSize(new java.awt.Dimension(100, 110));
        panelSlideShow.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 70;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(panelSlideShow, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(selecionado, gridBagConstraints);

        lblTipo.setFont(new java.awt.Font("Tahoma", 1, 12));
        lblTipo.setText("tipo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(lblTipo, gridBagConstraints);

        lblLocali.setFont(new java.awt.Font("Tahoma", 1, 12));
        lblLocali.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLocali.setText("locali");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(lblLocali, gridBagConstraints);

        lblEndereco.setText("endereco");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(lblEndereco, gridBagConstraints);

        lblPreco.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblPreco.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPreco.setText("preco");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(lblPreco, gridBagConstraints);

        lblDorms.setText("dorms");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(lblDorms, gridBagConstraints);

        lblGars.setText("gars");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(lblGars, gridBagConstraints);

        lblSalas.setText("salas");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(lblSalas, gridBagConstraints);

        lblSuites.setText("suites");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(lblSuites, gridBagConstraints);

        lblBanheiros.setText("banheiros");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(lblBanheiros, gridBagConstraints);

        lblArea.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblArea.setText("area");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(lblArea, gridBagConstraints);

        lblStatus.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStatus.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblStatus.setText("status");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(lblStatus, gridBagConstraints);

        lblPortaria.setText("portaria");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(lblPortaria, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblArea;
    private javax.swing.JLabel lblBanheiros;
    private javax.swing.JLabel lblDorms;
    private javax.swing.JLabel lblEndereco;
    private javax.swing.JLabel lblGars;
    private javax.swing.JLabel lblLocali;
    private javax.swing.JLabel lblPortaria;
    private javax.swing.JLabel lblPreco;
    private javax.swing.JLabel lblSalas;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblSuites;
    private javax.swing.JLabel lblTipo;
    private javax.swing.JPanel panelSlideShow;
    private javax.swing.JCheckBox selecionado;
    // End of variables declaration//GEN-END:variables

    private void setOpaqueForAll(JBeanPanel panelSlide) {
        for (int x = 0; x < panelSlide.getComponentCount(); x++) {
            ((JComponent) panelSlide.getComponent(x)).setOpaque(false);
        }
    }

    public Boolean isSelecionado() {
        return selecionado.isSelected();
    }

    public void setSelecionado(Boolean value) {
        this.selecionado.setSelected(value);
    }

    public Imovel getImovel() {
        return imovel;
    }

    public void setImovel(Imovel imovel) {
        this.imovel = imovel;
    }

    private Color getColor(String status) {

        if (status.equals("Ativo")) {
            return Color.green;
        }
        if (status.equals("Proposta")) {
            return new Color(204, 187, 61);
        }
        if (status.equals("Suspenso")) {
            return Color.gray;
        }
        if (status.equals("Vendido")) {
            return Color.red;
        }

        return Color.black;
    }

    private String getTextoNumero(Imovel imovel) {
        StringBuilder numeros = new StringBuilder();

        if (isNotEmpty(imovel.getNumeros())) {

            numeros.append("Nº ");

            for (Numero numero : imovel.getNumeros()) {
                numeros.append(numero.getNumero());
                numeros.append(", ");
            }

            String retorno = numeros.toString();

            return retorno.substring(0, retorno.length() - 2);
        }

        return numeros.toString();
    }
}
