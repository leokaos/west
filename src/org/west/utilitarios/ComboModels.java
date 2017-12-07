package org.west.utilitarios;

import org.west.componentes.DesktopSession;
import org.west.entidades.Anuncio;
import org.west.entidades.Usuario;
import org.west.entidades.Veiculo;
import org.west.entidades.VeiculoCriteria;
import org.west.entidades.VeiculoDAO;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import org.hibernate.criterion.Order;
import org.swingBean.descriptor.DependentComboModel;
import org.west.entidades.Zona;
import org.west.formulario.corretor.TipoAcompanhado;

public class ComboModels implements DependentComboModel {

    private static ComboModels instance = new ComboModels();

    public ComboBoxModel getVeiculos() {
        VeiculoCriteria criteria = new VeiculoCriteria();
        criteria.addOrder(Order.asc("nome"));
        return new DefaultComboBoxModel(criteria.listVeiculos().toArray());
    }

    public static ComboModels getInstance() {
        return instance;
    }

    @Override
    public ComboBoxModel getComboModel(Object o) {
        DefaultComboBoxModel modelo = new DefaultComboBoxModel();
        Iterator it;
        if (o != null) {
            Veiculo veiculo = VeiculoDAO.load(o.toString());

            if (veiculo != null) {
                List lista = new ArrayList(veiculo.getAnuncios());

                for (it = lista.iterator(); it.hasNext();) {
                    Anuncio anuncio = (Anuncio) it.next();
                    modelo.addElement(anuncio);
                }
            }

        }

        return modelo;
    }

    public ComboBoxModel getStatus() {
        Usuario usuario = (Usuario) DesktopSession.getInstance().getObject("usuario");
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement("Ativo");
        model.addElement("Suspenso");

        if (usuario.getNivel() != 2) {
            model.addElement("Proposta");
        }

        model.addElement("Vendido");
        return model;
    }

    public ComboBoxModel getZona() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement(null);
        model.addElement(Zona.CENTRO);
        model.addElement(Zona.LESTE);
        model.addElement(Zona.NORTE);
        model.addElement(Zona.OESTE);
        model.addElement(Zona.SUL);
        return model;
    }

    public ComboBoxModel getModelEstado() {
        return new DefaultComboBoxModel(new Object[]{"AC", "AL", "AP", "AM", "BA", "CE", "DF", "GO", "ES", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SP", "SC", "SE", "TO"});
    }

    public ComboBoxModel getModelTipoAcompanhado() {
        DefaultComboBoxModel model = new DefaultComboBoxModel(TipoAcompanhado.getTodosValores().toArray());
        return model;
    }
}
