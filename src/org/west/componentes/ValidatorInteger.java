package org.west.componentes;

import org.swingBean.descriptor.validator.Validator;
import org.swingBean.util.BeanUtils;

public class ValidatorInteger extends Validator{

    @Override
    public boolean isValid(Object o) {
        Object obj = BeanUtils.getProperty(o, getProperty());
        return (obj!=null);
    }

    @Override
    protected String getGeneratedErrorMessage(String string) {
        return "Valor do campo "+string+" é inválido.";
    }
}