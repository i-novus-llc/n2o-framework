package net.n2oapp.framework.api.metadata.meta.widget.toolbar;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.UrlAware;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.meta.ModelLink;

import java.util.Map;

/**
 * Клиентская модель кнопки в меню
 */

@Getter
@Setter
public class PerformButton extends AbstractButton implements UrlAware {
    @JsonProperty
    private String url;
    @JsonProperty
    private Target target;
    @JsonProperty
    private Boolean rounded;
    @JsonProperty
    private Map<String, ModelLink> pathMapping;
    @JsonProperty
    private Map<String, ModelLink> queryMapping;
}
