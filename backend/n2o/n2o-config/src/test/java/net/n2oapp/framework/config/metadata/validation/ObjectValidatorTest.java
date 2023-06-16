package net.n2oapp.framework.config.metadata.validation;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oObjectsPack;
import net.n2oapp.framework.config.metadata.validation.standard.object.ObjectValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/object/checkForUniqueFields.object.xml")
        );
        assertEquals("Поле id встречается более чем один раз в объекте checkForUniqueFields", exception.getMessage());
    }

    @Test
    void testCheckForUniqueOperations() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/object/checkForUniqueOperations.object.xml")
        );
        assertEquals("Действие create встречается более чем один раз в объекте checkForUniqueOperations", exception.getMessage());
    }

    @Test
    void testCheckForUniqueValidations() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/object/checkForUniqueValidations.object.xml")
        );
        assertEquals("Валидация nonEmpty встречается более чем один раз в объекте checkForUniqueValidations", exception.getMessage());
    }

    @Test
    void testCheckForReferenceObjectExistsInFields() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/object/checkForExistsReferenceObject.object.xml")
        );
        assertEquals("Поле ref2 в объекте checkForExistsReferenceObject ссылается на несуществующий объект nonExistantObject", exception.getMessage());
    }

    @Test
    void testCheckForReferenceObjectExists2InOperationsFields() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/object/checkForExistsReferenceObject2.object.xml")
        );
        assertEquals("Поле inRef2 в объекте checkForExistsReferenceObject2 ссылается на несуществующий объект nonExistantObject", exception.getMessage());
    }

    @Test
    void testCheckValidationSide() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/object/checkValidationSide.object.xml")
        );
        assertEquals("Атрибут 'side' валидации 'test' операции 'op' объекта 'checkValidationSide' не может иметь значение client", exception.getMessage());
    }

    @Test
    void checkSwitchWithoutCases() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/object/checkSwitchWithoutCases.object.xml")
        );
        assertEquals("В элементе '<switch>' поля 'name' отсутствует '<case>'", exception.getMessage());
    }

    @Test
    void checkSwitchWithEmptyValueInCases() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/object/checkSwitchWithEmptyValueInCases.object.xml")
        );
        assertEquals("В '<case>' элемента '<switch>' поля 'name' атрибут 'value' пустой", exception.getMessage());
    }

    @Test
    void checkSwitchWithEmptyCase() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/object/checkSwitchWithEmptyCase.object.xml")
        );
        assertEquals("В '<case>' элемента '<switch>' поля 'name' отсутствует тело", exception.getMessage());
    }
}
