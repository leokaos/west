package org.west.grafico;

import org.west.entidades.Tarefa;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;

public class ItemBolha extends JLabel{

    private Tarefa tarefa;

    public ItemBolha() {
        super();
    }

    public void setTarefa(Tarefa tarefa){
        this.tarefa = tarefa;
        SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
        this.setToolTipText(fm.format(tarefa.getPrevisaoTermino()) + ": " + tarefa.getNome());
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    public void paint(Graphics gra) {
        super.paint(gra);
        Graphics2D g = (Graphics2D) gra;

        Color fill;
        g.setStroke(new BasicStroke(10));

        if (tarefa.getPrevisaoTermino().before(new Date())){
            fill = new Color(244,57,57,150);
        }
        else{
            double diferenca = (new Date().getTime() - tarefa.getDataInicio().getTime());
            diferenca/= ( - tarefa.getPrevisaoTermino().getTime() - tarefa.getDataInicio().getTime());

            if (diferenca >= 0.7){
                fill = new Color(251,251,90,150);
            }
            else{
                fill = new Color(112,250,94,150);
            }
        }

        gra.setColor(fill);
        gra.fillOval(0,0,this.getBounds().width,this.getBounds().height);
    }
}