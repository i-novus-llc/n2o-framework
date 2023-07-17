package net.n2oapp.framework.config.metadata.validation.provider;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oDataProvidersPack;
import net.n2oapp.framework.config.metadata.pack.N2oObjectsPack;
import net.n2oapp.framework.config.metadata.pack.N2oQueriesPack;
import net.n2oapp.framework.config.metadata.validation.standard.invocation.JavaDataProviderValidator;
import net.n2oapp.framework.config.metadata.validation.standard.object.ObjectValidator;
import net.n2oapp.framework.config.metadata.validation.standard.query.QueryValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование валидации java provider
 */
public class JavaDataProviderValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oObjectsPack(), new N2oDataProvidersPack(), new N2oQueriesPack());
        builder.validators(new ObjectValidator(), new QueryValidator(), new JavaDataProviderValidator());
    }

    @Test
    void testQueryNotMethod() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                ()-> validate("net/n2oapp/framework/config/metadata/validation/provider/java/testNonMethodQuery.query.xml"));
        assertEquals("В <java> провайдере выборки 'testNonMethodQuery' не указан атрибут 'method'", exception.getMessage());
    }

    @Test
    void testObjectOperationNotMethod() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                ()-> validate("net/n2oapp/framework/config/metadata/validation/provider/java/testNonMethodObjectOperation.object.xml"));
        assertEquals("В <java> провайдере операции 'test' объекта 'testNonMethodObjectOperation' не указан атрибут 'method'", exception.getMessage());
    }

    @Test
    void testObjectOperationValidationNotMethod() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                ()-> validate("net/n2oapp/framework/config/metadata/validation/provider/java/testNonMethodObjectOperationValidation.object.xml"));
        assertEquals("В <java> провайдере валидации 'test' операции 'op1' объекта 'testNonMethodObjectOperationValidation' не указан атрибут 'method'", exception.getMessage());
    }

    @Test
    void testObjectValidationNotMethod() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                ()-> validate("net/n2oapp/framework/config/metadata/validation/provider/java/testNonMethodObjectValidation.object.xml"));
        assertEquals("В <java> провайдере валидации 'test' объекта 'testNonMethodObjectValidation' не указан атрибут 'method'", exception.getMessage());
    }
}
