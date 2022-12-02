package net.n2oapp.framework.api.metadata.meta.action.link;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.UrlAware;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.ActionPayload;

import java.util.Map;

/**
 * Полезная нагрузка действия перехода по ссылке
 */
@Getter
@Setter
public class LinkActionPayload implements ActionPayload, UrlAware {
    @JsonProperty
    private String url;
    @JsonProperty
    private String modelLink;
    @JsonProperty
    private Target target;
    @JsonProperty
    private Map<String, ModelLink> pathMapping = new StrictMap<>();
    @JsonProperty
    private Map<String, ModelLink> queryMapping = new StrictMap<>();
}
