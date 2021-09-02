package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.meta.action.refresh.RefreshAction;
import net.n2oapp.framework.api.metadata.meta.action.refresh.RefreshPayload;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oActionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тестирование компиляции действия обновления данных виджета
 */
public class RefreshActionCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack());
    }

    @Test
    public void testRefresh() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/testRefreshAction.page.xml")
                .get(new PageContext("testRefreshAction"));

        List<AbstractButton> buttons = ((Table) page.getRegions().get("single").get(0).getContent().get(1))
                .getToolbar().get("topLeft").get(0).getButtons();

        assertThat(buttons.size(), is(2));
        RefreshAction action = (RefreshAction) buttons.get(0).getAction();
        assertThat(action.getType(), is("n2o/widgets/DATA_REQUEST"));
        assertThat(((RefreshPayload) action.getPayload()).getWidgetId(), is("testRefreshAction_ds2"));

        action = (RefreshAction) buttons.get(1).getAction();
        assertThat(((RefreshPayload) action.getPayload()).getWidgetId(), is("testRefreshAction_ds1"));

        action = (RefreshAction) page.getToolbar().getButton("btn3").getAction();
        assertThat(((RefreshPayload) action.getPayload()).getWidgetId(), is("testRefreshAction_ds1"));

        action = (RefreshAction) page.getToolbar().getButton("btn4").getAction();
        assertThat(((RefreshPayload) action.getPayload()).getWidgetId(), is("testRefreshAction_ds2"));
    }
}
