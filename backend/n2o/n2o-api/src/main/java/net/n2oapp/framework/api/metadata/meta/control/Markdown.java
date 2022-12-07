package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.Action;

import java.util.List;

/**
 * Клиентская модель компонента вывода html
 */
@Getter
@Setter
public class Markdown extends Field {
    @JsonProperty
    private List<Action> actions;
    @JsonProperty
    private String content;
}
