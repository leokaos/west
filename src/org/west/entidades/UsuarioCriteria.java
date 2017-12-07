package org.west.entidades;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.west.estrutura.AbstractCriteria;

/**
 *Classe referente a especialização de Criteria referente à classe {@link Usuario}
 * @author WestGuerra Ltda.
 */
public class UsuarioCriteria extends AbstractCriteria{

    public UsuarioCriteria(){
        this(WestPersistentManager.getSession().createCriteria(Usuario.class));
    }
    
    public UsuarioCriteria(Criteria criteria) {
        super(criteria);
    }
    
    public ImobiliariaCriteria createImobiliariaCriteria(){
        return new ImobiliariaCriteria(createCriteria("imobiliaria"));
    }
    
    public Usuario uniqueUsuario(){
        return (Usuario) uniqueResult();
    }
    
    public List<Usuario> listUsuarios(){
        List<Usuario> listaUsuario = new ArrayList<Usuario>();
        
        for(Object object: super.list())
            listaUsuario.add((Usuario) object);
        
        return listaUsuario;
    }
}