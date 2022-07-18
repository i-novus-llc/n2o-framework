package net.n2oapp.framework.config.metadata.application.sidebar;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.application.ApplicationIO;
import net.n2oapp.framework.config.io.application.ApplicationIOv3;
import net.n2oapp.framework.config.metadata.compile.application.ApplicationValidator;
import net.n2oapp.framework.config.io.application.sidebar.SidebarIOv3;
import net.n2oapp.framework.config.metadata.compile.application.sidebar.SidebarValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Тест валдиции боковй панели приложения
 */
public class SidebarValidatorTest extends SourceValidationTestBase {

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
        builder.validators(new ApplicationValidator(), new SidebarValidator());
    }

    @Test
    public void testValidation() {
        validate("net/n2oapp/framework/config/metadata/application/sidebar/validation.application.xml");
    }

    @Test
    public void testValidationFail() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Menu test doesnt exists for sidebar");
        validate("net/n2oapp/framework/config/metadata/application/sidebar/validationFail.application.xml");
    }

    @Test
    public void testEmptyPathValidationFail() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("More than one sidebar does not contain a path");
        validate("net/n2oapp/framework/config/metadata/application/sidebar/emptyPathValidationFail.application.xml");
    }

    @Test
    public void testEqualPathsValidationFail() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("The /persons path is already taken by one of the sidebars");
        validate("net/n2oapp/framework/config/metadata/application/sidebar/equalPathsValidationFail.application.xml");
    }
}
