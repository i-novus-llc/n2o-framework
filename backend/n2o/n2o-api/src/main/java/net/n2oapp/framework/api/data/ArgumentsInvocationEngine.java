package net.n2oapp.framework.api.data;

import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oArgumentsInvocation;

/**
 * Сервис выполненения операций в виджетах, который на вход принимает массив аргументов
 */
public interface ArgumentsInvocationEngine<T extends N2oArgumentsInvocation> extends ActionInvocationEngine<T> {


    /**
     * Выполнение операции
     * @param invocation source модель action-a
     * @param data входные данные
     * @return данные после выполнения операции
     */
    Object invoke(T invocation, Object[] data);

    default Object invoke(T invocation, Object data) {
        return invoke(invocation, (Object[]) data);
    }
}
