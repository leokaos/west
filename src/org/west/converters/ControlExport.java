package org.west.converters;

import java.util.ArrayList;
import java.util.List;
import org.west.entidades.Imovel;
import org.west.exception.ExportException;
import org.west.exception.GeracaoException;

public abstract class ControlExport{
    
    protected List<Imovel> listaImovel;
    private List<ExportListener> listeners = new ArrayList<ExportListener>();
    
    public abstract void gerarExport() throws GeracaoException;
    public abstract void enviarExport() throws ExportException;
    protected abstract List<Imovel> getListImoveis();   
    
    public Integer getQuantidadeImoveis() {
        return listaImovel.size();
    }    
    
    public synchronized void addExportListener(ExportListener exportListener) {
        if(!listeners.contains(exportListener)) {
            listeners.add(exportListener);
        }
    }    
    
    public synchronized void removeExportListener(ExportListener exportListener) {
        listeners.remove(exportListener);
    }   
    
    protected synchronized void fireExportInit(String mensagem,int count){
        ExportEvent event = new ExportEvent(this);
        event.setCount(count);
        event.setMessage(mensagem);
        
        for(ExportListener listener : listeners)
            listener.exportInit(event);
    }
    
    protected synchronized void fireExporMove(String mensagem,int count){
        ExportEvent event = new ExportEvent(this);
        event.setCount(count);
        event.setMessage(mensagem);
        
        for(ExportListener listener : listeners)
            listener.exportMove(event);
    }
    
    protected synchronized void fireExporFinish(String mensagem,int count){
        ExportEvent event = new ExportEvent(this);
        event.setCount(count);
        event.setMessage(mensagem);
        
        for(ExportListener listener : listeners)
            listener.exportFinish(event);
    }    
}