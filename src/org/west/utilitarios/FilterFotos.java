package org.west.utilitarios;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.west.entidades.Foto;
import org.west.entidades.FotoDAO;

public class FilterFotos implements FTPFileFilter{
    
    private String codigo;
    private List<String> listaFotos = new ArrayList<String>();

    public FilterFotos(Long codigo) {
        this.codigo = String.valueOf(codigo);
        
        for(Foto foto : FotoDAO.listFotoByQuery("imovel=" + this.codigo, "caminho"))
            listaFotos.add(foto.getCaminho());
    }

    @Override
    public boolean accept(FTPFile file) {
        return listaFotos.contains(file.getName());
    }    
}