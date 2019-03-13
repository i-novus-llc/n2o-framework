package net.n2oapp.framework.api.util;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;

import java.util.List;
import java.util.Map;

/**
 * Процессор для вычисления вложенных моделей
 */
public interface SubModelsProcessor extends MetadataEnvironmentAware {

    /**
     * Разрешает значения полей для вложенных моделей выборки
     *
     * @param subQueries - список вложенных моделей выборки
     * @param dataSet    - входной набор данных
     */
    void executeSubModels(List<SubModelQuery> subQueries, DataSet dataSet);

    interface OnErrorCallback {
        void onError(RuntimeException e, Map<String, Object> dataSet, String controlId);
    }
}
