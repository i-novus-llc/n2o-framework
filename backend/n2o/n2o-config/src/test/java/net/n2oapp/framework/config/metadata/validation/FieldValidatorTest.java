package net.n2oapp.framework.config.metadata.validation;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oControlsPack;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.metadata.validation.standard.control.FieldValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.FormValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Тестирование валидации страницы
 */
public class FieldValidatorTest extends SourceValidationTestBase {

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
        builder.packs(new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
        builder.validators(new FormValidator(), new FieldValidator());
    }

    @Test
    public void testUniqueDependenciesType() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("В поле field2 повторяются зависимости одного типа");
        validate("net/n2oapp/framework/config/metadata/validation/field/testUniqueDependenciesType.widget.xml");
    }

    @Test
    public void testDefaultValueNotNull() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Атрибут default-value не является ссылкой или не задан: null");
        validate("net/n2oapp/framework/config/metadata/validation/field/testDefaultValueNotNull.widget.xml");
    }

    @Test
    public void testDefaultValueNotNullForList() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("У поля field1 атрибут default-value не задан");
        validate("net/n2oapp/framework/config/metadata/validation/field/testDefaultValueNotNullForList.widget.xml");
    }

    @Test
    public void testDefaultValueNotNullForInterval() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("У поля field1 атрибут default-value не задан");
        validate("net/n2oapp/framework/config/metadata/validation/field/testDefaultValueNotNullForInterval.widget.xml");
    }

    @Test
    public void testDefaultValue() {
        validate("net/n2oapp/framework/config/metadata/validation/field/testDefaultValue.widget.xml");
    }
}
