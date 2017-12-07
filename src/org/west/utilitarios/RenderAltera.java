package org.west.utilitarios;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.SwingConstants;
import org.west.entidades.Altera;
import org.west.entidades.Imovel;

public class RenderAltera extends DefaultListCellRenderer{

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
        setHorizontalTextPosition(SwingConstants.LEFT);
        setIconTextGap(200);
                
        Altera altera = (Altera) list.getModel().getElementAt(index);
        Imovel imovel = altera.getImovel();
        
        String endereco = imovel.getEndereco();

        if (imovel.getApto() != null && !imovel.getApto().isEmpty() && !imovel.getApto().equals("0"))
            endereco+= " , Apto " + imovel.getApto();
        if (imovel.getBloco() != null && !imovel.getBloco().isEmpty() && !imovel.getBloco().equals("0"))
            endereco+= ", Bloco " + imovel.getBloco();
        
        setText(endereco);
               
        if (altera.getDescricao().equals("status"))
            setForeground(getColor(altera.getImovel().getStatus()));
        
        if (altera.getDescricao().equals("menor"))
            setIcon(new ImageIcon(getClass().getResource("/org/west/imagens/arrow_down.png")));
        
        if (altera.getDescricao().equals("maior"))
            setIcon(new ImageIcon(getClass().getResource("/org/west/imagens/arrow_up.png")));
        
        if (altera.getDescricao().equals("divulgar"))
            setIcon(new ImageIcon(getClass().getResource("/org/west/imagens/megafone.png")));
                        
        return this;
    }
    
    private Color getColor(String status){

        if (status.equals("Ativo"))
            return Color.green;
        if (status.equals("Proposta"))
            return new Color(204,187,61);
        if (status.equals("Suspenso"))
            return Color.GRAY;
        if (status.equals("Vendido"))
            return Color.red;

        return Color.black;
    }
}
