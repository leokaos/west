package org.west.componentes;

import org.west.entidades.Anuncio;
import org.west.entidades.AnuncioDAO;
import org.west.entidades.Bairro;
import org.west.entidades.BairroDAO;
import org.west.entidades.LancamentoPadrao;
import org.west.entidades.LancamentoPadraoDAO;
import org.west.entidades.Lazer;
import org.west.entidades.LazerDAO;
import org.west.entidades.Tipo;
import org.west.entidades.TipoDAO;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.west.entidades.Departamento;
import org.west.entidades.DepartamentoDAO;
import org.west.entidades.Usuario;
import org.west.entidades.UsuarioCriteria;
import org.west.entidades.UsuarioDAO;
import org.west.entidades.WestPersistentManager;
import org.west.utilitarios.ModelAnuncio;

public class ListModelFactory {

    public ListModelFactory() {
    }

    public static ListModelFactory getInstance() {
        return new ListModelFactory();
    }

    public ListModel getModelTipo() {
        DefaultListModel model = new DefaultListModel();

        for (Tipo tipo : TipoDAO.listTipoByQuery("tipo is not null", "tipo")) {
            model.addElement(tipo);
        }

        return model;
    }

    public ListModel getModelBairro() {
        DefaultListModel model = new DefaultListModel();
        List<Bairro> lista = Arrays.asList(BairroDAO.listBairroByQuery("nome is not null", "usado desc,nome"));
        for (Iterator<Bairro> it = lista.iterator(); it.hasNext();) {
            Bairro bairro = it.next();
            model.addElement(bairro);
        }
        return model;
    }

    public ListModel getModelStatus() {
        DefaultListModel model = new DefaultListModel();
        model.addElement("Ativo");
        model.addElement("Proposta");
        model.addElement("Suspenso");
        model.addElement("Vendido");
        return model;
    }

    public ListModel getModelLazeres() {
        DefaultListModel model = new DefaultListModel();

        for (Lazer lazer : LazerDAO.listLazerByQuery("nome is not null", "nome")) {
            model.addElement(lazer);
        }

        return model;
    }

    public ListModel getModelDepartamento() {
        DefaultListModel model = new DefaultListModel();

        for (Departamento departamento : DepartamentoDAO.listDepartamentoByQuery("id > 0", "nome")) {
            model.addElement(departamento);
        }

        return model;
    }

    public ComboBoxModel getModelLancamentos() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();

        List<LancamentoPadrao> lista = Arrays.asList(LancamentoPadraoDAO.listLancamentoPadraoByQuery("codigo>0", "descricao"));

        for (Iterator<LancamentoPadrao> it = lista.iterator(); it.hasNext();) {
            LancamentoPadrao lancamentosPadrao = it.next();
            model.addElement(lancamentosPadrao.getDescricao());
        }

        return model;
    }

    public ListModel getModelAnuncio() {
        Anuncio[] anuncios = AnuncioDAO.listAnuncioByQuery("veiculo in ('Anuncio','Internet','Placa')", "usado desc,nome desc");

        ModelAnuncio model = new ModelAnuncio();

        if (anuncios != null) {
            model = new ModelAnuncio(anuncios);
        }

        return model;
    }

    public ComboBoxModel getModelCorretores() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();

        try {
            for (Usuario usuario : UsuarioDAO.listUsuarioByQuery("nivel = 2 and status = 1 and nome not like '%/%'", "nome")) {
                model.addElement(usuario);
            }

            WestPersistentManager.getSession().clear();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return model;
    }

    public ComboBoxModel getModelArea() {
        UsuarioCriteria criteria = new UsuarioCriteria();

        ProjectionList proje = Projections.projectionList();
        proje.add(Projections.groupProperty("grupo"));

        criteria.setProjection(proje);

        return new DefaultComboBoxModel(criteria.list().toArray());
    }
}