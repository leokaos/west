package org.west.formulario.recepcao;

import org.west.entidades.Cliente;
import org.west.entidades.ClienteCriteria;
import org.west.entidades.Imobiliaria;
import org.west.entidades.ImobiliariaDAO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.west.entidades.Usuario;
import org.west.entidades.Veiculo;

public class ClienteControl {

    public static List<Cliente> getLista(String busca) {
        List<Cliente> clientes = new ArrayList<Cliente>();
        List<String> valores = Arrays.asList(busca.split(" "));
               
        try{
            ClienteCriteria clienteCriteria = new ClienteCriteria();
            clienteCriteria.createAlias("telefones", "tele",Criteria.LEFT_JOIN);
            Disjunction disjunctionCliente = Restrictions.disjunction();

            for (Iterator<String> it = valores.iterator(); it.hasNext();) {
                String telefone = it.next();    
                
                if (isNumero(telefone)){
                    disjunctionCliente.add(Restrictions.like("telefone", "%" + telefone + "%"));
                    disjunctionCliente.add(Restrictions.ilike("tele.telefone", telefone, MatchMode.ANYWHERE));
                }                    
            }            
            
            clienteCriteria.add(disjunctionCliente); 
            clientes = clienteCriteria.list();
        }
        catch(Exception ex){ex.printStackTrace();}
        
        return clientes;        
    }

    private static Boolean isNumero(String teste) {
        char[] array = teste.toCharArray();
        Boolean retorno = Boolean.valueOf(true);

        for (int i = 0; i < array.length; ++i) {
            if (!Character.isDigit(array[i])) {
                retorno = Boolean.valueOf(false);
                break;
            }
        }

        return retorno;
    }
    
    public Usuario checkUsuario(String imovel,Veiculo veiculo){
        return null;
    }
}