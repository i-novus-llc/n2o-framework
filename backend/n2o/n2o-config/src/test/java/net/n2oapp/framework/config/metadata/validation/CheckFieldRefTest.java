package net.n2oapp.framework.config.metadata.validation;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oControlsPack;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.metadata.validation.standard.control.FieldValidator;
import net.n2oapp.framework.config.metadata.validation.standard.fieldset.SetFieldSetValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.FormValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CheckFieldRefTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
        builder.validators(new FormValidator(), new SetFieldSetValidator(), new FieldValidator());
    }

    @Test
    void testCheckDefaultValueIsNotDefined() {
        N2oMetadataValidationException exception = Assertions.assertThrows(N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/fieldRef/testCheckDefaultValueIsNotDefined.widget.xml"));
        Assertions.assertEquals("У поля 'id' атрибут default-value не является ссылкой или не задан: 'null'", exception.getMessage());
    }

    @Test
    void testCheckDefaultValueIsNotLink() {
        N2oMetadataValidationException exception = Assertions.assertThrows(N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/fieldRef/testCheckDefaultValueIsNotLink.widget.xml"));
        Assertions.assertEquals("У поля 'id' атрибут default-value не является ссылкой или не задан: 'test'", exception.getMessage());
    }
}
