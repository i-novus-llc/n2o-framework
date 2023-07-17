package net.n2oapp.framework.config.metadata.validation;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oQueriesPack;
import net.n2oapp.framework.config.metadata.validation.standard.query.QueryValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование валидации выборок
 */
public class QueryValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oQueriesPack());
        builder.validators(new QueryValidator());
    }

    @Test
    void testCheckForExistsObject() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/query/checkForExistsObject.query.xml"));
        assertEquals("Выборка 'checkForExistsObject' ссылается на несуществующий объект 'nonExistantObjectId'", exception.getMessage());
    }

    @Test
    void testCheckForUniqueFields() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/query/checkForUniqueFields.query.xml"));
        assertEquals("Поле 'code' встречается более чем один раз в выборке 'checkForUniqueFields'", exception.getMessage());
    }

    @Test
    void testCheckForUniqueFilterFields() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/query/checkForUniqueFilterFields.query.xml"));
        assertEquals("Фильтр 'nameEq' в выборке 'checkForUniqueFilterFields' повторяется", exception.getMessage());
    }

    @Test
    void testCheckForExistsFiltersInSelections() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/query/checkForExistsFiltersInSelections.query.xml"));
        assertEquals( "<unique> ссылается на несуществующий фильтр 'codeEq2' в выборке 'checkForExistsFiltersInSelections'", exception.getMessage());
    }

    @Test
    void testCheckFieldIdExistence() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/query/checkFieldIdExistence.page.xml"));
        assertEquals( "Одно из полей выборки 'checkFieldIdExistence' не имеет 'id'", exception.getMessage());
    }
}
