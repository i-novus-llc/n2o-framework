package net.n2oapp.framework.api.metadata.application;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.control.N2oComponent;
import net.n2oapp.framework.api.metadata.jackson.ExtAttributesSerializer;

import java.util.Map;

/**
 * Исходная модель подвала (footer)
 */
@Getter
@Setter
public class N2oFooter extends N2oComponent {

    /**
     * Текст справа
     */
    private String rightText;

    /**
     * Текст слева
     */
    private String leftText;

    /**
     * Видимость подвала
     */
    private Boolean visible;
    @ExtAttributesSerializer
    private Map<N2oNamespace, Map<String, String>> extAttributes;
}
