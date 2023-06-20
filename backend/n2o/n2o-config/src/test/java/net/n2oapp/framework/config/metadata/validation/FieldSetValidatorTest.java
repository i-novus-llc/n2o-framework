package net.n2oapp.framework.config.metadata.validation;


import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oControlsPack;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.metadata.validation.standard.control.FieldValidator;
import net.n2oapp.framework.config.metadata.validation.standard.fieldset.*;
import net.n2oapp.framework.config.metadata.validation.standard.widget.FormValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование валидации филдсетов
 */
public class FieldSetValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
        builder.validators(new FormValidator(), new FieldSetRowValidator(), new FieldSetColumnValidator(),
                new SetFieldSetValidator(), new LineFieldSetValidator(),
                new MultiFieldSetValidator(), new FieldValidator());
    }

    @Test
    void testUniqueFieldId() {
        validate("net/n2oapp/framework/config/metadata/validation/fieldset/testUniqueFieldId.widget.xml");
    }

    @Test
    void testNonUniqueFieldId() {
        validate("net/n2oapp/framework/config/metadata/validation/fieldset/testNonUniqueFieldId.widget.xml");
    }

    @Test
    void testNonUniqueFieldIdWithDependencies() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/fieldset/testNonUniqueFieldIdWithDependencies.widget.xml"),
                "Поле test1 встречается более одного раза"
        );
    }

    @Test
    void testNonUniqueFieldIdWithDependenciesWithMultiSet() {
        validate("net/n2oapp/framework/config/metadata/validation/fieldset/testNonUniqueFieldIdWithDependencies2.widget.xml");
    }

    @Test
    void testNonUniqueFieldIdWithDependenciesInMultiSet() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/fieldset/testNonUniqueFieldIdInMultiSet.widget.xml"),
                "Поле test1 встречается более одного раза"
        );
    }
}
