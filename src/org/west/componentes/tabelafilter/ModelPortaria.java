 package org.west.componentes.tabelafilter;

import org.west.componentes.DesktopSession;
import org.west.componentes.LoggerManager;
import org.west.entidades.Frequencia;
import org.west.utilitarios.ListPredicate;
import org.west.entidades.FrequenciaCriteria;
import org.west.entidades.Portaria;
import org.west.entidades.PortariaDAO;
import org.west.entidades.Usuario;
import org.west.entidades.WestPersistentManager;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.table.AbstractTableModel;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.functors.AllPredicate;
import org.apache.commons.collections.iterators.FilterIterator;
import org.hibernate.criterion.Restrictions;
import org.west.utilitarios.FrequenciaComparator;
import org.west.utilitarios.FrequenciaPredicate;
import org.west.utilitarios.Util;

public class ModelPortaria extends AbstractTableModel{

    private List<Portaria> listagem;
    private List<Portaria> listagemOriginal;
    private List<String> atributos;
    private List<String> labels;
    private Integer maxVisitas;
    private Date dtFinal;
    private Date dtInicio;
    private Map<Integer,List<String>> mapaFiltro;

    public ModelPortaria (List<Portaria> listagem){
        this(listagem, new Date());
    }

    public ModelPortaria(List<Portaria> listagem,Date now) {
        this(listagem, Util.getExtremidadesMes(now, Util.INICIO), Util.getExtremidadesMes(now, Util.FIM));
    }

    public ModelPortaria(List<Portaria> listagem,Date inicio,Date fim){
        this.listagem = listagem;
        this.listagemOriginal = new ArrayList<Portaria>(listagem);
        this.dtFinal = fim;
        this.dtInicio = inicio;
        this.mapaFiltro = new HashMap<Integer, List<String>>();

        atributos = Arrays.asList(new String[]{"codigo","prioridade","cep","numero","edificio","quantidadeImovel",
                "retornoFicha","ultimaVisita","usuario","bairro","zelador","telefone","porteiro"});

        labels = Arrays.asList(new String[]{"Código","","Endereço","Nº","Edifício","Imóveis",
                "Retornos","Última Visita","Corretor","Bairro","Zelador","Telefone","Porteiro"});

        defineMaxVisitas();

        defineAtributosExtras();
    }

    public Integer getMaxVisitas() {
        return maxVisitas;
    }

    @Override
    public int getRowCount() {
        return listagem.size();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        String nomeColuna = getColumnName(columnIndex);
        
        if (nomeColuna.equals("Zelador") || nomeColuna.equals("Telefone") || nomeColuna.equals("Edifício") || nomeColuna.equals("Porteiro"))
            return true;
        else
            return false;
    }

    @Override
    public int getColumnCount() {
        return labels.size() + maxVisitas.intValue();
    }

    @Override
    public String getColumnName(int column) {
        if (column <= 7 )
            return labels.get(column);
        
        if (column > 7 + maxVisitas)
            return labels.get(column - maxVisitas);
            
       return "Visita";            
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        if (rowIndex < 0 || rowIndex >= listagem.size())
            return "";

        Portaria portaria = listagem.get(rowIndex);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        if (columnIndex <= 7 || columnIndex > (7 + maxVisitas)){
            
            if (columnIndex >= 7 + maxVisitas)
                columnIndex = columnIndex - maxVisitas;
          
            String metodo = atributos.get(columnIndex);
            metodo = metodo.substring(0, 1).toUpperCase() + metodo.substring(1);
            metodo = "get" + metodo;

            try{
                Method met = portaria.getClass().getDeclaredMethod(metodo, new Class[0]);
                Object obj = met.invoke(portaria, new Object[0]);

                if (obj != null){
                    if (obj.getClass().equals(Date.class))
                        return format.format(obj);
                }
                else
                    obj = "";

                return obj;
            }
            catch(Exception ex){
                ex.printStackTrace();
                return "";
            }
        }
        else{
            int pos = columnIndex - 8;

            if (pos < portaria.getFrequenciaPeriodo().size()){
                Frequencia frequencia = portaria.getFrequenciaPeriodo().get(pos);
                return frequencia;
            }
            else
                return "";
        }
    }

    public Portaria getRow(int index){
        return listagem.get(index);
    }

    private void defineMaxVisitas() {
        SimpleDateFormat forma = new SimpleDateFormat("yyyy-MM-dd");
        String query = "select count(dataVisita) from tab_frequencia group by portaria having count(datavisita) ="
                +"(select max(cont) from (select count(portaria) as cont from tab_frequencia"
                + " where dataVisita between '" + forma.format(dtInicio) + "' and '" + forma.format(dtFinal) + "' group by portaria) as C)";

        try{
            List lista = WestPersistentManager.getSession().createSQLQuery(query).list();

            if (lista.size() > 0)
                maxVisitas = new Integer(lista.get(0).toString());
            else
                maxVisitas = 0;

            WestPersistentManager.clear();
        }
        catch(Exception ex){ex.printStackTrace();}
    }

    public void resetLista(){
        this.listagem = this.listagemOriginal;
        fireTableDataChanged();
    }

    public void filterModel(Map<Integer,List<String>> mapa){
        this.mapaFiltro = mapa;
        
        Predicate[] predicados = new Predicate[mapaFiltro.size()];
        int x = 0;

        for (Iterator it = mapaFiltro.keySet().iterator(); it.hasNext();) {
            Integer col = (Integer) it.next();
            List lista = mapaFiltro.get(col);
            Predicate predicado;
            
            if (col <= 7 || col > (7 + maxVisitas)){
                if (col > (7 + maxVisitas))
                    col = col - maxVisitas;
                                
                predicado = new ListPredicate(lista, atributos.get(col));
            }
            else{
                int index = col - 8;
                predicado = new FrequenciaPredicate(index, lista);
            }            
            
            predicados[x] = predicado;
            x++;
        }

        FilterIterator itera = new FilterIterator(listagemOriginal.iterator(), new AllPredicate(predicados));

        listagem = new ArrayList<Portaria>();

        while (itera.hasNext()) 
            listagem.add((Portaria) itera.next());

        fireTableDataChanged();
    }

    public void ordenarListagem(Map<Integer,Integer> mapa){
       ComparatorChain chain = new ComparatorChain();

        for(Iterator<Integer> it = mapa.keySet().iterator();it.hasNext();){
            Integer col = it.next();
            Comparator comp;
            
            if (col <= 7)
                comp = new ObjectComparator(atributos.get(col));
            else{
                if (col > 7 + maxVisitas)
                    comp = new ObjectComparator(atributos.get(col - maxVisitas));
                else
                    comp = new FrequenciaComparator(col - 8);
            }
            
            chain.addComparator(comp,mapa.get(col) == 1);
        }
        
        if (chain.size() > 0)
            Collections.sort(listagem,chain);        
    }

    public void filterContains(String busca){
        Iterator<Portaria> it = listagem.iterator();

        List<Portaria> listaTemp = new ArrayList<Portaria>();

        busca = busca.toLowerCase();

        int x = 0;

        while(it.hasNext()){
            Portaria portaria = it.next();
            
            for(int y = 0;y < getColumnCount(); y++){
                Object obj = getValueAt(x, y);
                String str = (obj == null ? "" : obj.toString()).toLowerCase();

                if (str.indexOf(busca) > -1 && !listaTemp.contains(portaria))
                    listaTemp.add(portaria);
            }
            x++;
        }

        listagem = new ArrayList<Portaria>(listaTemp);

        fireTableDataChanged();
    }

    private void defineAtributosExtras() {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

        for(Portaria portaria : listagem){
            
            if (portaria.getQuantidadeImovel() == null || portaria.getQuantidadeImovel().isEmpty()) {
                
                String retorno = "";                

                try {
                    String query = "SELECT COUNT(I.portaria),SUM(case when I.captacao between '" + formato.format(dtInicio) + "' and "
                            + "'" + formato.format(dtFinal) + "' then 1 else 0 end) FROM tab_imovel I "
                            + "where I.portaria = " + portaria.getCodigo() + " GROUP BY I.portaria;";
                    
                    Object[] obj = (Object[]) WestPersistentManager.getSession().createSQLQuery(query).uniqueResult();

                    if (obj != null)
                        retorno = (obj[0] != null?obj[0]:"0") + " / " + (obj[1] != null?obj[1]:"0");
                    else
                        retorno = "0 / 0";

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                portaria.setQuantidadeImovel(retorno);
            }

            if (portaria.getRetornoFicha() == null || portaria.getRetornoFicha().isEmpty()) {
                String retorno = "";
                
                try {
                    String query = "SELECT count(I.imovel),SUM(	case when I.dataEntrada between "
                            + "'" + formato.format(dtInicio) + "' and '" + formato.format(dtFinal) + "' then 1 else 0 end) "
                            + "FROM tab_imobiliaria I WHERE I.imovel = '" + portaria.getCodigo() + "' and veiculo='Portaria';";
                                       
                    Object[] obj = (Object[]) WestPersistentManager.getSession().createSQLQuery(query).uniqueResult();

                    if (obj != null)
                        retorno = (obj[0] != null?obj[0]:"0") + " / " + (obj[1] != null?obj[1]:"0");
                    else
                        retorno = "0 / 0";
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                portaria.setRetornoFicha(retorno);
            }

            if (portaria.getUltimaVisita() == null) {
                try {
                    
                    Object obj = WestPersistentManager.getSession().
                            createSQLQuery("SELECT max(dataVisita) from tab_frequencia where portaria=" + portaria.getCodigo()).uniqueResult();

                    if (obj != null) {
                        portaria.setUltimaVisita(formato.parse(obj.toString()));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            if (portaria.getFrequenciaPeriodo() == null || portaria.getFrequenciaPeriodo().isEmpty()){

                try{
                    FrequenciaCriteria criteria = new FrequenciaCriteria();
                    criteria.add(Restrictions.eq("portaria", portaria));
                    criteria.add(Restrictions.between("dataVisita", dtInicio, dtFinal));

                    portaria.setFrequenciaPeriodo(criteria.list());
                }
                catch(Exception ex){ex.printStackTrace();}
            }
        }
        
        WestPersistentManager.clear();
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Portaria portaria = getRow(rowIndex);
        String nomeColuna = getColumnName(columnIndex);

        String log = "";

        if (nomeColuna.equals("Zelador")) {
            log = "Portaria " + portaria.getCodigo() + ": Zelador " + portaria.getZelador() + " modificado para " + aValue.toString();
            portaria.setZelador(aValue.toString());
        }

        if (nomeColuna.equals("Telefone")) {
            log = "Portaria " + portaria.getCodigo() + ": Telefone " + portaria.getTelefone() + " modificado para " + aValue.toString();
            portaria.setTelefone(aValue.toString());
        }
        
        if (nomeColuna.equals("Edifício")){
            portaria.setEdificio(aValue.toString());
        }
        
        if (nomeColuna.equals("Porteiro")){
            log = "Portaria " + portaria.getCodigo() + ": Porteiro " + portaria.getPorteiro() + " modificado para " + aValue.toString();
            portaria.setPorteiro(aValue.toString());            
        }

        log += " por " + ((Usuario) DesktopSession.getInstance().getObject("usuario")).getNome();

        if (PortariaDAO.save(portaria)) {
            
            PortariaDAO.refresh(portaria);
            
            if (!log.isEmpty())
                LoggerManager.gravaLogPortaria(log);
        }
    }
}