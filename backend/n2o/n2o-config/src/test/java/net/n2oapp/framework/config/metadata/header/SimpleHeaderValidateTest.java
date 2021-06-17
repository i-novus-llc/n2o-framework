package net.n2oapp.framework.config.metadata.header;


import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.header.SimpleHeaderIOv2;
import net.n2oapp.framework.config.metadata.compile.header.SimpleHeaderValidator;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.Before;
import org.junit.Test;

/**
 * Тест валидации простого хедера
 */
public class SimpleHeaderValidateTest extends SourceValidationTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.ios(new SimpleHeaderIOv2());
        builder.validators(new SimpleHeaderValidator());
        builder.sources(new CompileInfo("menuForHeaderValidation", N2oSimpleMenu.class));
    }

    @Test
    public void testHeaderValidation() {
        validate("net/n2oapp/framework/config/metadata/header/headerValidator.header.xml");
    }
}
