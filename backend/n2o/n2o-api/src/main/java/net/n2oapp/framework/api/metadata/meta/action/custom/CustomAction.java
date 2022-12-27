package net.n2oapp.framework.api.metadata.meta.action.custom;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.AbstractAction;
import net.n2oapp.framework.api.metadata.meta.saga.AsyncMetaSaga;

/**
 * Клиентская модель кастомного действия
 */
@Getter
@Setter
public class CustomAction extends AbstractAction<CustomActionPayload, AsyncMetaSaga> {
    public CustomAction() {
        super(null, new AsyncMetaSaga());
    }
}
