package net.n2oapp.framework.config.util;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.data.QueryProcessor;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;
import net.n2oapp.framework.api.util.SubModelsProcessor;
import net.n2oapp.framework.config.compile.pipeline.N2oPipelineSupport;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Процессор для вычисления вложенных справочных значений свойств модели.
 * Использовать только для вычислений под конкретного пользователя!
 */
public class N2oSubModelsProcessor implements SubModelsProcessor {

    private static final Logger logger = LoggerFactory.getLogger(N2oSubModelsProcessor.class);

    private QueryProcessor queryProcessor;
    private MetadataEnvironment environment;

    public N2oSubModelsProcessor(QueryProcessor queryProcessor, MetadataEnvironment environment) {
        this.queryProcessor = queryProcessor;
        this.environment = environment;
    }

    @SuppressWarnings("unchecked")
    public void executeSubModels(List<SubModelQuery> subQueries, DataSet dataSet, OnErrorCallback callback) {
        if (dataSet.isEmpty()) return;
        for (SubModelQuery subModelQuery : subQueries) {
            try {
                CompiledQuery subQuery = environment.getReadCompileBindTerminalPipelineFunction()
                        .apply(new N2oPipelineSupport(environment))
                        .get(new QueryContext(subModelQuery.getQueryId()), dataSet);

                subModelQuery.applySubModel(
                        dataSet,
                        subQuery,
                        (query, criteria) -> queryProcessor.executeOneSizeQuery(query, criteria));
            } catch (RuntimeException e) {
                logger.error(e.getMessage(), e);
                callback.onError(e, dataSet, subModelQuery.getSubModel());
            }
        }
    }
}
