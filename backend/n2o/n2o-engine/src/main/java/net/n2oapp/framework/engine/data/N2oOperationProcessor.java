package net.n2oapp.framework.engine.data;


import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.dataset.DataSetUtil;
import net.n2oapp.framework.api.data.InvocationProcessor;
import net.n2oapp.framework.api.data.OperationExceptionHandler;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSimpleField;
import net.n2oapp.framework.api.metadata.local.CompiledObject;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Процессор действий
 */
public class N2oOperationProcessor {

    private InvocationProcessor invocationProcessor;
    private OperationExceptionHandler exceptionHandler;

    public N2oOperationProcessor(InvocationProcessor invocationProcessor,
                                 OperationExceptionHandler exceptionHandler) {
        this.invocationProcessor = invocationProcessor;
        this.exceptionHandler = exceptionHandler;
    }

    public DataSet invoke(CompiledObject.Operation action, DataSet inDataSet) {
        validateRequiredFields(action.getInParametersMap().values(), inDataSet);
        return invoke(
                action,
                inDataSet,
                action.getInParametersMap().values(),
                action.getOutParametersMap().values()
        );
    }

    public DataSet invoke(CompiledObject.Operation operation,
                          DataSet inDataSet,
                          Collection<AbstractParameter> inParameters,
                          Collection<ObjectSimpleField> outParameters) {
        try {
            return invocationProcessor.invoke(
                    operation.getInvocation(),
                    inDataSet,
                    inParameters,
                    outParameters
            );
        } catch (Exception e) {
            inDataSet.putAll(getFailOutParameters(operation.getFailOutParametersMap(), e));
            throw exceptionHandler.handle(operation, inDataSet, e);
        }
    }

    /**
     * Получение данных исключения по fail-out параметрам
     *
     * @param failOutParameters Параметры операции в случае ошибки
     * @param e                 Исключение
     * @return Данные исключения по fail-out параметрам
     */
    private DataSet getFailOutParameters(Map<String, ObjectSimpleField> failOutParameters, Exception e) {
        if (failOutParameters.isEmpty())
            return new DataSet();

        Map<String, String> failOutParamsMapping = failOutParameters.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getMapping()));
        return DataSetUtil.extract(e, failOutParamsMapping);
    }

    private void validateRequiredFields(Collection<AbstractParameter> inParameters, DataSet inDataSet) {
        if (inParameters == null || inParameters.isEmpty()) {
            return;
        }

        List<String> requiredFields = inParameters.stream()
                .filter(in -> in.getRequired() != null && in.getRequired())
                .map(AbstractParameter::getId)
                .collect(Collectors.toList());

        boolean allMatch = requiredFields.stream()
                .allMatch(inDataSet::containsKey);
        if (!allMatch) {
            throw new IllegalStateException(String.format("Action required fields[%s]",
                    String.join(",", requiredFields)));
        }
    }
}
