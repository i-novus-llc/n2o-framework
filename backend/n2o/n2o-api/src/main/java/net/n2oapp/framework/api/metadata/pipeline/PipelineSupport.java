package net.n2oapp.framework.api.metadata.pipeline;

/**
 * Класс поддержки для создания конвейеров сборки метаданных
 */
public interface PipelineSupport {
    /**
     * Получить конвейер на этапе считывания метаданных
     *
     * @return Конвейер считывания метаданных
     */
    ReadTerminalPipeline<ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline>> read();

    /**
     * Получить конвейер на этапе сборки метаданных.
     *
     * @return Конвейер сборки метаданных
     */
    CompileTerminalPipeline<CompileBindTerminalPipeline> compile();

    /**
     * Получить конвейер на этапе слияния метаданных.
     *
     * @return Конвейер сборки метаданных
     */
    CompilePipeline merge();

    /**
     * Получить конвейер на этапе связывания метаданных с данными
     *
     * @return Конвейер связывания метаданных с данными
     */
    BindTerminalPipeline bind();

    /**
     * Получить конвейер на этапе сохранения метаданных
     *
     * @return Конвейер сохранения метаданных
     */
    PersistTerminalPipeline persist();

    /**
     * Получить конвейер на этапе десериализации метаданных
     *
     * @return Конвейер десериализации метаданных
     */
    DeserializeTerminalPipeline<DeserializePersistTerminalPipeline> deserialize();
}
