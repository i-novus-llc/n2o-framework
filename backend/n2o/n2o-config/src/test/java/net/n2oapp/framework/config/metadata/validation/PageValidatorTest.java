package net.n2oapp.framework.config.metadata.validation;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
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
public class PageValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack());
        builder.validators(new PageValidator(), new BasePageValidator());
    }

    @Test
    void testObjectNotExists() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/page/testObjectNotExists.page.xml"),
                "Страница testObjectNotExists ссылается на несуществующий объект nonExistantObjectId"
        );
    }

    @Test
    void testObjectNotExistsOnSimplePage() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/page/testObjectNotExistsOnSimplePage.page.xml"));
        assertEquals(exception.getMessage(), "Страница testObjectNotExistsOnSimplePage ссылается на несуществующий объект nonExistantObjectId");
    }

    @Test
    void testDependsWidgetFind() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/page/testDependsWidgetFind.page.xml"));
        assertEquals(exception.getMessage(), "Атрибут depends-on ссылается на несуществующий виджет main");
    }

    @Test
    void testDatasourcesIdUnique() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/page/testDatasourcesIdUnique.page.xml"));
        assertEquals(exception.getMessage(), "Источник данных ds1 встречается более чем один раз в метаданной страницы testDatasourcesIdUnique");
    }

    @Test
    void testWidgetIds() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/page/testWidgetIds.page.xml"));
        assertEquals(exception.getMessage(), "Идентификатор виджета 'ds1' уже используется источником данных");
    }

    @Test
    void testWidgetDatasource() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/page/testWidgetDatasource.page.xml"));
        assertEquals(exception.getMessage(), "Виджет 'w1' одновременно ссылается на источник данных 'ds1' и имеет свой источник данных");
    }
}
