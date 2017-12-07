package org.west.utilitarios;

import javax.swing.text.BadLocationException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DocumentNotRemoveTest {

    private static final String str = "leonardo alejando guerra otero";
    private static final int total = 30;

    public DocumentNotRemoveTest() {
    }

    @Test
    public void removeTest() throws BadLocationException {
        DocumentNotRemove doc = new DocumentNotRemove(str);
        doc.insertString(0, str, null);

        doc.remove(0, 29);

        assertEquals(doc.getText(0, doc.getLength()), str);
    }

    @Test
    public void insertTest() throws BadLocationException {
        DocumentNotRemove doc = new DocumentNotRemove(str);
        doc.insertString(0, str, null);

        doc.replace(0, 0, "aaa", null);

        assertEquals(doc.getText(0, 30), str);
    }
}
