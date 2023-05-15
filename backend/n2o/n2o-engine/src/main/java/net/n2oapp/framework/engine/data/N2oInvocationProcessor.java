package net.n2oapp.framework.engine.data;

import net.n2oapp.criteria.dataset.ArrayMergeStrategy;
import net.n2oapp.criteria.dataset.DataList;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.dataset.FieldMapping;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.context.ContextProcessor;
import net.n2oapp.framework.api.data.ActionInvocationEngine;
import net.n2oapp.framework.api.data.ArgumentsInvocationEngine;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.data.InvocationProcessor;
import net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oArgumentsInvocation;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oInvocation;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectListField;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectReferenceField;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSetField;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSimpleField;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.engine.exception.N2oSpelException;
import net.n2oapp.framework.engine.util.MappingProcessor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.*;

import static java.util.Objects.isNull;
import static net.n2oapp.framework.engine.util.ArgumentsInvocationUtil.mapToArgs;
import static net.n2oapp.framework.engine.util.MapInvocationUtil.mapToMap;
import static net.n2oapp.framework.engine.util.MappingProcessor.normalizeValue;
import static net.n2oapp.framework.engine.util.MappingProcessor.outMap;

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
                          Collection<AbstractParameter> outParameters) {
        final Map<String, FieldMapping> inMapping = MappingProcessor.extractFieldMapping(inParameters);
        final Map<String, FieldMapping> outMapping = MappingProcessor.extractFieldMapping(outParameters);
        prepareInValues(inParameters, inDataSet);
        DataSet resolvedInDataSet = resolveInValuesMapping(inParameters, inDataSet);
        DataSet resultDataSet = invoke(invocation, resolvedInDataSet, inMapping, outMapping, invocation.getResultMapping());
        resolveOutValues(outParameters, resultDataSet);
        inDataSet.merge(resultDataSet, ArrayMergeStrategy.replace, true);
        return inDataSet;
    }

    private DataSet invoke(final N2oInvocation invocation, final DataSet inDataSet,
                           final Map<String, FieldMapping> inMapping,
                           final Map<String, FieldMapping> outMapping, String resultMapping) {
        final ActionInvocationEngine engine = invocationFactory.produce(invocation.getClass());
        Object result;
        if (engine instanceof ArgumentsInvocationEngine)
            result = ((ArgumentsInvocationEngine) engine).invoke((N2oArgumentsInvocation) invocation,
                    mapToArgs((N2oArgumentsInvocation) invocation, inDataSet, inMapping, domainProcessor));
        else
            result = engine.invoke(invocation, mapToMap(inDataSet, inMapping));

        if (resultMapping != null)
            result = outMap(result, resultMapping, Object.class);

        return extractFields(result, outMapping);
    }

    /**
     * Преобразование исходящих данных вызова
     *
     * @param parameters    Исходящие поля операции
     * @param resultDataSet Исходящие данные вызова
     */
    protected void resolveOutValues(Collection<AbstractParameter> parameters, DataSet resultDataSet) {
        if (parameters == null) return;
        for (AbstractParameter parameter : parameters) {
            Object value = resultDataSet.get(parameter.getId());
            if (parameter instanceof ObjectReferenceField) {
                if (value != null) {
                    ObjectReferenceField referenceField = (ObjectReferenceField) parameter;
                    if (value instanceof Collection) {
                        for (Object obj : (Collection<?>) value)
                            resolveOutValues(List.of(referenceField.getFields()), (DataSet) obj);
                    } else if (value instanceof DataSet)
                        resolveOutValues(List.of(referenceField.getFields()), (DataSet) value);
                }
            } else {
                ObjectSimpleField simpleField = (ObjectSimpleField) parameter;
                resolveOutField(simpleField, resultDataSet, value);
            }
        }
    }

    private void resolveOutField(ObjectSimpleField simpleField, DataSet resultDataSet, Object value) {
        if (value == null && simpleField.getDefaultValue() != null && simpleField.getMapping() == null)
            value = contextProcessor.resolve(simpleField.getDefaultValue());
        if (value != null && simpleField.getNormalize() != null)
            value = tryToNormalize(value, simpleField, resultDataSet, applicationContext);
        resultDataSet.put(simpleField.getId(), value);
    }

    /**
     * Преобразование входящих данных вызова с учетом маппинга
     *
     * @param invocationParameters Входящие поля операции
     * @param inDataSet            Входящие данные вызова
     * @return Преобразованные входящие данные вызова
     */
    protected DataSet resolveInValuesMapping(Collection<AbstractParameter> invocationParameters,
                                             DataSet inDataSet) {

        if (invocationParameters == null || inDataSet == null)
            return new DataSet();
        return normalizeAndMapFields(invocationParameters, inDataSet);
    }

    private DataSet normalizeAndMapFields(Collection<AbstractParameter> invocationParameters,
                                          DataSet inDataSet) {
        DataSet resultDataSet = new DataSet();
        // normalize values
        invocationParameters.forEach(parameter -> {
            Object value = inDataSet.get(parameter.getId());
            if (parameter.getNormalize() != null)
                value = tryToNormalize(value, parameter, inDataSet, applicationContext);
            resultDataSet.put(parameter.getId(), value);
        });
        // remove not enabled data
        invocationParameters.stream().filter(parameter -> !isMappingEnabled(parameter, inDataSet))
                .forEach(parameter -> resultDataSet.remove(parameter.getId()));
        // normalize children
        invocationParameters.stream()
                .filter(parameter -> parameter instanceof ObjectReferenceField &&
                        ((ObjectReferenceField) parameter).getFields() != null && resultDataSet.get(parameter.getId()) != null)
                .forEach(parameter -> normalizeInnerFields((ObjectReferenceField) parameter, resultDataSet));
        // mapping
        invocationParameters.stream().filter(parameter -> parameter instanceof ObjectReferenceField
                        && ((ObjectReferenceField) parameter).getFields() != null && ((ObjectReferenceField) parameter).getEntityClass() != null)
                .forEach(parameter -> MappingProcessor.mapParameter((ObjectReferenceField) parameter, resultDataSet));
        return resultDataSet;
    }

    private void normalizeInnerFields(ObjectReferenceField field, DataSet dataSet) {
        List<AbstractParameter> innerParams = Arrays.asList(field.getFields());
        if (field instanceof ObjectListField || field instanceof ObjectSetField) {
            DataList list = new DataList(dataSet.getList(field.getId()));
            for (int i = 0; i < list.size(); i++)
                list.set(i, normalizeAndMapFields(innerParams, (DataSet) dataSet.getList(field.getId()).get(i)));
            dataSet.put(field.getId(), list);
        } else {
            dataSet.put(field.getId(), normalizeAndMapFields(innerParams, dataSet.getDataSet(field.getId())));
        }
    }

    /**
     * Установка значений во входящие поля вызова с учетом значения по умолчанию и
     * типа соответствующего поля операции
     *
     * @param parameters Входящее поле операции
     * @param inDataSet  Входящие данные вызова
     */
    private void prepareInValues(Collection<AbstractParameter> parameters, DataSet inDataSet) {
        if (isNull(inDataSet)) return;
        for (AbstractParameter parameter : parameters) {
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
                    for (Object dataSet : Objects.requireNonNullElse((Collection) inDataSet.get(parameter.getId()), Collections.emptyList()))
                        prepareInValues(Arrays.asList(((ObjectReferenceField) parameter).getFields()), (DataSet) dataSet);
            }
        }
    }

    private Object tryToNormalize(Object value,
                                  AbstractParameter parameter,
                                  DataSet inDataSet,
                                  ApplicationContext applicationContext) {
        try {
            value = normalizeValue(value, parameter.getNormalize(), inDataSet, parser, applicationContext);
        } catch (N2oSpelException e) {
            e.setFieldId(parameter.getId());
            throw e;
        }
        return value;
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

    /**
     * Построение набора данных по объекту с учетом маппинга полей
     *
     * @param source        Исходный объект
     * @param fieldsMapping Маппинг полей
     * @return Набор данных объекта
     */
    private DataSet extractFields(Object source, Map<String, FieldMapping> fieldsMapping) {
        DataSet result = new DataSet();
        for (Map.Entry<String, FieldMapping> map : fieldsMapping.entrySet()) {
            String fieldMapping = map.getValue().getMapping() != null ? map.getValue().getMapping() : Placeholders.spel(map.getKey());
            //Object value = outMap(source, fieldMapping, Object.class);
            outMap(result, source, map.getKey(), fieldMapping, null, contextProcessor);
            if (map.getValue().getChildMapping() != null) {
                Object value = result.get(map.getKey());
                if (value instanceof Collection) {
                    DataList list = new DataList();
                    for (Object obj : (Collection<?>) value)
                        list.add(extractFields(obj, map.getValue().getChildMapping()));
                    result.put(map.getKey(), list);
                } else
                    result.put(map.getKey(), extractFields(value, map.getValue().getChildMapping()));
            }
        }
        return result;
    }
}
