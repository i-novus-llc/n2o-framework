package net.n2oapp.framework.api.metadata.meta;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.CustomActionPayload;
import net.n2oapp.framework.api.metadata.meta.action.ReduxActionOptions;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;

import java.util.Map;

@Getter
@Setter
public class ReduxAction extends ReduxActionOptions<CustomActionPayload, MetaSaga> {

    public ReduxAction() {
    }

    public ReduxAction(String type) {
        super(type, new CustomActionPayload());
    }

    public ReduxAction(String type, Map<String, Object> params) {
        super(type, new CustomActionPayload(params));
    }
}
