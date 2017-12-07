package org.west.utilitarios;

import org.west.entidades.Imobiliaria;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class RenderImob extends DefaultTableCellRenderer {

    private List<Imobiliaria> lista;

    public RenderImob(List<Imobiliaria> lista) {
        this.lista = lista;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        setBackground(Color.WHITE);
        setFont(getFont().deriveFont(Font.PLAIN));

        Imobiliaria imob = (Imobiliaria) this.lista.get(row);
        
        if (imob.getUsuario() == null)
            setBackground(new Color(227,227,77));
        else{        
            if (imob.isAcompanhado())
                setBackground(new Color(255, 187, 70));
            else {
                if (imob.getStatus() == Imobiliaria.SEM_CIENCIA)
                    setBackground(new Color(251, 120, 120));
                else
                    if (imob.getPrioridade())
                        setBackground(new Color(225,233,254));
                    else{
                        switch (imob.getStatus()) {
                            case Imobiliaria.SEM_RETORNO: {
                                setFont(getFont().deriveFont(Font.BOLD));
                                break;
                            }
                            case Imobiliaria.SEM_CONTATO: {
                                setBackground(new Color(160, 240, 185));
                                break;
                            }
                        }              
                    }
            }
        }
        
        if (value instanceof Date){
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            setText(format.format((Date) value));
        }

        return this;
    }
}