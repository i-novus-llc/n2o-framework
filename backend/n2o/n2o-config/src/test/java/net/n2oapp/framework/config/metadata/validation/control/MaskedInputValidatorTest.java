package net.n2oapp.framework.config.metadata.validation.control;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.metadata.validation.standard.control.FieldValidator;
import net.n2oapp.framework.config.metadata.validation.standard.control.MaskedInputValidator;
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

class MaskedInputValidatorTest extends SourceValidationTestBase {

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
                new StandardDatasourceValidator(), new FormValidator(), new FieldValidator(), new MaskedInputValidator());
    }

    @Test
    void testEmptyMask() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/field/maskedInput/testEmptyMask.widget.xml"));
        assertEquals("В поле 'phone' виджета 'testEmptyMask' не задан обязательный атрибут 'mask'",
                exception.getMessage());
    }

    @Test
    void testValidMask() {
        validate("net/n2oapp/framework/config/metadata/validation/field/maskedInput/testValidMask.widget.xml");
    }
}