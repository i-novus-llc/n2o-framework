package net.n2oapp.framework.api.metadata.meta.action;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;

import java.util.Map;

/**
 * Клиентская модель действия Perform
 */
@Getter
@Setter
public class Perform extends AbstractAction<ActionPayload, MetaSaga> {

    public Perform(Map<String, Object> extAttributes) {
        super(null, null);
        if (extAttributes != null) {
            this.setPayload(new PerformActionPayload(extAttributes));
        }
    }
}