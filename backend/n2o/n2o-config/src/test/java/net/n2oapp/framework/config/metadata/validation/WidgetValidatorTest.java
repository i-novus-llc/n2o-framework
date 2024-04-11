package net.n2oapp.framework.config.metadata.validation;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.metadata.validation.standard.page.BasePageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.PageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.StandardPageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.FormValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.TableValidator;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование валидации виджета
 */
class WidgetValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(
                new N2oPagesPack(),
                new N2oRegionsPack(),
                new N2oWidgetsPack(),
                new N2oAllDataPack()
        );
        builder.validators(
                new PageValidator(),
                new StandardPageValidator(),
                new BasePageValidator(),
                new FormValidator(),
                new TableValidator()
        );
        builder.sources(
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.object.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank2.object.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/validation/widget/testWidgetValidator.query.xml")
        );
    }

    /**
     * Проверяется, уникальность кнопок тулбара виджета
     */
    @Test
    void testUniqueMenuItemsId() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/widget/testUniqueMenuItemsId.widget.xml"));
        assertEquals("Кнопка 'id2' встречается более чем один раз в виджете 'testUniqueMenuItemsId'", exception.getMessage());
    }

    /**
     * Проверяется, что префильтр ссылается через ref-widget на существующий виджет
     */
    @Test
    void testRequiredReferenceForPrefiltersFound() {
        validate("net/n2oapp/framework/config/metadata/validation/widget/testWidgetPreFilters2.widget.xml");
    }

    /**
     * Проверяется, что учитываются параметры выборки
     */
    @Test
    void testQueryParams() {
        validate("net/n2oapp/framework/config/metadata/validation/widget/testWidgetPreFilters8.widget.xml");
    }

    /**
     * Проверяется наличия идентификатора действия у виджета
     */
    @Test
    void testActionIdExistence() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/widget/testActionIdExistence.page.xml"));
        assertEquals("Не задан 'id' у <action> виджета 'testActionIdExistence'", exception.getMessage());
    }

    /**
     * Проверяется уникальность идентификаторов действий виджета
     */
    @Test
    void testActionIdsUnique() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/widget/testActionIdsUnique.widget.xml"));
        assertEquals("Действие 'test' встречается более чем один раз в метаданной виджета 'testActionIdsUnique'", exception.getMessage());
    }

    /**
     * Проверяется уникальность идентификаторов действий страницы и виджета
     */
    @Test
    void testPageAndWidgetActionIdsUnique() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/widget/testPageAndWidgetActionIdsUnique.page.xml"));
        assertEquals("Идентификатор действия 'test' дублируется на странице и в виджете 'main'", exception.getMessage());
    }

    @Test
    void testPaginationShowLastAndCount() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/widget/testPaginationShowLastAndCount.widget.xml"));
        assertEquals("Используется некорректная комбинация атрибутов 'show-last=\"false\"' и 'show-count=\"always\"' пагинации виджета 'testPaginationShowLastAndCount'", exception.getMessage());
    }

    @Test
    void testEmptyDependency() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/widget/testEmptyDependency.page.xml"));
        assertEquals("Зависимость виджета 'testEmptyDependency' имеет пустое тело", exception.getMessage());
    }

    @Test
    void widgetHasNotExistingDatasource() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/widget/widgetHasNotExistingDatasource.page.xml")
        );
        assertEquals("Виджет 'w1' cсылается на несуществующий источник данных 'ds2'", exception.getMessage());
    }

    @Test
    void widgetHasTwoDifferentDatasources() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/widget/widgetHasTwoDifferentDatasources.page.xml")
        );
        assertEquals("Виджет 'w1' использует внутренний источник и ссылку на источник данных одновременно", exception.getMessage());
    }

    @Test
    void testEmptyToolbar() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/widget/testEmptyToolbar.page.xml")
        );
        assertEquals("Не заданы элементы или атрибут 'generate' в тулбаре виджета 'test'", exception.getMessage());
    }
}
