package net.n2oapp.framework.config.compile.pipeline;

import net.n2oapp.engine.factory.EngineNotFoundException;
import net.n2oapp.engine.factory.EngineNotUniqueException;
import net.n2oapp.engine.factory.integration.spring.OverrideBean;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.factory.MetadataFactory;
import net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware;
import net.n2oapp.framework.api.metadata.aware.PipelineOperationTypeAware;
import net.n2oapp.framework.api.metadata.pipeline.PipelineOperation;
import net.n2oapp.framework.api.metadata.pipeline.PipelineOperationFactory;
import net.n2oapp.framework.api.metadata.pipeline.PipelineOperationType;
import net.n2oapp.framework.config.factory.AwareFactorySupport;
import net.n2oapp.framework.config.factory.BaseMetadataFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Реализация фабрики операций в конвейере по сборке метаданных
 */
public class N2oPipelineOperationFactory implements MetadataFactory<PipelineOperation>, PipelineOperationFactory,
        MetadataEnvironmentAware {

    private Map<PipelineOperationType, PipelineOperation> enginesMap = new HashMap<>();

    public N2oPipelineOperationFactory() {
        super();
    }

    public N2oPipelineOperationFactory(Map<String, PipelineOperation> beans) {
        enginesMap.putAll(OverrideBean.removeOverriddenBeans(beans).values()
                .stream().collect(Collectors.toMap(o -> ((PipelineOperationTypeAware)o).getPipelineOperationType(), o -> o)));
    }

    @Override
    public PipelineOperation<?, ?> produce(PipelineOperationType type) {
        PipelineOperation operation = enginesMap.get(type);
        if (operation == null)
            throw new EngineNotFoundException(type);
       return operation;
    }

    @Override
    public MetadataFactory<PipelineOperation> add(PipelineOperation... g) {
        Stream.of(g).forEach(o -> enginesMap.put(((PipelineOperationTypeAware)o).getPipelineOperationType(), o));
        return this;
    }

    @Override
    public void setEnvironment(MetadataEnvironment environment) {
        enginesMap.values().forEach(o -> AwareFactorySupport.enrich(o, environment));
    }
}
