package org.west.componentes;

import org.west.entidades.Cep;
import org.west.entidades.CepDAO;

public class WrapperCep extends WrapperEdit{

    @Override
    public Object getValue() {
        Object obj = super.getValue();
        
        if (obj != null){
            String texto = obj.toString().replace("-", "").replace(" ","");

            if (texto.length() == 8){
                Cep cep = CepDAO.get(texto);

                if (cep != null)
                    return cep;
                else
                    this.valor = null;
            }
        }

        return null;
    }

    @Override
    public void setValue(Object o) {
        Cep Cep = (Cep) o;
        String str = Cep.getCEP();
        o = str;
        super.setValue(o);
    }   
}