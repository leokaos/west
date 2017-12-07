package org.west.componentes;

import javax.swing.Icon;
import org.west.entidades.Foto;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import org.swingBean.gui.wrappers.ComponentWrapper;
import org.west.utilitarios.Util;

/**
 * Componente responsável por exibir as fotos de um determinado imóvel.
 * Para exibir as fotos do imóvel este componente segue o modelo de {@link ComponentWrapper} do SwingBean,
 * recebendo um {@link Set} de {@link Foto}. Ele carrega as fotos em outra Thread para melhorar o desempenho
 * de exibição.
 * @author West Guerra Ltda.
 */
public class WrapperSlideShow implements ComponentWrapper {

    private List<Foto> lista;
    private JPanel panel;
    private JLabel imagem;
    private JLabel contador;
    private Integer posicao;
    private JButton esquerda;
    private JButton direita;
    private Integer size;
    private String tamanho;
    
    @Override
    public Object getValue() {
        return lista;
    }

    @Override
    public void setValue(Object o) {
        lista = new ArrayList((Set) o);

        posicao = new Integer(0);
        size = lista.size() - 1;

        contador.setText("( 0 de 0 )");
        
        esquerda.setEnabled(false);
        direita.setEnabled(false);
        imagem.setIcon(null);
        imagem.setText("Sem Foto");      
        imagem.revalidate();
        imagem.repaint();

        if (!lista.isEmpty()){
            imagem.setText(null);

            for (ActionListener listener: esquerda.getActionListeners())
                esquerda.removeActionListener(listener);

            for (ActionListener listener: direita.getActionListeners())
                direita.removeActionListener(listener);

            esquerda.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    posicao--;
                    setFoto();
                }
            });

            direita.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    posicao++;
                    setFoto();
                }
            });
            
            new Thread(new Runnable() {

                @Override
                public void run() {
                    setFoto();
                }
            }).start();            
        }
    }

    private void setFoto() {
        imagem.setText(null);
        esquerda.setEnabled(posicao>0);
        direita.setEnabled(posicao < size);
        Foto foto = lista.get(posicao);
        setIcon(foto.getCaminho());
        contador.setText("( " + (posicao + 1) + " de " + lista.size() + " )");
        
        panel.firePropertyChange("posicao",0,posicao);
    }

    private void setIcon(String caminho) {
        caminho = Util.getPropriedade("west.foto_dir") + "\\" + caminho;
        File file = new File(caminho);
        
        try{
            BufferedImage image = ImageIO.read(file);
            Double proporcao = (double) image.getWidth() / image.getHeight();
            Integer largura;
            Integer altura;           
            
            if (image.getWidth() > image.getHeight()){
                largura = new Integer(tamanho);
                altura = new Double(largura/ proporcao).intValue();
            }
            else{
                altura = new Integer(tamanho);
                largura = new Double(altura * proporcao).intValue();
            }
            
            imagem.setIcon(new ImageIcon(image.getScaledInstance(largura, altura, Image.SCALE_SMOOTH)));
            image = null;
        }
        catch(Exception ex){ex.printStackTrace();}
    }

    public void setTamanho(String tamanho){
        this.tamanho = tamanho;
    }

    @Override
    public void cleanValue() {
        lista = new ArrayList();
        imagem.setIcon(null);
        imagem.setText("Sem foto");
        panel.revalidate();
        panel.repaint();
        esquerda.setEnabled(false);
        direita.setEnabled(false);
        contador.setText("( 0 de 0 )");
    }

    @Override
    public Component getComponent() {
        return panel;
    }

    @Override
    public void setEnable(boolean bln) {
        panel.setEnabled(bln);
    }

    @Override
    public void initComponent() {
        panel = new JPanel(new BorderLayout());

        posicao = new Integer(0);

        size = new Integer(0);

        imagem = new JLabel("Sem foto");
        imagem.setPreferredSize(new Dimension(70,70));
        imagem.setMaximumSize(new Dimension(70, 70));
        imagem.setMinimumSize(new Dimension(70, 70));
        imagem.setHorizontalAlignment(SwingConstants.CENTER);
        imagem.setVerticalAlignment(SwingConstants.CENTER);
        imagem.setOpaque(false);
        
        direita = new JButton(new ImageIcon(getClass().getResource("/org/west/imagens/seta_direita_black.png")));
        direita.setPreferredSize(new Dimension(20, 0));
        direita.setOpaque(false);
        esquerda = new JButton(new ImageIcon(getClass().getResource("/org/west/imagens/seta_esquerda_black.png")));
        esquerda.setPreferredSize(new Dimension(20, 0));
        esquerda.setOpaque(false);

        esquerda.setEnabled(false);
        direita.setEnabled(false);

        contador = new JLabel("( 0 de 0 )",SwingConstants.CENTER);
        contador.setOpaque(false);
        
        panel.add(esquerda,BorderLayout.WEST);
        panel.add(imagem,BorderLayout.CENTER);
        panel.add(direita,BorderLayout.EAST);
        panel.add(contador,BorderLayout.SOUTH);

        panel.setOpaque(false);
    }
}