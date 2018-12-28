package net.n2oapp.framework.config.util;

import net.n2oapp.framework.api.data.QueryProcessor;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.local.N2oCompiler;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static net.n2oapp.context.StaticSpringContext.getBean;

/**
 * Процессор для вычисления вложенных справочных значений свойств модели.
 * Использовать только для вычислений под конкретного пользователя!
 */
public class SubModelsProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SubModelsProcessor.class);

    private QueryProcessor queryProcessor;
    private N2oCompiler compiler;

    public SubModelsProcessor(QueryProcessor queryProcessor, N2oCompiler compiler) {
        this.queryProcessor = queryProcessor;
        this.compiler = compiler;
    }

    @SuppressWarnings("unchecked")
    public void executeSubModels(List<SubModelQuery> subQueries, Map<String, Object> dataSet, OnErrorCallback callback) {
        if (dataSet.isEmpty()) return;
        for (SubModelQuery subModelQuery : subQueries) {
            try {
                subModelQuery.applySubModel(
                        dataSet,
                        (queryId) -> compiler.get(queryId, CompiledQuery.class),
                        (query, criteria) -> queryProcessor.executeOneSizeQuery(query, criteria));
            } catch (RuntimeException e) {
                logger.error(e.getMessage(), e);
                callback.onError(e, dataSet, subModelQuery.getSubModel());
            }
        }
    }

    public interface OnErrorCallback {
        void onError(RuntimeException e, Map<String, Object> dataSet, String controlId);
    }


    public static final OnErrorCallback ON_ERROR_NOTHING_CALLBACK = (e, dataSet, controlId) -> {
    };


}
