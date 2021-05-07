package net.n2oapp.framework.config.metadata.validation;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.object.N2oObjectValidator;
import net.n2oapp.framework.config.metadata.compile.query.N2oQueryValidator;
import net.n2oapp.framework.config.metadata.pack.N2oDataProvidersPack;
import net.n2oapp.framework.config.metadata.pack.N2oObjectsPack;
import net.n2oapp.framework.config.metadata.pack.N2oQueriesPack;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Тестирование валидации java provider
 */
public class JavaProviderValidatorTest extends SourceValidationTestBase {

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
        builder.packs(new N2oObjectsPack(), new N2oDataProvidersPack(), new N2oQueriesPack());
        N2oObjectValidator n2oObjectValidator = new N2oObjectValidator();
        n2oObjectValidator.setMapping("map");
        N2oQueryValidator n2oQueryValidator = new N2oQueryValidator();
        n2oQueryValidator.setMapping("map");
        builder.validators(n2oObjectValidator, n2oQueryValidator);
    }

    @Test
    public void testNameIsNotNullInObject() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("В объекте testValidationJavaProvider для всех аргументов с java provider должно быть задано имя");
        validate("net/n2oapp/framework/config/metadata/validation/provider/testValidationJavaProvider.object.xml");
    }

    @Test
    public void testNameIsNotNullInQuery() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("В запросе testValidationJavaProvider для всех аргументов в java provider должно быть задано имя");
        validate("net/n2oapp/framework/config/metadata/validation/provider/testValidationJavaProvider.query.xml");
    }

}
