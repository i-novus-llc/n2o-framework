package net.n2oapp.framework.api.metadata.meta.action.multi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.action.ActionPayload;

import java.util.List;

/**
 * Полезная нагрузка, содержащая последовательность действий
 */
@Getter
@Setter
public class MultiActionPayload implements ActionPayload {
    @JsonProperty
    private List<Action> actions;

    public MultiActionPayload(List<Action> actions) {
        this.actions = actions;
    }
}
