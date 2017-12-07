package org.west.formulario.corretor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.table.AbstractTableModel;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.west.componentes.DesktopSession;
import org.west.entidades.Anuncio;
import org.west.entidades.AnuncioCriteria;
import org.west.entidades.Imovel;
import org.west.entidades.Informacao;
import org.west.entidades.InformacaoCriteria;
import org.west.entidades.InformacaoDAO;
import org.west.entidades.Numero;
import org.west.entidades.Usuario;
import org.west.entidades.VeiculoDAO;
import org.west.utilitarios.ValidadorUtil;

public class TableModelAnuncios extends AbstractTableModel {

    private List<Imovel> imoveis;
    private List<Anuncio> anuncios;
    private String[] colunas = {"Ref.", "Endereço"};
    private Set<Imovel> imoveisAlterados = new HashSet<Imovel>();
    private Usuario usuario;

    public TableModelAnuncios(List<Imovel> imoveis) {
        this.imoveis = imoveis;

        AnuncioCriteria criteria = new AnuncioCriteria();
        criteria.add(Restrictions.eq("veiculo", VeiculoDAO.load("Anuncio")));
        criteria.add(Restrictions.eq("travado", Boolean.FALSE));

        anuncios = criteria.list();

        usuario = (Usuario) DesktopSession.getInstance().getObject("usuario");

        for (Anuncio anuncio : anuncios) {
            InformacaoCriteria info = new InformacaoCriteria();
            info.add(Restrictions.eq("anuncio", anuncio));
            info.add(Restrictions.eq("usuario", usuario));
            info.add(Restrictions.eq("liberado", true));
            info.createImovelCriteria().createAnuncioCriteria().add(Restrictions.eq("nome", anuncio.getNome()));
            info.setProjection(Projections.rowCount());

            anuncio.setQuantidade(new Integer(info.uniqueResult().toString()));
        }

        for (Imovel imovel : this.imoveis) {
            for (Anuncio anuncio : anuncios) {
                if (imovel.getAnuncios().contains(anuncio)) {
                    Informacao info = InformacaoDAO.get(imovel, anuncio);
                    if (info != null && info.getUsuario() != null && info.getUsuario().equals(usuario)) {
                        imoveisAlterados.add(imovel);
                    }
                }
            }
        }
    }

    @Override
    public int getRowCount() {
        return imoveis.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length + anuncios.size();
    }

    public Imovel getImovelAt(int x) {
        return imoveis.get(x);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex < colunas.length) {
            if (columnIndex == 0) {
                return Long.class;
            } else {
                return String.class;
            }
        } else {
            return Boolean.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex < colunas.length) {
            return false;
        } else {
            Informacao info = InformacaoDAO.get(imoveis.get(rowIndex), anuncios.get(columnIndex - colunas.length));

            if (info == null) {
                return true;
            }

            return info.getUsuario() == null || info.getUsuario().equals(usuario);
        }
    }

    @Override
    public String getColumnName(int column) {
        if (column < colunas.length) {
            return colunas[column];
        } else {
            Anuncio anuncio = anuncios.get(column - colunas.length);
            return anuncio.getNome() + " (" + anuncio.getQuantidade() + ")";
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Imovel imovel = imoveis.get(rowIndex);

        int index = columnIndex - colunas.length;
        Anuncio anuncio = anuncios.get(index);
        Boolean valor = (Boolean) aValue;

        if (valor) {
            anuncio.setQuantidade(anuncio.getQuantidade() + 1);
            imovel.getAnuncios().add(anuncio);
        } else {
            anuncio.setQuantidade(anuncio.getQuantidade() - 1);
            imovel.getAnuncios().remove(anuncio);
        }

        getImoveisAlterados().add(imovel);

        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Imovel imovel = imoveis.get(rowIndex);

        if (columnIndex < colunas.length) {

            switch (columnIndex) {

                case 0: {
                    return imovel.getReferencia();
                }

                case 1: {
                    String endereco = imovel.getCep().toString() + ", nº " + createLabelNumero(imovel.getNumeros());

                    if (imovel.getBloco() != null && !imovel.getBloco().isEmpty() && !imovel.getBloco().equals("0")) {
                        endereco += ", bloco " + imovel.getBloco();
                    }

                    if (imovel.getApto() != null && !imovel.getApto().isEmpty() && !imovel.getApto().equals("0")) {
                        endereco += ", apto " + imovel.getApto();
                    }

                    return endereco;
                }
            }
        } else {
            Anuncio anuncio = anuncios.get(columnIndex - colunas.length);
            Boolean retorno = imovel.getAnuncios().contains(anuncio);

            return retorno;
        }

        return "";
    }

    public Set<Imovel> getImoveisAlterados() {
        return imoveisAlterados;
    }

    public void clear() {
        imoveis.clear();
        fireTableDataChanged();
    }

    private String createLabelNumero(List<Numero> numeros) {
        StringBuilder numerosStr = new StringBuilder();

        if (ValidadorUtil.isNotEmpty(numeros)) {

            for (Numero numero : numeros) {
                numerosStr.append(numero.getNumero());
                numerosStr.append(", ");
            }

            String retorno = numerosStr.toString();

            return retorno.substring(0, retorno.length() - 2);
        }

        return numerosStr.toString();
    }
}