package net.n2oapp.framework.engine.data;


import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.InvocationProcessor;
import net.n2oapp.framework.api.data.OperationExceptionHandler;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSimpleField;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.engine.exception.N2oSpelException;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Map;

import static net.n2oapp.framework.engine.util.MappingProcessor.normalizeValue;
import static net.n2oapp.framework.engine.util.MappingProcessor.outMap;

/**
 * Процессор действий
 */
public class N2oOperationProcessor {
    private final InvocationProcessor invocationProcessor;
    private final OperationExceptionHandler exceptionHandler;
    private static final ExpressionParser parser = new SpelExpressionParser();

    public N2oOperationProcessor(InvocationProcessor invocationProcessor,
                                 OperationExceptionHandler exceptionHandler) {
        this.invocationProcessor = invocationProcessor;
        this.exceptionHandler = exceptionHandler;
    }

    public DataSet invoke(CompiledObject.Operation operation,
                          DataSet inDataSet,
                          Collection<AbstractParameter> inParameters,
                          Collection<AbstractParameter> outParameters,
                          boolean useFailOut) {
        try {
            return invocationProcessor.invoke(
                    operation.getInvocation(),
                    inDataSet,
                    inParameters,
                    outParameters
            );
        } catch (Exception e) {
            if (useFailOut && !CollectionUtils.isEmpty(operation.getFailOutParametersMap())) {
                addFailOutParameters(operation.getFailOutParametersMap(), inDataSet, e);
                return inDataSet;
            }
            throw exceptionHandler.handle(operation, inDataSet, e);
        }
    }

    private void addFailOutParameters(Map<String, ObjectSimpleField> failOutParameters, DataSet inDataSet, Exception exception) {
        for (Map.Entry<String, ObjectSimpleField> entry : failOutParameters.entrySet()) {
            Object result = null;
            if (entry.getValue().getMapping() != null)
                result = outMap(exception, entry.getValue().getMapping(), Object.class);

            if (result != null && entry.getValue().getNormalize() != null)
                try {
                    result = normalizeValue(result, entry.getValue().getNormalize(), null, parser, null);
                } catch (N2oSpelException e) {
                    e.setFieldId(entry.getKey());
                    throw e;
                }

            inDataSet.put(entry.getKey(), result);
        }
    }
}
