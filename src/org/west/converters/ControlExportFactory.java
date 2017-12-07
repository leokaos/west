package org.west.converters;

import java.util.Date;

/**
 * Classe responsável por criar os ControlExports.
 *
 * @author West Guerra Ltda.
 */
public class ControlExportFactory {

    private ControlExportFactory() {
        super();
    }

    /**
     * Cria uma instância de {@link SiteControl} com a data passada.
     *
     * @param data data de corte para o export do site;
     * @return siteControl : instância de {@link SiteControl}.
     */
    public static SiteControl createSiteControl(Date data) {
        return new SiteControl(data);
    }

    /**
     * Cria uma instância de {@link ZapControl}.
     *
     * @return zapControl : instância de {@link ZapControl}.
     */
    public static ZapControl createZapControl() {
        return new ZapControl();
    }

    /**
     * Cria uma instância de {@link VivaRealControl}.
     *
     * @return vivaRealControl : instância de {@link VivaRealControl}.
     */
    public static VivaRealControl createVivaRealControl(Date dataCorte) {
        return new VivaRealControl(dataCorte);
    }

    public static I123Control create123iControl() {
        return new I123Control();
    }

    public static ImovelWebControl createImovelWebControl() {
        return new ImovelWebControl();
    }
}