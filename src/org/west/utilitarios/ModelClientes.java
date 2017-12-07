package org.west.utilitarios;

import org.west.entidades.Imobiliaria;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class ModelClientes extends AbstractTableModel {

    private List<Imobiliaria> lista;
    private String[] colunas = {"Código","Nome","Corretor","Data"};

    public ModelClientes(List<Imobiliaria> lista) {
        this.lista = lista;
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public String getColumnName(int column) {
        return colunas[column];
    }

    @Override
    public int getRowCount() {
        return lista.size();
    }

    @Override
    public Object getValueAt(int row, int column) {
        Imobiliaria imob = lista.get(row);
        String retorno = "";        
        SimpleDateFormat sp = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        switch(column){

            case 0:{
                retorno = String.valueOf(imob.getCliente().getCodigo());
                break;
            }

            case 1:{
                retorno = imob.getCliente().getNome();
                break;
            }

            case 2:{
                retorno = imob.getUsuario().getNome();
                break;
            }

            case 3:{
                retorno = sp.format(imob.getDataEntrada());
                break;
            }
        }

        return retorno;
    }   

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}