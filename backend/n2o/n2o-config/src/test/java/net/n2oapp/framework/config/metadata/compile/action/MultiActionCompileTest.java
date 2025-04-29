package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.global.view.action.control.TargetEnum;
import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.api.metadata.meta.action.alert.AlertAction;
import net.n2oapp.framework.api.metadata.meta.action.custom.CustomAction;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkActionImpl;
import net.n2oapp.framework.api.metadata.meta.action.multi.MultiAction;
import net.n2oapp.framework.api.metadata.meta.action.set_value.SetValueAction;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.PerformButton;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Проверка компиляции последовательности действий
 */
class MultiActionCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testBind.object.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testBind.page.xml"));
    }

    @Test
    void simple() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/multiaction/testMultiAction.page.xml")
                .get(new PageContext("testMultiAction"));

        PerformButton button = (PerformButton) page.getToolbar().getButton("test1");
        MultiAction action = (MultiAction) button.getAction();
        assertThat(action, instanceOf(MultiAction.class));
        assertThat(action.getType(), is("n2o/api/action/sequence"));
        assertThat(action.getPayload().getActions().size(), is(3));
        assertThat(action.getPayload().getActions().get(0), instanceOf(AlertAction.class));
        assertThat(action.getPayload().getActions().get(1), instanceOf(SetValueAction.class));
        assertThat(action.getPayload().getActions().get(2), instanceOf(LinkAction.class));
        assertThat(((LinkAction) action.getPayload().getActions().get(2)).getUrl(), is("/test1"));
        assertThat(((LinkAction) action.getPayload().getActions().get(2)).getTarget(), is(TargetEnum.application));

        button = (PerformButton) page.getToolbar().getButton("test2");
        action = (MultiAction) button.getAction();
        assertThat(action, instanceOf(MultiAction.class));
        assertThat(action.getType(), is("n2o/api/action/sequence"));
        assertThat(action.getPayload().getActions().size(), is(2));
        assertThat(action.getPayload().getActions().get(0), instanceOf(CustomAction.class));
        assertThat(action.getPayload().getActions().get(1), instanceOf(LinkAction.class));
        assertThat(((LinkAction) action.getPayload().getActions().get(1)).getUrl(), is("/test2"));
        assertThat(((LinkAction) action.getPayload().getActions().get(1)).getTarget(), is(TargetEnum.application));
    }

    @Test
    void testBind() {
        StandardPage page = (StandardPage) bind("net/n2oapp/framework/config/metadata/compile/action/multiaction/testBindMultiAction.page.xml")
                .get(new PageContext("testBindMultiAction", "/p/w/:parent_id/modal"),
                        new DataSet("parent_id", 123));
        PerformButton button = (PerformButton) page.getToolbar().getButton("test1");
        assertThat(((LinkAction) ((MultiAction) button.getAction()).getPayload().getActions().get(2)).getUrl(),
                is("/p/w/123/modal/multi3"));

        MultiAction action = (MultiAction) button.getAction();
        assertThat(action.getPayload().getActions().size(), is(3));
        assertThat(((InvokeAction) action.getPayload().getActions().get(0)).getPayload().getDataProvider().getUrl(),
                is("n2o/data/p/w/123/modal/multi1"));
        assertThat(((InvokeAction) action.getPayload().getActions().get(1)).getPayload().getDataProvider().getUrl(),
                is("n2o/data/p/w/123/modal/multi2"));
        assertThat(((InvokeAction) action.getPayload().getActions().get(1)).getMeta().getSuccess().getRedirect().getPath(),
                is("/123/redirect"));
    }

    @Test
    void testDefaultDataproviderRoutes() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/action/multiaction/testDefaultDataproviderRoutes.page.xml")
                .get(new PageContext("testDefaultDataproviderRoutes"));

        MultiAction multiAction = (MultiAction) page.getWidget().getToolbar().getButton("b1").getAction();
        assertThat(((InvokeAction) multiAction.getPayload().getActions().get(0)).getPayload().getDataProvider().getUrl(),
                is("n2o/data/testDefaultDataproviderRoutes/multi3"));
        assertThat(((InvokeAction) multiAction.getPayload().getActions().get(1)).getPayload().getDataProvider().getUrl(),
                is("n2o/data/testDefaultDataproviderRoutes/multi4"));

        multiAction = (MultiAction) page.getWidget().getToolbar().getButton("b2").getAction();
        assertThat(((InvokeAction) multiAction.getPayload().getActions().get(0)).getPayload().getDataProvider().getUrl(),
                is("n2o/data/testDefaultDataproviderRoutes/act_multi0"));
        assertThat(((InvokeAction) multiAction.getPayload().getActions().get(1)).getPayload().getDataProvider().getUrl(),
                is("n2o/data/testDefaultDataproviderRoutes/act_multi1"));
    }

    @Test
    void withOnFail() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/multiaction/testMultiActionWithOnFail.page.xml")
                .get(new PageContext("testMultiActionWithOnFail"));

        PerformButton button = (PerformButton) page.getToolbar().getButton("test1");
        MultiAction action = (MultiAction) button.getAction();
        assertThat(action, instanceOf(MultiAction.class));
        assertThat(action.getType(), is("n2o/api/action/sequence"));
        assertThat(action.getPayload().getActions().size(), is(1));
        assertThat(action.getPayload().getActions().get(0), instanceOf(InvokeAction.class));
        assertThat(action.getPayload().getFallback(), instanceOf(AlertAction.class));

        button = (PerformButton) page.getToolbar().getButton("test2");
        action = (MultiAction) button.getAction();
        assertThat(action, instanceOf(MultiAction.class));
        assertThat(action.getType(), is("n2o/api/action/sequence"));
        assertThat(action.getPayload().getActions().size(), is(1));
        assertThat(action.getPayload().getActions().get(0), instanceOf(InvokeAction.class));
        assertThat(action.getPayload().getFallback(), instanceOf(MultiAction.class));

        action = (MultiAction) action.getPayload().getFallback();
        assertThat(action.getPayload().getActions().size(), is(2));
        assertThat(action.getPayload().getActions().get(0), instanceOf(AlertAction.class));
        assertThat(action.getPayload().getActions().get(1), instanceOf(LinkActionImpl.class));
    }
}
