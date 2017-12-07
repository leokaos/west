package org.west.utilitarios;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Classe responsável por limitar a deleção de caracteres de uma String. Dada
 * uma String inicial, ela servirá de base para o controle da deleção, evitando
 * a remoção de caracteres, mas permetindo a adição.
 *
 * @author WestGuerra Ltda.
 */
public class DocumentNotRemove extends PlainDocument {

    /**
     * Valor do campo inicialmente.
     */
    private String str;

    /**
     * Construtor que recebe a string inicial, a qual será o conteúdo inicial do
     * campo.
     *
     * @param str
     */
    public DocumentNotRemove(String str) {
        this.str = str;
    }

    /**
     * Sobreescreve o método remove, controlando a deleção de carateres.
     */
    @Override
    public void remove(int offs, int len) throws BadLocationException {
        if (offs < str.length()) {
            return;
        }

        super.remove(offs, len);
    }

    /**
     * Sobreescreve o método replace, evitando a substituição de caracteres da sequência inicial.
     */
    @Override
    public void replace(int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        String valorAtual = getText(0, getLength());

        if (valorAtual.isEmpty()) {
            super.replace(offset, length, text, attrs);
        } else if (offset >= str.length()) {
            super.replace(offset, length, text, attrs);
        }
    }
}