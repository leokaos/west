package org.west.converters;

import org.west.entidades.AnuncioDAO;
import org.west.entidades.Imovel;
import org.west.entidades.Informacao;
import org.west.entidades.InformacaoDAO;

public class SiteConverter implements ImovelConverter {

    public SiteConverter() {
    }

    @Override
    public Object toFormato(Imovel imovel) {

        try {
            StringBuilder builder = new StringBuilder();

            builder.append("INSERT INTO tab_imovel(referencia,bairro,imediacoes,dorms,suites,");
            builder.append("vagas,terreno,construida,tipo,valor,destaque,textoAnuncio) VALUES (");

            builder.append(imovel.getReferencia());
            builder.append(",");
            builder.append("'").append(imovel.getBairro().getNome().replaceAll("'", " ")).append("',");
            builder.append("'").append(imovel.getImediacoes().replaceAll("'", " ")).append("'");
            builder.append(",").append(imovel.getDorms());
            builder.append(",").append(imovel.getSuites());
            builder.append(",").append(imovel.getGaragens());

            if (imovel.getTipo().isColetivo()) {
                builder.append(",0.0");
                builder.append(",").append((imovel.getPrivativa() == null) ? "0.0" : imovel.getPrivativa());
            } else {
                builder.append(",").append((imovel.getPrivativa() == null) ? "0.0" : imovel.getTerreno());
                builder.append(",").append((imovel.getPrivativa() == null) ? "0.0" : imovel.getConstruido());
            }

            builder.append(",'").append(imovel.getTipo().getTipo()).append("'");
            builder.append(",").append(imovel.getValor());
            builder.append(",").append(imovel.getDestaque());

            String texto = "";

            if (imovel.getReversivel() != null && imovel.getReversivel()) {
                texto += imovel.getDorms() + "º DORM. REVERSÍVEL";
            }

            Informacao info = InformacaoDAO.get(imovel, AnuncioDAO.load("Site"));

            if (info != null && info.getTextoAnuncio() != null && !info.getTextoAnuncio().isEmpty() && info.getLiberado()) {
                texto += ", " + info.getTextoAnuncio();
            }

            builder.append(",'").append(texto.replaceAll("'", " ")).append("')");

            return builder.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}