package net.n2oapp.framework.config.metadata.validation.action;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.metadata.validation.standard.action.PageActionValidator;
import net.n2oapp.framework.config.metadata.validation.standard.button.ButtonValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.BasePageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.PageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.SimplePageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.TableValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.WidgetValidator;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.Before;
import org.junit.Test;

public class PageActionValidatorTest extends SourceValidationTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack(), new N2oCellsPack(), new N2oObjectsPack());
        builder.validators(new WidgetValidator(), new PageValidator(), new PageActionValidator(),
                new TableValidator(), new BasePageValidator(), new SimplePageValidator(), new ButtonValidator());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.page.xml"));
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.object.xml"));
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/validation/action/page/blankObject.object.xml"));
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/validation/action/page/blankWidget.widget.xml"));
    }

    @Test(expected = N2oMetadataValidationException.class)
    public void testPageActionValidationShowModalInToolbar() {
        validate("net/n2oapp/framework/config/metadata/validation/action/page/testPageActionValidationShowModalInToolbar.page.xml");
    }

    @Test(expected = N2oMetadataValidationException.class)
    public void testPageActionValidationInToolbar() {
        validate("net/n2oapp/framework/config/metadata/validation/action/page/testPageActionValidationInToolbar.page.xml");
    }

    @Test(expected = N2oMetadataValidationException.class)
    public void testPageActionValidationInRowClick() {
        validate("net/n2oapp/framework/config/metadata/validation/action/page/testPageActionValidationInRowClick.page.xml");
    }

    @Test(expected = N2oMetadataValidationException.class)
    public void testPageActionValidationInColumnLink() {
        validate("net/n2oapp/framework/config/metadata/validation/action/page/testPageActionValidationInColumnLink.page.xml");
    }

    @Test(expected = N2oMetadataValidationException.class)
    public void testPageActionValidationInWidgetsToolbar() {
        validate("net/n2oapp/framework/config/metadata/validation/action/page/testPageActionValidationInWidgetsToolbar.page.xml");
    }

    @Test(expected = N2oMetadataValidationException.class)
    public void testRefreshDatasourceNonExistent() {
        validate("net/n2oapp/framework/config/metadata/validation/action/page/testPageActionValidationRefreshDatasourcesNonExistent.page.xml");
    }

    @Test
    public void testPageActionValidationShowModalInToolbar2() {
        validate("net/n2oapp/framework/config/metadata/validation/action/page/testPageActionValidationShowModalInToolbar2.page.xml");
    }

    @Test
    public void testPageActionValidationInToolbar2() {
        validate("net/n2oapp/framework/config/metadata/validation/action/page/testPageActionValidationInToolbar2.page.xml");
    }

    @Test
    public void testPageActionValidationInRowClick2() {
        validate("net/n2oapp/framework/config/metadata/validation/action/page/testPageActionValidationInRowClick2.page.xml");
    }

    @Test
    public void testPageActionValidationInColumnLink2() {
        validate("net/n2oapp/framework/config/metadata/validation/action/page/testPageActionValidationInColumnLink2.page.xml");
    }

    @Test
    public void testPageActionValidationInWidgetsToolbar2() {
        validate("net/n2oapp/framework/config/metadata/validation/action/page/testPageActionValidationInWidgetsToolbar2.page.xml");
    }

    @Test(expected = N2oMetadataValidationException.class)
    public void testPageActionValidationPageExists() {
        validate("net/n2oapp/framework/config/metadata/validation/action/page/testPageActionValidationPageExists.page.xml");
    }

    @Test(expected = N2oMetadataValidationException.class)
    public void testPageActionValidationOperationExists() {
        validate("net/n2oapp/framework/config/metadata/validation/action/page/testPageActionValidationOperationExists.page.xml");
    }

    @Test
    public void testPageActionValidationOperationExists2() {
        validate("net/n2oapp/framework/config/metadata/validation/action/page/testPageActionValidationOperationExists2.page.xml");
    }

    @Test (expected = N2oMetadataValidationException.class)
    public void testPageActionValidationRefreshNonexistentWidget() {
        validate("net/n2oapp/framework/config/metadata/validation/action/page/testPageActionValidationRefreshNonexistentWidget.page.xml");
    }

    @Test
    public void testPageActionValidationRefreshExistentWidget() {
        validate("net/n2oapp/framework/config/metadata/validation/action/page/testPageActionValidationRefreshExistentWidget.page.xml");
    }
}
