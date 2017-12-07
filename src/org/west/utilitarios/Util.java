package org.west.utilitarios;

import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.RootPaneContainer;

/**
 * A classe Util tem como função agrupar várias funcionalidades utilizadas em todo o sistema,
 * como o tratamento de datas ou utilização de sons.
 * @author Leonardo Guerra
 */

public class Util {

    /** Constante usada para definir o início de uma data*/
    public static final int INICIO = 1;
    /** Constante usada para definir o fim de uma data*/
    public static final int FIM = 2;
    /** Object usado para criar o loading dentro do sistema*/
    private static JLabel label;

    /**
     * Para fins de busca no banco de dados, é importante definir a parte de hora no
     * objeto data. Com este método é possivél definir o começo ou o fim do dia, dada uma 
     * determinada data.
     * @param data A data ser corrigida.
     * @param tipo Tipo de correção, para o início ou final da data.
     * @return data - Data depois de corrigida.
     * 
     * @see #INICIO
     * @see #FIM
     */
    public static Date corrigirDate(Date data, Integer tipo) {
        GregorianCalendar calenda = new GregorianCalendar();
        calenda.setTime(data);

        if (tipo.intValue() == INICIO) {
            calenda.set(Calendar.HOUR_OF_DAY, 0);
            calenda.set(Calendar.MINUTE, 0);
            calenda.set(Calendar.SECOND, 0);
        }

        if (tipo.intValue() == FIM) {
            calenda.set(Calendar.HOUR_OF_DAY, 23);
            calenda.set(Calendar.MINUTE, 59);
            calenda.set(Calendar.SECOND, 59);
        }

        return calenda.getTime();
    }
    
    /**
     * Adiciona uma quantidade de dias a uma determinada data. Pode-se adicionar 
     * dias negativamente, por exemplo, ao adicionar -1 dias, a data regride 1 dia.
     * @param data Data a ser modifica.
     * @param qt Quantidade de dias a ser adicionada à data.
     * @return data - Data modificada.
     */

    public static Date addDias(Date data, Integer qt) {
        GregorianCalendar calenda = new GregorianCalendar();
        calenda.setTime(data);

        calenda.add(Calendar.DAY_OF_YEAR, qt.intValue());

        return calenda.getTime();
    }

    /**
     * Adiciona uma quantidade de horas a uma determinada data. Pode-se adicionar
     * horas negativamente, por exemplo, ao adicionar -1 hora, a data regride 1 hora.
     * @param data Data a ser modifica.
     * @param qt Quantidade de horas a ser adicionada à data.
     * @return data - Data modificada.
     */
    public static Date addHoras(Date data, Integer qt) {
        GregorianCalendar calenda = new GregorianCalendar();
        calenda.setTime(data);

        calenda.add(Calendar.HOUR, qt.intValue());

        return calenda.getTime();
    }
    
    /**
     * Recupera uma determinado campo de uma data.
     * @param data Objeto {@link java.util.Date}
     * @param campo Campo que se deseja recuperar.
     * @return valor : Valor recuperado.
     */

    public static int getCampo(Date data, int campo) {
        GregorianCalendar calenda = new GregorianCalendar();
        calenda.setTime(data);

        return calenda.get(campo);
    }
    
    /**
     * Dadas duas datas distintas, retorna verdadeiro apenas se as duas datas
     * tiveram o mesmo valor no campo dia do ano e ano.
     * @param date1 Primeira data a ser comparada.
     * @param date2 Segunda data a ser comparada.
     * @return Boolean : Resultado da comparação.
     */

    public static boolean isMesmoDia(Date date1, Date date2) {
        return Util.getCampo(date1, Calendar.DAY_OF_YEAR) == Util.getCampo(date2,Calendar.DAY_OF_YEAR)
                &&
                Util.getCampo(date1,Calendar.YEAR) == Util.getCampo(date2,Calendar.YEAR);
    }
    
    /**
     * Dadas duas datas distintas, retorna o número de dias entre as 
     * duas datas.
     * @param ini Data inicial do período.
     * @param fim Data final do período.
     * @return cont : Quantidade de dias entre as duas datas.
     */

    public static int getDiasEntre(Date ini,Date fim){
        int cont = 0;
        Date inicio = new Date(ini.getTime());

        while (inicio.before(fim)){
            cont++;
            inicio = Util.addDias(inicio, 1);
        }

        return cont;
    }

    /**
     * Retorna as extremidades do mês, seja o começo ou fim dele.
     * @param date A data a ser processada.
     * @param tipo Inteiro referente ao início ou fim do mês.
     * @return data : Objeto {@link java.util.Date} referente ao final ou início do mês.
     */
    public static Date getExtremidadesMes(Date date,int tipo){
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        if (tipo == INICIO)
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));

        if (tipo == FIM)
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        return calendar.getTime();
    }

    /**
     * Mêtodo responsável por executar um determinado arquivo de som, utilizando
     * os paramêtros padrão do sistema. O arquivo deve estar dentro das pastas do sistema. 
     * @param arquivo String que representa o caminho do arquivo no sistema.
     */
    public static void tocarSom(String arquivo) {
        try {
            AudioInputStream aviso = AudioSystem.getAudioInputStream(new File(System.getProperty("user.dir") + arquivo));

            AudioFormat format = aviso.getFormat();

            if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
                format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), format.getSampleSizeInBits(), format.getChannels(), format.getFrameSize(), format.getFrameRate(), true);

                aviso = AudioSystem.getAudioInputStream(format, aviso);
            }

            DataLine.Info info = new DataLine.Info(Clip.class, aviso.getFormat(), (int) aviso.getFrameLength() * format.getFrameSize());
            Clip clip = (Clip) AudioSystem.getLine(info);

            clip.open(aviso);
            clip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * O sistema possui um arquivo de propriedades, que armazena diversas informações
     * de configuração. Este mêtodo recupera o valor de uma determinada propriedade.
     * @param busca String que representa a propriedade.
     * @return retorno : Valor armazenado no arquivo referente a propriedade.
     */
    public static String getPropriedade(String busca) {
        String retorno = null;
        try {
            BufferedReader in = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/system/user.properties"));
            String str = null;

            while(in.ready()){
                str = in.readLine();
                if (str.startsWith(busca)){
                    retorno = str.substring(str.indexOf("=") + 1).trim();
                    break;
                }
            }
            
            in.close();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return retorno;
    }
    
    /**
     * O sistema possui um arquivo de propriedades, que armazena diversas informações
     * de configuração. Este mêtodo armazena no arquivo no formato chave-valor
     * @param prop Nome da propriedade que será adicionado.
     * @param value Valor da propriedade.
     */

    public static void setPropriedade(String prop, String value) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/system/user.properties"));
            BufferedWriter out = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "/system/user1.properties",true));

            while (in.ready()) {
                String str = in.readLine();
                if (str.startsWith(prop)) {
                    str = str.substring(0, str.indexOf("=") + 1).concat(value);
                }
                out.write(str);
                out.newLine();
            }

            if (getPropriedade(prop) == null)
                out.append(prop + "=" + value);

            out.flush();

            out.close();
            in.close();

            File file0 = new File(System.getProperty("user.dir") + "/system/user.properties");
            File file1 = new File(System.getProperty("user.dir") + "/system/user1.properties");
            file0.delete();
            file1.renameTo(new File(System.getProperty("user.dir") + "/system/user.properties"));
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * A fim de distinguir ao usuário quando o sistema está em operação,
     * e mostrar a finalização do processo, este mêtodo utiliza a camada de
     * de pop-up menu para mostrar o load do sistema.
     * @param comp Componente onde deve mostrar o load.
     * @see #unload(java.awt.Component)
     */

    public static void load(Component comp){
        Container container = comp.getParent();
        RootPaneContainer root = null;

        while(true){
            if (container instanceof RootPaneContainer){
                root = (RootPaneContainer)container;
                break;
            }
            else
                container = container.getParent();
        }
        
        int w = comp.getWidth();
        int h = comp.getHeight();                
        
        ImageIcon icon = new ImageIcon(Util.class.getResource("/org/west/imagens/loading.gif"));
        w-= icon.getIconWidth();
        w/= 2;
        h-= icon.getIconHeight();
        h/= 2;        
        
        Point pComp = comp.getLocationOnScreen();
        Point pContainer = container.getLocationOnScreen();
        
        Point real = new Point(pComp.x - pContainer.x,pComp.y - pContainer.y);
        real.translate(w, h);
        
        label = new JLabel();        
        label.setIcon(icon);        
        label.setSize(icon.getIconWidth(),icon.getIconHeight());
        label.setLocation(real);
        root.getLayeredPane().add(label,JLayeredPane.POPUP_LAYER);
    }
   
    /**
     * Após utilizar o mêtodo {@link #load(java.awt.Component)}, este mêtodo deve ser chamado para 
     * desabilitar o load e continuar o funcionamento do sistema.
     * @param comp : Componente ao qual foi atachado o load.
     */

    public static void unload(Component comp){
        Container container = comp.getParent();
        RootPaneContainer root = null;

        while(true){
            if (container instanceof RootPaneContainer){
                root = (RootPaneContainer)container;
                break;
            }
            else
                container = container.getParent();
        }

        root.getLayeredPane().remove(label);
        root.getLayeredPane().repaint();
    }
    
    /**
     * Adiciona uma quantidade de anos a uma determinada data. Pode-se adicionar
     * anos negativamente, por exemplo, ao adicionar -1, a data regride 1 ano.
     * @param data Data a ser modifica.
     * @param qt Quantidade de anos a ser adicionada à data.
     * @return data - Data modificada.
     */
    public static Date addAnos(Date data, Integer qt){
        GregorianCalendar calenda = new GregorianCalendar();
        calenda.setTime(data);
        
        Integer anoAtual = getCampo(data, Calendar.YEAR);
        
        anoAtual+= qt;
        
        calenda.set(Calendar.YEAR, anoAtual);
        
        return calenda.getTime();
    }
}