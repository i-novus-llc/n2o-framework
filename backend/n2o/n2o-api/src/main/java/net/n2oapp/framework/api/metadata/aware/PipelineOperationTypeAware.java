package net.n2oapp.framework.api.metadata.aware;

import net.n2oapp.framework.api.metadata.pipeline.PipelineOperationType;

/**
 * Знание о типе операции в конвейере по сборке метаданных
 */
public interface PipelineOperationTypeAware {
    /**
     * Получить тип операции
     *
     * @return Тип операции
     */
    PipelineOperationType getPipelineOperationType();
}
