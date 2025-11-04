package net.n2oapp.framework.config.metadata.validation.widget;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.metadata.validation.standard.widget.TableValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.columns.FilterColumnValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TableValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(
                new N2oPagesPack(),
                new N2oRegionsPack(),
                new N2oWidgetsPack(),
                new N2oAllDataPack(),
                new N2oControlsPack()
        );
        builder.validators(
                new TableValidator(),
                new FilterColumnValidator()
        );
    }

    @Test
    void testUniqueColumnsTableSetting() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/widget/testColumnsTableSetting.widget.xml")
        );
        assertEquals("В таблице 'testColumnsTableSetting' найдено несколько элементов <ts:columns/>. Допускается только один элемент.", exception.getMessage());
    }

    @Test
    void testUniqueExportTableSetting() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/widget/testExportTableSetting.widget.xml")
        );
        assertEquals("В таблице 'testExportTableSetting' найдено несколько элементов <ts:export/>. Допускается только один элемент.", exception.getMessage());
    }

    @Test
    void testExportTableSettingDefaultFormat() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/widget/testDefaultFormat.widget.xml")
        );
        assertEquals("В таблице 'testDefaultFormat' в элементе <ts:export/> значение default-format=\"xlsx\" не содержится в списке format", exception.getMessage());
    }

    @Test
    void testDuplicateColumnIds() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/widget/testDuplicateColumnIds.widget.xml")
        );
        assertEquals("Таблица 'testDuplicateColumnIds' содержит повторяющиеся значения id=\"id1\" в <column>", exception.getMessage());
    }

    @Test
    void testDuplicateColumnTextFieldId() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/widget/testDuplicateColumnTextFieldId.widget.xml")
        );
        assertEquals("Таблица 'testDuplicateColumnTextFieldId' содержит повторяющиеся значения text-field-id=\"c1\" в <column>", exception.getMessage());
    }

    @Test
    void testOverlayToolbar() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/widget/testOverlayToolbar.widget.xml")
        );
        assertEquals("Не заданы элементы или атрибут 'generate' в тулбаре в <overlay> таблицы 'testOverlayToolbar'", exception.getMessage());
    }

    @Test
    void testFilterFetchOnAttributes() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/widget/testFilterFetchOnAttributes.page.xml")
        );
        assertEquals("В фильтрах таблицы 'testFilterFetchOnAttributes' заданы несочетаемые атрибуты 'fetch-on-change=\"true\"' и 'fetch-on-clear=\"false\"'",
                exception.getMessage());
    }

    @Test
    void testFilterColumnFilterExistence() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/widget/columns/testFilterColumnFilterExistence.page.xml")
        );
        assertEquals("В <filter-column text-field-id='test'> таблицы не задан <filter>", exception.getMessage());
    }

    @Test
    void testInvalidLeftFixedColumns() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/widget/columns/testInvalidLeftFixed.widget.xml")
        );
        assertEquals("В таблице 'testInvalidLeftFixed' колонка 'Возраст' c 'fixed=\"left\"' не может находиться после нефиксированных слева колонок",
                exception.getMessage());
    }

    @Test
    void testInvalidRightFixedColumns() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/widget/columns/testInvalidRightFixed.widget.xml")
        );
        assertEquals("В таблице 'testInvalidRightFixed' колонка 'Возраст' c 'fixed=\"right\"' не может находиться перед нефиксированными справа колонками",
                exception.getMessage());
    }

    @Test
    void testMixedFixedColumns() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/widget/columns/testMixedFixed.widget.xml")
        );
        assertEquals("В таблице 'testMixedFixed' колонка 'Зарплата' c 'fixed=\"left\"' не может находиться после нефиксированных слева колонок",
                exception.getMessage());
    }

    @Test
    void testFixedOnlyOnFirstLevelColumns() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/widget/columns/testFixedOnlyOnFirstLevel.widget.xml")
        );
        assertEquals("В таблице 'testFixedOnlyOnFirstLevel' колонка 'Фамилия' не может иметь атрибут 'fixed', так как находится внутри <multi-column> 'ФИО'",
                exception.getMessage());
    }
}