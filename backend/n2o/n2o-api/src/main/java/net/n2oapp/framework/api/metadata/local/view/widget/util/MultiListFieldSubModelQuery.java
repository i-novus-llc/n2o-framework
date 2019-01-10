package net.n2oapp.framework.api.metadata.local.view.widget.util;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * User: operehod
 * Date: 26.01.2015
 * Time: 17:48
 */
public class MultiListFieldSubModelQuery extends SubModelQuery {

    private static final Logger logger = LoggerFactory.getLogger(SingleListFieldSubModelQuery.class);

    public MultiListFieldSubModelQuery(String subModel, String queryId, String valueFieldId, String labelFieldId) {
        super(subModel, queryId, valueFieldId, labelFieldId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void applySubModel(
            Map<String, Object> dataSet,
            CompiledQuery subQuery,
            BiFunction<CompiledQuery, N2oPreparedCriteria, CollectionPage<DataSet>> queryExecutor) {

        Object subModelValue = dataSet.get(getSubModel());

        //работа с dataSet закончена
        dataSet = null;

        if (!(subModelValue instanceof Collection)) return;
        if (((List) subModelValue).isEmpty()) return;
        if (!(((Collection) subModelValue).iterator().next() instanceof Map))
            return;

        List<Map<String, Object>> subModels = (List<Map<String, Object>>) subModelValue;

        if (subModels.isEmpty()) return;

        if (subModels.get(0) == null) {
            subModels.clear();
            return;
        }

        if (subModels.get(0).get(getLabelFieldId()) == null && subModels.get(0).get(getValueFieldId()) == null) {
            subModels.clear();
            return;
        }

        N2oQuery.Field field = subQuery.getFieldsMap().get(getValueFieldId());

        if (field == null)
            throw new N2oException(String.format("field [%s] not found in query [%s]", getValueFieldId(), getQueryId()));

        for (Map<String, Object> subModel : subModels) {

            //если label есть, то subQuery не выполняем
            if (subModel.get(getLabelFieldId()) != null || subModel.get(getValueFieldId()) == null) return;


            Object value = subModel.get(getValueFieldId());
            //если значение динамическое, то subQuery не выполняем
            if (StringUtils.isDynamicValue(value)) return;

            value = DomainProcessor.getInstance().doDomainConversion(field.getDomain(), value);
            N2oPreparedCriteria criteria = N2oPreparedCriteria.simpleCriteriaOneRecord(getValueFieldId(), value);
            CollectionPage<DataSet> subData = queryExecutor.apply(subQuery, criteria);
            if (subData.getCollection().size() > 1) {
                logger.warn("SubQuery for subModel '{}' return more then 1 row...({} rows)", subModel, subData.getCollection().size());
            }
            for (N2oQuery.Field queryField : subQuery.getDisplayFields()) {
                Object fieldValue = subData.getCollection().iterator().next().get(queryField.getId());
                subModel.put(queryField.getId(), fieldValue);
            }
        }


    }


}
