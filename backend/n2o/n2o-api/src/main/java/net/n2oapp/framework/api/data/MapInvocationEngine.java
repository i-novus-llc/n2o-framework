package net.n2oapp.framework.api.data;

import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oMapInvocation;

import java.util.Map;

/**
 * Сервис выполненения операций в виджетах, который на вход принимает карту ключ-значение
 */
public interface MapInvocationEngine<T extends N2oMapInvocation> extends ActionInvocationEngine<T> {


    /**
     * Выполнение операции
     * @param invocation source модель action-a
     * @param data входные данные
     * @return данные после выполнения операции
     */
    Object invoke(T invocation, Map<String, Object> data);

    default Object invoke(T invocation, Object data) {
        return invoke(invocation, (Map<String, Object>) data);
    }
}
