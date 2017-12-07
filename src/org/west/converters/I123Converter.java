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
import org.west.entidades.InformacaoDAO;
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
public class I123Converter implements ImovelConverter {

    /**
     * Contém o mapeamento sistema Imob para o sistema ZAP.
     */
    private Map<Tipo, String[]> mapaTipo;
    /**
     * Documento raiz para a adição do imóvel atual.
     */
    private Document doc;

    /**
     * Construtor que recebe o {@link Document}.
     *
     * @param doc Documento criado pela classe {@link ZapControl} para conversão
     * dos imóveis.
     */
    public I123Converter(Document doc) {
        this.doc = doc;
        createMapaTipo();
    }

    @Override
    public Object toFormato(Imovel imovel) {

        Element imovelXML = createElementoGenerico("Imovel", null);

        imovelXML.appendChild(createElementoGenerico("CodigoCliente", "459306"));
        imovelXML.appendChild(createElementoGenerico("CodigoImovel", String.valueOf(imovel.getReferencia())));

        imovelXML = adicionaTipo(imovel, imovelXML);

        imovelXML.appendChild(createElementoGenerico("Cidade", imovel.getBairro().getCidade()));
        imovelXML.appendChild(createElementoGenerico("Bairro", getNomeBairro(imovel.getBairro().getNome())));
        imovelXML.appendChild(createElementoGenerico("Endereco", imovel.getImediacoes()));

        //ver numero e cep

        imovelXML.appendChild(createElementoGenerico("PrecoVenda", NumberFormat.getIntegerInstance().format(imovel.getValor())));

        imovelXML.appendChild(createElementoGenerico("QtdDormitorios", String.valueOf(imovel.getDorms())));
        imovelXML.appendChild(createElementoGenerico("QtdSuites", String.valueOf(imovel.getSuites())));
        imovelXML.appendChild(createElementoGenerico("QtdBanheiros", String.valueOf(imovel.getBanheiros())));
        imovelXML.appendChild(createElementoGenerico("QtdSalas", String.valueOf(imovel.getSalas())));
        imovelXML.appendChild(createElementoGenerico("QtdVagas", String.valueOf(imovel.getGaragens())));

        createAreaNode(imovel, imovelXML);

        String texto = "";

        if (imovel.getReversivel() != null && imovel.getReversivel()) {
            texto = imovel.getDorms() + "º Dorm. Reversível ";
        }

        Informacao info = InformacaoDAO.loadInformacaoByQuery("imovel=" + imovel.getReferencia() + " and anuncio='123i'", "anuncio");

        if (info != null) {

            if (info.getTextoAnuncio() != null) {
                texto += info.getTextoAnuncio();
            }

            imovelXML.appendChild(createElementoGenerico("Observacao", texto));

            imovelXML.appendChild(createElementoGenerico("Detalhes", null));

            if (info.getItens() != null) {
                for (String str : info.getItens().split(";")) {
                    Zap zap = ZapDAO.get(str);

                    if (zap != null) {
                        imovelXML.appendChild(createElementoGenerico(zap.getTag(), "1"));
                    }
                }
            }

            imovelXML.appendChild(createElementoGenerico("TipoOferta", info.getDestaque().toString()));
        }

        if (!imovel.getFotos().isEmpty()) {
            inserirFotos(imovel, imovelXML);
        }

        return imovelXML;
    }

    /**
     * Gera um {@link Element} com o valor e nome especificado.
     *
     * @param name Nome do elemento.
     * @param value Valor do elemento
     * @return elementoGenerico : Elemento criado e adicionado no documento
     * atual.
     */
    private Element createElementoGenerico(String name, String value) {

        Element elementoGenerico = doc.createElement(name);

        if (value != null) {
            Text valorElementoGenerico = doc.createTextNode(value);
            elementoGenerico.appendChild(valorElementoGenerico);
        }

        return elementoGenerico;
    }

    /**
     * Faz o mapeamento de tipo IMOB-ZAP e insere no documento.
     *
     * @param imovel {@link Imovel} atual.
     * @param imovelXML {@link Element} que representa o imovel atual.
     * @return imovelXML : retorno o elemento já preenchido com o Tipo.
     */
    private Element adicionaTipo(Imovel imovel, Element imovelXML) {
        String[] valores = mapaTipo.get(imovel.getTipo());

        imovelXML.appendChild(createElementoGenerico("TipoImovel", valores[0]));
        imovelXML.appendChild(createElementoGenerico("SubTipoImovel", valores[1]));
        imovelXML.appendChild(createElementoGenerico("CategoriaImovel", valores[2]));

        return imovelXML;
    }

    /**
     * Inicializa o {@link #mapaTipo}, criando as equivalencias de tipo IMOB-ZAP
     */
    private void createMapaTipo() {
        mapaTipo = new HashMap<Tipo, String[]>();

        mapaTipo.put(TipoDAO.load("Apartamento"), new String[]{"apartamento", "", ""});
        mapaTipo.put(TipoDAO.load("Apartamento Duplex"), new String[]{"apartamento duplex", "apartamento padrao", "duplex"});
        mapaTipo.put(TipoDAO.load("Casa"), new String[]{"casa", "", ""});
        mapaTipo.put(TipoDAO.load("Casa Comercial"), new String[]{"escritório comercial", "casa comercial", ""});
        mapaTipo.put(TipoDAO.load("Casa Cond. Fechado"), new String[]{"casa em condominio", "casa de condominio", ""});
        mapaTipo.put(TipoDAO.load("Cobertura"), new String[]{"apartamento", "apartamento padrao", "cobertura"});
        mapaTipo.put(TipoDAO.load("Conjunto Comercial"), new String[]{"escritório conjunto comercial", "conjunto comercial/sala", ""});
        mapaTipo.put(TipoDAO.load("Flat"), new String[]{"flat", "flat padrao", ""});
        mapaTipo.put(TipoDAO.load("Galpão"), new String[]{"galpao", "galpao/deposito/armazem", ""});
        mapaTipo.put(TipoDAO.load("Loja"), new String[]{"comercial", "loja/salao", ""});
        mapaTipo.put(TipoDAO.load("Prédio"), new String[]{"predio", "predio inteiro", ""});
        mapaTipo.put(TipoDAO.load("Prédio Comercial"), new String[]{"predio", "comercial", ""});
        mapaTipo.put(TipoDAO.load("Sala Comercial"), new String[]{"escritório comercial", "conjunto comercial/sala", ""});
        mapaTipo.put(TipoDAO.load("Sítio"), new String[]{"sitio", "sitio", ""});
        mapaTipo.put(TipoDAO.load("Sobrado"), new String[]{"sobrado", "casa padrao", "sobrado/duplex"});
        mapaTipo.put(TipoDAO.load("Sobrado Novo"), new String[]{"sobrado", "casa padrao", "sobrado/duplex"});
        mapaTipo.put(TipoDAO.load("Terrea Nova"), new String[]{"casa", "", ""});
        mapaTipo.put(TipoDAO.load("Terreno"), new String[]{"terreno", "", ""});
        mapaTipo.put(TipoDAO.load("Casa de Vila"), new String[]{"sobrado", "casa padrao", "sobrado/duplex"});
    }

    /**
     * Transforma a classe {@link Foto} para o formato ZAP e inclui no elemento
     * atual.
     *
     * @param imovel {@link Imovel} imóvel atual.
     * @param imovelXML {@link Element} que representa o imóvel atual,
     * semi-preenchido.
     */
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

    private String getNomeBairro(String bairro) {
        Map<String, String> mapa = new HashMap<String, String>();

        mapa.put("JARDIM DA SAÚDE", "JD SAÚDE");

        if (mapa.containsKey(bairro)) {
            return mapa.get(bairro);
        } else {
            return bairro;
        }

    }
}
