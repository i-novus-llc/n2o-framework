package net.n2oapp.framework.config.metadata.validation;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.metadata.validation.standard.action.InvokeActionValidator;
import net.n2oapp.framework.config.metadata.validation.standard.action.PageActionValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.BasePageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.PageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.TableValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.WidgetValidator;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.Before;
import org.junit.Test;

public class InvokeActionValidateTest extends SourceValidationTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack(), new N2oCellsPack(), new N2oObjectsPack());
        builder.validators(new WidgetValidator(), new PageValidator(), new PageActionValidator(), new BasePageValidator(),
                new TableValidator(), new InvokeActionValidator());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/validation/page/PageAction/blankWidget.widget.xml"));
    }

    @Test
    public void testRefreshRefreshExistentWidget() {
        validate("/net/n2oapp/framework/config/metadata/validation/action/testInvokeActionValidationRefreshExistentWidget.page.xml");
    }

    @Test(expected = N2oMetadataValidationException.class)
    public void testRefreshRefreshNonexistentWidget() {
        validate("/net/n2oapp/framework/config/metadata/validation/action/testInvokeActionValidationRefreshNonexistentWidget.page.xml");

    }
}
