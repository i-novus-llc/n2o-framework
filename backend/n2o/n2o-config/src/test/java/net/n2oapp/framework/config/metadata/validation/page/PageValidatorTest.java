package net.n2oapp.framework.config.metadata.validation.page;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oActionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.metadata.validation.standard.page.BasePageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.PageValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование валидации страницы
 */
class PageValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack());
        builder.validators(new PageValidator(), new BasePageValidator());
    }

    @Test
    void testObjectNotExists() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/page/testObjectNotExists.page.xml"));
        assertEquals("Страница 'testObjectNotExists' ссылается на несуществующий объект 'nonExistantObjectId'", exception.getMessage());
    }

    @Test
    void testObjectNotExistsOnSimplePage() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/page/testObjectNotExistsOnSimplePage.page.xml"));
        assertEquals("Страница 'testObjectNotExistsOnSimplePage' ссылается на несуществующий объект 'nonExistantObjectId'", exception.getMessage());
    }

    @Test
    void testDependsWidgetFind() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/page/testDependsWidgetFind.page.xml"));
        assertEquals("Атрибут depends-on ссылается на несуществующий виджет 'main'", exception.getMessage());
    }

    @Test
    void testDatasourcesIdUnique() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/page/testDatasourcesIdUnique.page.xml")
        );
        assertEquals("Источник данных 'ds1' встречается более чем один раз в метаданной страницы 'testDatasourcesIdUnique'", exception.getMessage());
    }

    @Test
    void testWidgetIdMatchDatasourceId() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/page/testWidgetIdMatchDatasourceId.page.xml")
        );
        assertEquals("Идентификатор виджета 'ds1' не должен использоваться в качестве идентификатора источника данных", exception.getMessage());
    }

    @Test
    void testWidgetDatasourceWithId() {
        validate("net/n2oapp/framework/config/metadata/validation/page/testWidgetDatasourceWithId.page.xml");
    }

    @Test
    void testEmptyToolbar() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/page/testEmptyToolbar.page.xml")
        );
        assertEquals("Не заданы элементы или атрибут 'generate' в тулбаре страницы", exception.getMessage());
    }

    @Test
    void testOnFailAction() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/page/testOnFailAction.page.xml")
        );
        assertEquals("Не может быть более одного элемента <on-fail>", exception.getMessage());
    }
}
