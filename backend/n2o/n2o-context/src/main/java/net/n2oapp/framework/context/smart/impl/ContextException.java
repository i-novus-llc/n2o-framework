package net.n2oapp.framework.context.smart.impl;

import net.n2oapp.framework.api.exception.N2oException;

/**
 * Ошибка smart контекста
 */
public class ContextException extends N2oException {

    public ContextException(String techMessage) {
        super(techMessage, null);
    }

}
