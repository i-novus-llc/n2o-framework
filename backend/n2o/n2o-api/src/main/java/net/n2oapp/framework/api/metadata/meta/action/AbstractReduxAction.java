package net.n2oapp.framework.api.metadata.meta.action;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;


/**
 * Абстрактная реализация клиентской модели действия с полезной нагрузкой
 */
@Getter
@Setter
@Deprecated
public abstract class AbstractReduxAction<P extends ActionPayload, M extends MetaSaga>
        extends AbstractAction<P, M> {

    public AbstractReduxAction(P payload, M meta) {
        super(payload, meta);
    }
}
