package org.west.componentes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import org.west.entidades.Cliente;
import org.west.entidades.ClienteDAO;
import org.west.entidades.Servico;
import org.west.entidades.ServicoDAO;
import org.west.entidades.Telefone;

/**
 * 
 * @author West Guerra Ltda.
 */
public class JRCapaServico extends JRHibernateSource{
    
    private Servico servico;

    public JRCapaServico(Servico servico) {
        super(Arrays.asList(new Object[]{servico}));
        this.servico = servico;
    }

    @Override
    public Object getFieldValue(JRField jrf) throws JRException {
        
        if (jrf.getName().equals("compradores") || jrf.getName().equals("vendedores")){
            
            StringBuilder str = new StringBuilder();
            ServicoDAO.lock(servico);
            
            Set<Cliente> set = new HashSet<Cliente>();
            
            if (jrf.getName().equals("compradores"))
                set = servico.getCompradores();
            
            if (jrf.getName().equals("vendedores"))
                set = servico.getVendedores();            
            
            for(Cliente cliente : set){
                str.append(" - ").append(cliente.getNome());
                str.append("\n");
                
                ClienteDAO.lock(cliente);
                
                for(Telefone telefone : cliente.getTelefones()){
                    str.append("    ").append(telefone);
                    str.append("\n");
                }
                str.append("\n\n");
            }
            
            return str.toString();
        }
        else
            return super.getFieldValue(jrf);
    }
}