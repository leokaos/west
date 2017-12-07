package org.west.componentes;

import org.swingBean.descriptor.validator.Validator;
import org.swingBean.util.BeanUtils;

public class ValidatorCNPJ extends Validator{

    @Override
    public boolean isValid(Object o) {
        String text = (String) BeanUtils.getProperty(o, getProperty());
        text = text.replace(".", "").replace("-", "").replace("/","").trim();
        
        if (text.isEmpty())
            return true;
        
        String multi = "678923456789";
        String digitos = "";
        StringBuilder cnpj = new StringBuilder(text.substring(0, text.length() - 2));
        
        int total = 0;
        
        for(int x  = 0; x < cnpj.length();x++)
            total+= Integer.parseInt(multi.substring(x, x+1)) * Integer.parseInt(cnpj.substring(x, x+1));

        String resto = String.valueOf(total%11);
        digitos = resto.substring(resto.length() - 1);
        
        multi = "5678923456789";
        total = 0;
        cnpj.append(digitos);
                
        for(int x  = 0; x < cnpj.length();x++)
            total+= Integer.parseInt(multi.substring(x, x+1)) * Integer.parseInt(cnpj.substring(x, x+1));
                
        resto = String.valueOf(total%11);
        digitos = resto.substring(resto.length() - 1);
        
        cnpj.append(digitos);
        
        return cnpj.toString().equals(text);
    }

    @Override
    protected String getGeneratedErrorMessage(String string) {
        return "CNPJ inválido!";
    }
    
}