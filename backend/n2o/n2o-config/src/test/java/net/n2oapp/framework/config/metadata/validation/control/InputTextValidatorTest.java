package net.n2oapp.framework.config.metadata.validation.control;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oControlsPack;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.metadata.validation.standard.control.FieldValidator;
import net.n2oapp.framework.config.metadata.validation.standard.control.InputTextValidator;
import net.n2oapp.framework.config.metadata.validation.standard.datasource.StandardDatasourceValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.BasePageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.PageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.StandardPageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.FormValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InputTextValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
        builder.validators(new PageValidator(), new StandardPageValidator(), new BasePageValidator(),
                new StandardDatasourceValidator(), new FormValidator(), new FieldValidator(), new InputTextValidator());
    }

    @Test
    void testDefaultValueStringDomainInteger() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/field/inputText/testDefValueStringDomainInt.page.xml"));
        assertEquals("Значение 'default-value' не соответствует указанному 'domain=integer' для поля 'field1' виджета 'testDefValueStringDomainInt'", exception.getMessage());
    }

    @Test
    void testDefaultValueDoubleDomainInteger() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/field/inputText/testDefValueDoubleDomainInt.page.xml"));
        assertEquals("Значение 'default-value' не соответствует указанному 'domain=integer' для поля 'field1' виджета 'testDefValueDoubleDomainInt'", exception.getMessage());
    }

    @Test
    void testDefaultValueStringDomainNumeric() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/field/inputText/testDefValueStringDomainNumeric.page.xml"));
        assertEquals("Значение 'default-value' не соответствует указанному 'domain=numeric' для поля 'field1' виджета 'testDefValueStringDomainNumeric'", exception.getMessage());
    }

    @Test
    void testDefaultValueStringDomainShort() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/field/inputText/testDefValueStringDomainShort.page.xml"));
        assertEquals("Значение 'default-value' не соответствует указанному 'domain=short' для поля 'field1' виджета 'testDefValueStringDomainShort'", exception.getMessage());
    }

    @Test
    void testDefaultIntegerStringDomainByte() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/field/inputText/testDefValueIntDomainByte.page.xml"));
        assertEquals("Значение 'default-value' не соответствует указанному 'domain=byte' для поля 'field1' виджета 'testDefValueIntDomainByte'", exception.getMessage());
    }

    @Test
    void testStringStep() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/field/inputText/testStepString.page.xml"));
        assertEquals("Значение 'step' не соответствует указанному 'domain=integer' для поля 'field1' виджета 'testStepString'", exception.getMessage());
    }

    @Test
    void testDoubleStep() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/field/inputText/testStepDouble.page.xml"));
        assertEquals("Значение 'step' не соответствует указанному 'domain=integer' для поля 'field1' виджета 'testStepDouble'", exception.getMessage());
    }
}
