package net.n2oapp.framework.config.util;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.data.QueryProcessor;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;
import net.n2oapp.framework.api.util.SubModelsProcessor;
import net.n2oapp.framework.config.compile.pipeline.N2oPipelineSupport;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Процессор для вычисления вложенных справочных значений свойств модели.
 * Использовать только для вычислений под конкретного пользователя!
 */
public class N2oSubModelsProcessor implements SubModelsProcessor, MetadataEnvironmentAware {

    private QueryProcessor queryProcessor;
    private MetadataEnvironment environment;
    private DomainProcessor domainProcessor;

    public N2oSubModelsProcessor(QueryProcessor queryProcessor, DomainProcessor domainProcessor) {
        this.queryProcessor = queryProcessor;
        this.domainProcessor = domainProcessor;
    }

    @Override
    public void executeSubModels(List<SubModelQuery> subQueries, DataSet dataSet) {
        if (dataSet.isEmpty() || subQueries == null) return;
        for (SubModelQuery subModelQuery : subQueries) {
            CompiledQuery subQuery = subModelQuery.getQueryId() == null
                    ? null
                    : environment.getReadCompileBindTerminalPipelineFunction()
                    .apply(new N2oPipelineSupport(environment))
                    .get(new QueryContext(subModelQuery.getQueryId()), dataSet);

            executeSubModel(subModelQuery, dataSet, subQuery);
        }
    }

    @Override
    public CollectionPage<DataSet> getQueryResult(String queryId) {
        CompiledQuery query = environment.getReadCompileBindTerminalPipelineFunction()
                .apply(new N2oPipelineSupport(environment))
                .get(new QueryContext(queryId), new DataSet());
        return queryProcessor.executeOneSizeQuery(query, new N2oPreparedCriteria());
    }

    private void executeSubModel(SubModelQuery subModelQuery, Map<String, Object> dataSet, CompiledQuery subQuery) {
        String valueFieldId = subModelQuery.getValueFieldId() != null ? subModelQuery.getValueFieldId() : "id";
        String labelFieldId = subModelQuery.getLabelFieldId() != null ? subModelQuery.getLabelFieldId() : "name";

        List<Map<String, Object>> subModels = prepareSubModels(subModelQuery, dataSet, labelFieldId, valueFieldId);

        if (subModels == null) return;

        if (subQuery != null && subQuery.containsFilter("id", FilterType.eq))
            executeQuery(subQuery, subModelQuery, subModels, labelFieldId, valueFieldId);
        else if (subQuery == null)
            resolveOptions(subModelQuery, subModels, labelFieldId, valueFieldId);
    }

    private void executeQuery(CompiledQuery subQuery, SubModelQuery subModelQuery,
                              List<Map<String, Object>> subModels, String labelFieldId, String valueFieldId) {
        N2oQuery.Field field = subQuery.getFieldsMap().get(valueFieldId);
        if (field == null)
            throw new N2oException(String.format("field [%s] not found in query [%s]", valueFieldId, subModelQuery.getQueryId()));

        for (Map<String, Object> subModel : subModels) {
            if (subModel.get(labelFieldId) != null || subModel.get(valueFieldId) == null)
                return;
            Object value = subModel.get(valueFieldId);
            if (StringUtils.isDynamicValue(value))
                continue;
            N2oPreparedCriteria criteria = N2oPreparedCriteria.simpleCriteriaOneRecord(valueFieldId, value);
            CollectionPage<DataSet> subData = queryProcessor.executeOneSizeQuery(subQuery, criteria);

            DataSet first = subData.getCollection().iterator().next();
            for (N2oQuery.Field queryField : subQuery.getDisplayFields()) {
                subModel.put(queryField.getId(), first.get(queryField.getId()));
            }
        }
    }

    private void resolveOptions(SubModelQuery subModelQuery, List<Map<String, Object>> subModels, String labelFieldId, String valueFieldId) {
        if (subModelQuery.getOptions() == null) return;
        for (Map<String, Object> subModel : subModels) {
            if (subModel.get(labelFieldId) != null || subModel.get(valueFieldId) == null)
                return;
            if (StringUtils.isDynamicValue(subModel.get(valueFieldId)))
                continue;
            Object value = domainProcessor.deserialize(subModel.get(valueFieldId));
            subModelQuery.getOptions().forEach(option -> {
                if (value.equals(option.get(valueFieldId)) && option.get(labelFieldId) != null) {
                    subModel.put(labelFieldId, option.get(labelFieldId));
                }
            });
        }
    }

    private List<Map<String, Object>> prepareSubModels(SubModelQuery subModelQuery, Map<String, Object> dataSet, String labelFieldId, String valueFieldId) {
        Object subModelValue;
        if (subModelQuery.getSubModel() != null)
            subModelValue = dataSet.get(subModelQuery.getSubModel());
        else
            subModelValue = dataSet;

        List<Map<String, Object>> subModels;
        if (subModelValue instanceof Collection) {
            if (((Collection) subModelValue).isEmpty()) return null;
            if (!(((Collection) subModelValue).iterator().next() instanceof Map))
                return null;
            subModels = (List<Map<String, Object>>) subModelValue;
            if (subModels.get(0) == null) {
                subModels.clear();
                return null;
            }
            if (subModels.get(0).get(labelFieldId) == null && subModels.get(0).get(valueFieldId) == null) {
                subModels.clear();
                return null;
            }
        } else if (subModelValue instanceof Map)
            subModels = Collections.singletonList((Map<String, Object>) subModelValue);
        else
            return null;

        return subModels;
    }

    @Override
    public void setEnvironment(MetadataEnvironment environment) {
        this.environment = environment;
    }
}
