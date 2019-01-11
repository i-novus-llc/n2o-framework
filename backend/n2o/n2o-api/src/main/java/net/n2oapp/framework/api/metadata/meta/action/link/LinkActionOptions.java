package net.n2oapp.framework.api.metadata.meta.action.link;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.meta.BindLink;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.ActionOptions;

import java.util.Map;

/**
 * Настройки действия ссылки
 */
@Getter
@Setter
public class LinkActionOptions implements ActionOptions {
    @JsonProperty
    private String path;
    @JsonProperty
    private Target target;
    @JsonProperty
    private Map<String, ModelLink> pathMapping = new StrictMap<>();
    @JsonProperty
    private Map<String, ModelLink> queryMapping = new StrictMap<>();
}
