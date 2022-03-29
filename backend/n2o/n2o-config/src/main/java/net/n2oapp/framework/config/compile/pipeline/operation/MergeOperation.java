package net.n2oapp.framework.config.compile.pipeline.operation;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware;
import net.n2oapp.framework.api.metadata.aware.PipelineOperationTypeAware;
import net.n2oapp.framework.api.metadata.aware.RefIdAware;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.SourceMergerFactory;
import net.n2oapp.framework.api.metadata.pipeline.*;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;

import java.util.function.Supplier;

/**
 * Операция слияния метаданных в конвейере
 *
 * @param <S> Тип исходной метаданной
 */
public class MergeOperation<S> implements PipelineOperation<S, S>, PipelineOperationTypeAware, MetadataEnvironmentAware {

    private SourceMergerFactory sourceMergerFactory;

    /**
     * Конструктор с отложенной инициализацией на {@link #setEnvironment}
     */
    public MergeOperation() {
    }

    /**
     * Конструктор с непосредственной инициализацией
     *
     * @param sourceMergerFactory Фабрика слияний
     */
    public MergeOperation(SourceMergerFactory sourceMergerFactory) {
        this.sourceMergerFactory = sourceMergerFactory;
    }

    @Override
    public S execute(CompileContext<?, ?> context, DataSet data, Supplier<S> supplier, CompileProcessor compileProcessor,
                     BindProcessor bindProcessor,
                     SourceProcessor sourceProcessor) {
        S override = supplier.get();
        if (override instanceof RefIdAware && ((RefIdAware) override).getRefId() != null) {
            String refId = ((RefIdAware) override).getRefId();
            if (refId != null) {
                S source = (S) compileProcessor.getSource(refId, (Class<SourceMetadata>)override.getClass());
                return sourceMergerFactory.merge(source, override);
            }
        }
        return override;
    }

    @Override
    public void setEnvironment(MetadataEnvironment environment) {
        this.sourceMergerFactory = environment.getSourceMergerFactory();
    }

    @Override
    public PipelineOperationType getPipelineOperationType() {
        return PipelineOperationType.MERGE;
    }
}
