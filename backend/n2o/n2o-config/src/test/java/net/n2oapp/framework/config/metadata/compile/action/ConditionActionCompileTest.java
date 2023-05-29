package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.api.metadata.meta.action.alert.AlertAction;
import net.n2oapp.framework.api.metadata.meta.action.condition.ConditionAction;
import net.n2oapp.framework.api.metadata.meta.action.custom.CustomAction;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.action.modal.show_modal.ShowModal;
import net.n2oapp.framework.api.metadata.meta.action.multi.MultiAction;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование компиляции условного оператора
 */
public class ConditionActionCompileTest extends SourceCompileTestBase {
    
    @Override
    @BeforeEach
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
    void testConditionAction() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/condition/testConditionAction.page.xml")
                .get(new PageContext("testConditionAction"));
        //if верхнего уровня
        ConditionAction condition = (ConditionAction) ((MultiAction) ((Form) page.getRegions().get("single").get(0).getContent().get(0))
                .getToolbar().getButton("b1").getAction()).getPayload().getActions().get(0);
        assertThat(condition.getType(), is("n2o/api/action/condition"));
        assertThat(condition.getPayload().getDatasource(), is("testConditionAction_ds1"));
        assertThat(condition.getPayload().getModel(), is(ReduxModel.edit));
        assertThat(condition.getPayload().getCondition(), is("code == 'A' && type == 1"));

        //success if-а верхнего уровня
        MultiAction success = (MultiAction) condition.getPayload().getSuccess();
        assertThat(success.getPayload().getActions().size(), is(2));
        assertThat(success.getPayload().getActions().get(0), instanceOf(CustomAction.class));

        //вложенный if success-a if-а верхнего уровня
        ConditionAction successCondition = (ConditionAction) success.getPayload().getActions().get(1);
        assertThat(successCondition.getPayload().getDatasource(), is("testConditionAction_ds1"));
        assertThat(successCondition.getPayload().getModel(), is(ReduxModel.edit));
        assertThat(successCondition.getPayload().getCondition(), is("name == 'test1'"));
        assertThat(successCondition.getPayload().getSuccess(), instanceOf(MultiAction.class));

        //вложенный else success-a if-а верхнего уровня
        assertThat(successCondition.getPayload().getFail(), instanceOf(LinkAction.class));


        //else-if верхнего уровня, он же - fail if-а верхнего уровня
        ConditionAction fail = (ConditionAction) condition.getPayload().getFail();
        assertThat(fail.getPayload().getDatasource(), is("testConditionAction_ds1"));
        assertThat(fail.getPayload().getModel(), is(ReduxModel.edit));
        assertThat(fail.getPayload().getCondition(), is("code == 'A' && type == 2"));
        assertThat(fail.getPayload().getSuccess(), instanceOf(MultiAction.class));

        //success else-if-а верхнего уровня
        MultiAction failSuccess = (MultiAction) fail.getPayload().getSuccess();
        assertThat(failSuccess.getPayload().getActions().size(), is(2));
        assertThat(failSuccess.getPayload().getActions().get(1), instanceOf(ShowModal.class));

        //вложенный if success-a else-if-a верхнего уровня
        ConditionAction failSuccessCondition = (ConditionAction) failSuccess.getPayload().getActions().get(0);
        assertThat(failSuccessCondition.getPayload().getDatasource(), is("testConditionAction_ds2"));
        assertThat(failSuccessCondition.getPayload().getModel(), is(ReduxModel.filter));
        assertThat(failSuccessCondition.getPayload().getCondition(), is("name == 'test2'"));
        assertThat(failSuccessCondition.getPayload().getSuccess(), instanceOf(CustomAction.class));

        //вложенный else success-a else-if-a верхнего уровня
        assertThat(failSuccessCondition.getPayload().getFail(), instanceOf(LinkAction.class));


        //else верхнего уровня, он же - fail else-if-а верхнего уровня
        assertThat(fail.getPayload().getFail(), instanceOf(AlertAction.class));


        //Следующий if в кнопке b1
        condition = (ConditionAction) ((MultiAction) ((Form) page.getRegions().get("single").get(0).getContent().get(0))
                .getToolbar().getButton("b1").getAction()).getPayload().getActions().get(1);
        assertThat(condition.getType(), is("n2o/api/action/condition"));
        assertThat(condition.getPayload().getDatasource(), is("testConditionAction_ds1"));
        assertThat(condition.getPayload().getModel(), is(ReduxModel.edit));
        assertThat(condition.getPayload().getCondition(), is("code == 'B'"));
        assertThat(condition.getPayload().getSuccess(), instanceOf(AlertAction.class));


        //Проверка использования датасорса страницы для if-else в тулбаре (при отсутствии своего ds и ds у кнопки, для if-else любой вложенности берется датасорс страницы)
        condition = (ConditionAction) page.getToolbar().getButton("b2").getAction();
        assertThat(condition.getPayload().getDatasource(), is("testConditionAction_dsForPage"));
        condition = (ConditionAction) condition.getPayload().getSuccess();
        assertThat(condition.getPayload().getDatasource(), is("testConditionAction_dsForPage"));
    }

    @Test
    void testBindConditionAction() {
        StandardPage page = (StandardPage) bind("net/n2oapp/framework/config/metadata/compile/action/condition/testBindConditionAction.page.xml")
                .get(new PageContext("testBindConditionAction", "/p/w/:parent_id/modal"),
                        new DataSet("parent_id", 123));
        PerformButton button = (PerformButton) page.getToolbar().getButton("b1");
        //Все урлы должны быть уникальными
        ConditionAction ifAction = (ConditionAction) button.getAction();
        String invokeUrl1 = ((InvokeAction) ifAction.getPayload().getSuccess()).getPayload().getDataProvider().getUrl();
        assertThat(invokeUrl1, is("n2o/data/p/w/123/modal/condition_2"));

        ConditionAction elseIfAction1 = (ConditionAction) ifAction.getPayload().getFail();
        String invokeUrl2 = ((InvokeAction) elseIfAction1.getPayload().getSuccess()).getPayload().getDataProvider().getUrl();
        assertThat(invokeUrl2, is("n2o/data/p/w/123/modal/condition_3"));

        ConditionAction elseIfAction2 = (ConditionAction) elseIfAction1.getPayload().getFail();
        String invokeUrl3 = ((InvokeAction) ((ConditionAction) elseIfAction2.getPayload().getSuccess()).getPayload().getSuccess())
                .getPayload().getDataProvider().getUrl();
        assertThat(invokeUrl3, is("n2o/data/p/w/123/modal/condition_5"));

        String invokeUrl4 = ((InvokeAction) elseIfAction2.getPayload().getFail()).getPayload().getDataProvider().getUrl();
        assertThat(invokeUrl4, is("n2o/data/p/w/123/modal/condition_6"));

        //проверка кейса когда мультиэкшен внутри if или else
        PerformButton button2 = (PerformButton) page.getToolbar().getButton("b2");
        InvokeAction but2Action = (InvokeAction) ((MultiAction) button2.getAction()).getPayload().getActions().get(0);
        String but2invokeUrl = but2Action.getPayload().getDataProvider().getUrl();
        assertThat(but2invokeUrl, is("n2o/data/p/w/123/modal/multi8"));
        ConditionAction but2ifAction = (ConditionAction) ((MultiAction) button2.getAction()).getPayload().getActions().get(1);
        String but2invokeUrl1 = ((InvokeAction) but2ifAction.getPayload().getSuccess()).getPayload().getDataProvider().getUrl();
        assertThat(but2invokeUrl1, is("n2o/data/p/w/123/modal/condition_10"));

        InvokeAction but2elseIfAction1 = (InvokeAction) ((MultiAction) but2ifAction.getPayload().getFail()).getPayload().getActions().get(0);
        String but2invokeUrl2 = but2elseIfAction1.getPayload().getDataProvider().getUrl();
        assertThat(but2invokeUrl2, is("n2o/data/p/w/123/modal/multi12"));
    }

    @Test
    void testNestedWithoutDatasource() {
        N2oException e = assertThrows(
                N2oException.class,
                () -> compile("net/n2oapp/framework/config/metadata/compile/action/condition/testNestedWithoutDatasource.page.xml")
                        .get(new PageContext("testNestedWithoutDatasource"))
        );
        assertThat(e.getMessage(), is("Datasource is undefined for if-branch with test=\"name == 'test1'\""));
    }
}
