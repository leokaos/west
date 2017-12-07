package org.west.componentes;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 * Utilizando a interface {@link net.sf.jasperreports.engine.JRDataSource}, este datasource recebe uma lista de javabeans
 * que representam as entidades do banco de dados.
 * @author West Guerra Ltda.
 */
public class JRHibernateSource implements JRDataSource{

    /**
     * Iterador que guarda a posição atual da lista recebida como parâmetro.
     */
    private Iterator itera;
    /**
     * Objeto atual da lista de objetos vindos do banco de dados. 
     */
    private Object valorAtual;
    
    /**
     * Contrutor que recebe uma lita de javabeans que representam os valores do banco de dados.
     * @param valores lista de objetos advindos do banco de dados.
     */
    public JRHibernateSource(List valores) {
        this.itera = valores.iterator();
    }    

    /**
     * Retorna se existe um mais objetos a serem processados.
     * @return boolean : true apenas se existir mais objetos a serem processados.
     * @throws JRException 
     */
    @Override
    public boolean next() throws JRException {
        if (itera.hasNext())
            valorAtual = itera.next();
        else
            valorAtual = null;

        return valorAtual != null;
    }

    /**
     * Retorna o objeto referente ao campo do relatório.
     * @param jrf campo do relatório.
     * @return object : objeto referente ao campo e ao item analisado.
     * @throws JRException 
     */
    @Override
    public Object getFieldValue(JRField jrf) throws JRException {
        Object atual = valorAtual;
        String names[] = jrf.getName().split("_");

        try{
            for(String name : names){
                
                if (atual == null){
                    atual = jrf.getValueClass().newInstance();
                    break;
                }
                else{
                    Method met = atual.getClass().getDeclaredMethod(montaGet(name), new Class[0]);
                    if (met != null)
                        atual = met.invoke(atual, new Object[0]);
                    else{
                        atual = jrf.getValueClass().newInstance();
                        break;                        
                    }
                }
            }
        }
        catch(Exception ex){ex.printStackTrace();}
        
        return atual;
    }
    
    /**
     * Mêtodo auxiliar para recuperar o mêtodo get de um dado atributo.
     * @param campo Atributo da classe.
     * @return metodoGet : retorna o get do atributo.
     */
    private String montaGet(String campo){
        return "get" + campo.substring(0, 1).toUpperCase() + campo.substring(1);
    }
}