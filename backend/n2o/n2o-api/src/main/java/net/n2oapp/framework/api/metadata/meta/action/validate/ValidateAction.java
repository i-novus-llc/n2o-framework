package net.n2oapp.framework.api.metadata.meta.action.validate;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.AbstractAction;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;

/**
 * Клиентская модель действия валидации
 */
@Getter
@Setter
public class ValidateAction extends AbstractAction<ValidateActionPayload, MetaSaga> {

    public ValidateAction() {
        super(new ValidateActionPayload(), null);
    }
}