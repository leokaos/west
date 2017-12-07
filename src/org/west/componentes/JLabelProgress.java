package org.west.componentes;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JLabel;

/**
 * Semelhante ao {@link javax.swing.JLabel}, mas utiliza uma estrutura de uma progressbar
 * para exibir uma relação entre dois valores.
 * @author West Guerra Ltda.
 */
public class JLabelProgress extends JLabel{

    /**
     *  Objeto da classe {@link java.awt.Color} que define a cor que será utilizada como backgroud.
     */
    private Color progressColor;
    
    /**
     * Valor inteiro de máximo.
     */
    private Integer maximo;
    /**
     * Valor inteiro de minímo.
     */    
    private Integer minimo;

    /**
     * Cria uma instância de {@link JLabelProgress} com o texto e o alinhamento especificados.
     * @param str texto de exibição do componente.
     * @param align alinhamento utilizado pelo texto.
     */
    public JLabelProgress(String str,int align) {
        super(str,align);
    }

    /**
     * Retorna a cor utilizada no background.
     * @return cor : Objeto {@link java.awt.Color} referente ao fundo do componente.
     */
    public Color getProgressColor() {
        return progressColor;
    }
    
    /**
     * Seta a cor de fundo do componente.
     * @param progressColor a cor desejada para o fundo.
     */
    public void setProgressColor(Color progressColor) {
        this.progressColor = progressColor;
    }

    /**
     * Chamado para desenhar o componente na tela. Ele sobreescreve o método {@link javax.swing.JLabel#paint(java.awt.Graphics)}
     * adicionando o desenho da progressbar,utlizando como parâmetros {@link #maximo} como total, e {@link #minimo} como parte, construindo
     * assim a relação parte/total, como porcentagem.
     * @param g O contexto gráfico atual.
     */
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        
        Double largura = getWidth() * 0.8;
        Double altura = getHeight() * 0.8;
        Double x = (getWidth() - largura) / 2;
        Double y = (getHeight() - altura) / 2;

        g.setColor(new Color(150, 150, 150));
        g.drawRect(x.intValue(), y.intValue(), largura.intValue(), altura.intValue());
        g.setColor(Color.white);
        g.fillRect(x.intValue() + 1, y.intValue() + 1, largura.intValue() - 1, altura.intValue() - 1);

        if (maximo != null && minimo != null && maximo > 0){
            Double relac = minimo.doubleValue() / maximo.doubleValue();

            if (relac > 0.0) {
                largura = largura * relac;
                altura = altura - 1;
                g.setColor(progressColor);
                g.fillRect(x.intValue() + 1, y.intValue() + 1, largura.intValue() - 1, altura.intValue());
            }       
        }
        
        super.paintComponent(g);
    }
    
    /**
     * Retorna o valor máximo.
     * @return maximo : valor máximo.
     */
    public Integer getMaximo() {
        return maximo;
    }

    /**
     * Seta o valor máximo.
     * @param valor valor máximo.
     */
    public void setMaximo(Integer maximo) {
        this.maximo = maximo;
    }
    
    /**
     * Retorna o valor minímo.
     * @return minimo : valor minímo.
     */
    public Integer getMinimo() {
        return minimo;
    }

    /**
     * Seta o valor minímo.
     * @param valor valor minímo.
     */    
    public void setMinimo(Integer minimo) {
        this.minimo = minimo;
    }
}