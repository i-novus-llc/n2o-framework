package net.n2oapp.framework.engine.data;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.dataset.DataSetUtil;
import net.n2oapp.criteria.dataset.FieldMapping;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.context.ContextProcessor;
import net.n2oapp.framework.api.data.ActionInvocationEngine;
import net.n2oapp.framework.api.data.ArgumentsInvocationEngine;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.data.InvocationProcessor;
import net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oArgumentsInvocation;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oInvocation;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectReferenceField;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSimpleField;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.engine.util.InvocationParametersMapping;
import net.n2oapp.framework.engine.util.MappingProcessor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import static net.n2oapp.framework.engine.util.InvocationParametersMapping.*;
import static net.n2oapp.framework.engine.util.MapInvocationMapping.mapToMap;

/**
 * Процессор вызова процедур
 */
public class N2oInvocationProcessor implements InvocationProcessor, MetadataEnvironmentAware, ApplicationContextAware {
    private static final ExpressionParser parser = new SpelExpressionParser();

    private N2oInvocationFactory invocationFactory;
    private ContextProcessor contextProcessor;
    private DomainProcessor domainProcessor;
    private ApplicationContext applicationContext;

    public N2oInvocationProcessor(N2oInvocationFactory invocationFactory) {
        this.invocationFactory = invocationFactory;
    }

    @Override
    public DataSet invoke(N2oInvocation invocation, DataSet inDataSet,
                          Collection<AbstractParameter> inParameters,
                          Collection<ObjectSimpleField> outParameters) {
        final Map<String, FieldMapping> inMapping = InvocationParametersMapping.extractInFieldMapping(inParameters);
        final Map<String, String> outMapping = InvocationParametersMapping.extractOutFieldMapping(outParameters);
        prepareInValues(inParameters, inDataSet);
        DataSet resolvedInDataSet = resolveInValuesMapping(inMapping, inParameters, inDataSet);
        DataSet resultDataSet = invoke(invocation, resolvedInDataSet, inMapping, outMapping);
        resolveOutValues(outParameters, resultDataSet);
        inDataSet.merge(resultDataSet);
        return inDataSet;
    }

    private DataSet invoke(final N2oInvocation invocation, final DataSet inDataSet,
                           final Map<String, FieldMapping> inMapping,
                           final Map<String, String> outMapping) {
        final ActionInvocationEngine engine = invocationFactory.produce(invocation.getClass());
        Object result;
        if (engine instanceof ArgumentsInvocationEngine)
            result = ((ArgumentsInvocationEngine) engine).invoke((N2oArgumentsInvocation) invocation,
                    mapToArgs((N2oArgumentsInvocation) invocation, inDataSet, inMapping, domainProcessor));
        else
            result = engine.invoke(invocation, mapToMap(inDataSet, inMapping));

        return DataSetUtil.extract(result, outMapping);
    }


    /**
     * Преобразование исходящих данных вызова
     *
     * @param invocationParameters Исходящие поля операции
     * @param resultDataSet        Исходящие данные вызова
     */
    protected void resolveOutValues(Collection<ObjectSimpleField> invocationParameters, DataSet resultDataSet) {
        if (invocationParameters == null) return;

        for (ObjectSimpleField parameter : invocationParameters)
            if (parameter.getDefaultValue() != null && parameter.getMapping() == null)
                resultDataSet.put(parameter.getId(), contextProcessor.resolve(parameter.getDefaultValue()));
    }

    /**
     * Преобразование входящих данных вызова с учетом маппинга
     *
     * @param mappingMap           Map маппингов полей
     * @param invocationParameters Входящие поля операции
     * @param inDataSet            Входящие данные вызова
     * @return Преобразованные входящие данные вызова
     */
    protected DataSet resolveInValuesMapping(Map<String, FieldMapping> mappingMap,
                                             Collection<AbstractParameter> invocationParameters,
                                             DataSet inDataSet) {
        if (invocationParameters == null)
            return inDataSet;

        for (AbstractParameter parameter : invocationParameters) {
            if (isMappingEnabled(parameter, inDataSet)) {
                if (parameter instanceof ObjectReferenceField && ((ObjectReferenceField) parameter).getFields() != null) {
                    ObjectReferenceField refParameter = (ObjectReferenceField) parameter;
                    if (parameter.getClass().equals(ObjectReferenceField.class)) {
                        inDataSet.put(parameter.getId(), resolveInValuesMapping(
                                mappingMap.get(parameter.getId()).getChildMapping(),
                                Arrays.asList(refParameter.getFields()),
                                (DataSet) inDataSet.get(parameter.getId())));
                    } else {
                        for (Object dataSet : (Collection) inDataSet.get(parameter.getId()))
                            ((DataSet) dataSet).putAll(resolveInValuesMapping(
                                    mappingMap.get(parameter.getId()).getChildMapping(),
                                    Arrays.asList(refParameter.getFields()),
                                    (DataSet) dataSet));
                    }
                    if (refParameter.getEntityClass() != null)
                        MappingProcessor.mapParameter(refParameter, inDataSet);
                }
            } else {
                mappingMap.remove(parameter.getId());
            }
        }
        return normalize(invocationParameters, inDataSet);
    }

    /**
     * Установка значений во входящие поля вызова с учетом значения по умолчанию и
     * типа соответствующего поля операции
     *
     * @param parameters Входящее поле операции
     * @param inDataSet  Входящие данные вызова
     */
    private void prepareInValues(Collection<AbstractParameter> parameters, DataSet inDataSet) {
        for (AbstractParameter parameter : parameters)
            if (parameter instanceof ObjectSimpleField) {
                ObjectSimpleField simpleField = (ObjectSimpleField) parameter;
                Object value = inDataSet.get(simpleField.getId());
                if (value == null)
                    value = simpleField.getDefaultValue();
                value = contextProcessor.resolve(value);
                value = domainProcessor.deserialize(value, simpleField.getDomain());
                inDataSet.put(simpleField.getId(), value);
            } else if (parameter instanceof ObjectReferenceField) {
                if (parameter.getClass().equals(ObjectReferenceField.class))
                    prepareInValues(Arrays.asList(((ObjectReferenceField) parameter).getFields()),
                            (DataSet) inDataSet.get(parameter.getId()));
                else
                    for (Object dataSet : (Collection) inDataSet.get(parameter.getId()))
                        prepareInValues(Arrays.asList(((ObjectReferenceField) parameter).getFields()), (DataSet) dataSet);
            }
    }

    /**
     * Нормализация входящих данных вызова
     *
     * @param invocationParameters Входящие поля операции
     * @param inDataSet            Входящие данные вызова
     * @return Нормализованные входящие данные вызова
     */
    private DataSet normalize(Collection<AbstractParameter> invocationParameters, DataSet inDataSet) {
        DataSet copiedDataSet = new DataSet(inDataSet);
        for (AbstractParameter parameter : invocationParameters) {
            if (parameter instanceof ObjectSimpleField && ((ObjectSimpleField) parameter).getNormalize() != null) {
                Object value = inDataSet.get(parameter.getId());
                if (value != null) {
                    value = normalizeValue(value, ((ObjectSimpleField) parameter).getNormalize(), inDataSet, parser, applicationContext);
                    copiedDataSet.put(parameter.getId(), value);
                }
            }
        }
        return copiedDataSet;
    }

    private boolean isMappingEnabled(AbstractParameter inParam, DataSet inDataSet) {
        return inParam.getEnabled() == null || ScriptProcessor.evalForBoolean(inParam.getEnabled(), inDataSet);
    }

    @Override
    public void setEnvironment(MetadataEnvironment environment) {
        this.contextProcessor = environment.getContextProcessor();
        this.domainProcessor = environment.getDomainProcessor();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
