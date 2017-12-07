package org.west.componentes;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import org.swingBean.descriptor.look.LookProvider;
import org.swingBean.gui.wrappers.ComponentWrapper;

public class WrapperImage implements ComponentWrapper{

    private JLabel imagem;
    private String caminho;
    private Integer tamanho = 100;
    private String pasta = "";

    @Override
    public Object getValue() {
        return caminho;
    }

    @Override
    public void setValue(Object o) {
        try{
            caminho = o.toString();
            File file = new File(pasta + "\\" + caminho);

            if (file.exists()){
                ImageIcon logo = new ImageIcon(file.getAbsolutePath());
                Double proporcao = new Double((double) logo.getIconWidth() / logo.getIconHeight());
                Integer largura = 0;
                Integer altura = 0;

                if (logo.getIconWidth() > logo.getIconHeight()){
                    largura = new Integer(tamanho);
                    altura = new Double(largura/ proporcao).intValue();
                }
                else{
                    altura = new Integer(tamanho);
                    largura = new Double(altura * proporcao).intValue();
                }

                imagem.setIcon(new ImageIcon(logo.getImage().getScaledInstance(largura,altura, Image.SCALE_SMOOTH)));
                imagem.setText("");
            }
            else
                imagem.setText("Erro ao carregar imagem!");
        }
        catch(Exception ex){ex.printStackTrace();}
    }

    @Override
    public void cleanValue() {
        imagem.setIcon(null);
        imagem.setText("");
    }

    @Override
    public Component getComponent() {
        return imagem;
    }

    @Override
    public void setEnable(boolean bln) {
        imagem.setEnabled(bln);
    }

    @Override
    public void initComponent() throws Exception {
        imagem = new JLabel();

        imagem.setPreferredSize(new Dimension(0, tamanho));
        imagem.setMaximumSize(new Dimension(0, tamanho));
        imagem.setMinimumSize(new Dimension(0, tamanho));

        imagem.setHorizontalAlignment(SwingConstants.CENTER);

        imagem.setBorder(new EmptyBorder(3, 3, 3, 3));
        imagem.setFont(LookProvider.getLook().getTextFont());
    }

    public void setPasta(String pasta) {
        this.pasta = pasta;
    }
}