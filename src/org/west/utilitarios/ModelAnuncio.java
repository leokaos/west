package org.west.utilitarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.AbstractListModel;
import org.west.entidades.Anuncio;

public class ModelAnuncio extends AbstractListModel {

    private List<Anuncio> listaAnuncios;

    public ModelAnuncio(List<Anuncio> listaAnuncios) {
        this.listaAnuncios = listaAnuncios;
    }

    public ModelAnuncio(Anuncio[] anuncios) {
        this.listaAnuncios = new ArrayList<Anuncio>(Arrays.asList(anuncios));
    }

    public ModelAnuncio() {
        this.listaAnuncios = new ArrayList<Anuncio>();
    }

    @Override
    public int getSize() {
        return listaAnuncios.size();
    }

    @Override
    public Object getElementAt(int index) {
        return listaAnuncios.get(index);
    }

    public void sortElements(final List<Anuncio> listAnuncio) {
        Collections.sort(listaAnuncios, new Comparator<Anuncio>() {
            @Override
            public int compare(Anuncio o1, Anuncio o2) {
                if (listAnuncio.contains(o1) && !listAnuncio.contains(o2)) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
    }
}