package net.n2oapp.framework.api.util;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;

import java.util.List;
import java.util.Map;

public interface SubModelsProcessor {

    void executeSubModels(List<SubModelQuery> subQueries, DataSet dataSet);

    interface OnErrorCallback {
        void onError(RuntimeException e, Map<String, Object> dataSet, String controlId);
    }
}
