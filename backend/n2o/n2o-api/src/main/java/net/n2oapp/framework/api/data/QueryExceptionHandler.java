package net.n2oapp.framework.api.data;

import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;

/**
 * Обработчик исключений от провайдеров данных при получении выборки
 */
public interface QueryExceptionHandler {
    /**
     * Преобразовать исключение провайдера данных в N2O исключение
     *
     * @param query    Выборка
     * @param criteria Критерии поиска
     * @param e        Исключение от провайдера данных
     * @return Исключение N2O
     */
    N2oException handle(CompiledQuery query, N2oPreparedCriteria criteria, Exception e);
}
