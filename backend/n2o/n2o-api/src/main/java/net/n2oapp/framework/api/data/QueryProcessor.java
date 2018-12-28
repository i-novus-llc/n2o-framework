package net.n2oapp.framework.api.data;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;

/**
 * Процессор выполнения запросов
 */
public interface QueryProcessor {

    /**
     * Получение списка данных по запросу
     * @param query     запрос за данными
     * @param criteria  ограничения(фильтры)
     * @return          список данных
     */
    CollectionPage<DataSet> execute(final CompiledQuery query, final N2oPreparedCriteria criteria);

    /**
     * Получение количества записей для определенного запроса с ограничениями
     * @param query     запрос
     * @param criteria  ограничения(фильтры)
     * @return          количество записей
     */
    Integer executeCount(final CompiledQuery query, final N2oPreparedCriteria criteria);

    /**
     * Получение списка данных из одной строки
     * @param query     запрос
     * @param criteria  ограничения(фильтры)
     * @return          список из одной записи
     */
    CollectionPage<DataSet> executeOneSizeQuery(CompiledQuery query, N2oPreparedCriteria criteria);
}
