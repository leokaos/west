package org.west.componentes;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import org.swingBean.gui.EmptyNumberFormatterNoLetter;
import org.swingBean.gui.wrappers.ComponentWrapper;
import org.west.entidades.Imovel;
import org.west.entidades.Numero;
import static org.west.utilitarios.ValidadorUtil.isNotNull;

/**
 * Componente responsável por exibir os números de um imóvel. Dentro da entidade
 * {@link Imovel} é possível que tenha mais de um número, portanto, este
 * componente tem a função de criar quantos campos de texto forem necessários,
 * através de um menu de contexto.
 *
 * @author West Guerra Ltda
 */
public class WrapperNumeroImovel implements ComponentWrapper {

    private List<JFormattedTextField> listaCampos;
    private String separador;
    private JPanel panel;
    private JPanel panelBotoes;
    private JLabel labelAdd;
    private JLabel labelRemove;

    @Override
    public Object getValue() {
        List<Numero> numeros = new ArrayList<Numero>();

        for (JFormattedTextField jFormattedTextField : listaCampos) {
            Long numero = getNumeroFromField(jFormattedTextField);
            numeros.add(new Numero(null, numero));
        }

        return numeros;
    }

    @Override
    public void setValue(Object o) {
        if (o != null) {
            Collection<Numero> numeros = (Collection) o;

            listaCampos.clear();

            for (Numero numero : numeros) {
                JFormattedTextField txt = createField(numero.getNumeroPK().getNumero());
                listaCampos.add(txt);
            }
        }

        reconstrirPanel();
    }

    @Override
    public void cleanValue() {
        listaCampos.clear();
        reconstrirPanel();
    }

    @Override
    public Component getComponent() {
        return panel;
    }

    @Override
    public void setEnable(boolean bln) {
        panel.setEnabled(bln);

        for (Component comp : panel.getComponents()) {
            comp.setEnabled(bln);
        }

        labelAdd.setEnabled(bln);
        labelRemove.setEnabled(bln);
    }

    @Override
    public void initComponent() throws Exception {
        listaCampos = new LinkedList<JFormattedTextField>();
        panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

        createPanelBotoes();

        reconstrirPanel();
    }

    private void reconstrirPanel() {
        panel.removeAll();

        if (listaCampos.isEmpty()) {
            JFormattedTextField txt = createField();
            listaCampos.add(txt);
            panel.add(txt);
            txt.addFocusListener(createFocusListener());
        } else {
            Iterator<JFormattedTextField> it;

            for (it = listaCampos.iterator(); it.hasNext();) {
                JFormattedTextField txt = it.next();
                panel.add(txt);

                if (it.hasNext()) {
                    panel.add(createLabel());
                }

                txt.addFocusListener(createFocusListener());
            }
        }

        panel.add(panelBotoes);

        panel.revalidate();
        panel.repaint();
    }

    private JLabel createLabelActions(String str) {
        JLabel labelAction = new JLabel(new ImageIcon(super.getClass().getResource(str)));
        labelAction.setPreferredSize(new Dimension(10, 10));
        labelAction.setHorizontalAlignment(SwingConstants.CENTER);

        return labelAction;
    }

    private void createPanelBotoes() {
        panelBotoes = new JPanel(new BorderLayout(0, 0));

        labelAdd = createLabelActions("/org/west/imagens/add.png");
        labelRemove = createLabelActions("/org/west/imagens/aberta.png");

        labelAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (labelAdd.isEnabled()) {
                    listaCampos.add(createField());
                    reconstrirPanel();
                }
            }
        });

        labelRemove.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (labelRemove.isEnabled()) {
                    listaCampos.remove(listaCampos.size() - 1);
                    reconstrirPanel();
                }
            }
        });

        panelBotoes.add(labelAdd, BorderLayout.NORTH);
        panelBotoes.add(labelRemove, BorderLayout.SOUTH);
    }

    private JFormattedTextField createField() {
        JFormattedTextField txt = new JFormattedTextField(createFormatoField());
        txt.setPreferredSize(getTamanhoPadrao());
        return txt;
    }

    private JFormattedTextField createField(Long numero) {
        JFormattedTextField txt = createField();
        txt.setValue(numero.doubleValue());

        try {
            txt.commitEdit();
        } catch (ParseException ex) {
            txt.setText(String.valueOf(numero));
        }
        return txt;
    }

    private JLabel createLabel() {
        final JLabel label = new JLabel(separador, SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(15, 20));
        return label;
    }

    private Dimension getTamanhoPadrao() {
        return new Dimension(40, 23);
    }

    private EmptyNumberFormatterNoLetter createFormatoField() {
        return new EmptyNumberFormatterNoLetter();
    }

    public void setSeparador(String separador) {
        this.separador = separador;
    }

    protected List<JFormattedTextField> getListaCampos() {
        return listaCampos;
    }

    private FocusListener createFocusListener() {
        return new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                panel.firePropertyChange("numero", 2, 1);
            }
        };
    }

    private Long getNumeroFromField(JFormattedTextField jFormattedTextField) {
        Long retorno = new Long(0);

        try {
            jFormattedTextField.commitEdit();

            if (isNotNull(jFormattedTextField.getValue())) {
                if (jFormattedTextField.getValue() instanceof Long) {
                    retorno = (Long) jFormattedTextField.getValue();
                }
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        return retorno;
    }
}
