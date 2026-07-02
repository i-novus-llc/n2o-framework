package net.n2oapp.framework.config.metadata.validation.widget;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.validation.standard.control.FieldValidator;
import net.n2oapp.framework.config.metadata.validation.standard.fieldset.FieldSetColValidator;
import net.n2oapp.framework.config.metadata.validation.standard.fieldset.FieldSetRowValidator;
import net.n2oapp.framework.config.metadata.validation.standard.fieldset.MultiFieldSetValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.StandardPageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.MultiFormValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование валидации виджета
 */
class MultiFormValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(
                new N2oAllDataPack(),
                new N2oAllPagesPack()
        );
        builder.validators(
                new StandardPageValidator(),
                new MultiFormValidator(),
                new FieldSetRowValidator(), new FieldSetColValidator(),
                new MultiFieldSetValidator(), new FieldValidator()
        );
    }

    @Test
    void testEmptyCol() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/widget/multi_form/testEmptyCol.widget.xml"));
        assertEquals("Для <сol> виджета 'testEmptyCol' необходимо задать поля, либо же атрибут 'size'", exception.getMessage());
    }

    @Test
    void testEmptyColValid() {
        validate("net/n2oapp/framework/config/metadata/validation/widget/multi_form/testEmptyColValid.widget.xml");
    }

    @Test
    void testEmptyRow() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/widget/multi_form/testEmptyRow.widget.xml"));
        assertEquals("Для <row> виджета 'testEmptyRow' необходимо задать поля", exception.getMessage());
    }

}
