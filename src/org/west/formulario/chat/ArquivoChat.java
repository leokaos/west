package org.west.formulario.chat;

import org.west.componentes.CriptoManager;
import org.west.entidades.Usuario;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import javax.swing.JTextArea;

public class ArquivoChat implements Runnable {

    private String chave;
    private String texto;
    private String nome;
    private Integer tipo;
    private JTextArea area;
    private Usuario usuario;
    public static final Integer LER = 0;
    public static final Integer GRAVAR = 1;

    public ArquivoChat(String nome, Usuario usuario, Integer tipo) {
        this.chave = CriptoManager.hash(String.valueOf(usuario.getCodigo())).substring(0, 16);
        this.nome = nome;
        this.tipo = tipo;
        this.usuario = usuario;
    }

    public void setTexto(String texto) {
        this.texto = (texto);
    }

    public void setArea(JTextArea area) {
        this.area = area;
    }

    @Override
    public void run() {
        if (this.tipo == GRAVAR) {
            try {
                File file = new File(System.getProperty("user.dir") + "/system/" + this.usuario.getNome());

                if (!file.exists()) {
                    file.mkdir();
                }
                BufferedWriter arquivo = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "/system/" + this.usuario.getNome() + "/" + this.nome + ".wes", true));
                String cifra = CriptoManager.cifrar(this.texto, this.chave.getBytes());
                arquivo.append(cifra);
                arquivo.newLine();
                arquivo.append("-->");
                arquivo.newLine();
                arquivo.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (this.tipo == LER) {
            try {
                File file = new File(System.getProperty("user.dir") + "/system/" + this.usuario.getNome() + "/" + this.nome + ".wes");
                if (file.exists()) {
                    BufferedReader in = new BufferedReader(new FileReader(file));
                    String str = "";
                    while (in.ready()) {
                        String line = in.readLine();
                        if (line.equals("-->")){
                            this.area.setText(this.area.getText() + CriptoManager.decifrar(str, chave.getBytes()));
                            str = "";
                        }
                        else
                            str+=line;
                    }

                    this.area.setText(this.area.getText() + CriptoManager.decifrar(str, chave.getBytes()));
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}