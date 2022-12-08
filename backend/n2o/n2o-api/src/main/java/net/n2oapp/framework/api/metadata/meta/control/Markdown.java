package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.Action;

import java.util.Map;

/**
 * Клиентская модель компонента markdown
 */
@Getter
@Setter
public class Markdown extends Field {
    @JsonProperty
    private Map<String, Action> actions;
    @JsonProperty
    private String content;
}
