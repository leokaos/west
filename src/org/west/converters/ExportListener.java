package org.west.converters;

import java.util.EventListener;

public interface ExportListener extends EventListener{
    
    public void exportInit(ExportEvent evt);
    public void exportMove(ExportEvent evt);
    public void exportFinish(ExportEvent evt);    
}
