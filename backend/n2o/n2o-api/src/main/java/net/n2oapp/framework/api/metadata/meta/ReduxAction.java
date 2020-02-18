package net.n2oapp.framework.api.metadata.meta;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.PerformActionPayload;
import net.n2oapp.framework.api.metadata.meta.action.ReduxActionOptions;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;

import java.util.Map;

@Getter
@Setter
public class ReduxAction extends ReduxActionOptions<PerformActionPayload, MetaSaga> {

    public ReduxAction() {
    }

    public ReduxAction(String type) {
        super(type, new PerformActionPayload());
    }

    public ReduxAction(String type, Map<String, Object> params) {
        super(type, new PerformActionPayload(params));
    }
}
