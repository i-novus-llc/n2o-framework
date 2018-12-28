package net.n2oapp.framework.api.metadata.pipeline;

import net.n2oapp.framework.api.factory.MetadataFactory;

/**
 * Фабрика операций по сборке метаданных в конвеере
 */
public interface PipelineOperationFactory extends MetadataFactory<PipelineOperation> {

    /**
     * Получить операцию по сборке метаданных в конвеере по её типу
     *
     * @param type Тип операции
     * @return Операция
     */
    PipelineOperation<?, ?> produce(PipelineOperationType type);


}
