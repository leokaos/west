/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.west.utilitarios;

import java.io.File;
import java.io.FileInputStream;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author leo
 */
public class FtpTeste {

    public FtpTeste() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @Test
    public void churros() throws Exception {
        FTPClient ftp = new FTPClient();

        ftp.connect("ftp.westguerra.com.br");
        ftp.enterLocalPassiveMode();

        if (ftp.login("westguerra", "w3s4t5g6")) {
            
            ftp.changeWorkingDirectory("httpdocs/Fotos/leo");
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);

            String caminho = "\\\\weststorage\\Fotos\\34574_1.jpg";

            FileInputStream in = new FileInputStream(new File(caminho));
            ftp.storeFile("leo.jpg", in);
        }
    }
}
