package net.n2oapp.framework.api.util;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;

import java.util.List;

/**
 * Процессор для вычисления вложенных моделей
 */
public interface SubModelsProcessor {

    /**
     * Разрешает значения полей для вложенных моделей выборки
     *
     * @param subQueries - список вложенных моделей выборки
     * @param dataSet    - входной набор данных
     */
    void executeSubModels(List<SubModelQuery> subQueries, DataSet dataSet);

    /**
     * Получение результата выборки по ее идентификатору
     *
     * @param queryId Идентификатор выборки
     * @return Результат выборки
     */
    CollectionPage<DataSet> getQueryResult(String queryId);
}
