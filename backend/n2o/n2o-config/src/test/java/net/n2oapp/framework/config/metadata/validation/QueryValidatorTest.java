package net.n2oapp.framework.config.metadata.validation;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oQueriesPack;
import net.n2oapp.framework.config.metadata.validation.standard.query.QueryValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Тестирование валидации выборок
 */
public class QueryValidatorTest extends SourceValidationTestBase {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Override
    @Before
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
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Выборка checkForExistsObject ссылается на несуществующий объект nonExistantObjectId");
        validate("net/n2oapp/framework/config/metadata/validation/query/checkForExistsObject.query.xml");
    }

    @Test
    public void testCheckForUniqueFields() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Поле code встречается более чем один раз в выборке checkForUniqueFields");
        validate("net/n2oapp/framework/config/metadata/validation/query/checkForUniqueFields.query.xml");
    }

    @Test
    public void testCheckForUniqueFilterFields() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Фильтр nameEq в выборке checkForUniqueFilterFields повторяется");
        validate("net/n2oapp/framework/config/metadata/validation/query/checkForUniqueFilterFields.query.xml");
    }

    @Test
    public void testCheckForExistsFiltersInSelections() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("<unique> ссылается на несуществующий фильтр codeEq2");
        validate("net/n2oapp/framework/config/metadata/validation/query/checkForExistsFiltersInSelections.query.xml");
    }
}
