package net.n2oapp.framework.api.data;

import net.n2oapp.engine.factory.ClassedEngine;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oInvocation;

/**
 * Сервис выполненения операций в виджетах
 */
public interface ActionInvocationEngine<T extends N2oInvocation> extends ClassedEngine<T> {

    /**
     * Выполнение операции
     * @param invocation source модель action-a
     * @param data входные данные
     * @return данные после выполнения операции
     */
    Object invoke(T invocation, Object data);
}
