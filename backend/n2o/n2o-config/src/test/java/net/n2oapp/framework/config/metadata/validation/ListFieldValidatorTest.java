package net.n2oapp.framework.config.metadata.validation;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oControlsPack;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.metadata.validation.standard.fieldset.FieldSetRowValidator;
import net.n2oapp.framework.config.metadata.validation.standard.fieldset.SetFieldSetValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.FormValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.ListFieldValidator;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Тестирование валидатора списковых полей
 */
public class ListFieldValidatorTest extends SourceValidationTestBase {

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
        builder.validators(new FormValidator(), new FieldSetRowValidator(),
                new SetFieldSetValidator(), new ListFieldValidator());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank2.query.xml"));
    }

    @Test
    public void testCheckForExistsQuery() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Поле auto ссылается на несуществующую выборку unknown");
        validate("net/n2oapp/framework/config/metadata/validation/field/list/checkForExistsQuery.widget.xml");
    }

    @Test
    public void testUsingQueryAndDatasourceAtTheSameTime() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Поле 'select' использует выборку и ссылку на источник данных одновременно");
        validate("net/n2oapp/framework/config/metadata/validation/field/list/testUsingQueryAndDatasourceAtTheSameTime.widget.xml");
    }

    @Test
    public void testUsingQueryAndOptionsAtTheSameTime() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Поле 'select' использует выборку и элемент '<options>' одновременно");
        validate("net/n2oapp/framework/config/metadata/validation/field/list/testUsingQueryAndOptionsAtTheSameTime.widget.xml");
    }

    @Test
    public void testUsingDatasourceAndOptionsAtTheSameTime() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Поле 'select' использует ссылку на источник данных и элемент '<options>' одновременно");
        validate("net/n2oapp/framework/config/metadata/validation/field/list/testUsingDatasourceAndOptionsAtTheSameTime.widget.xml");
    }
}
