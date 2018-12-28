package net.n2oapp.framework.api.metadata.pipeline;

/**
 * Класс поддержки для создания конвееров сборки метаданных
 */
public interface PipelineSupport {
    /**
     * Получить конвеер на этапе считывания метаданных
     *
     * @return Конвеер считывания метаданных
     */
    ReadTerminalPipeline<ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline>> read();

    /**
     * Получить конвеер на этапе сборки метаданных.
     *
     * @return Конвеер сборки метаданных
     */
    CompileTerminalPipeline<CompileBindTerminalPipeline> compile();

    /**
     * Получить конвеер на этапе слияния метаданных.
     *
     * @return Конвеер сборки метаданных
     */
    CompilePipeline merge();

    /**
     * Получить конвеер на этапе связывания метаданных с данными
     *
     * @return Конвеер связывания метаданных с данными
     */
    BindTerminalPipeline bind();
}
