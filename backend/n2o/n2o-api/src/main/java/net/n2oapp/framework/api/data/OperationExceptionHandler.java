package net.n2oapp.framework.api.data;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.local.CompiledObject;

/**
 * Обработчик исключений от провайдеров данных при выполнении операций
 */
public interface OperationExceptionHandler {
    /**
     * Преобразовать исключение провайдера данных в N2O исключение
     * @param o Операция
     * @param data Данные операции
     * @param e Исключение от провайдера данных
     * @return Исключение N2O
     */
    N2oException handle(CompiledObject.Operation o, DataSet data, Exception e);
}
