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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование валидатора списковых полей
 */
public class ListFieldValidatorTest extends SourceValidationTestBase {
    
    @Override
    @BeforeEach
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
    void testCheckForExistsQuery() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/field/list/checkForExistsQuery.widget.xml"),
                "Поле auto ссылается на несуществующую выборку unknown"
        );
    }

    @Test
    void testUsingQueryAndDatasourceAtTheSameTime() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/field/list/testUsingQueryAndDatasourceAtTheSameTime.widget.xml"),
                "Поле 'select' использует выборку и ссылку на источник данных одновременно"
        );
    }

    @Test
    void testUsingQueryAndOptionsAtTheSameTime() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/field/list/testUsingQueryAndOptionsAtTheSameTime.widget.xml"),
                "Поле 'select' использует выборку и элемент '<options>' одновременно"
        );
    }

    @Test
    void testUsingDatasourceAndOptionsAtTheSameTime() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/field/list/testUsingDatasourceAndOptionsAtTheSameTime.widget.xml"),
                "Поле 'select' использует ссылку на источник данных и элемент '<options>' одновременно"
        );
    }
}
