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
import java.util.function.Function;

/**
 * User: operehod
 * Date: 26.01.2015
 * Time: 17:48
 */
public class SingleListFieldSubModelQuery implements SubModelQuery {

    private static final Logger logger = LoggerFactory.getLogger(SingleListFieldSubModelQuery.class);

    private String subModel;
    private String queryId;
    private String valueFieldId;
    private String labelFieldId;

    public SingleListFieldSubModelQuery(String subModel, String queryId, String valueFieldId, String labelFieldId) {
        this.subModel = subModel;
        this.labelFieldId = labelFieldId;
        this.queryId = queryId;
        this.valueFieldId = valueFieldId;
    }


    @Override
    public String getSubModel() {
        return subModel;
    }


    @Override
    public void applySubModel(Map<String, Object> dataSet,
                              Function<String, CompiledQuery> querySupplier,
                              BiFunction<CompiledQuery, N2oPreparedCriteria, CollectionPage<DataSet>> queryExecutor) {

        String valueField = getSubModel() + "." + valueFieldId;
        String labelField = getSubModel() + "." + labelFieldId;

        //если label есть, то subQuery не выполняем
        if (dataSet.get(labelField) != null || dataSet.get(valueField) == null) return;

        CompiledQuery subQuery = querySupplier.apply(queryId);
        N2oQuery.Field field = subQuery.getFieldsMap().get(valueFieldId);
        if (field == null)
            throw new RuntimeException(String.format("field [%s] not found in query [%s]", valueFieldId, queryId));
        Object value = dataSet.get(valueField);
        //если значение динамическое, то subQuery не выполняем
        if (StringUtils.isDynamicValue(value)) return;

        value = DomainProcessor.getInstance().doDomainConversion(field.getDomain(), value);
        N2oPreparedCriteria criteria = N2oPreparedCriteria.simpleCriteriaOneRecord(valueFieldId, value);
        CollectionPage<DataSet> subData = queryExecutor.apply(subQuery, criteria);
        if (subData.getCollection().size() > 1) {
            logger.warn("SubQuery for subModel '{}' return more then 1 row...({} rows)", subModel, subData.getCollection().size());
        }
        for (N2oQuery.Field queryField : subQuery.getDisplayFields()) {
            Object fieldValue = subData.getCollection().iterator().next().get(queryField.getId());
            dataSet.put(subModel + "." + queryField.getId(), fieldValue);
        }

    }


}
