package org.west.componentes;

import java.net.InetAddress;
import org.apache.log4j.Appender;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.west.entidades.Usuario;

public class LoggerManager {

    final static Logger logger = Logger.getLogger(LoggerManager.class);

    static {
        logger.setLevel(Level.ERROR);
    }

    public static void escreveLog(String str) {
        BasicConfigurator.configure();
        try {
            Appender fileAppender = new FileAppender(new PatternLayout("[%p] %d{dd/MM/yyyy HH:mm:ss} -> %m %n"), "\\\\weststorage\\Fotos\\log.txt");
            logger.addAppender(fileAppender);
            logger.setLevel(Level.INFO);
            logger.info(str);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void gravarEntrada(Usuario usuario) {
        String log = " ";
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            log += (ip);
        } catch (Exception ex) {
            ex.printStackTrace();
            log += ("Erro de IP");
        }
        log += (" " + usuario.getNome());
        escreveLog(log);
    }

    public static void gravaErro(Exception ex) {
        BasicConfigurator.configure();
        try {
            Appender fileAppender = new FileAppender(new PatternLayout("[%p] %d{dd/MM/yyyy HH:mm:ss} -> %m %n"), "/system/error.log");
            logger.addAppender(fileAppender);
            logger.setLevel(Level.ERROR);
            logger.info(ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void gravaLogPortaria(String str) {
        BasicConfigurator.configure();
        try {
            Appender fileAppender = new FileAppender(new PatternLayout("[%p] %d{dd/MM/yyyy HH:mm:ss} -> %m %n"), "\\\\servidor\\Fotos\\logPortaria.txt");
            logger.addAppender(fileAppender);
            logger.setLevel(Level.INFO);
            logger.info(str);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}