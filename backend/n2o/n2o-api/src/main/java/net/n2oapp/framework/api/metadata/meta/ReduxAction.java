package net.n2oapp.framework.api.metadata.meta;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.PerformActionPayload;
import net.n2oapp.framework.api.metadata.meta.action.ReduxActionOptions;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;

@Getter
@Setter
@NoArgsConstructor
public class ReduxAction extends ReduxActionOptions<PerformActionPayload, MetaSaga> {

    public ReduxAction(String type, PerformActionPayload actionPayload) {
        super(type, actionPayload);
    }
}
