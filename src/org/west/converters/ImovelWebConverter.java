package org.west.converters;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.west.entidades.Foto;
import org.west.entidades.FotoDAO;
import org.west.entidades.Imovel;
import org.west.entidades.Informacao;
import org.west.entidades.Tipo;
import org.west.entidades.TipoDAO;
import org.west.entidades.Zap;
import org.west.entidades.ZapDAO;

/**
 * Classe que converte um imóvel para o formato ZAP. Para transferência para o
 * Zap, é usado um formato XML, portanto, cada imóvel é transformado em um item
 * dentro do XML na tag imovel.
 *
 * @author WestGuerra Ltda.
 */
public class ImovelWebConverter implements ImovelConverter {

    private Document doc;
    private Map<Tipo, String[]> mapaTipo;

    public ImovelWebConverter(Document doc) {
        this.doc = doc;

        createMapaTipo();
    }

    @Override
    public Object toFormato(Imovel imovel) {
        Element imovelXML = createElementoGenerico("Imovel", null);

        imovelXML.appendChild(createElementoGenerico("CodigoCentralVendas", "459306"));
        imovelXML.appendChild(createElementoGenerico("CodigoImovel", imovel.getReferencia().toString()));
        imovelXML.appendChild(createElementoGenerico("Modelo", "Simples"));

        imovelXML = adicionaTipo(imovel, imovelXML);

        Informacao informacao = getInformacaoImovelWeb(imovel);

        if (informacao != null) {

            if (informacao.getDestaque() == 1) {
                imovelXML.appendChild(createElementoGenerico("EmDestaque1", "1"));
            }
            if (informacao.isIptu() && imovel.getIptu() != null) {
                imovelXML.appendChild(createElementoGenerico("PrecoIptuImovel", String.valueOf(imovel.getIptu().intValue())));
            }

            if (informacao.getVideo() != null && !informacao.getVideo().isEmpty()) {

                Element videos = createElementoGenerico("Videos", null);
                imovelXML.appendChild(videos);

                Element videoXML = createElementoGenerico("Video", null);

                videoXML.appendChild(createElementoGenerico("Descricao", ""));
                videoXML.appendChild(createElementoGenerico("URLArquivo", informacao.getVideo()));
                videoXML.appendChild(createElementoGenerico("Principal", "1"));

                videos.appendChild(videoXML);
            }

            if (informacao.isCondominio() && imovel.getCondominio() != null) {
                imovelXML.appendChild(createElementoGenerico("PrecoCondominio", String.valueOf(imovel.getCondominio().intValue())));
            }

            if (informacao.getItens() != null) {
                for (String str : informacao.getItens().split(";")) {
                    Zap zap = ZapDAO.get(str);

                    if (zap != null) {
                        imovelXML.appendChild(createElementoGenerico(zap.getTag(), "1"));
                    }
                }
            }
        }

        imovelXML.appendChild(createElementoGenerico("UF", imovel.getBairro().getUf()));
        imovelXML.appendChild(createElementoGenerico("Cidade", imovel.getCidade()));
        imovelXML.appendChild(createElementoGenerico("Bairro", imovel.getBairro().getNome()));
        imovelXML.appendChild(createElementoGenerico("Endereco", imovel.getImediacoes()));
        imovelXML.appendChild(createElementoGenerico("DivulgarEndereco", "true"));

        imovelXML.appendChild(createElementoGenerico("PrecoVenda", NumberFormat.getIntegerInstance().format(imovel.getValor())));

        imovelXML.appendChild(createElementoGenerico("QtdDormitorios", String.valueOf(imovel.getDorms())));
        imovelXML.appendChild(createElementoGenerico("QtdSuites", String.valueOf(imovel.getSuites())));
        imovelXML.appendChild(createElementoGenerico("QtdBanheiros", String.valueOf(imovel.getBanheiros())));
        imovelXML.appendChild(createElementoGenerico("QtdSalas", String.valueOf(imovel.getSalas())));
        imovelXML.appendChild(createElementoGenerico("QtdVagas", String.valueOf(imovel.getGaragens())));

        createAreaNode(imovel, imovelXML);

        if (!imovel.getFotos().isEmpty()) {
            inserirFotos(imovel, imovelXML);
        }

        return imovelXML;
    }

    private Element adicionaTipo(Imovel imovel, Element imovelXML) {
        String[] valores = mapaTipo.get(imovel.getTipo());

        imovelXML.appendChild(createElementoGenerico("TipoImovel", valores[0]));
        imovelXML.appendChild(createElementoGenerico("SubTipoImovel", valores[1]));

        if (!valores[2].isEmpty()) {
            imovelXML.appendChild(createElementoGenerico("CategoriaImovel", valores[2]));
        }

        return imovelXML;
    }

    private Informacao getInformacaoImovelWeb(Imovel imovel) {

        for (Informacao informacao : imovel.getInformacoes()) {
            if (informacao.getAnuncio().getNome().equals("Imóvel Web")) {
                return informacao;
            }
        }

        return null;
    }

    private Element createElementoGenerico(String name, String value) {

        Element elementoGenerico = doc.createElement(name);

        if (value != null) {
            Text valorElementoGenerico = doc.createTextNode(value);
            elementoGenerico.appendChild(valorElementoGenerico);
        }

        return elementoGenerico;
    }

    private void createMapaTipo() {
        mapaTipo = new HashMap<Tipo, String[]>();

        mapaTipo.put(TipoDAO.load("Apartamento"), new String[]{"Apartamento", "Padrão", "Padrão"});
        mapaTipo.put(TipoDAO.load("Apartamento Duplex"), new String[]{"Apartamento", "Apartamento", "Duplex"});
        mapaTipo.put(TipoDAO.load("Casa"), new String[]{"Casa", "Padrão", "Térrea"});
        mapaTipo.put(TipoDAO.load("Casa Comercial"), new String[]{"Comercial", "Casa Comercial", ""});
        mapaTipo.put(TipoDAO.load("Casa Cond. Fechado"), new String[]{"Casa", "Casa de Condomínio", "Sobrado"});
        mapaTipo.put(TipoDAO.load("Cobertura"), new String[]{"Apartamento", "Apartamento", "Cobertura"});
        mapaTipo.put(TipoDAO.load("Conjunto Comercial"), new String[]{"Comercial", "Conjunto Comercial/Sala", ""});
        mapaTipo.put(TipoDAO.load("Flat"), new String[]{"FApartamento", "Flat", "Padrão"});
        mapaTipo.put(TipoDAO.load("Galpão"), new String[]{"Comercial", "Galpão/Depósito/Armazém", ""});
        mapaTipo.put(TipoDAO.load("Loja"), new String[]{"Comercial", "Loja/Salão", ""});
        mapaTipo.put(TipoDAO.load("Prédio"), new String[]{"Comercial", "Prédio Inteiro", ""});
        mapaTipo.put(TipoDAO.load("Prédio Comercial"), new String[]{"Comercial", "Prédio Inteiro", ""});
        mapaTipo.put(TipoDAO.load("Sala Comercial"), new String[]{"Comercial", "Conjunto Comercial/Sala", ""});
        mapaTipo.put(TipoDAO.load("Sítio"), new String[]{"Rural", "Sítio", ""});
        mapaTipo.put(TipoDAO.load("Sobrado"), new String[]{"Casa", "Padrão", "Sobrado"});
        mapaTipo.put(TipoDAO.load("Sobrado Novo"), new String[]{"Casa", "Padrão", "Sobrado"});
        mapaTipo.put(TipoDAO.load("Terrea Nova"), new String[]{"Casa", "Padrão", "Térrea"});
        mapaTipo.put(TipoDAO.load("Terreno"), new String[]{"Terreno", "Terreno Padrão", ""});
        mapaTipo.put(TipoDAO.load("Casa de Vila"), new String[]{"Casa", "Casa de Vila", "Sobrado"});
    }

    private void inserirFotos(Imovel imovel, Element imovelXML) {
        Element fotos = createElementoGenerico("Fotos", null);
        imovelXML.appendChild(fotos);

        try {
            for (Foto foto : FotoDAO.listFotoByQuery("site = 1 and imovel=" + imovel.getReferencia(), "codigo")) {
                Element fotoXML = createElementoGenerico("Foto", null);
                fotoXML.appendChild(createElementoGenerico("NomeArquivo", foto.getCaminho()));
                fotoXML.appendChild(createElementoGenerico("URLArquivo", "http://westguerra.com.br/fotos/" + foto.getCaminho()));
                fotoXML.appendChild(createElementoGenerico("Principal", foto.isPrincipal() ? "1" : "0"));

                fotos.appendChild(fotoXML);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void createAreaNode(Imovel imovel, Element imovelXML) {
        Integer area = 0;

        if (imovel.getTipo().isColetivo()) {
            area = imovel.getPrivativa().intValue();
        } else {
            area = imovel.getConstruido().intValue();
        }

        if (area == null) {
            area = 0;
        }

        String nomeTipo = imovel.getTipo().getTipo();

        if (nomeTipo.equals("Terreno") || nomeTipo.equals("Sítio")) {
            imovelXML.appendChild(createElementoGenerico("AreaTotal", NumberFormat.getIntegerInstance().format(area)));
        } else {
            imovelXML.appendChild(createElementoGenerico("AreaUtil", NumberFormat.getIntegerInstance().format(area)));
        }
    }
}
