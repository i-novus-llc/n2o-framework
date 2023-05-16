package net.n2oapp.framework.config.metadata.validation;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.metadata.validation.standard.action.InvokeActionValidator;
import net.n2oapp.framework.config.metadata.validation.standard.action.PageActionValidator;
import net.n2oapp.framework.config.metadata.validation.standard.button.ButtonValidator;
import net.n2oapp.framework.config.metadata.validation.standard.control.FieldValidator;
import net.n2oapp.framework.config.metadata.validation.standard.datasource.StandardDatasourceValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.BasePageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.PageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.FormValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.WidgetValidator;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DatasourceLinkValidationTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(),
                new N2oAllDataPack(), new N2oActionsPack(), new N2oFieldSetsPack(),
                new N2oControlsPack());
        builder.validators(new PageValidator(), new WidgetValidator(), new BasePageValidator(),
                new StandardDatasourceValidator(), new PageActionValidator(), new InvokeActionValidator(),
                new FormValidator(), new FieldValidator(), new ButtonValidator());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/validation/datasource/link/utDsLinkBlank.page.xml"));
    }

    /**
     * Проверяется, что в атрибуте refresh-datasources действия invoke указан существующий источник данных
     */
    @Test
    void testNonExistentRefreshDatasourceInInvoke() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/datasource/link/testNonExistentRefreshDatasourceInInvoke.page.xml"),
                "Атрибут \"refresh-datasources\" действия create ссылается на несуществующий источник данных 'ds1'"
        );
    }

    /**
     * Проверяется, что поле ссылается на существующий источник данных
     */
    @Test
    void testNonExistentRefDatasourceInField() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/datasource/link/testNonExistentRefDatasourceInField.page.xml"),
                "В ссылке на источник данных поля id содержится несуществующий источник данных 'ds1'"
        );
    }

    /**
     * Проверяется, что в кнопке указан существующий источник данных
     */
    @Test
    void testNonExistentDatasourceInButton() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/datasource/link/testNonExistentDatasourceInButton.page.xml"),
                "Кнопка save ссылается на несуществующий источник данных 'ds1'"
        );
    }

    /**
     * Проверяется, что в атрибуте validate-datasources кнопки указан существующий источник данных
     */
    @Test
    void testNonExistentValidateDatasourceInButton() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/datasource/link/testNonExistentValidateDatasourceInButton.page.xml"),
                "Атрибут \"validate-datasources\" кнопки save содержит несуществующий источник данных 'ds1'"
        );
    }
}
