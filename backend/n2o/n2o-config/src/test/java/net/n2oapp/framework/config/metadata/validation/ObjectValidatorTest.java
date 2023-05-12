package net.n2oapp.framework.config.metadata.validation;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oObjectsPack;
import net.n2oapp.framework.config.metadata.validation.standard.object.ObjectValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование валидации объектов
 */
public class ObjectValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oObjectsPack());
        builder.validators(new ObjectValidator());
    }

    @Test
    void testCheckForUniqueFields() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/object/checkForUniqueFields.object.xml"),
                "Поле id встречается более чем один раз в объекте checkForUniqueFields"
        );
    }

    @Test
    void testCheckForUniqueOperations() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/object/checkForUniqueOperations.object.xml"),
                "Действие create встречается более чем один раз в объекте checkForUniqueOperations"
        );
    }

    @Test
    void testCheckForUniqueValidations() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/object/checkForUniqueValidations.object.xml"),
                "Валидация nonEmpty встречается более чем один раз в объекте checkForUniqueValidations"
        );
    }

    @Test
    void testCheckForReferenceObjectExistsInFields() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/object/checkForExistsReferenceObject.object.xml"),
                "Поле ref2 в объекте checkForExistsReferenceObject ссылается на несуществующий объект nonExistantObject"
        );
    }

    @Test
    void testCheckForReferenceObjectExists2InOperationsFields() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/object/checkForExistsReferenceObject2.object.xml"),
                "Поле inRef2 в объекте checkForExistsReferenceObject2 ссылается на несуществующий объект nonExistantObject"
        );
    }

    @Test
    void testCheckValidationSide() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/object/checkValidationSide.object.xml"),
                "Атрибут 'side' валидации 'test' операции 'op' объекта 'checkValidationSide' не может иметь значение client"
        );
    }
}
