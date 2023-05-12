package net.n2oapp.framework.config.metadata.validation;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oQueriesPack;
import net.n2oapp.framework.config.metadata.validation.standard.query.QueryValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    public void testCheckForExistsObject() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/query/checkForExistsObject.query.xml"),
                "Выборка checkForExistsObject ссылается на несуществующий объект nonExistantObjectId"
        );
    }

    @Test
    public void testCheckForUniqueFields() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/query/checkForUniqueFields.query.xml"),
                "Поле code встречается более чем один раз в выборке checkForUniqueFields"
        );
    }

    @Test
    public void testCheckForUniqueFilterFields() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/query/checkForUniqueFilterFields.query.xml"),
                "Фильтр nameEq в выборке checkForUniqueFilterFields повторяется"
        );
    }

    @Test
    public void testCheckForExistsFiltersInSelections() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/query/checkForExistsFiltersInSelections.query.xml"),
                "<unique> ссылается на несуществующий фильтр codeEq2"
        );
    }
}
