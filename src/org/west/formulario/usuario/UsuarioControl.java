package org.west.formulario.usuario;

import org.hibernate.criterion.Restrictions;
import org.west.componentes.DesktopSession;
import org.west.componentes.LoggerManager;
import org.west.entidades.Ponto;
import org.west.entidades.PontoDAO;
import org.west.entidades.Usuario;
import org.west.entidades.UsuarioCriteria;
import org.west.utilitarios.Util;

public class UsuarioControl {

    public static Usuario login(Usuario usuario) {

        UsuarioCriteria criteria = new UsuarioCriteria();
        criteria.add(Restrictions.eq("nome", usuario.getNome()));
        criteria.add(Restrictions.eq("senha", usuario.getSenha()));

        if (criteria.list().size() == 1) {
            usuario = criteria.uniqueUsuario();

            DesktopSession.getInstance().setObject("usuario", usuario);

            Ponto ponto = new Ponto();
            ponto.setUsuario(usuario);
            ponto.setDataPonto(PontoDAO.getDateServer());
            ponto.setObs("Entrada");

            PontoDAO.save(ponto);

            LoggerManager.gravarEntrada(usuario);
            Util.setPropriedade("west.user", usuario.getNome());
        } else {
            usuario = null;
        }

        return usuario;
    }
}