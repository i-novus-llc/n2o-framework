package net.n2oapp.framework.config.metadata.validation;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oObjectsPack;
import net.n2oapp.framework.config.metadata.validation.standard.object.ObjectValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Тестирование валидации объектов
 */
public class ObjectValidatorTest extends SourceValidationTestBase {

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
        builder.packs(new N2oObjectsPack());
        builder.validators(new ObjectValidator());
    }

    @Test
    public void testCheckForUniqueFields() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Поле id встречается более чем один раз в объекте checkForUniqueFields");
        validate("net/n2oapp/framework/config/metadata/validation/object/checkForUniqueFields.object.xml");
    }

    @Test
    public void testCheckForUniqueOperations() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Действие create встречается более чем один раз в объекте checkForUniqueOperations");
        validate("net/n2oapp/framework/config/metadata/validation/object/checkForUniqueOperations.object.xml");
    }

    @Test
    public void testCheckForUniqueValidations() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Валидация nonEmpty встречается более чем один раз в объекте checkForUniqueValidations");
        validate("net/n2oapp/framework/config/metadata/validation/object/checkForUniqueValidations.object.xml");
    }

    @Test
    public void testCheckForReferenceObjectExistsInFields() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Поле ref2 в объекте checkForExistsReferenceObject ссылается на несуществующий объект nonExistantObject");
        validate("net/n2oapp/framework/config/metadata/validation/object/checkForExistsReferenceObject.object.xml");
    }

    @Test
    public void testCheckForReferenceObjectExists2InOperationsFields() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Поле inRef2 в объекте checkForExistsReferenceObject2 ссылается на несуществующий объект nonExistantObject");
        validate("net/n2oapp/framework/config/metadata/validation/object/checkForExistsReferenceObject2.object.xml");
    }
}
