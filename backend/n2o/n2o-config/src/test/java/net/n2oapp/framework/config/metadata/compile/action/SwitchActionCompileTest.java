package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.meta.action.Perform;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.action.multi.MultiAction;
import net.n2oapp.framework.api.metadata.meta.action.switchaction.SwitchAction;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
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
import static org.junit.Assert.assertThrows;

/**
 * Тестирование компиляции действия switch
 */
public class SwitchActionCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oAllDataPack());

        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testBind.object.xml"));
    }

    @Test
    public void testSwitchAction() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/switch_action/testSwitchAction.page.xml")
                .get(new PageContext("testSwitchAction"));

        SwitchAction action = (SwitchAction) ((Form) page.getRegions().get("single").get(0).getContent().get(0))
                .getToolbar().getButton("b1").getAction();
        assertThat(action.getType(), is("n2o/api/action/switch"));
        assertThat(action.getPayload().getDatasource(), is("testSwitchAction_ds1"));
        assertThat(action.getPayload().getModel(), is(ReduxModel.multi));
        assertThat(action.getPayload().getValueFieldId(), is("test1"));
        assertThat(action.getPayload().getCases().size(), is(2));
        assertThat(action.getPayload().getCases().get("A"), instanceOf(MultiAction.class));
        assertThat(action.getPayload().getCases().get("B"), instanceOf(SwitchAction.class));
        assertThat(((SwitchAction) action.getPayload().getCases().get("B")).getPayload().getDatasource(), is("testSwitchAction_ds2"));
        assertThat(((SwitchAction) action.getPayload().getCases().get("B")).getPayload().getModel(), is(ReduxModel.edit));
        assertThat(action.getPayload().getDefaultCase(), instanceOf(Perform.class));

        action = (SwitchAction) ((Form) page.getRegions().get("single").get(0).getContent().get(0))
                .getToolbar().getButton("b2").getAction();
        assertThat(action.getType(), is("n2o/api/action/switch"));
        assertThat(action.getPayload().getDatasource(), is("testSwitchAction_ds2"));
        assertThat(action.getPayload().getModel(), is(ReduxModel.filter));

        //Проверка использования датасорса страницы для switch в тулбаре (при отсутствии своего ds и ds у кнопки, для switch любой вложенности берется датасорс страницы)
        action = (SwitchAction) page.getToolbar().getButton("b3").getAction();
        assertThat(action.getPayload().getDatasource(), is("testSwitchAction_dsForPage"));
        action = (SwitchAction) action.getPayload().getCases().get("A");
        assertThat(action.getPayload().getDatasource(), is("testSwitchAction_dsForPage"));
    }

    @Test
    public void testBindSwitchAction() {
        StandardPage page = (StandardPage) bind("net/n2oapp/framework/config/metadata/compile/action/switch_action/testBindSwitchAction.page.xml")
                .get(new PageContext("testBindSwitchAction", "/p/w/:parent_id/modal"),
                        new DataSet("parent_id", 123));
        PerformButton button = (PerformButton) page.getToolbar().getButton("b1");

        SwitchAction action = (SwitchAction) button.getAction();
        assertThat(((InvokeAction) action.getPayload().getCases().get("A")).getPayload().getDataProvider().getUrl(),
                is("n2o/data/p/w/123/modal/A_case1"));
        assertThat(((InvokeAction) action.getPayload().getCases().get("B")).getPayload().getDataProvider().getUrl(),
                is("n2o/data/p/w/123/modal/B_case1"));
        assertThat(((InvokeAction) action.getPayload().getCases().get("B")).getMeta().getSuccess().getRedirect().getPath(),
                is("/123/redirect"));
        assertThat(((InvokeAction) action.getPayload().getDefaultCase()).getPayload().getDataProvider().getUrl(),
                is("n2o/data/p/w/123/modal/default_case_1"));
        assertThat(((InvokeAction) ((SwitchAction) action.getPayload().getCases().get("C")).getPayload().getCases().get("A")).getPayload().getDataProvider().getUrl(),
                is("n2o/data/p/w/123/modal/A_case2"));
        assertThat(((InvokeAction) ((SwitchAction) action.getPayload().getCases().get("C")).getPayload().getDefaultCase()).getPayload().getDataProvider().getUrl(),
                is("n2o/data/p/w/123/modal/default_case_2"));
    }

    @Test
    public void testNestedWithoutDatasource() {
        N2oException e = assertThrows(N2oException.class,
                () -> compile("net/n2oapp/framework/config/metadata/compile/action/switch_action/testNested.page.xml")
                        .get(new PageContext("testNested")));
        assertThat(e.getMessage(), is("Datasource is undefined for switch action with value-field-id=test2"));
    }
}
