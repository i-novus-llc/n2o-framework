package net.n2oapp.framework.config.metadata.validation.provider;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oDataProvidersPack;
import net.n2oapp.framework.config.metadata.pack.N2oObjectsPack;
import net.n2oapp.framework.config.metadata.pack.N2oQueriesPack;
import net.n2oapp.framework.config.metadata.validation.standard.invocation.MongoDbDataProviderValidator;
import net.n2oapp.framework.config.metadata.validation.standard.object.ObjectValidator;
import net.n2oapp.framework.config.metadata.validation.standard.query.QueryValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование валидации mongodb provider
 */
class MongoDbDataProviderValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oObjectsPack(), new N2oDataProvidersPack(), new N2oQueriesPack());
        builder.validators(new ObjectValidator(), new QueryValidator(), new MongoDbDataProviderValidator());
    }

    @Test
    void testQueryNotCollectionName() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/provider/mongodb/testNonCollectionNameQuery.query.xml"));
        assertEquals("В \"<mongodb>\" провайдере выборки 'testNonCollectionNameQuery' не указан атрибут 'collection-name'", exception.getMessage());
    }

    @Test
    void testObjectOperationNotCollectionName() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/provider/mongodb/testNonCollectionNameObjectOperation.object.xml"));
        assertEquals("В \"<mongodb>\" провайдере операции 'test' объекта 'testNonCollectionNameObjectOperation' не указан атрибут 'collection-name'", exception.getMessage());
    }

    @Test
    void testObjectOperationValidationNotCollectionName() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/provider/mongodb/testNonCollectionNameObjectOperationValidation.object.xml"));
        assertEquals("В \"<mongodb>\" провайдере валидации 'test' операции 'op1' объекта 'testNonCollectionNameObjectOperationValidation' не указан атрибут 'collection-name'", exception.getMessage());
    }

    @Test
    void testObjectValidationNotCollectionName() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/provider/mongodb/testNonCollectionNameObjectValidation.object.xml"));
        assertEquals("В \"<mongodb>\" провайдере валидации 'test' объекта 'testNonCollectionNameObjectValidation' не указан атрибут 'collection-name'", exception.getMessage());
    }

    @Test
    void testObjectValidationNotCollectionNameAfterOperationValidation() {
        N2oMetadataValidationException exception = assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/provider/mongodb/testNonCollectionNameObjectValidationAfterOperation.object.xml"));
        assertEquals("В \"<mongodb>\" провайдере валидации 'test' объекта 'testNonCollectionNameObjectValidationAfterOperation' не указан атрибут 'collection-name'",
                exception.getMessage());
    }
}