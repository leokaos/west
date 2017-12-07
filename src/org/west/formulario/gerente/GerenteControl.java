package org.west.formulario.gerente;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;
import org.hibernate.type.Type;
import org.west.entidades.Anuncio;
import org.west.entidades.AnuncioDAO;
import org.west.entidades.Historico;
import org.west.entidades.HistoricoCriteria;
import org.west.entidades.Imobiliaria;
import org.west.entidades.ImobiliariaCriteria;
import org.west.entidades.UsuarioCriteria;
import org.west.entidades.Veiculo;
import org.west.entidades.VeiculoDAO;
import org.west.utilitarios.Util;

public class GerenteControl {
    
    private List<String> listaAnuncios = new ArrayList<String>();
    private List<String> listaVeiculos = new ArrayList<String>(); 
    
    public GerenteControl() {
        for(Anuncio anuncio : AnuncioDAO.listAnuncioByQuery("nome is not null","nome"))
            listaAnuncios.add(anuncio.getNome());
        
        for(Veiculo veiculo : VeiculoDAO.listVeiculoByQuery("nome is not null","nome"))
            listaVeiculos.add(veiculo.getNome());   
    }   
    
    public List<String> getListaAnuncios() {
        return listaAnuncios;
    }

    public List<String> getListaVeiculos() {
        return listaVeiculos;
    }    
    
    public List getSumario(){
        return getSumario(new Date(),new Date());
    }    
    
    public List getSumario(Date ini, Date fim){
        List lista = new ArrayList();
        
        ini = Util.corrigirDate(ini, Util.INICIO);
        fim = Util.corrigirDate(fim, Util.FIM);

        try {
            ImobiliariaCriteria criteria = new ImobiliariaCriteria();

            criteria.add(Restrictions.between("dataEntrada", ini, fim));
            criteria.addOrder(Order.asc("status"));

            ProjectionList proje = Projections.projectionList();
            proje.add(Projections.property("status"));
            proje.add(Projections.rowCount());
            proje.add(Projections.groupProperty("status"));

            criteria.setProjection(proje);
            lista = criteria.list();

        } catch (Exception ex) {ex.printStackTrace();}
        
        return lista;
    }    
    
    public Double getTempoMedio(Date ini,Date fim){
        Double tempo = new Double(0.0);

        try{
            ImobiliariaCriteria criteria = new ImobiliariaCriteria();
            criteria.add(Restrictions.between("dataEntrada", ini, fim));
            criteria.add(Restrictions.eq("status", Imobiliaria.ATENDIDO));
            criteria.createUsuarioCriteria().add(Restrictions.not(Restrictions.eq("nivel", new Integer(1))));

            List lista = criteria.list();

            if (lista.isEmpty()) {
                return new Double(-1.0D);
            }
            for (Iterator it = lista.iterator(); it.hasNext();) {
                Imobiliaria imo = (Imobiliaria) it.next();

                HistoricoCriteria criteriaHisto = new HistoricoCriteria();
                criteriaHisto.add(Restrictions.eq("imobiliaria", imo));
                criteriaHisto.createUsuarioCriteria().add(Restrictions.eq("nivel", new Integer(2)));
                criteriaHisto.addOrder(Order.asc("dataEntrada"));
                
                List listaHistorico = criteriaHisto.list();
                
                if (!listaHistorico.isEmpty()){                
                    Historico historico = (Historico) listaHistorico.get(0);

                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(historico.getDataEntrada());
                    long difer = calendar.getTimeInMillis();
                    calendar.setTime(imo.getDataEntrada());
                    difer = difer - calendar.getTimeInMillis();

                    tempo = Double.valueOf(tempo.doubleValue() + difer);
                }
            }

            tempo = Double.valueOf(tempo.doubleValue() / lista.size());
        }
        catch(Exception ex){ex.printStackTrace();}

        return tempo;
    }

    public List<Imobiliaria> getClientes(){
        List<Imobiliaria> lista = new ArrayList<Imobiliaria>();

        try{
            Calendar calendario = Calendar.getInstance();
            calendario.setTime(new Date());
            calendario.add(Calendar.DAY_OF_YEAR, -2);
            Date doisDias = calendario.getTime();
            
            ImobiliariaCriteria criteria = new ImobiliariaCriteria();
            criteria.add(Restrictions.lt("status",Imobiliaria.ATENDIDO));
            criteria.add(Restrictions.lt("dataEntrada", doisDias));
            criteria.addOrder(Order.asc("status"));
            criteria.addOrder(Order.asc("dataEntrada"));
            criteria.addOrder(Order.desc("prioridade"));

            lista = criteria.list();
        }
        catch(Exception ex){ex.printStackTrace();}
        
        return lista;
    }
    
    public List<Object> getListaCorretores(Date ini, Date fim) {
        List retorno = new ArrayList();
        try {
            
            UsuarioCriteria criteria = new UsuarioCriteria();
            criteria.createImobiliariaCriteria().add(Restrictions.between("dataEntrada", ini, fim));

            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

            retorno = criteria.list();
            
        } catch (Exception ex) {ex.printStackTrace();}
        
        return retorno;
    }
    
    public List getVeiculosPorCorretor(Date ini, Date fim, String corretor) {
        List lista = new ArrayList();

        lista.add("Todos");
        try {
            ImobiliariaCriteria criteria = new ImobiliariaCriteria();

            criteria.add(Restrictions.between("dataEntrada", 
                                        Util.corrigirDate(ini, Util.INICIO),
                                        Util.corrigirDate(fim, Util.FIM)));

            if (!corretor.equals("Todos"))
                criteria.createUsuarioCriteria().add(Restrictions.eq("nome", corretor));

            ProjectionList project = Projections.projectionList();
            project.add(Projections.distinct(Projections.property("veiculo")));

            criteria.setProjection(project);
            criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            criteria.addOrder(Order.asc("veiculo"));

            lista.addAll(criteria.list());
        }
        catch (Exception ex) {ex.printStackTrace();}
        
        return lista;
    }    
        
    public List getContagemVeiculos(Date ini, Date fim, String imovel, String corretor, String veiculo,Boolean usuario) {
        ini = Util.corrigirDate(ini, Util.INICIO);
        fim = Util.corrigirDate(fim, Util.FIM);
        
        List retorno = new ArrayList();
        
        try {
            ImobiliariaCriteria criteria = new ImobiliariaCriteria();
            criteria.add(Restrictions.between("dataEntrada",ini,fim ));
            
            if (!imovel.isEmpty()) 
                criteria.add(Restrictions.eq("imovel", imovel));
            
            if (!corretor.isEmpty() && !corretor.equals("Todos") && !corretor.equals("null"))
                criteria.createUsuarioCriteria().add(Restrictions.ilike("nome", corretor,MatchMode.EXACT));            

            ProjectionList proje = Projections.projectionList();
            
            if (usuario){
                proje.add(Projections.groupProperty("veiculo"));
                proje.add(Projections.count("veiculo"));
                proje.add(Projections.groupProperty("usuario"));           
            }
            else{
                if (!veiculo.isEmpty() && !veiculo.equals("null")){

                    if (!veiculo.equals("Todos"))
                        criteria.createVeiculoCriteria().add(Restrictions.eq("nome", veiculo));

                    proje = Projections.projectionList();

                    proje.add(Projections.property("imovel"));
                    proje.add(Projections.rowCount(), "count");
                    proje.add(Projections.groupProperty("imovel"));

                    criteria.setProjection(proje);
                }    
                else{
                    proje.add(Projections.property("veiculo"));
                    proje.add(Projections.rowCount(), "count");
                    proje.add(Projections.groupProperty("veiculo")); 
                }  
            }
            
            criteria.setProjection(proje);
            
            retorno = criteria.list();
        } catch (Exception ex) {ex.printStackTrace();}
        
        return retorno;
    }

    public List getContagemImovel(Date ini, Date fim, Integer qt) {
        List listagem = new ArrayList();
        ini = Util.corrigirDate(ini, Util.INICIO);
        fim = Util.corrigirDate(fim, Util.FIM);
        
        try{
            ImobiliariaCriteria criteria = new ImobiliariaCriteria();
            criteria.add(Restrictions.between("dataEntrada", ini, fim));

            ProjectionList proje = Projections.projectionList();
            proje.add(Projections.property("imovel"));
            proje.add(Projections.rowCount(),"count");
            proje.add(Projections.groupProperty("imovel"));

            criteria.setProjection(proje);
            criteria.setMaxResults(qt);
            criteria.addOrder(Order.desc("count"));

            listagem = criteria.list();
        }
        catch(Exception ex){ex.printStackTrace();}

        return listagem;        
    }

    public List getContagemAtendimentosPorMes(Date ini, Date fim,String selecionados,Object corretor) {
        ini = Util.corrigirDate(ini, Util.INICIO);
        fim = Util.corrigirDate(fim, Util.FIM);
        List lista = new ArrayList();
        
        try{
            //todos
	    ImobiliariaCriteria criteria = new ImobiliariaCriteria();
            criteria.add(Restrictions.between("dataEntrada", ini, fim));
            criteria.addOrder(Order.asc("ano")).addOrder(Order.asc("mes"));
            
            if (corretor != null && !corretor.toString().equals("Todos"))
                criteria.createUsuarioCriteria().add(Restrictions.ilike("nome", corretor.toString(), MatchMode.ANYWHERE));
            
            ProjectionList proje = Projections.projectionList();
            
            proje.add(Projections.alias(Projections.sqlGroupProjection(
                                        "month({alias}.dataEntrada) as mes",
                                        "month({alias}.dataEntrada)", 
                                        new String[]{"mes"}, 
                                        new Type[] {new IntegerType()}),"mes"));
            
            proje.add(Projections.alias(Projections.sqlGroupProjection(
                                        "year({alias}.dataEntrada) as ano",
                                        "year({alias}.dataEntrada)", 
                                        new String[]{"ano"}, 
                                        new Type[] {new IntegerType()}),"ano"));              
            
            proje.add(Projections.count("codigo"));            
            criteria.setProjection(proje);            
            lista.addAll(criteria.list());
            
            //Veiculo
            if (selecionados.isEmpty() || !getVeiculosSelecionados(selecionados).isEmpty()){
                
                criteria = new ImobiliariaCriteria();
                criteria.add(Restrictions.between("dataEntrada", ini, fim));

                if (corretor != null && !corretor.toString().equals("Todos"))
                    criteria.createUsuarioCriteria().add(Restrictions.ilike("nome", corretor.toString(), MatchMode.ANYWHERE));         

                proje = Projections.projectionList();

                proje.add(Projections.alias(Projections.sqlGroupProjection(
                                            "month({alias}.dataEntrada) as mes",
                                            "month({alias}.dataEntrada)", 
                                            new String[]{"mes"}, 
                                            new Type[] {new IntegerType()}),"mes"));
                
                proje.add(Projections.alias(Projections.sqlGroupProjection(
                                        "year({alias}.dataEntrada) as ano",
                                        "year({alias}.dataEntrada)", 
                                        new String[]{"ano"}, 
                                        new Type[] {new IntegerType()}),"ano"));                  

                proje.add(Projections.count("veiculo"));
                proje.add(Projections.groupProperty("veiculo"));  
                
                if (!getVeiculosSelecionados(selecionados).isEmpty())                
                    criteria.createVeiculoCriteria().add(Restrictions.in("nome", getVeiculosSelecionados(selecionados)));
                
                criteria.setProjection(proje);            
                lista.addAll(criteria.list());
            }
            
            //Anuncio
            if (selecionados.isEmpty() || !getAnunciosSelecionados(selecionados).isEmpty()){
                
                criteria = new ImobiliariaCriteria();
                criteria.add(Restrictions.between("dataEntrada", ini, fim));
                
                if (corretor != null && !corretor.toString().equals("Todos"))
                    criteria.createUsuarioCriteria().add(Restrictions.ilike("nome", corretor.toString(), MatchMode.ANYWHERE));                 

                proje = Projections.projectionList();

                proje.add(Projections.alias(Projections.sqlGroupProjection(
                                            "month({alias}.dataEntrada) as mes",
                                            "month({alias}.dataEntrada)", 
                                            new String[]{"mes"}, 
                                            new Type[] {new IntegerType()}),"mes"));
                
                proje.add(Projections.alias(Projections.sqlGroupProjection(
                                        "year({alias}.dataEntrada) as ano",
                                        "year({alias}.dataEntrada)", 
                                        new String[]{"ano"}, 
                                        new Type[] {new IntegerType()}),"ano"));                 

                proje.add(Projections.count("anuncio"));
                proje.add(Projections.groupProperty("anuncio"));  
                
                if (!getAnunciosSelecionados(selecionados).isEmpty())                
                    criteria.createAnuncioCriteria().add(Restrictions.in("nome", getAnunciosSelecionados(selecionados)));
                
                criteria.setProjection(proje);            
                lista.addAll(criteria.list());
            }            
        }
        catch(Exception ex){ex.printStackTrace();}
        
        return lista;
    }
    
    private List<String> getAnunciosSelecionados(String str){
        List<String> lista = new ArrayList<String>();
        
        for(String string : str.split(";"))
            if (listaAnuncios.contains(string))
                lista.add(string);
                
        return lista;
    }
    
    private List<String> getVeiculosSelecionados(String str){
        List<String> lista = new ArrayList<String>();
        
        for(String string : str.split(";"))
            if (listaVeiculos.contains(string))
                lista.add(string);
        
        return lista;
    }    
}