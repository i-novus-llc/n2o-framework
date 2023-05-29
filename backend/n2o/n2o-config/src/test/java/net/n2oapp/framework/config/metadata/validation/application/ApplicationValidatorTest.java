package net.n2oapp.framework.config.metadata.validation.application;

import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.application.ApplicationIOv3;
import net.n2oapp.framework.config.metadata.compile.application.ApplicationValidator;
import net.n2oapp.framework.config.io.application.sidebar.SidebarIOv3;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тест валидатора приложения
 */
public class ApplicationValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.ios(new ApplicationIOv3(), new SidebarIOv3());
        builder.validators(new ApplicationValidator());
        builder.sources(new CompileInfo("menuForHeaderValidation", N2oSimpleMenu.class));
    }

    @Test
    void testHeaderValidation() {
        validate("net/n2oapp/framework/config/metadata/application/applicationHeaderValidate.application.xml");
    }

    @Test
    void testHeaderValidationFail() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/application/applicationHeaderValidateFail.application.xml")
        );
    }
}
