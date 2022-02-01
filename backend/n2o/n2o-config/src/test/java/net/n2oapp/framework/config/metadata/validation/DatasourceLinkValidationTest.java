package net.n2oapp.framework.config.metadata.validation;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.metadata.validation.standard.action.InvokeActionValidator;
import net.n2oapp.framework.config.metadata.validation.standard.action.PageActionValidator;
import net.n2oapp.framework.config.metadata.validation.standard.button.ButtonValidator;
import net.n2oapp.framework.config.metadata.validation.standard.control.FieldValidator;
import net.n2oapp.framework.config.metadata.validation.standard.datasource.DatasourceValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.BasePageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.PageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.FormValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.WidgetValidator;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DatasourceLinkValidationTest extends SourceValidationTestBase {

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
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(),
                new N2oAllDataPack(), new N2oActionsPack(), new N2oFieldSetsPack(),
                new N2oControlsPack());
        builder.validators(new PageValidator(), new WidgetValidator(), new BasePageValidator(),
                new DatasourceValidator(), new PageActionValidator(), new InvokeActionValidator(),
                new FormValidator(), new FieldValidator(), new ButtonValidator());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/validation/datasource/link/utDsLinkBlank.page.xml"));
    }

    /**
     * Проверяется, что источник данных, указанный в атрибуте действия открытия страницы, существует
     */
    @Test
    public void testNonExistentDatasourceReferenceInOpenPage() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Действие открытия сотраницы utDsLinkBlank сылается на несуществующий источник данных 'ds1'");
        validate("net/n2oapp/framework/config/metadata/validation/datasource/link/testNonExistentDatasourceReferenceInOpenPage.page.xml");
    }

    /**
     * Проверяется, что в атрибуте target-datasource действия открытия модального окна указан существующий источник данных
     */
    @Test
    public void testNonExistentTargetDatasourceInShowModal() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Атрибут \"target-datasource\" действия открытия страницы utDsLinkBlank ссылается на несущетсвующий источник данных 'ds1'");
        validate("net/n2oapp/framework/config/metadata/validation/datasource/link/testNonExistentTargetDatasourceInShowModal.page.xml");
    }

    /**
     * Проверяется, что действие invoke ссылается на существующий источник данных
     */
    @Test
    public void testNonExistentDatasourceInInvoke() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Действие create ссылается на несуществующий источник данных 'ds1'");
        validate("net/n2oapp/framework/config/metadata/validation/datasource/link/testNonExistentDatasourceInInvoke.page.xml");
    }

    /**
     * Проверяется, что в атрибуте refresh-datasources действия invoke указан существующий источник данных
     */
    @Test
    public void testNonExistentRefreshDatasourceInInvoke() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Атрибут \"refresh-datasources\" действия create ссылается на несуществующий источник данных 'ds1'");
        validate("net/n2oapp/framework/config/metadata/validation/datasource/link/testNonExistentRefreshDatasourceInInvoke.page.xml");
    }

    /**
     * Проверяется, что поле ссылается на существующий источник данных
     */
    @Test
    public void testNonExistentRefDatasourceInField() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("В ссылке на источник данных поля id содержится несуществующий источник данных 'ds1'");
        validate("net/n2oapp/framework/config/metadata/validation/datasource/link/testNonExistentRefDatasourceInField.page.xml");
    }

    /**
     * Проверяется, что в кнопке указан существующий источник данных
     */
    @Test
    public void testNonExistentDatasourceInButton() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Кнопка save ссылается на несуществующий источник данных 'ds1'");
        validate("net/n2oapp/framework/config/metadata/validation/datasource/link/testNonExistentDatasourceInButton.page.xml");
    }

    /**
     * Проверяется, что в атрибуте validate-datasources кнопки указан существующий источник данных
     */
    @Test
    public void testNonExistentValidateDatasourceInButton() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Атрибут \"validate-datasources\" кнопки save содержит несуществующий источник данных 'ds1'");
        validate("net/n2oapp/framework/config/metadata/validation/datasource/link/testNonExistentValidateDatasourceInButton.page.xml");
    }
}
