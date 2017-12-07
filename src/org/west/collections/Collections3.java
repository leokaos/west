package org.west.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Collections3 {

    public static <T, S> Collection<T> filter(Collection<T> originalCollection, FilterByValue<T, S> filter) {
        Collection<T> finalCollection = new ArrayList<T>();

        for (T t : originalCollection) {

            if (!hasElement(finalCollection, filter, t)) {
                finalCollection.add(t);
            }
        }

        return finalCollection;
    }

    public static <T, S> boolean hasElement(Collection<T> collection, FilterByValue<T, S> filter, T element) {

        for (T t : collection) {
            if (filter.getValue(t).equals(filter.getValue(element))) {
                return true;
            }
        }

        return false;
    }

    public static <T, S> List<S> extract(Collection<T> collection, FilterByValue<T, S> filter) {
        List<S> lista = new ArrayList<S>();

        for (T t : collection) {
            lista.add(filter.getValue(t));
        }

        return lista;
    }

    public static <T, S> T first(Collection<T> collection, FilterByValue<T, S> filter, S search) {
        for (T t : collection) {
            if (search.equals(filter.getValue(t))) {
                return t;
            }
        }

        return null;
    }

    public static <T, S> List<T> extractByFilter(Collection<T> collection, Filtro<T> filter) {
        List<T> lista = new ArrayList<T>();

        for (T t : collection) {
            if (filter.accept(t)){
                lista.add(t);
            }
        }

        return lista;
    }
}
