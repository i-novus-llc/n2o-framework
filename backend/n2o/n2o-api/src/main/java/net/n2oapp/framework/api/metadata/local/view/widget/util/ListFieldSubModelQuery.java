package net.n2oapp.framework.api.metadata.local.view.widget.util;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class ListFieldSubModelQuery extends SubModelQuery {

    public ListFieldSubModelQuery(String subModel, String queryId, String valueFieldId, String labelFieldId, Boolean multi) {
        super(subModel, queryId, valueFieldId, labelFieldId, multi);
    }

    @Override
    public void applySubModel(
            Map<String, Object> dataSet,
            CompiledQuery subQuery,
            BiFunction<CompiledQuery, N2oPreparedCriteria, CollectionPage<DataSet>> queryExecutor) {

        Object subModelValue = dataSet.get(getSubModel());
        List<Map<String, Object>> subModels = null;
        if (subModelValue instanceof Collection) {
            if (((Collection) subModelValue).isEmpty()) return;
            if (!(((Collection) subModelValue).iterator().next() instanceof Map))
                return;
            subModels = (List<Map<String, Object>>) subModelValue;
            if (subModels.get(0) == null) {
                subModels.clear();
                return;
            }
            if (subModels.get(0).get(getLabelFieldId()) == null && subModels.get(0).get(getValueFieldId()) == null) {
                subModels.clear();
                return;
            }
        } else if (subModelValue instanceof Map)
            subModels = Collections.singletonList((Map<String, Object>) subModelValue);

        N2oQuery.Field field = subQuery.getFieldsMap().get(getValueFieldId());
        if (field == null)
            throw new N2oException(String.format("field [%s] not found in query [%s]", getValueFieldId(), getQueryId()));

        for (Map<String, Object> subModel : subModels) {
            if (subModel.get(getLabelFieldId()) != null || subModel.get(getValueFieldId()) == null)
                return;
            Object value = subModel.get(getValueFieldId());
            if (StringUtils.isDynamicValue(value))
                continue;
            value = DomainProcessor.getInstance().doDomainConversion(field.getDomain(), value);
            N2oPreparedCriteria criteria = N2oPreparedCriteria.simpleCriteriaOneRecord(getValueFieldId(), value);
            CollectionPage<DataSet> subData = queryExecutor.apply(subQuery, criteria);

            for (N2oQuery.Field queryField : subQuery.getDisplayFields()) {
                Object fieldValue = subData.getCollection().iterator().next().get(queryField.getId());
                subModel.put(queryField.getId(), fieldValue);
            }
        }
    }
}
