package net.n2oapp.framework.engine.data;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.dataset.DataSetMapper;
import net.n2oapp.framework.api.context.ContextProcessor;
import net.n2oapp.framework.api.data.ActionInvocationEngine;
import net.n2oapp.framework.api.data.ArgumentsInvocationEngine;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.data.InvocationProcessor;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oArgumentsInvocation;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oInvocation;
import net.n2oapp.framework.api.metadata.global.dao.object.InvocationParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.dao.object.PluralityType;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.engine.util.InvocationParametersMapping;
import net.n2oapp.framework.engine.util.MappingProcessor;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import static net.n2oapp.framework.engine.util.InvocationParametersMapping.*;

/**
 * Процессор вызова процедур
 */
public class N2oInvocationProcessor implements InvocationProcessor {
    private static final ExpressionParser parser = new SpelExpressionParser();

    private N2oInvocationFactory invocationFactory;
    private ContextProcessor contextProcessor;

    public N2oInvocationProcessor(N2oInvocationFactory invocationFactory,
                                  ContextProcessor contextProcessor) {
        this.invocationFactory = invocationFactory;
        this.contextProcessor = contextProcessor;
    }

    @Override
    public DataSet invoke(
            N2oInvocation invocation,
            DataSet inDataSet,
            Collection<? extends InvocationParameter> inParameters,
            Collection<? extends InvocationParameter> outParameters) {
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
                    mapToArgs((N2oArgumentsInvocation) invocation, inDataSet, inMapping));
        } else {
            result = engine.invoke(invocation, mapToMap(inDataSet, inMapping));
        }
        return DataSetMapper.extract(result, outMapping);
    }


    protected void resolveOutValues(Collection<? extends InvocationParameter> invocationParameters, DataSet resultDataSet) {
        if (invocationParameters == null) return;
        for (InvocationParameter parameter : invocationParameters) {
            if (parameter.getDefaultValue() != null)
                if (parameter.getMapping() == null) {
                    resultDataSet.put(parameter.getId(), contextProcessor.resolve(parameter.getDefaultValue()));
                }
        }
    }

    protected DataSet resolveInValues(Map<String, String> inMapping,
                                      Collection<? extends InvocationParameter> invocationParameters,
                                      DataSet inDataSet) {
        if (invocationParameters == null)
            return inDataSet;

        for (InvocationParameter parameter : invocationParameters) {
            resolveDefaultValue(parameter, inDataSet);
            resolveMappingCondition(parameter, inMapping, inDataSet);
            if (!inMapping.containsKey(parameter.getId())) continue;

            if (parameter instanceof N2oObject.Parameter
                    && parameter.getEntityClass() != null
                    && ((N2oObject.Parameter) parameter).getChildParams() != null) {

                if (parameter.getPluralityType() == PluralityType.list
                        || parameter.getPluralityType() == PluralityType.set) {
                    for (Object dataSet : (Collection) inDataSet.get(parameter.getId())) {
                        ((DataSet) dataSet).putAll(
                                normalize(Arrays.asList(((N2oObject.Parameter) parameter).getChildParams()), (DataSet) dataSet)
                        );
                    }
                } else {
                    inDataSet.put(
                            parameter.getId(),
                            normalize(Arrays.asList(((N2oObject.Parameter) parameter).getChildParams()), (DataSet) inDataSet.get(parameter.getId()))
                    );
                }
                MappingProcessor.mapParameter(parameter, inDataSet);
            }
        }

        return normalize(invocationParameters, inDataSet);
    }

    public void resolveDefaultValue(InvocationParameter inParameter, DataSet inDataSet) {
        if (inParameter != null && inParameter.getDefaultValue() != null) {
            if (inDataSet.get(inParameter.getId()) == null) {
                Object value = DomainProcessor.getInstance().doDomainConversion(inParameter.getDomain(), inParameter.getDefaultValue());
                value = contextProcessor.resolve(value);
                inDataSet.put(inParameter.getId(), value);
            }
        }
        if (inParameter instanceof N2oObject.Parameter &&
                ((N2oObject.Parameter) inParameter).getChildParams() != null) {
            for (InvocationParameter childParam : ((N2oObject.Parameter) inParameter).getChildParams()) {
                if (inParameter.getPluralityType() == PluralityType.list
                        || inParameter.getPluralityType() == PluralityType.set) {
                    for (Object dataSet : (Collection) inDataSet.get(inParameter.getId())) {
                        resolveDefaultValue(childParam, (DataSet) dataSet);
                    }
                } else {
                    resolveDefaultValue(childParam, (DataSet) inDataSet.get(inParameter.getId()));
                }
            }
        }
    }

    public DataSet normalize(Collection<? extends InvocationParameter> invocationParameters, DataSet inDataSet) {
        DataSet copiedDataSet = new DataSet(inDataSet);
        for (InvocationParameter parameter : invocationParameters) {
            if (parameter.getNormalize() != null) {
                Object value = inDataSet.get(parameter.getId());
                if (value != null) {
                    value = normalizeValue(value, parameter.getNormalize(), inDataSet, parser);
                    copiedDataSet.put(parameter.getId(), value);
                }
            }
        }
        return copiedDataSet;
    }

    private void resolveMappingCondition(InvocationParameter inParam,
                                         Map<String, String> inMapping,
                                         DataSet inDataSet) {
        boolean unmappable = inParam.getNullIgnore() != null && inParam.getNullIgnore() && inDataSet.get(inParam.getId()) == null
                || inParam.getMappingCondition() != null && !ScriptProcessor.evalForBoolean(inParam.getMappingCondition(), inDataSet);
        if (unmappable) {
            inMapping.remove(inParam.getId());
        }
    }
}
