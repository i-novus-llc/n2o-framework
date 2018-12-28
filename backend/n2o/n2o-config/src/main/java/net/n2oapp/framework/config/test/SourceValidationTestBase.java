package net.n2oapp.framework.config.test;

import net.n2oapp.framework.api.metadata.local.CompilerHolder;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.CompileProcessorAdapter;
import net.n2oapp.framework.config.metadata.compile.N2oCompileProcessor;
import net.n2oapp.framework.config.selective.CompileInfo;

/**
 * Базовый класс для тестирования валидаций
 */
public abstract class SourceValidationTestBase extends N2oTestBase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        new CompilerHolder(new CompileProcessorAdapter(new N2oCompileProcessor(builder.getEnvironment()),
                builder.getEnvironment().getMetadataRegister()));
    }

    protected void validate(String pathXml) {
        CompileInfo info = new CompileInfo(pathXml);
        builder.sources(info).read().validate().get(info.getId(), info.getBaseSourceClass());
    }

}
