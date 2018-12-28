package net.n2oapp.framework.api.metadata.local.view.widget.util;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.BiFunction;

/**
 * User: operehod
 * Date: 26.01.2015
 * Time: 17:48
 */
public class SingleListFieldSubModelQuery extends SubModelQuery {

    private static final Logger logger = LoggerFactory.getLogger(SingleListFieldSubModelQuery.class);

    public SingleListFieldSubModelQuery(String subModel, String queryId, String valueFieldId, String labelFieldId) {
        super(subModel, queryId, valueFieldId, labelFieldId);
    }

    @Override
    public void applySubModel(Map<String, Object> dataSet,
                              CompiledQuery subQuery,
                              BiFunction<CompiledQuery, N2oPreparedCriteria, CollectionPage<DataSet>> queryExecutor) {

        String valueField = getSubModel() + "." + getValueFieldId();
        String labelField = getSubModel() + "." + getLabelFieldId();

        //если label есть, то subQuery не выполняем
        if (dataSet.get(labelField) != null || dataSet.get(valueField) == null) return;

        N2oQuery.Field field = subQuery.getFieldsMap().get(getValueFieldId());
        if (field == null)
            throw new RuntimeException(String.format("field [%s] not found in query [%s]", getValueFieldId(), getQueryId()));
        Object value = dataSet.get(valueField);
        //если значение динамическое, то subQuery не выполняем
        if (StringUtils.isDynamicValue(value)) return;

        value = DomainProcessor.getInstance().doDomainConversion(field.getDomain(), value);
        N2oPreparedCriteria criteria = N2oPreparedCriteria.simpleCriteriaOneRecord(getValueFieldId(), value);
        CollectionPage<DataSet> subData = queryExecutor.apply(subQuery, criteria);
        if (subData.getCollection().size() > 1) {
            logger.warn("SubQuery for subModel '{}' return more then 1 row...({} rows)", getSubModel(), subData.getCollection().size());
        }
        for (N2oQuery.Field queryField : subQuery.getDisplayFields()) {
            Object fieldValue = subData.getCollection().iterator().next().get(queryField.getId());
            dataSet.put(getSubModel() + "." + queryField.getId(), fieldValue);
        }

    }


}
