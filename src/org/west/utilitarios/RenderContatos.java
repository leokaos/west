package org.west.utilitarios;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.packet.Presence;

public class RenderContatos extends DefaultListCellRenderer {

    private Roster roster;

    public RenderContatos(Roster roster) {
        this.roster = roster;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        RosterEntry entry = (RosterEntry) value;
        Presence presenca = this.roster.getPresence(entry.getUser());

        if (presenca.getType() == Presence.Type.available) {
            setIcon(new ImageIcon(super.getClass().getResource("/org/west/imagens/online.png")));
        }
        if (presenca.getType() == Presence.Type.unavailable) {
            setIcon(new ImageIcon(super.getClass().getResource("/org/west/imagens/offline.png")));
        }
        setText(entry.getName());

        if (presenca.getStatus() != null) {
            setText(getText() + " - " + presenca.getStatus());
        }
        setPreferredSize(new Dimension(0, 30));

        return this;
    }
}
