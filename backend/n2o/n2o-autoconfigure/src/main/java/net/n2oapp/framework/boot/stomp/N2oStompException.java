package net.n2oapp.framework.boot.stomp;

import net.n2oapp.framework.api.exception.N2oException;

/**
 * Ошибка отправки данных по STOMP протоколу
 */
public class N2oStompException extends N2oException {

    public N2oStompException(String message) {
        super(message);
    }
}
