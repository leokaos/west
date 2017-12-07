package org.west.componentes.email;

public class EmailUtil {

    private static final String BR = "<br>";

    public static StringBuilder adicionarQuebraDeLinha(StringBuilder builder, Integer linhas) {
        for (int i = 0; i < linhas; i++) {
            builder.append(BR);
        }
        
        return builder;
    }
    
    public static StringBuilder adicionarQuebraDeLinha(StringBuilder builder){
        return adicionarQuebraDeLinha(builder, 1);
    }
}
