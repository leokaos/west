package org.west.model.table;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import org.hibernate.SQLQuery;
import org.west.entidades.Anuncio;
import org.west.entidades.WestPersistentManager;

/**
 * Esta classe implementa a extende {@link DefaultTableModel}, para o sumário da tela 
 * de anúncios da recepção.
 * @author West Guerra
 */
public class ModelSumario extends AbstractTableModel{
    
    private List listaDados;
    private Anuncio anuncio;
    private final String[] colunas = new String[]{"Corretor","Normal","Em destaque"};
    
    public ModelSumario(Anuncio anuncio) {
        this.anuncio = anuncio;
        this.listaDados = createListDados();
    }    

    @Override
    public int getRowCount() {
        return listaDados.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object[] dados = (Object[]) listaDados.get(rowIndex);
        return dados[columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return colunas[column];
    }

    private List createListDados(){
        WestPersistentManager.getSession().close();
        
        SQLQuery query = WestPersistentManager.getSession().createSQLQuery("SELECT  U.nome,"
                + "  sum(case when I.destaque = 1 then 1 else 0 end),"
                + "sum(case when I.destaque = 2 then 1 else 0 end)"
                + " FROM tab_informacoes I inner join tab_usuario U on U.codigo = I.usuario "
                + "inner join tab_imovel_anuncio A on A.anuncio = I.anuncio and A.imovel = I.referencia "
                + "where I.anuncio = '" + anuncio.getNome() + "' and I.liberado = 1 group by I.usuario");
        
        return query.list();
    }
    
    public boolean hasSumario(){
        return ! listaDados.isEmpty();
    }
}