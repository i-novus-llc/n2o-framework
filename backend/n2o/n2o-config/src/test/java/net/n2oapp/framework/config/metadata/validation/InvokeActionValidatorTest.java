package net.n2oapp.framework.config.metadata.validation;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oActionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.metadata.validation.standard.action.InvokeActionValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.BasePageValidator;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Тестирование валидации действия вызова операции
 */
public class InvokeActionValidatorTest extends SourceValidationTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack());
        builder.validators(new BasePageValidator(), new InvokeActionValidator());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/validation/page/PageAction/blankWidget.widget.xml"));
    }

    @Test
    public void testRefreshExistentWidget() {
        validate("/net/n2oapp/framework/config/metadata/validation/action/invoke/testInvokeActionValidationRefreshExistentWidget.page.xml");
    }

    @Test
    public void testRefreshNonexistentWidget() {
        N2oMetadataValidationException e = assertThrows(N2oMetadataValidationException.class,
                () -> validate("/net/n2oapp/framework/config/metadata/validation/action/invoke/testInvokeActionValidationRefreshNonexistentWidget.page.xml"));
        assertEquals("Атрибут \"refresh-datasources\" действия referesh ссылается на несуществующий источник данных 'nonexistentWidget'", e.getMessage());
    }

    @Test
    public void testRefreshNonExistentDatasource() {
        N2oMetadataValidationException e = assertThrows(N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/action/invoke/testRefreshNonExistentDatasource.page.xml"));
        assertEquals("Атрибут \"refresh-datasources\" действия refresh ссылается на несуществующий источник данных 'ds1'", e.getMessage());
    }
}
