package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.api.metadata.meta.action.Perform;
import net.n2oapp.framework.api.metadata.meta.action.alert.AlertAction;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.action.multi.MultiAction;
import net.n2oapp.framework.api.metadata.meta.action.set_value.SetValueAction;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.PerformButton;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Проверка компиляции последовательности действий
 */
public class MultiActionCompileTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/multiaction/bind/test.object.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/multiaction/bind/test.page.xml"));
    }

    @Test
    public void simple() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/multiaction/testMultiAction.page.xml")
                .get(new PageContext("testMultiAction"));

        PerformButton button = (PerformButton) page.getToolbar().getButton("test1");
        assertThat(button.getUrl(), is("/test1"));
        assertThat(button.getTarget(), is(Target.application));
        MultiAction action = (MultiAction) button.getAction();
        assertThat(action, instanceOf(MultiAction.class));
        assertThat(action.getType(), is("n2o/api/action/sequence"));
        assertThat(action.getPayload().getActions().size(), is(3));
        assertThat(action.getPayload().getActions().get(0), instanceOf(AlertAction.class));
        assertThat(action.getPayload().getActions().get(1), instanceOf(SetValueAction.class));
        assertThat(action.getPayload().getActions().get(2), instanceOf(LinkAction.class));

        button = (PerformButton) page.getToolbar().getButton("test2");
        assertThat(button.getUrl(), is("/test2"));
        assertThat(button.getTarget(), is(Target.application));
        action = (MultiAction) button.getAction();
        assertThat(action, instanceOf(MultiAction.class));
        assertThat(action.getType(), is("n2o/api/action/sequence"));
        assertThat(action.getPayload().getActions().size(), is(2));
        assertThat(action.getPayload().getActions().get(0), instanceOf(Perform.class));
        assertThat(action.getPayload().getActions().get(1), instanceOf(LinkAction.class));
    }

    @Test
    public void testBind() {
        StandardPage page = (StandardPage) bind("net/n2oapp/framework/config/metadata/compile/action/multiaction/bind/testBindMultiAction.page.xml")
                .get(new PageContext("testBindMultiAction", "/p/w/:parent_id/modal"),
                        new DataSet("parent_id", 123));
        PerformButton button = (PerformButton) page.getToolbar().getButton("test1");
        assertThat(button.getUrl(), is("/p/w/123/modal/multi2"));

        MultiAction action = (MultiAction) button.getAction();
        assertThat(action.getPayload().getActions().size(), is(3));
        assertThat(((InvokeAction) action.getPayload().getActions().get(0)).getPayload().getDataProvider().getUrl(),
                is("n2o/data/p/w/123/modal/multi0"));
        assertThat(((InvokeAction) action.getPayload().getActions().get(1)).getPayload().getDataProvider().getUrl(),
                is("n2o/data/p/w/123/modal/multi1"));
        assertThat(((InvokeAction) action.getPayload().getActions().get(1)).getMeta().getSuccess().getRedirect().getPath(),
                is("/123/redirect"));
    }
}
