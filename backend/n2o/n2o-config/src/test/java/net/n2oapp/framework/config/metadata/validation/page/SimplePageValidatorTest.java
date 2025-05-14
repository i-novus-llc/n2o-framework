package net.n2oapp.framework.config.metadata.validation.page;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.metadata.validation.standard.page.PageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.SimplePageValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SimplePageValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oWidgetsPack());
        builder.validators(new PageValidator(), new SimplePageValidator());
    }

    @Test
    void testMissingWidget() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/page/simple/testMissingWidget.page.xml"));
        assertEquals("Не задан виджет простой страницы 'testMissingWidget'", exception.getMessage());
    }

    @Test
    void testWidgetIdMatchDatasourceId() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/page/simple/testWidgetIdMatchDatasourceId.page.xml"));
        assertEquals("Идентификатор виджета 'ds' не должен использоваться в качестве идентификатора источника данных", exception.getMessage());
    }

    @Test
    void testWidgetDatasourceWithId() {
        validate("net/n2oapp/framework/config/metadata/validation/page/simple/testWidgetDatasourceWithId.page.xml");
    }
}
