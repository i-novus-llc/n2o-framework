package net.n2oapp.framework.api.metadata.meta.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.action.ActionAware;

@Getter
@Setter
public class ActionCell extends AbstractCell implements ActionAware {
    @JsonProperty
    private Action action;
}
