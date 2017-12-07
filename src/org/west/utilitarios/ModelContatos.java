package org.west.utilitarios;

import javax.swing.DefaultListModel;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.packet.Presence;

public class ModelContatos extends DefaultListModel {

    public void mudaStatus(Presence presence) {
        for (int x = 0; x < getSize(); ++x) {
            RosterEntry entry = (RosterEntry) get(x);
            if (presence.getFrom().startsWith(entry.getUser())) {
                remove(x);
                add(x, entry);
            }
        }
    }
}
