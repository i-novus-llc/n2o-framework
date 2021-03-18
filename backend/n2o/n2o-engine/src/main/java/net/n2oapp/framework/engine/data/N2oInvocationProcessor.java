package net.n2oapp.framework.engine.data;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.dataset.DataSetMapper;
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
    public DataSet invoke(
            N2oInvocation invocation,
            DataSet inDataSet,
            Collection<AbstractParameter> inParameters,
            Collection<ObjectSimpleField> outParameters) {
        final Map<String, String> inMapping = InvocationParametersMapping.extractMapping(inParameters);
        final Map<String, String> outMapping = InvocationParametersMapping.extractMapping(outParameters);
        DataSet resolvedInDataSet = resolveInValues(inMapping, inParameters, inDataSet);
        DataSet resultDataSet = invoke(invocation, resolvedInDataSet, inMapping, outMapping);
        resolveOutValues(outParameters, resultDataSet);
        inDataSet.merge(resultDataSet);
        return inDataSet;
    }

    private DataSet invoke(
            final N2oInvocation invocation,
            final DataSet inDataSet,
            final Map<String, String> inMapping,
            final Map<String, String> outMapping) {
        final ActionInvocationEngine engine = invocationFactory.produce(invocation.getClass());
        Object result;
        if (engine instanceof ArgumentsInvocationEngine) {
            result = ((ArgumentsInvocationEngine) engine).invoke((N2oArgumentsInvocation) invocation,
                    mapToArgs((N2oArgumentsInvocation) invocation, inDataSet, inMapping, domainProcessor));
        } else {
            result = engine.invoke(invocation, mapToMap(inDataSet, inMapping));
        }
        return DataSetMapper.extract(result, outMapping);
    }


    protected void resolveOutValues(Collection<ObjectSimpleField> invocationParameters, DataSet resultDataSet) {
        if (invocationParameters == null) return;

        for (ObjectSimpleField parameter : invocationParameters)
            if (parameter.getDefaultValue() != null && parameter.getMapping() == null)
                resultDataSet.put(parameter.getId(), contextProcessor.resolve(parameter.getDefaultValue()));
    }

    // TODO - будет переписано в следующей задаче
    protected DataSet resolveInValues(Map<String, String> inMapping,
                                      Collection<AbstractParameter> invocationParameters,
                                      DataSet inDataSet) {
        if (invocationParameters == null)
            return inDataSet;

        for (AbstractParameter parameter : invocationParameters) {
            prepareValue(parameter, inDataSet);
            if (isMappingEnabled(parameter, inMapping, inDataSet) &&
                    parameter instanceof ObjectReferenceField &&
                    ((ObjectReferenceField) parameter).getEntityClass() != null &&
                    ((ObjectReferenceField) parameter).getFields() != null) {

                if (parameter.getClass().equals(ObjectReferenceField.class)) {
                    inDataSet.put(
                            parameter.getId(),
                            normalize(Arrays.asList(((ObjectReferenceField) parameter).getFields()), (DataSet) inDataSet.get(parameter.getId()))
                    );
                } else {
                    for (Object dataSet : (Collection) inDataSet.get(parameter.getId())) {
                        ((DataSet) dataSet).putAll(
                                normalize(Arrays.asList(((ObjectReferenceField) parameter).getFields()), (DataSet) dataSet)
                        );
                    }
                }
                MappingProcessor.mapParameter((ObjectReferenceField) parameter, inDataSet);
            }
        }

        return normalize(invocationParameters, inDataSet);
    }

    // TODO - будет переписано в следующей задаче
    private void prepareValue(AbstractParameter inParameter, DataSet inDataSet) {
        Object value = inDataSet.get(inParameter.getId());
        if (inParameter instanceof ObjectSimpleField) {
            if (value == null)
                value = ((ObjectSimpleField) inParameter).getDefaultValue();
            value = contextProcessor.resolve(value);
            value = domainProcessor.deserialize(value, ((ObjectSimpleField) inParameter).getDomain());
        } else if (inParameter instanceof ObjectReferenceField && ((ObjectReferenceField) inParameter).getFields() != null) {
            for (AbstractParameter childParam : ((ObjectReferenceField) inParameter).getFields()) {
                if (inParameter.getClass().equals(ObjectReferenceField.class))
                    prepareValue(childParam, (DataSet) value);
                else
                    for (Object dataSet : (Collection) value)
                        prepareValue(childParam, (DataSet) dataSet);
            }
        }
        inDataSet.put(inParameter.getId(), value);
    }

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

    private boolean isMappingEnabled(AbstractParameter inParam, Map<String, String> inMapping, DataSet inDataSet) {
        boolean unmappable = inParam.getEnabled() != null && !ScriptProcessor.evalForBoolean(inParam.getEnabled(), inDataSet);
        if (unmappable) {
            inMapping.remove(inParam.getId());
            return false;
        }
        return true;
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
