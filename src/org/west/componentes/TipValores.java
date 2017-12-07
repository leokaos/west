package org.west.componentes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import net.java.balloontip.BalloonTip;
import net.java.balloontip.styles.EdgedBalloonStyle;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.Range;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.west.entidades.Imovel;
import org.west.entidades.Valor;
import org.west.entidades.ValorDAO;
import org.west.utilitarios.Util;

public class TipValores extends BalloonTip{

    private Imovel imovel;
    
    public TipValores(JComponent attachedComponent, String text,Imovel imovel) {
        super(attachedComponent, text);
        this.imovel = imovel;
              
        configurarGrafico();
    }

    private void configurarGrafico() {
        setStyle(new EdgedBalloonStyle(new Color(236, 241, 140), new Color(161, 241, 140)));

        try{
            setVisible(false);
            removeAll();
            repaint();
            setLayout(new BorderLayout(5, 5));
            setSize(500, 200);
            getStyle().flipY(true);  
            
            List<Valor> valores = Arrays.asList(ValorDAO.listValorByQuery("imovel = " + imovel.getReferencia() ,"dataAltera"));
            JLabel grafico = new JLabel();
            grafico.setBorder(new EmptyBorder(5, 5, 5, 5));
            grafico.setHorizontalAlignment(SwingConstants.CENTER);
            add(grafico,BorderLayout.CENTER);

            if (valores != null && valores.size() > 0){
                               
                XYSeriesCollection dataset = new XYSeriesCollection();
                XYSeries serie = new XYSeries("Valor");
                
                for (Valor valor : valores) {
                    serie.add(valor.getValor().doubleValue(),valor.getDataAltera().getTime());
                }            
                
                dataset.addSeries(serie);
                
                JFreeChart chart = ChartFactory.createXYLineChart("", "", "", dataset, PlotOrientation.HORIZONTAL, false, false, false);
                
                XYPlot plot = (XYPlot) chart.getPlot();
                               
                DateAxis dateAxis = new DateAxis();
                dateAxis.setDateFormatOverride(new SimpleDateFormat("dd/MM/yyyy"));
                
                plot.getDomainAxis().setRange(getRangeImovel(valores));
                
                dateAxis.setLowerMargin(0.4);
                dateAxis.setUpperMargin(0.4);
                
                plot.setRangeAxis(dateAxis);
                
                XYLineAndShapeRenderer render = (XYLineAndShapeRenderer) plot.getRenderer();
                render.setBaseItemLabelsVisible(true);
                
                StandardXYItemLabelGenerator label = new StandardXYItemLabelGenerator(){
                    @Override
                    public String generateLabel(XYDataset dataset, int series, int item) {
                        DecimalFormat decFormat = new DecimalFormat("###,###,###");
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        
                        Double xValue = dataset.getXValue(series, item);
                        Double yValue = dataset.getYValue(series, item);
                        
                        Date data = new Date(yValue.longValue());
                        
                        return decFormat.format(xValue) + " em " + dateFormat.format(data);
                    }                   
                };
                render.setBaseItemLabelGenerator(label);

                plot.setRenderer(render);
                
                grafico.setIcon(new ImageIcon(chart.createBufferedImage(475,190)));
            }        
            else
                grafico.setText("Não existem alterações de valor!");
        }
        catch(Exception ex){ex.printStackTrace();}
    }

    private Range getRangeImovel(List<Valor> valores) {
        Double max = valores.get(0).getValor();
        Double min = valores.get(0).getValor();
        
        for (Valor valor : valores) {
            if (valor.getValor() > max)
                max = valor.getValor();
            else if (valor.getValor() < min)
                    min = valor.getValor();            
        }
        
        return new Range(min * 0.9, max * 1.1);
    }
}
