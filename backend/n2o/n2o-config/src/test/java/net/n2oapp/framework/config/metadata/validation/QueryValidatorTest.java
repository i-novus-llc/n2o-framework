package net.n2oapp.framework.config.metadata.validation;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.query.TestEngineQueryTransformer;
import net.n2oapp.framework.config.metadata.pack.N2oDataProvidersPack;
import net.n2oapp.framework.config.metadata.pack.N2oQueriesPack;
import net.n2oapp.framework.config.metadata.validation.standard.query.QueryValidator;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестирование валидации выборок
 */
class QueryValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() {
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
        assertEquals("\"<unique>\" ссылается на несуществующий фильтр 'codeEq2' в выборке 'checkForExistsFiltersInSelections'", exception.getMessage());
    }

    @Test
    void testCheckForExistsFiltersInSelectionsWithNoFilterId() {
        assertDoesNotThrow(() -> validate("net/n2oapp/framework/config/metadata/validation/query/checkForExistsFiltersInSelectionsWithNoFilterId.query.xml"));
    }

    @Test
    void testCheckFieldIdExistence() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/query/checkFieldIdExistence.page.xml"));
        assertEquals("Одно из полей выборки 'checkFieldIdExistence' не имеет 'id'", exception.getMessage());
    }

    @Test
    void checkSwitchWithoutCases() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/query/checkSwitchWithoutCases.query.xml"));
        assertEquals("В элементе \"<switch>\" поля 'name' отсутствует \"<case>\"", exception.getMessage());
    }

    @Test
    void checkSwitchWithEmptyValueInCases() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/query/checkSwitchWithEmptyValueInCases.query.xml"));
        assertEquals("В \"<case>\" элемента \"<switch>\" поля 'name' атрибут 'value' пустой", exception.getMessage());
    }

    @Test
    void checkSwitchWithEmptyCase() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/query/checkSwitchWithEmptyCase.query.xml"));
        assertEquals("В \"<case>\" элемента \"<switch>\" поля 'name' отсутствует тело", exception.getMessage());
    }

    @Test
    void testCheckForExistsFiltersInSelectionsWithNestedField() {
        builder.packs(new N2oDataProvidersPack()).transformers(new TestEngineQueryTransformer());
        CompileInfo info = new CompileInfo("net/n2oapp/framework/config/metadata/validation/query/testQueryNestedFieldFilter.query.xml");
        assertDoesNotThrow(() -> builder.sources(info).read().transform().validate().get(info.getId(), info.getBaseSourceClass()));
    }

    @Test
    void testCheckForExistsFiltersInSelectionsWithNestedFields() {
        builder.packs(new N2oDataProvidersPack()).transformers(new TestEngineQueryTransformer());
        CompileInfo info = new CompileInfo("net/n2oapp/framework/config/metadata/validation/query/testQueryNestedFieldFilters.query.xml");
        assertDoesNotThrow(() -> builder.sources(info).read().transform().validate().get(info.getId(), info.getBaseSourceClass()));
    }

    @Test
    void testMissingFilterInSelection() {
        N2oMetadataValidationException e = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/query/testQueryMissingFilter.query.xml"));
        assertEquals("\"<list>\" ссылается на несуществующий фильтр 'type.id' в выборке 'testQueryMissingFilter'", e.getMessage());
    }

    @Test
    void testFilterFieldIdExistence() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/query/checkFilterFieldIdExistence.query.xml"));
        assertEquals("В фильтре 'type2' выборки 'checkFilterFieldIdExistence' не задан обязательный атрибут 'field-id'", exception.getMessage());
    }

    @Test
    void testFilterFieldIdExistsInFields() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/query/checkFilterFieldIdExists.query.xml"));
        assertEquals("Фильтр 'type3' выборки 'checkFilterFieldIdExists' ссылается на несуществующее поле 'type3'", exception.getMessage());
    }

    @Test
    void testListFilterWithoutFilters() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/query/testQueryListFilterWithoutFilters.query.xml"));
        assertEquals("\"<list>\" ссылается на несуществующий фильтр 'type' в выборке 'testQueryListFilterWithoutFilters'",
                exception.getMessage());
    }

    @Test
    void testListFilterWithoutFiltersBlock() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/query/testQueryListFilterNoFiltersBlock.query.xml"));
        assertEquals("\"<list>\" ссылается на несуществующий фильтр 'type' в выборке 'testQueryListFilterNoFiltersBlock'",
                exception.getMessage());
    }

    @Test
    void testCheckForUniqueNestedFields() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/query/checkForUniqueNestedFields.query.xml"));
        assertEquals("Поле 'name' встречается более чем один раз в выборке 'checkForUniqueNestedFields'",
                exception.getMessage());
    }
}
