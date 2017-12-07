package org.west.componentes;

import org.swingBean.descriptor.validator.Validator;
import org.swingBean.util.BeanUtils;

public class ValidatorCPF extends Validator{

    @Override
    public boolean isValid(Object o) {
        String text = (String) BeanUtils.getProperty(o, getProperty());
        text = text.replace(".", "").replace("-", "").trim();
        
        if (text.isEmpty())
            return true;
        
        String str = text.substring(0, text.length() - 2);
        String digitos = "";
        
        if (str.length() == 9){
            Integer total = 0;
            
            for(int x = 0; x < str.length();x++){
                Character caracter = str.charAt(x);
                Integer digito = new Integer(caracter.toString());
                total = total + (digito * (10 - x));
            }
            
            if (total%11 >= 2)
                digitos = String.valueOf(11 - (total%11));
            else
                digitos = "0";
            
            str+= digitos;
                        
            total = 0;
            
            for(int x = 0; x < str.length(); x++){
                Character caracter = str.charAt(x);
                Integer digito = new Integer(caracter.toString());
                total = total + (digito * (11 - x));       
            }
                        
            if (total%11 >= 2)
                digitos = String.valueOf(11 - (total%11));
            else
                digitos = "0";
            
            str+= digitos;
                                    
            if (str.equals(text.replace(".", "").replace("-", "")))
                return true;
            else
                return false;
        }
        else
            return false;
    }

    @Override
    protected String getGeneratedErrorMessage(String string) {
        return "CPF inválido!";
    }
    
}