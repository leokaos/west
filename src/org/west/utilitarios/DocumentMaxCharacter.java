package org.west.utilitarios;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class DocumentMaxCharacter extends PlainDocument {

    private int tamanhoMaximo = 0;

    public DocumentMaxCharacter(int tamanhoMaximo) {
        this.tamanhoMaximo = tamanhoMaximo;
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {

        if (tamanhoMaximo > 0){
            if((getLength() + str.length()) <= tamanhoMaximo)
                super.insertString(offs, str, a);
        }
        else
            super.insertString(offs, str, a);
    }
}