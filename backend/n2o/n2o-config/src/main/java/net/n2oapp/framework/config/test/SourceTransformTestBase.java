package net.n2oapp.framework.config.test;

import net.n2oapp.framework.api.metadata.pipeline.ReadTerminalPipeline;
import net.n2oapp.framework.config.selective.CompileInfo;

public class SourceTransformTestBase extends N2oTestBase {

    protected ReadTerminalPipeline<?> transform(String uri) {
        return builder.sources(new CompileInfo(uri))
                .read().transform();
    }
}
