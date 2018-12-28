package net.n2oapp.framework.api.metadata.dataprovider;

import lombok.Getter;
import lombok.Setter;

/**
 * Модель поиска Ejb бина
 */
@Getter
@Setter
public class EjbProvider implements DIProvider {
    private String ejbBean;
    private String ejbProtocol;
    private String ejbApplication;
    private String ejbModule;
    private String ejbDistinct;
    private Boolean ejbStateful;
    private String ejbUri;
}
