package net.n2oapp.framework.config.util;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.data.QueryProcessor;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;
import net.n2oapp.framework.api.util.SubModelsProcessor;
import net.n2oapp.framework.config.compile.pipeline.N2oPipelineSupport;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;

import java.util.List;

/**
 * Процессор для вычисления вложенных справочных значений свойств модели.
 * Использовать только для вычислений под конкретного пользователя!
 */
public class N2oSubModelsProcessor implements SubModelsProcessor {

    private QueryProcessor queryProcessor;
    private MetadataEnvironment environment;

    public N2oSubModelsProcessor(QueryProcessor queryProcessor, MetadataEnvironment environment) {
        this.queryProcessor = queryProcessor;
        this.environment = environment;
    }

    @SuppressWarnings("unchecked")
    public void executeSubModels(List<SubModelQuery> subQueries, DataSet dataSet) {
        if (dataSet.isEmpty()) return;
        for (SubModelQuery subModelQuery : subQueries) {
            try {
                CompiledQuery subQuery = environment.getReadCompileBindTerminalPipelineFunction()
                        .apply(new N2oPipelineSupport(environment))
                        .get(new QueryContext(subModelQuery.getQueryId()), dataSet);

                if (subQuery.getFieldsMap() != null && subQuery.getFieldsMap().containsKey("id")) {
                    N2oQuery.Filter[] filters = subQuery.getFieldsMap().get("id").getFilterList();
                    if (filters != null) {
                        for (N2oQuery.Filter filter : filters) {
                            if (FilterType.eq.equals(filter.getType())) {
                                subModelQuery.applySubModel(
                                        dataSet,
                                        subQuery,
                                        (query, criteria) -> queryProcessor.executeOneSizeQuery(query, criteria));
                            }
                        }
                    }
                }
            } catch (RuntimeException e) {
                throw new N2oException(e);
            }
        }
    }
}
