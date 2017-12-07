package org.west.formulario.imoveis;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import org.west.entidades.Imovel;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;
import org.apache.commons.mail.EmailException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.hibernate.criterion.Restrictions;
import org.jfree.ui.tabbedui.VerticalLayout;
import org.west.componentes.DesktopSession;
import org.west.componentes.email.EmailFotos;
import org.west.componentes.email.EmailManager;
import org.west.entidades.Foto;
import org.west.entidades.FotoCriteria;
import org.west.entidades.FotoDAO;
import org.west.entidades.PontoDAO;
import org.west.entidades.Usuario;
import org.west.utilitarios.FilterFotos;
import org.west.utilitarios.Util;

/**
 * Classe responsável por carregar e exibir as fotos de um determinado
 * {@link Imovel}.
 *
 * @author West Guerra Ltda.
 */
public class FrmFotos extends JDialog implements PropertyChangeListener {

    /**
     * Imóvel cujas fotos serão exibidas.
     */
    private Imovel imovel;
    /**
     * Lista de {@link PanelFoto}, para desenhar a galeria.
     */
    private List<PanelFoto> listaPanel = new ArrayList<PanelFoto>();
    /**
     * Valor que guarda qual foto está sendo exibida.
     */
    private Integer position = null;
    /**
     * Valor da posição inicial.
     */
    private Integer positionInicial;

    /**
     * Construtor que recebe um {@link Imovel} para ser exibido.
     *
     * @param imo
     */
    public FrmFotos(Imovel imo) {
        super((JFrame) null, true);

        initComponents();

        this.imovel = imo;

        setSize(800, 550);
        setLocationRelativeTo(null);

        panelProgress.setVisible(false);
        panelGaleria.setLayout(new VerticalLayout());

        if (imovel != null) {
            this.setTitle("Sistema West - Fotos da Referência: " + imovel.getReferencia());

            JPopupMenu menu = new JPopupMenu();
            final JMenuItem itemPrincipal = new JMenuItem("Definir como foto principal", new ImageIcon(getClass().getResource("/org/west/imagens/foto.png")));
            menu.add(itemPrincipal);

            itemPrincipal.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (position != null) {
                        Foto fotoAtual = listaPanel.get(position).getFoto();

                        for (PanelFoto panel : listaPanel) {
                            Foto foto = panel.getFoto();
                            foto.setPrincipal(false);
                            FotoDAO.save(foto);
                        }

                        listaPanel.get(position).setPrincipal(true);
                        fotoAtual.setPrincipal(true);
                        FotoDAO.save(fotoAtual);

                        carregaGaleria();
                    }
                }
            });

            lblFoto.setComponentPopupMenu(menu);

            addWindowListener(new WindowAdapter() {

                @Override
                public void windowOpened(WindowEvent e) {
                    super.windowOpened(e);
                    carregaGaleria();
                }
            });

            //action
            AbstractAction actionCima = new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (position == null || position == 0) {
                        position = listaPanel.size() - 1;
                    } else {
                        position--;
                    }

                    configPanels(listaPanel.get(position));
                }
            };

            AbstractAction actionBaixo = new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (position == null || (position == listaPanel.size() - 1)) {
                        position = 0;
                    } else {
                        position++;
                    }

                    configPanels(listaPanel.get(position));
                }
            };

            AbstractAction actionExit = new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            };

            panelGaleria.getActionMap().put("cima", actionCima);
            panelGaleria.getActionMap().put("baixo", actionBaixo);
            panelGaleria.getActionMap().put("exit", actionExit);

            panelGaleria.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"), "cima");
            panelGaleria.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), "baixo");
            panelGaleria.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "exit");

            Usuario usuario = (Usuario) DesktopSession.getInstance().getObject("usuario");

            btnRemover.setVisible(!(!usuario.isSupervisor() || usuario.getNivel() == 2));
        }

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);

                Object[] options = {"Sim", "Não"};

                int option = JOptionPane.showOptionDialog(null, "Deseja sincronizar as fotos agora?", "Sincronizar", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, "Sim");

                if (option == 0) {
                    btnSincronizarActionPerformed(null);
                }
            }
        });
    }

    /**
     * Carrega a galeria em uma {@link Thread} nova para não travar a interface.
     */
    private void carregaGaleria() {
        Util.load(jScrollPane1);
        final FrmFotos frmFotos = this;

        new Thread(new Runnable() {

            @Override
            public void run() {
                panelGaleria.removeAll();
                panelGaleria.setVisible(false);
                listaPanel.clear();

                Foto[] fotos = FotoDAO.listFotoByQuery("imovel = " + imovel.getReferencia(), "principal desc,site desc,caminho");

                if (fotos.length == 0) {
                    lblFoto.setText("Sem Fotos!");
                } else {
                    panelGaleria.setLayout(new VerticalLayout());
                    panelGaleria.setPreferredSize(new Dimension(100, fotos.length * 100));
                    int fotoSite = 0;

                    for (Foto foto : fotos) {
                        PanelFoto panel = new PanelFoto(foto);
                        panel.addPropertyChangeListener("clicado", frmFotos);
                        panelGaleria.add(panel);
                        listaPanel.add(panel);

                        if (foto.isSite()) {
                            fotoSite++;
                        }
                    }

                    TitledBorder border = (TitledBorder) jScrollPane1.getBorder();
                    border.setTitle("Galeria (" + fotoSite + " Site, " + (fotos.length - fotoSite) + " Sistema)");

                    panelGaleria.revalidate();
                    panelGaleria.repaint();
                }

                if (positionInicial != null) {
                    position = positionInicial;
                    listaPanel.get(position).firePropertyChange("clicado", false, true);
                }

                Util.unload(jScrollPane1);
                panelGaleria.setVisible(true);
            }
        }).start();
    }

    /**
     * Carrega as fotos para dentro do sistema, conferindo o tamanho através do
     * método {@link #confereFoto(java.io.File)}, aplica a marca d'agua através
     * de {@link #aplicarMarca(java.io.File)} e finalmente grava no banco.
     *
     * @param site True indica se as fotos são para o site e False para o
     * sistema.
     */
    private void carregaFotos(final Boolean site) {
        JFileChooser chooser = new JFileChooser();

        chooser.setDialogTitle((site) ? "Fotos Para O Site" : "Fotos Para O Sistema");

        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        try {
            int escolha = chooser.showOpenDialog(null);

            if (escolha == JFileChooser.APPROVE_OPTION) {
                final File pastaOrigem = new File(chooser.getSelectedFile().getAbsolutePath());

                final int proporcao = new Double(100 / pastaOrigem.listFiles().length).intValue();

                panelProgress.setVisible(true);
                pg.setValue(0);
                pg.setString("Iniciando...");

                final Thread th = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        Date date = PontoDAO.getDateServer();
                        List<File> arquivosRecusados = new ArrayList<File>();
                        List<File> arquivosAceitos = new ArrayList<File>();

                        Integer qtFotosInicio = verificarQuantidadeFotos();

                        for (File file : pastaOrigem.listFiles()) {
                            File pastaDestino = new File(Util.getPropriedade("west.foto_dir"));
                            pg.setString("Analisando: " + file.getName());

                            if (confereFoto(file)) {

                                pg.setString("Analisado! Copiando arquivo " + file.getName() + "...");

                                try {
                                    FileChannel chanelOrigem = new FileInputStream(file).getChannel();

                                    String nome = getNomeFoto();

                                    File fileDestino = new File(pastaDestino.getAbsolutePath() + "\\" + nome);
                                    FileChannel chanelDestino = new FileOutputStream(fileDestino).getChannel();

                                    chanelDestino.transferFrom(chanelOrigem, 0, chanelOrigem.size());

                                    pg.setString("Terminado! Aplicando marca d'agua...");

                                    aplicarMarca(fileDestino);

                                    Foto foto = new Foto();
                                    foto.setCaminho(nome);
                                    foto.setSite(site);
                                    foto.setImovel(imovel);
                                    foto.setDescricao("");
                                    foto.setPrincipal(false);
                                    foto.setDataEntrada(date);

                                    FotoDAO.save(foto);

                                    arquivosAceitos.add(file);

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                            } else {
                                pg.setString("Arquivo recusado!");
                                arquivosRecusados.add(file);
                            }

                            pg.setValue(pg.getValue() + proporcao);
                        }

                        pg.setString("Concluido!");
                        btnRemover.setEnabled(true);
                        btnOk.setEnabled(true);
                        pg.setValue(pg.getMaximum());

                        String sumario = "Sumário:\n\nArquivos Aceitos: " + arquivosAceitos.size() + "\n\nRecusados: " + arquivosRecusados.size();
                        JOptionPane.showMessageDialog(null, sumario);

                        if (qtFotosInicio == 0 && !arquivosAceitos.isEmpty()) {
                            try {
                                EmailFotos email = new EmailFotos(imovel, arquivosAceitos.size(), arquivosRecusados.size());

                                EmailManager.sendEmail(email);

                            } catch (EmailException ex) {
                                JOptionPane.showMessageDialog(null, "Erro ao enviar email: " + ex.getMessage());
                            }
                        }

                        carregaGaleria();
                    }
                });

                th.start();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String getNomeFoto() {
        Integer qt_fotos = FotoDAO.listFotoByQuery("imovel=" + imovel.getReferencia(), "imovel").length;

        StringBuilder builder = new StringBuilder();
        builder.append(imovel.getReferencia());
        builder.append("_");
        builder.append(qt_fotos + 1);
        builder.append(".jpg");

        return builder.toString();
    }

    private Integer verificarQuantidadeFotos() {
        FotoCriteria fotoCriteria = new FotoCriteria();

        fotoCriteria.add(Restrictions.eq("imovel", imovel));

        return fotoCriteria.list().size();
    }

    /**
     * Confere se a foto está dentro do padrão escolhido.
     *
     * @param file Arquivo de imagem escolhido pelo usuário.
     * @return true : Apenas se a foto for JPEG e e tiver no máximo 150KB.
     */
    private Boolean confereFoto(File file) {
        if (file.length() > 150 * 1024) {
            return false;
        }

        if (!file.getName().toLowerCase().endsWith(".jpg")) {
            return false;
        }

        return true;
    }

    /**
     * Aplica uma marca d'agua na foto passada.
     *
     * @param file Arquivo de imagem escolhido pelo usuário.
     * @return imagem : Imagem já com a marca d'agua.
     */
    private File aplicarMarca(File file) {
        try {
            BufferedImage logo = ImageIO.read(getClass().getResourceAsStream("/org/west/imagens/logo10.png"));
            BufferedImage image = ImageIO.read(file);

            int left = (int) (image.getWidth() - logo.getWidth()) / 2;
            int top = (int) (image.getHeight() - logo.getHeight()) / 2;

            Graphics2D gra = image.createGraphics();
            gra.drawImage(logo, left, top, logo.getWidth(), logo.getHeight(), null);
            gra.dispose();

            ImageIO.write(image, "jpg", file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return file;
    }

    /**
     * Configura os painéis setando o ícone no {@link #lblFoto} com a imagem do
     * {@link PanelFoto}.
     *
     * @param panelAtual PanelFoto atual.
     */
    private void configPanels(PanelFoto panelAtual) {
        lblFoto.setIcon(new ImageIcon(escalaImagem(listaPanel.get(position).getImagem())));
        lblFoto.setText("");

        TitledBorder border = (TitledBorder) lblFoto.getBorder();
        border.setTitle("Foto Atual: " + (position + 1) + " de " + listaPanel.size());

        for (PanelFoto panel : listaPanel) {
            panel.setClicado(panel.equals(panelAtual));
        }

        jScrollPane1.getVerticalScrollBar().setValue(position * 100);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        PanelFoto panelAtual = (PanelFoto) evt.getSource();
        position = listaPanel.indexOf(panelAtual);
        configPanels(panelAtual);
    }

    public Image escalaImagem(Image img) {
        Double alturaReal = (double) lblFoto.getHeight() * 0.95;
        Double larguraReal = (double) lblFoto.getWidth() * 0.95;

        Double altura = (double) img.getHeight(null);
        Double largura = (double) img.getWidth(null);
        Double proporcao = largura / altura;

        if (largura > altura) {
            largura = larguraReal;
            altura = new Double(largura / proporcao);
        } else {
            altura = alturaReal;
            largura = new Double(altura * proporcao);
        }

        BufferedImage newImage = new BufferedImage(largura.intValue(), altura.intValue(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = newImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(img, 0, 0, largura.intValue(), altura.intValue(), null);
        g2d.dispose();

        return newImage;
    }

    public Integer getPositionInicial() {
        return positionInicial;
    }

    public void setPositionInicial(Integer positionInicial) {
        this.positionInicial = positionInicial;
    }

    private int deletarArquivosFtp(int fotos) {
        try {
            FTPClient ftp = new FTPClient();

            ftp.connect("ftp://westguerra.com.br/");

            if (FTPReply.isPositiveCompletion(ftp.getReplyCode())) {

                if (ftp.login("westguerra", "w3s4t5g6")) {

                    String caminhoRemoto = Util.getPropriedade("west.ftp_dir");

                    ftp.changeWorkingDirectory(caminhoRemoto);
                    ftp.setFileType(FTPClient.BINARY_FILE_TYPE);

                    List<FTPFile> listaArquivos = Arrays.asList(ftp.listFiles(caminhoRemoto, new FilterFotos(imovel.getReferencia())));

                    int porcentagem = 100 / (fotos + listaArquivos.size());

                    for (FTPFile file : listaArquivos) {
                        pg.setString("Deletando arquivo " + file.getName());
                        pg.setValue(pg.getValue() + porcentagem);
                        ftp.deleteFile(caminhoRemoto + file.getName());
                    }

                    return porcentagem;
                }

            } else {
                ftp.disconnect();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }

        return 0;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblFoto = new javax.swing.JLabel();
        btnSite = new javax.swing.JButton();
        btnSistema = new javax.swing.JButton();
        btnRemover = new javax.swing.JButton();
        panelProgress = new javax.swing.JPanel();
        pg = new javax.swing.JProgressBar();
        btnOk = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(100);
        panelGaleria = new javax.swing.JPanel();
        btnSincronizar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Sistema West - Fotos");
        setMinimumSize(new java.awt.Dimension(700, 450));
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        lblFoto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFoto.setBorder(javax.swing.BorderFactory.createTitledBorder("Foto Atual"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(lblFoto, gridBagConstraints);

        btnSite.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/west/imagens/site.png"))); // NOI18N
        btnSite.setIconTextGap(5);
        btnSite.setLabel("Site...");
        btnSite.setPreferredSize(new java.awt.Dimension(120, 25));
        btnSite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSiteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        getContentPane().add(btnSite, gridBagConstraints);

        btnSistema.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/west/imagens/system.png"))); // NOI18N
        btnSistema.setText("Sistema...");
        btnSistema.setIconTextGap(5);
        btnSistema.setPreferredSize(new java.awt.Dimension(120, 25));
        btnSistema.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSistemaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(btnSistema, gridBagConstraints);

        btnRemover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/west/imagens/cancel.png"))); // NOI18N
        btnRemover.setText("Remover");
        btnRemover.setIconTextGap(5);
        btnRemover.setPreferredSize(new java.awt.Dimension(120, 25));
        btnRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoverActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        getContentPane().add(btnRemover, gridBagConstraints);

        panelProgress.setPreferredSize(new java.awt.Dimension(0, 33));
        panelProgress.setLayout(new java.awt.GridBagLayout());

        pg.setStringPainted(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panelProgress.add(pg, gridBagConstraints);

        btnOk.setText("OK");
        btnOk.setEnabled(false);
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 40;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panelProgress.add(btnOk, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(panelProgress, gridBagConstraints);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Galeria"));
        jScrollPane1.setMaximumSize(new java.awt.Dimension(200, 32767));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(200, 50));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(200, 0));

        panelGaleria.setMaximumSize(new java.awt.Dimension(180, 2147483647));
        panelGaleria.setMinimumSize(new java.awt.Dimension(180, 50));
        panelGaleria.setPreferredSize(new java.awt.Dimension(180, 50));
        panelGaleria.setLayout(new java.awt.GridBagLayout());
        jScrollPane1.setViewportView(panelGaleria);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jScrollPane1, gridBagConstraints);

        btnSincronizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/west/imagens/ftp.png"))); // NOI18N
        btnSincronizar.setText("Sincronizar");
        btnSincronizar.setIconTextGap(5);
        btnSincronizar.setPreferredSize(new java.awt.Dimension(120, 25));
        btnSincronizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSincronizarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(btnSincronizar, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSiteActionPerformed
        carregaFotos(true);
    }//GEN-LAST:event_btnSiteActionPerformed

    private void btnRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoverActionPerformed
        if (position != null) {
            Object[] options = {"Sim", "Não"};
            int escolha = JOptionPane.showOptionDialog(null, "Tem certeza que deseja deletar a foto?", "Confirmar",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

            if (escolha == 0) {
                String pasta = Util.getPropriedade("west.foto_dir");
                Foto foto = listaPanel.get(position).getFoto();
                File arquivo = new File(pasta + "\\" + foto.getCaminho());

                FotoDAO.delete(foto);
                arquivo.delete();
                lblFoto.setIcon(null);

                carregaGaleria();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Nenhuma foto selecionada!", "Erro", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnRemoverActionPerformed

    private void btnSistemaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSistemaActionPerformed
        carregaFotos(false);
    }//GEN-LAST:event_btnSistemaActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        panelProgress.setVisible(false);
    }//GEN-LAST:event_btnOkActionPerformed

    private void btnSincronizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSincronizarActionPerformed
        if (listaPanel.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Lista de Fotos Vazia!");
        } else {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    pg.setValue(0);
                    pg.setString("Inicializando...");
                    panelProgress.setVisible(true);

                    try {
                        //MYSQL HANDLER

                        Class.forName("com.mysql.jdbc.Driver");
                        Connection conn = DriverManager.getConnection("jdbc:mysql://187.18.58.105:3306/westguerra", "root", "w3s4t5g6");

                        PreparedStatement state = conn.prepareStatement("delete from westguerra.tab_fotos where imovel = " + imovel.getReferencia());

                        state.executeUpdate();

                        for (Foto foto : FotoDAO.listFotoByQuery("site = 1 and imovel=" + imovel.getReferencia(), "caminho")) {
                            conn.prepareStatement("insert into westguerra.tab_fotos (imovel,caminho,descricao,principal)"
                                    + " VALUES (" + imovel.getReferencia() + ",'" + foto.getCaminho() + "','" + foto.getDescricao() + "'," + foto.isPrincipal() + ")").executeUpdate();
                        }

                        String caminhoLocal = Util.getPropriedade("west.foto_dir") + "\\";

                        List<Foto> listaFotos = Arrays.asList(FotoDAO.listFotoByQuery("site = 1 and imovel = " + imovel.getReferencia(), "codigo"));

                        int porcentagem = deletarArquivosFtp(listaFotos.size());

                        try {

                            for (Foto foto : listaFotos) {
                                FTPClient ftp = new FTPClient();

                                ftp.connect("ftp.westguerra.com.br");
                                ftp.enterLocalPassiveMode();

                                if (FTPReply.isPositiveCompletion(ftp.getReplyCode())) {

                                    if (ftp.login("westguerra", "w3s4t5g6")) {

                                        String caminhoRemoto = Util.getPropriedade("west.ftp_dir");

                                        ftp.changeWorkingDirectory(caminhoRemoto);
                                        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);

                                        String caminho = caminhoLocal + foto.getCaminho();
                                        pg.setString("Enviando arquivo " + foto.getCaminho());
                                        pg.setValue(pg.getValue() + porcentagem);

                                        FileInputStream in = new FileInputStream(new File(caminho));
                                        ftp.storeFile(foto.getCaminho(), in);
                                    }
                                } else {
                                    ftp.disconnect();
                                }
                            }

                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage());
                        }

                        pg.setValue(100);
                        pg.setString("Concluído!");
                        btnOk.setEnabled(true);

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Erro ao sincronizar!");
                        btnOk.setEnabled(true);
                        pg.setString("Erro ao Sincronizar");
                        pg.setValue(pg.getModel().getMaximum());
                        ex.printStackTrace();
                    }
                }
            }).start();
        }
    }//GEN-LAST:event_btnSincronizarActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnRemover;
    private javax.swing.JButton btnSincronizar;
    private javax.swing.JButton btnSistema;
    private javax.swing.JButton btnSite;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblFoto;
    private javax.swing.JPanel panelGaleria;
    private javax.swing.JPanel panelProgress;
    private javax.swing.JProgressBar pg;
    // End of variables declaration//GEN-END:variables
}