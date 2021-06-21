package net.n2oapp.framework.config.metadata.application;


import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.application.ApplicationIO;
import net.n2oapp.framework.config.metadata.compile.application.ApplicationValidator;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.Before;
import org.junit.Test;

/**
 * Тест валидации приложения
 */
public class ApplicationValidateTest extends SourceValidationTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.ios(new ApplicationIO());
        builder.validators(new ApplicationValidator());
        builder.sources(new CompileInfo("menuForHeaderValidation", N2oSimpleMenu.class));
    }

    @Test
    public void testHeaderValidation() {
        validate("net/n2oapp/framework/config/metadata/application/applicationHeaderValidate.application.xml");
    }
}
