package org.west.converters;

import java.util.EventObject;

public class ExportEvent extends EventObject{
    
    private String message;
    private Integer count;

    public ExportEvent(ControlExport source) {
        super(source);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}