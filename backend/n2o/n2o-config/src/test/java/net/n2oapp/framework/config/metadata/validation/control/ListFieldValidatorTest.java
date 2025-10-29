package net.n2oapp.framework.config.metadata.validation.control;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.metadata.validation.standard.control.ListFieldValidator;
import net.n2oapp.framework.config.metadata.validation.standard.fieldset.FieldSetRowValidator;
import net.n2oapp.framework.config.metadata.validation.standard.fieldset.SetFieldSetValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.SimplePageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.FormValidator;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование валидатора списковых полей
 */
class ListFieldValidatorTest extends SourceValidationTestBase {
    
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllDataPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack(), new N2oPagesPack());
        builder.validators(new SimplePageValidator(), new FormValidator(), new FieldSetRowValidator(),
                new SetFieldSetValidator(), new ListFieldValidator());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank2.query.xml"));
    }

    @Test
    void testCheckForExistsQuery() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/field/list/checkForExistsQuery.widget.xml"));
        assertEquals("Поле 'auto' ссылается на несуществующую выборку 'unknown'", exception.getMessage());
    }

    @Test
    void testUsingQueryAndDatasourceAtTheSameTime() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/field/list/testUsingQueryAndDatasourceAtTheSameTime.widget.xml"));
        assertEquals("Поле 'select' использует выборку и ссылку на источник данных одновременно", exception.getMessage());
    }

    @Test
    void testUsingQueryAndOptionsAtTheSameTime() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/field/list/testUsingQueryAndOptionsAtTheSameTime.widget.xml"));
        assertEquals("Поле 'select' использует выборку и элемент '<options>' одновременно", exception.getMessage());
    }

    @Test
    void testUsingDatasourceAndOptionsAtTheSameTime() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/field/list/testUsingDatasourceAndOptionsAtTheSameTime.widget.xml"));
        assertEquals("Поле 'select' использует ссылку на источник данных и элемент '<options>' одновременно", exception.getMessage());
    }

    @Test
    void testDatasourceExistence() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/field/list/checkDatasourceExistence.page.xml"));
        assertEquals("Указан несуществующий источник данных 'test' для поля 'select' виджета 'form'", exception.getMessage());
    }

    @Test
    void testUsingDefaultValueAndDefaultValuesAtTheSameTime() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/field/list/testUsingDefaultValueAndDefaultValuesAtTheSameTime.widget.xml"));
        assertEquals("Поле 'select' использует элемент '<default-value>' и '<default-values>' одновременно", exception.getMessage());
    }

    @Test
    void testUsingDefaultValuesForSingleSelect() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/field/list/testUsingDefaultValuesForSingleSelect.widget.xml"));
        assertEquals("Поле 'select' должно использовать в single режиме элемент '<default-value>' вместо '<default-values>'", exception.getMessage());
    }
}
