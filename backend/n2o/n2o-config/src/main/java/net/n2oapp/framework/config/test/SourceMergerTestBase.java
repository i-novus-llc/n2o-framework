package net.n2oapp.framework.config.test;

import net.n2oapp.framework.api.metadata.pipeline.ReadTerminalPipeline;
import net.n2oapp.framework.config.selective.CompileInfo;

/**
 * Базовый класс для тестирования слияний
 */
public abstract class SourceMergerTestBase extends N2oTestBase {

    protected ReadTerminalPipeline<?> merge(String sourceXml, String overrideXml) {
        return builder.sources(new CompileInfo(sourceXml), new CompileInfo(overrideXml))
                .read().merge();
    }

}
