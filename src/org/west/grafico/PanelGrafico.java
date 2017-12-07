package org.west.grafico;

import org.west.entidades.Tarefa;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.swing.JPanel;
import org.swingBean.gui.JBeanPanel;
import org.west.utilitarios.Util;

public class PanelGrafico extends JPanel {

    private List<Tarefa> lista;
    private Date dataMax;
    private Date dataMin;
    private Rectangle areaPlot;
    private Insets pad;
    private Integer entreEixos;
    private Integer tamanhoH;
    private Integer tamanhoV;
    private JBeanPanel<Tarefa> panel;

    public PanelGrafico(List<Tarefa> lista,JBeanPanel<Tarefa> panel) {
        this.lista = lista;
        this.panel = panel;
        configPanel();
    }
    
    public void setListaTarefas(List<Tarefa> lista){
        this.lista = lista;
        configPanel();
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (lista.size() > 0){

            //definição do ambiente
            g.setFont(new Font("verdana", Font.PLAIN, 10));

            pad = new Insets(0, 0, 0, 0);
            //definição do espaçamento
            pad.top = 10;
            pad.left = g.getFontMetrics().stringWidth(" DD/MM/YYYY ");
            pad.right = 10;
            pad.bottom = 3 * g.getFontMetrics().getHeight();

            areaPlot = new Rectangle((pad.left + entreEixos),
                    pad.top,
                    this.getWidth() - pad.right - pad.left - entreEixos,
                    this.getHeight() - pad.bottom - pad.top - entreEixos);


            Graphics2D grapi = (Graphics2D) g;
            g.setColor(Color.WHITE);
            grapi.fillRect(0, 0, this.getWidth(), this.getHeight());

            g.setColor(new Color(210, 210, 210));
            grapi.fill(areaPlot);

            desenharEixos(grapi);
            desenharGrade(grapi);
            desenharMarca(grapi);
            plotarTarefas(grapi);
        }
        else{
            g.setFont(new Font("Verdana",Font.PLAIN,18));
            String str = "Não há tarefas cadastradas para esse usuário!";
            int x = (this.getBounds().width - g.getFontMetrics().stringWidth(str))/2;
            int y = (this.getBounds().height - g.getFontMetrics().getHeight())/2;
            g.drawString(str,x,y);
        }
    }

    private void desenharMarca(Graphics2D g) {
        if (dataMax.after(new Date()) &&  dataMin.before(new Date())){
            g.setStroke(new BasicStroke());
            g.setColor(Color.blue);
            int difer = Util.getDiasEntre(dataMin,dataMax);
            int x = (areaPlot.width / difer) * (Util.getDiasEntre(dataMin,new Date()));
            x+= areaPlot.x;
            g.drawLine(x ,areaPlot.y, x, areaPlot.y + areaPlot.height);
            g.setColor(Color.BLACK);
            g.drawString("Hoje",x - (g.getFontMetrics().stringWidth("Hoje")/2) , areaPlot.y - 3);
        }
    }

    private void desenharEixos(Graphics2D g) {
        g.setColor(new Color(50, 50, 50));

        Point p1 = new Point(areaPlot.x, areaPlot.y);
        Point p2 = new Point(areaPlot.x, areaPlot.height + areaPlot.y);
        Point p3 = new Point(areaPlot.x + areaPlot.width, areaPlot.height + areaPlot.y);

        //eixo X
        g.drawLine(p1.x - entreEixos, p1.y, p2.x - entreEixos, p2.y);

        //eixo Y
        g.drawLine(p2.x, p2.y + entreEixos, p3.x, p3.y + entreEixos);
    }

    private void desenharGrade(Graphics2D g) {

        float dash1[] = {2.0f};
        BasicStroke pontilha = new BasicStroke(0.2f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                5.0f, dash1, 2.0f);
        g.setStroke(pontilha);
        g.setColor(Color.white);

        int difer = Util.getDiasEntre(dataMin,dataMax) + 1;
        SimpleDateFormat fm = new SimpleDateFormat(" dd/MM ");

        //grade vertical
        Date aux = new Date(dataMin.getTime());
        tamanhoH = (int) areaPlot.getWidth() / (difer/7);
        int posX = 0;

        while(posX <= difer){
            g.setColor(Color.WHITE);
            g.drawLine(posX * tamanhoH + areaPlot.x, areaPlot.y, posX * tamanhoH + areaPlot.x, areaPlot.y + areaPlot.height);
            g.setColor(Color.BLACK);
            AffineTransform fontAT = new AffineTransform();
            Font theFont = g.getFont();
            fontAT.rotate(-Math.PI / 4);
            fontAT.translate(-32, 0);
            Font theDerivedFont = theFont.deriveFont(fontAT);
            g.setFont(theDerivedFont);
            g.drawString(fm.format(aux), posX * tamanhoH + areaPlot.x, areaPlot.height + areaPlot.y + 2*g.getFontMetrics().getHeight());
            g.setFont(theFont);
            aux = Util.addDias(aux, 7);
            posX++;
        }

        //grade horizontal
        tamanhoV = (int) areaPlot.getHeight() / (difer/7);
        int posY = 0;
        aux = new Date(dataMin.getTime());
        fm = new SimpleDateFormat(" dd/MM/yyyy ");

        while (posY <= difer) {
            int tamanho = areaPlot.y + areaPlot.height;
            g.setColor(Color.WHITE);
            g.drawLine(areaPlot.x,tamanho - (posY *tamanhoV),areaPlot.x + areaPlot.width ,tamanho - (posY *tamanhoV));
            g.setColor(Color.BLACK);
            g.drawString(fm.format(aux), 0, (tamanho - (posY *tamanhoV)) + g.getFontMetrics().getHeight() /2);
            aux = Util.addDias(aux, 7);
            posY++;
        }

        g.setColor(Color.WHITE);
    }

    private void plotarTarefas(Graphics2D gra) {
        int difer = Util.getDiasEntre(dataMin, dataMax) + 1;

        int escalaX = areaPlot.width / difer;
        int escalaY = areaPlot.height / difer;

        gra.setStroke(new BasicStroke(1));
        gra.setColor(new Color(100,100,100));

        for (Iterator<Tarefa> it = lista.iterator(); it.hasNext();) {
            final Tarefa tarefa = it.next();

            ItemBolha novo = new ItemBolha();
            novo.setTarefa(tarefa);

            novo.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);

                    if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON1)
                        panel.setBean(tarefa);
                }
            });

            Point atual = new Point(0,300);

            atual.x = areaPlot.x + (Util.getDiasEntre(dataMin,tarefa.getPrevisaoTermino()) * escalaX);
            atual.y = (areaPlot.y + areaPlot.height) - Util.getDiasEntre(dataMin,tarefa.getPrevisaoTermino()) * escalaY;
            gra.drawOval(atual.x-20,atual.y-20,40,40);
            novo.setBounds(atual.x-20,atual.y-20, 40, 40);

            this.add(novo);
        }
    }

    private Date getComecoSemana(Date data) {
        return Util.addDias(data, - (Util.getCampo(data, Calendar.DAY_OF_WEEK) - 1));
    }

    private Date getFinalSemana(Date data) {
        return Util.addDias(data, 7 - (Util.getCampo(data, Calendar.DAY_OF_WEEK)));
    }

    private void configPanel() {
        if (lista.size() > 0) {
            this.dataMax = getFinalSemana(this.lista.get(this.lista.size() - 1).getPrevisaoTermino());
            this.dataMin = getComecoSemana(this.lista.get(0).getPrevisaoTermino());
            this.entreEixos = 5;
            this.tamanhoH = new Integer(0);
            this.tamanhoV = new Integer(0);
            this.pad = new Insets(0, 0, 0, 0);
        }    
    }
}