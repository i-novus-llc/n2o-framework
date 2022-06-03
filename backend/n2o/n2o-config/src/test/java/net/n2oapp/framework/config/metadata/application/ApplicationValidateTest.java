package net.n2oapp.framework.config.metadata.application;


import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.application.ApplicationIO;
import net.n2oapp.framework.config.metadata.compile.application.ApplicationIOv3;
import net.n2oapp.framework.config.metadata.compile.application.ApplicationValidator;
import net.n2oapp.framework.config.metadata.compile.application.sidebar.SidebarIOv3;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Тест валидации приложения
 */
public class ApplicationValidateTest extends SourceValidationTestBase {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.ios(new ApplicationIO(), new ApplicationIOv3(), new SidebarIOv3());
        builder.validators(new ApplicationValidator());
        builder.sources(new CompileInfo("menuForHeaderValidation", N2oSimpleMenu.class));
    }

    @Test
    public void testHeaderValidation() {
        validate("net/n2oapp/framework/config/metadata/application/applicationHeaderValidate.application.xml");
    }

    @Test(expected = N2oMetadataValidationException.class)
    public void testHeaderValidationFail() {
        validate("net/n2oapp/framework/config/metadata/application/applicationHeaderValidateFail.application.xml");
    }
}
