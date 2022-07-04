package net.n2oapp.framework.config.metadata.validation;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class SubmitActionValidateTest extends SourceValidationTestBase {

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
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(),
                new N2oActionsPack(), new N2oCellsPack(), new N2oObjectsPack(), new N2oAllValidatorsPack());
    }

    @Test
    public void testValidSubmitDatasource() {
        validate("/net/n2oapp/framework/config/metadata/validation/action/testSubmitActionValidationDatasourceExistAndSupportSubmit.page.xml");
    }

    @Test
    public void testMissingDatasource() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("The attribute 'datasource' refers to a non-existent datasource");
        validate("/net/n2oapp/framework/config/metadata/validation/action/testSubmitActionValidationNoDatasource.page.xml");
    }

    @Test
    public void testMissingSubmitInDatasource() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Submit tag is not specified in 'datasource'");
        validate("/net/n2oapp/framework/config/metadata/validation/action/testSubmitActionValidationMissingSubmit.page.xml");
    }

    @Test
    public void testMissingDatasourceAttribute() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Submit action does not specify 'datasource'");
        validate("/net/n2oapp/framework/config/metadata/validation/action/testSubmitActionValidationMissingAttribute.page.xml");
    }

    @Test
    public void testUnsupportedDatasource() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("The datasource doesn't support submit action");
        validate("/net/n2oapp/framework/config/metadata/validation/action/testSubmitActionValidationUnsupport.page.xml");
    }

}
