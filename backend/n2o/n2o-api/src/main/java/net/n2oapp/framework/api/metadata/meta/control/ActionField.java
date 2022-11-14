package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.action.ActionAware;

/**
 * Абстрактное поле с действием
 */
@Getter
@Setter
public abstract class ActionField extends Field implements ActionAware {
    @JsonProperty
    private Action action;
}
