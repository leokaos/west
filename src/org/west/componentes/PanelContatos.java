package org.west.componentes;

import org.west.utilitarios.Util;
import org.west.utilitarios.ModelContatos;
import org.west.utilitarios.RenderContatos;
import org.west.entidades.Usuario;
import org.west.formulario.chat.FrmChat;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

public class PanelContatos extends JPanel {

    private JList listaContatos;
    private RenderContatos render;
    private ModelContatos model;
    private XMPPConnection conexao;
    private Roster roster;
    private Map<String, FrmChat> mapaJanelas;
    private Usuario usuario;

    public PanelContatos() {
        this.usuario = (Usuario) DesktopSession.getInstance().getObject("usuario");
        this.model = new ModelContatos();
        this.mapaJanelas = new HashMap();

        try {
            ConnectionConfiguration config = new ConnectionConfiguration("weststorage", 5222);
            conexao = new XMPPConnection(config);

            conexao.connect();

            conexao.login(usuario.getNome(), usuario.getSenha());

            roster = conexao.getRoster();

            Collection<RosterEntry> entries = roster.getEntries();

            for (RosterEntry entry : entries) {
                if (entry.getName() != null) {
                    model.addElement(entry);
                }
            }

            Presence online = new Presence(Presence.Type.available);
            online.setMode(Presence.Mode.available);
            this.conexao.sendPacket(online);

            this.roster.addRosterListener(new RosterListener() {

                @Override
                public void entriesAdded(Collection<String> lista) {
                    for (Iterator it = lista.iterator(); it.hasNext();) {
                        String string = (String) it.next();

                        Presence subscribe = new Presence(Presence.Type.subscribed);
                        subscribe.setTo(string);
                        subscribe.setFrom(getNome(conexao.getUser()) + "@west");
                        conexao.sendPacket(subscribe);

                        subscribe = new Presence(Presence.Type.subscribe);
                        subscribe.setTo(string);
                        subscribe.setFrom(getNome(conexao.getUser()) + "@west");
                        conexao.sendPacket(subscribe);
                    }
                    atualizaContatos();
                }

                @Override
                public void entriesUpdated(Collection<String> lista) {
                    for (Iterator it = lista.iterator(); it.hasNext();) {
                        String string = (String) it.next();

                        Presence subscribe = new Presence(Presence.Type.subscribed);
                        subscribe.setTo(string);
                        subscribe.setFrom(getNome(conexao.getUser()) + "@west");
                        conexao.sendPacket(subscribe);

                        subscribe = new Presence(Presence.Type.subscribe);
                        subscribe.setTo(string);
                        subscribe.setFrom(getNome(conexao.getUser()) + "@west");
                        conexao.sendPacket(subscribe);
                    }
                    atualizaContatos();
                }

                @Override
                public void entriesDeleted(Collection<String> clctn) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public void presenceChanged(Presence presence) {
                    model.mudaStatus(presence);
                }
            });

            PacketListener listener = new PacketListener() {

                @Override
                public void processPacket(Packet packet) {
                    Message msg = (Message) packet;
                    String from = getNome(msg.getFrom());
                    FrmChat chat = null;

                    if (!mapaJanelas.containsKey(from)) {
                        chat = new FrmChat(conexao.getChatManager(), msg.getFrom(), usuario.getNome());
                        mapaJanelas.put(from, chat);
                    } else {
                        chat = (FrmChat) mapaJanelas.get(from);
                    }

                    chat.addMensagem(msg);
                    chat.setVisible(true);
                    Util.tocarSom("/sons/Message.wav");
                }
            };

            PacketTypeFilter filter = new PacketTypeFilter(Message.class);

            this.conexao.addPacketListener(listener, filter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.render = new RenderContatos(this.conexao.getRoster());

        this.listaContatos = new JList(this.model);
        this.listaContatos.setCellRenderer(this.render);

        this.listaContatos.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (e.getClickCount() == 2) {
                    RosterEntry entry = (RosterEntry) listaContatos.getSelectedValue();
                    String from = entry.getUser().substring(0, entry.getUser().indexOf("@"));

                    FrmChat chat = null;
                    if (mapaJanelas.containsKey(from)) {
                        chat = (FrmChat) mapaJanelas.get(from);
                    } else {
                        chat = new FrmChat(conexao.getChatManager(), entry.getUser(), usuario.getNome());
                        mapaJanelas.put(getNome(entry.getUser()), chat);
                    }
                    chat.setVisible(true);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(this.listaContatos);
        scrollPane.setPreferredSize(new Dimension(200, 200));

        setLayout(new GridBagLayout());

        GridBagConstraints cons = new GridBagConstraints();
        cons.insets = new Insets(10, 10, 10, 10);
        cons.anchor = 10;
        cons.weightx = 1.0D;
        cons.weighty = 1.0D;
        cons.gridx = 0;
        cons.gridy = 0;
        cons.fill = 1;

        add(scrollPane, cons);

        JPanel panelAdd = new JPanel(new GridLayout(3, 2, 20, 20));
        panelAdd.setBorder(BorderFactory.createTitledBorder("Adicionar Usuário"));

        panelAdd.add(new JLabel("Usuário: "));
        final JTextField txtUsuario = new JTextField();
        panelAdd.add(txtUsuario);

        panelAdd.add(new JLabel("Nome: "));
        final JTextField txtNome = new JTextField();
        panelAdd.add(txtNome);
        panelAdd.add(new JLabel());

        JButton btnAdd = new JButton("Adicionar");

        btnAdd.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    conexao.getRoster().createEntry(txtUsuario.getText() + "@west", txtNome.getText(), null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                txtUsuario.setText("");
                txtNome.setText("");
                atualizaContatos();
            }
        });
        panelAdd.add(btnAdd);

        cons.gridy = 1;
        cons.weightx = 0.0D;
        cons.weighty = 0.0D;
        add(panelAdd, cons);
    }

    private void atualizaContatos() {
        this.model = new ModelContatos();
        this.roster = this.conexao.getRoster();

        Collection<RosterEntry> entries = this.roster.getEntries();

        for (RosterEntry entry : entries) {
            if (entry.getName() != null) {
                this.model.addElement(entry);
            }
        }

        this.listaContatos.setModel(this.model);
        this.listaContatos.setCellRenderer(new RenderContatos(this.roster));
    }

    private String getNome(String valor) {
        String retorno = null;
        int pos = valor.indexOf("@");

        if (pos == -1) {
            retorno = valor;
        } else {
            retorno = valor.substring(0, pos);
        }
        return retorno;
    }

    public void alteraSenha(String nova) {
        try {
            if (this.conexao.isConnected()) {
                this.conexao.getAccountManager().changePassword(nova);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}