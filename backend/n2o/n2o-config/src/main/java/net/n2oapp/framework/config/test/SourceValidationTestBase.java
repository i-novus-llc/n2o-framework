package net.n2oapp.framework.config.test;

import net.n2oapp.framework.config.N2oApplicationBuilder;
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
    }

    protected void validate(String pathXml) {
        CompileInfo info = new CompileInfo(pathXml);
        builder.sources(info).read().validate().get(info.getId(), info.getBaseSourceClass());
    }

}
