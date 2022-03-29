package net.n2oapp.framework.config.compile.pipeline;

import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.pipeline.*;

/**
 * Реализация класса поддержки для создания конвейеров сборки метаданных
 */
public class N2oPipelineSupport implements PipelineSupport {

    /**
     * Получить конвейер на этапе считывания метаданных
     *
     * @param env Окружение сборки
     * @return Конвейер считывания метаданных
     */
    public static ReadPipeline readPipeline(MetadataEnvironment env) {
        return new N2oReadPipeline(env);
    }

    /**
     * Получить конвейер на этапе сборки метаданных.
     * Считанные метаданные должны подаваться на вход.
     *
     * @param env Окружение сборки
     * @return Конвейер сборки метаданных
     */
    public static CompilePipeline compilePipeline(MetadataEnvironment env) {
        return new N2oCompilePipeline(env);
    }

    /**
     * Получить конвейер на этапе связывания метаданных с данными
     *
     * @param env Окружение сборки
     * @return Конвейер связывания метаданных с данными
     */
    public static BindPipeline bindPipeline(MetadataEnvironment env) {
        return new N2oBindPipeline(env);
    }

    /**
     * Получить конвейер на этапе связывания метаданных с данными
     *
     * @param env Окружение сборки
     * @return Конвейер связывания метаданных с данными
     */
    public static PersistPipeline persistPipeline(MetadataEnvironment env) {
        return new N2oPersistPipeline(env);
    }

    /**
     * Получить конвейер на этапе связывания метаданных с данными
     *
     * @param env Окружение сборки
     * @return Конвейер связывания метаданных с данными
     */
    public static DeserializePipeline deserializePipeline(MetadataEnvironment env) {
        return new N2oDeserializePipeline(env);
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

    @Override
    public PersistTerminalPipeline persist() {
        return persistPipeline(env).persist();
    }

    @Override
    public DeserializeTerminalPipeline<DeserializePersistTerminalPipeline> deserialize() {
        return deserializePipeline(env).deserialize();
    }
}
