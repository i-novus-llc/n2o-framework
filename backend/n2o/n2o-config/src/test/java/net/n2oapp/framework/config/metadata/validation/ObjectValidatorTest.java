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
                () -> validate("net/n2oapp/framework/config/metadata/validation/object/checkForUniqueFields.object.xml"));
        assertEquals("Поле 'id' встречается более чем один раз в объекте 'checkForUniqueFields'", exception.getMessage());
    }

    @Test
    void testCheckForUniqueOperations() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/object/checkForUniqueOperations.object.xml"));
        assertEquals("Операция 'create' встречается более чем один раз в объекте 'checkForUniqueOperations'", exception.getMessage());
    }

    @Test
    void testCheckForUniqueValidations() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/object/checkForUniqueValidations.object.xml"));
        assertEquals("Валидация 'nonEmpty' встречается более чем один раз в объекте 'checkForUniqueValidations'", exception.getMessage());
    }

    @Test
    void testCheckForReferenceObjectExistsInFields() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/object/checkForExistsReferenceObject.object.xml"));
        assertEquals("Поле 'ref2' в объекте 'checkForExistsReferenceObject' ссылается на несуществующий объект 'nonExistantObject'", exception.getMessage());
    }

    @Test
    void testCheckForReferenceObjectExists2InOperationsFields() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/object/checkForExistsReferenceObject2.object.xml"));
        assertEquals("Поле 'inRef2' в объекте 'checkForExistsReferenceObject2' ссылается на несуществующий объект 'nonExistantObject'", exception.getMessage());
    }

    @Test
    void testCheckValidationSide() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/object/checkValidationSide.object.xml"));
        assertEquals("Атрибут 'side' валидации 'test' операции 'op' объекта 'checkValidationSide' не может иметь значение client", exception.getMessage());
    }

    @Test
    void testFieldIdExistence() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/object/checkFieldIdExistence.page.xml"));
        assertEquals("В одном из полей объекта 'checkFieldIdExistence' не указан 'id'", exception.getMessage());
    }

    @Test
    void testInFieldIdExistence() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/object/checkInFieldIdExistence.page.xml"));
        assertEquals("В одном из <in> полей операции 'create' объекта 'checkInFieldIdExistence' не указан 'id'", exception.getMessage());
    }


    @Test
    void testOutFieldIdExistence() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/object/checkOutFieldIdExistence.page.xml"));
        assertEquals("В одном из <out> полей операции 'create' объекта 'checkOutFieldIdExistence' не указан 'id'", exception.getMessage());
    }

    @Test
    void testInFieldIdUnique() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/object/checkInFieldIdUnique.page.xml"));
        assertEquals("Поле 'name' встречается более чем один раз в <in> операции 'create' объекта 'checkInFieldIdUnique'", exception.getMessage());
    }


    @Test
    void testOutFieldIdUnique() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/object/checkOutFieldIdUnique.page.xml"));
        assertEquals("Поле 'id' встречается более чем один раз в <out> операции 'create' объекта 'checkOutFieldIdUnique'", exception.getMessage());
    }

    @Test
    void testValidationIdExistence() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/object/validation/checkValidationIdExistence.page.xml"));
        assertEquals("В одной из валидаций объекта 'checkValidationIdExistence' не указан параметр 'id'", exception.getMessage());
    }

    @Test
    void testOperationValidationIdExistence() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/object/validation/checkOperationValidationIdExistence.page.xml"));
        assertEquals("В одной из валидаций операции 'test' объекта 'checkOperationValidationIdExistence' не указан параметр 'id'", exception.getMessage());
    }

    @Test
    void testWhiteListNoValidation() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/object/validation/checkWhiteListNoValidation.page.xml"));
        assertEquals("В валидации операции 'test' объекта 'checkWhiteListNoValidation' указан атрибут 'white-list', но сам объект не имеет валидаций", exception.getMessage());
    }

    @Test
    void testWhiteListValidationExistence() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/object/validation/checkWhiteListValidationExistence.page.xml"));
        assertEquals("Атрибут 'white-list' операции 'testOperation' объекта 'checkWhiteListValidationExistence' ссылается на несуществующую валидацию 'test' объекта", exception.getMessage());
    }

    @Test
    void testMandatoryValidationFieldIdExistence() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/object/validation/checkMandatoryValidationFieldIdExistence.page.xml"));
        assertEquals("В <mandatory> валидации 'test' объекта 'checkMandatoryValidationFieldIdExistence' необходимо указать атрибут 'field-id'", exception.getMessage());
    }

    @Test
    void testOperationIdExistence() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/object/checkOperationIdExistence.page.xml"));
        assertEquals("В одной из операций объекта 'checkOperationIdExistence' не указан 'id'", exception.getMessage());
    }

    @Test
    void checkSwitchWithoutCases() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/object/checkSwitchWithoutCases.object.xml"));
        assertEquals("В элементе '<switch>' поля 'name' отсутствует '<case>'", exception.getMessage());
    }

    @Test
    void checkSwitchWithEmptyValueInCases() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/object/checkSwitchWithEmptyValueInCases.object.xml"));
        assertEquals("В '<case>' элемента '<switch>' поля 'name' атрибут 'value' пустой", exception.getMessage());
    }

    @Test
    void checkSwitchWithEmptyCase() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/object/checkSwitchWithEmptyCase.object.xml"));
        assertEquals("В '<case>' элемента '<switch>' поля 'name' отсутствует тело", exception.getMessage());
    }

    @Test
    void testValidationDialogEmptyToolbar() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/object/validation/testValidationDialogEmptyToolbar.object.xml")
        );
        assertEquals("Не заданы элементы или атрибут 'generate' в тулбаре валидации <dialog> объекта 'testValidationDialogEmptyToolbar'", exception.getMessage());
    }

    @Test
    void testValidationDialogEmptyToolbarInOperation() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/object/validation/testValidationDialogEmptyToolbarInOperation.object.xml")
        );
        assertEquals("Не заданы элементы или атрибут 'generate' в тулбаре валидации <dialog> в операции 'id1' объекта 'testValidationDialogEmptyToolbarInOperation'", exception.getMessage());
    }
}
