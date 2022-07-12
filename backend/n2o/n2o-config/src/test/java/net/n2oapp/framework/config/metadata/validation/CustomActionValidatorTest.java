package net.n2oapp.framework.config.metadata.validation;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oActionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.validation.standard.action.CustomActionValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.BasePageValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.Before;
import org.junit.Test;

/**
 * Тестирование валидации действия вызова операции
 */
public class CustomActionValidatorTest extends SourceValidationTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oActionsPack());
        builder.validators(new BasePageValidator(), new CustomActionValidator());
    }


    @Test(expected = N2oMetadataValidationException.class)
    public void testRefreshNonExistentDatasource() {
        validate("net/n2oapp/framework/config/metadata/validation/action/custom/testRefreshNonExistentDatasource.page.xml");
    }
}
