package net.n2oapp.framework.config.test;

import net.n2oapp.framework.config.selective.CompileInfo;

/**
 * Базовый класс для тестирования валидаций
 */
public abstract class SourceValidationTestBase extends N2oTestBase {

    protected void validate(String pathXml) {
        CompileInfo info = new CompileInfo(pathXml);
        builder.sources(info).read().validate().get(info.getId(), info.getBaseSourceClass());
    }

}
