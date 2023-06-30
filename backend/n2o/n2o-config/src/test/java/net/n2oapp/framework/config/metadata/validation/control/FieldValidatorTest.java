package net.n2oapp.framework.config.metadata.validation.control;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oControlsPack;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.metadata.validation.standard.control.FieldValidator;
import net.n2oapp.framework.config.metadata.validation.standard.datasource.StandardDatasourceValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.BasePageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.PageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.StandardPageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.FormValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.WidgetValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование валидации страницы
 */
public class FieldValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
        builder.validators(new PageValidator(), new StandardPageValidator(), new WidgetValidator(), new BasePageValidator(),
                new StandardDatasourceValidator(), new FormValidator(), new FieldValidator());
    }

    @Test
    void testUniqueDependenciesType() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/field/testUniqueDependenciesType.widget.xml"));
        assertEquals("В поле 'field2' повторяются зависимости одного типа", exception.getMessage());
    }

    @Test
    void testDefaultValueNotNull() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/field/testDefaultValueNotNull.widget.xml"));
        assertEquals("У поля 'field1' атрибут default-value не является ссылкой или не задан: 'null'", exception.getMessage());
    }

    @Test
    void testDefaultValueNotNullForInterval() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/field/testDefaultValueNotNullForInterval.widget.xml"));
        assertEquals("У поля 'field1' default-value не задан", exception.getMessage());
    }

    @Test
    void testDefaultValueIsLinkForList() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/field/testDefaultValueIsLinkForList.widget.xml"));
        assertEquals("У поля 'field1' default-value не является ссылкой", exception.getMessage());
    }

    @Test
    void testDefaultValueIsLinkForInterval() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/field/testDefaultValueIsLinkForInterval.widget.xml"));
        assertEquals("У поля 'field1' default-value не является ссылкой", exception.getMessage());
    }

    @Test
    void testDefaultValue() {
        validate("net/n2oapp/framework/config/metadata/validation/field/testDefaultValue.widget.xml");
    }

    /**
     * Проверяется наличие источника данных виджета для поля c white-list валидацией
     */
    @Test
    void testWhiteListValidationWithoutDatasource() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/field/testWhiteListValidationWithoutDatasource.page.xml"));
        assertEquals("Для компиляции поля 'name' необходимо указать атрибут 'datasource' или ввести внутренний источник данных виджета 'main'", exception.getMessage());
    }

    /**
     * Проверяется наличие объекта источника данных виджета для поля c white-list валидацией
     */
    @Test
    void testWhiteListValidationWithDatasourceWithoutObject() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/field/testWhiteListValidationWithDatasourceWithoutObject.page.xml"));
        assertEquals("Для компиляции поля 'name' виджета 'main' необходимо указать объект источника данных 'ds1'", exception.getMessage());
    }


    /**
     * Проверяется наличие объекта внутреннего источника данных виджета для поля c white-list валидацией
     */
    @Test
    void testWhiteListValidationWithInlineDatasourceWithoutObject() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/field/testWhiteListValidationWithInlineDatasourceWithoutObject.page.xml"));
        assertEquals("Для компиляции поля 'name' виджета 'main' необходимо указать объект источника данных ", exception.getMessage());
    }

}
