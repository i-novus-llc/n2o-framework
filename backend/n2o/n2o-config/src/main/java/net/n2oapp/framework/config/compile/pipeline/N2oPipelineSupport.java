package net.n2oapp.framework.config.compile.pipeline;

import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.pipeline.*;

/**
 * Реализация класса поддержки для создания конвееров сборки метаданных
 */
public class N2oPipelineSupport implements PipelineSupport {

    /**
     * Получить конвеер на этапе считывания метаданных
     *
     * @param env Окружение сборки
     * @return Конвеер считывания метаданных
     */
    public static ReadPipeline readPipeline(MetadataEnvironment env) {
        return new N2oReadPipeline(env);
    }

    /**
     * Получить конвеер на этапе сборки метаданных.
     * Считанные метаданные должны подаваться на вход.
     *
     * @param env Окружение сборки
     * @return Конвеер сборки метаданных
     */
    public static CompilePipeline compilePipeline(MetadataEnvironment env) {
        return new N2oCompilePipeline(env);
    }

    /**
     * Получить конвеер на этапе связывания метаданных с данными
     *
     * @param env Окружение сборки
     * @return Конвеер связывания метаданных с данными
     */
    public static BindPipeline bindPipeline(MetadataEnvironment env) {
        return new N2oBindPipeline(env);
    }

    private MetadataEnvironment env;

    public N2oPipelineSupport(MetadataEnvironment env) {
        this.env = env;
    }

    @Override
    public ReadTerminalPipeline<ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline>> read() {
        return readPipeline(env).read();
    }

    @Override
    public CompileTerminalPipeline<CompileBindTerminalPipeline> compile() {
        return compilePipeline(env).compile();
    }

    @Override
    public CompilePipeline merge() {
        return compilePipeline(env).merge();
    }

    @Override
    public BindTerminalPipeline bind() {
        return bindPipeline(env).bind();
    }
}
