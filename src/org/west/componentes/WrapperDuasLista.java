package org.west.componentes;

import java.util.Arrays;
import java.util.Collection;
import org.swingBean.gui.wrappers.ArrayWrapper;

public class WrapperDuasLista extends ArrayWrapper{

    @Override
    public Object getValue() {
        Object[] dados = (Object[]) super.getValue();
        return Arrays.asList(dados);
    }

    @Override
    public void setValue(Object value) {
        Collection lista = (Collection) value;
        super.setValue(lista.toArray());
    }
}
