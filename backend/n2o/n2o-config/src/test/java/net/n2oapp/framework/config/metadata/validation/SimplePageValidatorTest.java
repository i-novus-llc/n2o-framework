package net.n2oapp.framework.config.metadata.validation;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.validation.standard.page.PageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.SimplePageValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class SimplePageValidatorTest extends SourceValidationTestBase {

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
        builder.packs(new N2oPagesPack());
        builder.validators(new PageValidator(), new SimplePageValidator());
    }

    @Test
    public void testMissingWidget() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Не задан виджет простой страницы");
        validate("net/n2oapp/framework/config/metadata/validation/page/simple/testMissingWidget.page.xml");
    }
}
