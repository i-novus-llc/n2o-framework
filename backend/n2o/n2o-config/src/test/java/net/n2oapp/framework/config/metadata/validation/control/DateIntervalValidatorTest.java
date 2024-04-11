package net.n2oapp.framework.config.metadata.validation.control;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oControlsPack;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.metadata.validation.standard.control.DateIntervalValidator;
import net.n2oapp.framework.config.metadata.validation.standard.control.FieldValidator;
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

class DateIntervalValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
        builder.validators(new PageValidator(), new StandardPageValidator(), new BasePageValidator(),
                new StandardDatasourceValidator(), new FormValidator(), new FieldValidator(), new DateIntervalValidator());
    }

    @Test
    void testDefaultValueBegin() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/field/dateInterval/testDefValueBeginDateInterval.page.xml"));
        assertEquals("Значение 'begin' поля 'test' виджета 'testDefValueBeginDateInterval' должно иметь формат yyyy-MM-dd HH:mm:ss или yyyy-MM-dd", exception.getMessage());
    }

    @Test
    void testDefaultValueEnd() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/field/dateInterval/testDefValueEndDateInterval.page.xml"));
        assertEquals("Значение 'end' поля 'test' виджета 'testDefValueEndDateInterval' должно иметь формат yyyy-MM-dd HH:mm:ss или yyyy-MM-dd", exception.getMessage());
    }

    @Test
    void testMin() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/field/dateInterval/testMinDateInterval.page.xml"));
        assertEquals("Значение 'min' поля 'test' виджета 'testMinDateInterval' должно иметь формат yyyy-MM-dd HH:mm:ss или yyyy-MM-dd", exception.getMessage());
    }

    @Test
    void testMax() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/field/dateInterval/testMaxDateInterval.page.xml"));
        assertEquals("Значение 'max' поля 'test' виджета 'testMaxDateInterval' должно иметь формат yyyy-MM-dd HH:mm:ss или yyyy-MM-dd", exception.getMessage());
    }
}
