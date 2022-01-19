package net.n2oapp.framework.config.metadata.validation;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.metadata.validation.standard.action.PageActionValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.BasePageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.PageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.StandardPageValidator;
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
                new TableValidator(), new StandardPageValidator(), new BasePageValidator());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.page.xml"));
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.object.xml"));
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/validation/page/PageAction/blankObject.object.xml"));
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/validation/page/PageAction/blankWidget.widget.xml"));
    }

    @Test(expected = N2oMetadataValidationException.class)
    public void testPageActionValidationShowModalInToolbar() {
        validate("net/n2oapp/framework/config/metadata/validation/page/PageAction/testPageActionValidationShowModalInToolbar.page.xml");
    }

    @Test(expected = N2oMetadataValidationException.class)
    public void testPageActionValidationInToolbar() {
        validate("net/n2oapp/framework/config/metadata/validation/page/PageAction/testPageActionValidationInToolbar.page.xml");
    }

    @Test(expected = N2oMetadataValidationException.class)
    public void testPageActionValidationInRowClick() {
        validate("net/n2oapp/framework/config/metadata/validation/page/PageAction/testPageActionValidationInRowClick.page.xml");
    }

    @Test(expected = N2oMetadataValidationException.class)
    public void testPageActionValidationInColumnLink() {
        validate("net/n2oapp/framework/config/metadata/validation/page/PageAction/testPageActionValidationInColumnLink.page.xml");
    }

    @Test(expected = N2oMetadataValidationException.class)
    public void testPageActionValidationInWidgetsToolbar() {
        validate("net/n2oapp/framework/config/metadata/validation/page/PageAction/testPageActionValidationInWidgetsToolbar.page.xml");
    }

    @Test
    public void testPageActionValidationShowModalInToolbar2() {
        validate("net/n2oapp/framework/config/metadata/validation/page/PageAction/testPageActionValidationShowModalInToolbar2.page.xml");
    }

    @Test
    public void testPageActionValidationInToolbar2() {
        validate("net/n2oapp/framework/config/metadata/validation/page/PageAction/testPageActionValidationInToolbar2.page.xml");
    }

    @Test
    public void testPageActionValidationInRowClick2() {
        validate("net/n2oapp/framework/config/metadata/validation/page/PageAction/testPageActionValidationInRowClick2.page.xml");
    }

    @Test
    public void testPageActionValidationInColumnLink2() {
        validate("net/n2oapp/framework/config/metadata/validation/page/PageAction/testPageActionValidationInColumnLink2.page.xml");
    }

    @Test
    public void testPageActionValidationInWidgetsToolbar2() {
        validate("net/n2oapp/framework/config/metadata/validation/page/PageAction/testPageActionValidationInWidgetsToolbar2.page.xml");
    }

    @Test(expected = N2oMetadataValidationException.class)
    public void testPageActionValidationPageExists() {
        validate("net/n2oapp/framework/config/metadata/validation/page/PageAction/testPageActionValidationPageExists.page.xml");
    }

    @Test(expected = N2oMetadataValidationException.class)
    public void testPageActionValidationOperationExists() {
        validate("net/n2oapp/framework/config/metadata/validation/page/PageAction/testPageActionValidationOperationExists.page.xml");
    }

    @Test
    public void testPageActionValidationOperationExists2() {
        validate("net/n2oapp/framework/config/metadata/validation/page/PageAction/testPageActionValidationOperationExists2.page.xml");
    }

    @Test (expected = N2oMetadataValidationException.class)
    public void testPageActionValidationRefreshNonexistentWidget() {
        validate("net/n2oapp/framework/config/metadata/validation/page/PageAction/testPageActionValidationRefreshNonexistentWidget.page.xml");
    }

    @Test
    public void testPageActionValidationRefreshExistentWidget() {
        validate("net/n2oapp/framework/config/metadata/validation/page/PageAction/testPageActionValidationRefreshExistentWidget.page.xml");
    }

    @Test
    public void testPageActionValidationExistentDatasource() {
        validate("net/n2oapp/framework/config/metadata/validation/page/PageAction/testPageActionValidationExistentDatasource.page.xml");
    }

    @Test(expected = N2oMetadataValidationException.class)
    public void testPageActionValidationNonExistentDatasource() {
        validate("net/n2oapp/framework/config/metadata/validation/page/PageAction/testPageActionValidationNonExistentDatasource.page.xml");
    }

    @Test
    public void testPageActionValidationTargetExistentDatasource() {
        validate("net/n2oapp/framework/config/metadata/validation/page/PageAction/testPageActionValidationTargetExistentDatasource.page.xml");
    }

    @Test(expected = N2oMetadataValidationException.class)
    public void testPageActionValidationTargetNonExistentDatasource() {
        validate("net/n2oapp/framework/config/metadata/validation/page/PageAction/testPageActionValidationTargetExistentDatasource.page.xml");
    }

}
