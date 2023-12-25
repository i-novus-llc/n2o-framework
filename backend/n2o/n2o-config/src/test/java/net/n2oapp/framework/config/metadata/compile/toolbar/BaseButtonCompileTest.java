package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.compile.enums.Color;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ConfirmType;
import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.api.metadata.meta.action.condition.ConditionAction;
import net.n2oapp.framework.api.metadata.meta.action.confirm.ConfirmAction;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.action.multi.MultiAction;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Condition;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.PerformButton;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Тестирование компиляции базовой кнопки
 */
public class BaseButtonCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oWidgetsPack(), new N2oActionsPack(), new N2oAllDataPack(), new N2oPagesPack(), new N2oRegionsPack())
                .sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.object.xml"));
    }

    @Test
    void testButton() {
       StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/toolbar/testButton.page.xml")
                .get(new PageContext("testButton"));
        Table t = (Table) page.getRegions().get("single").get(0).getContent().get(0);

        AbstractButton btn = page.getToolbar().getButton("btn1");
        assertThat(btn.getLabel(), is("delete"));
        assertThat(btn.getIcon(), is(nullValue()));
        assertThat(btn.getColor(), is("danger"));
        assertThat(btn.getHintPosition(), is("right"));
        assertThat(btn.getClassName(), is("Button"));
        assertThat(btn.getStyle().size(), is(1));
        assertThat(btn.getStyle().get("color"), is("red"));
        assertThat(btn.getHint(), is("hint"));
        assertThat(btn.getDatasource(), is("testButton_table"));
        assertThat(((InvokeAction) btn.getAction()).getPayload().getDatasource(), is("testButton_table"));

        btn = page.getToolbar().getButton("btn2");
        assertThat(btn.getLabel(), is(nullValue()));
        assertThat(btn.getDatasource(), is(nullValue()));
        assertThat(btn.getIcon(), is("fa fa-pencil"));

        btn = page.getToolbar().getButton("btn3");
        assertThat(btn.getLabel(), is("load"));
        assertThat(btn.getIcon(), is("fa fa-download"));

        btn = page.getToolbar().getButton("btn4");
        assertThat(btn.getHint(), is("`description`"));

        btn = t.getToolbar().getButton("btn5");
        assertThat(btn.getDatasource(), is("testButton_table"));

        btn = page.getToolbar().getButton("btn6");
        assertThat(btn.getModel(), is(ReduxModel.datasource));

        btn = page.getToolbar().getButton("btn7");
        assertThat(btn.getAction().getClass(), is(MultiAction.class));
        assertThat(((MultiAction) btn.getAction()).getPayload().getActions().get(0).getClass(), is(ConfirmAction.class));
        assertThat(((MultiAction) btn.getAction()).getPayload().getActions().get(1).getClass(), is(InvokeAction.class));
        ConfirmAction confirm = (ConfirmAction) ((MultiAction) btn.getAction()).getPayload().getActions().get(0);
        assertThat(confirm.getPayload().getMode(), is(ConfirmType.MODAL));
        assertThat(confirm.getPayload().getTitle(), is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.title")));
        assertThat(confirm.getPayload().getText(), is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.text")));
        assertThat(confirm.getPayload().getOk().getLabel(), is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.ok_label")));
        assertThat(confirm.getPayload().getOk().getColor(), is(Color.primary.name()));
        assertThat(confirm.getPayload().getCancel().getLabel(), is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.cancel_label")));
        assertThat(confirm.getPayload().getCancel().getColor(), is(Color.secondary.name()));

        btn = page.getToolbar().getButton("btn8");
        confirm = (ConfirmAction)  btn.getAction();

        assertThat(confirm.getPayload().getTitle(), is("Предупреждение"));
        assertThat(confirm.getPayload().getText(), is("Подтвердите действие"));
        assertThat(confirm.getPayload().getOk().getLabel(), is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.ok_label")));
        assertThat(confirm.getPayload().getOk().getColor(), is(Color.danger.name()));
        assertThat(confirm.getPayload().getCancel().getLabel(), is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.cancel_label")));
        assertThat(confirm.getPayload().getCancel().getColor(), is(Color.primary.name()));

        btn = page.getToolbar().getButton("btn9");
        assertThat(btn.getAction().getClass(), is(ConditionAction.class));
        ConditionAction conditionAction = (ConditionAction) btn.getAction();
        assertThat(conditionAction.getPayload().getCondition(), is("test"));
        assertThat(conditionAction.getPayload().getDatasource(), is("testButton_test"));
        assertThat(conditionAction.getPayload().getSuccess().getClass(), is(ConfirmAction.class));
        assertThat(conditionAction.getPayload().getFail(), is(nullValue()));

        confirm = (ConfirmAction) conditionAction.getPayload().getSuccess();
        assertThat(confirm.getPayload().getMode(), is(ConfirmType.MODAL));
        assertThat(confirm.getPayload().getTitle(), is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.title")));
        assertThat(confirm.getPayload().getText(), is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.text")));
        assertThat(confirm.getPayload().getOk().getLabel(), is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.ok_label")));
        assertThat(confirm.getPayload().getOk().getColor(), is(Color.primary.name()));
        assertThat(confirm.getPayload().getCancel().getLabel(), is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.cancel_label")));
        assertThat(confirm.getPayload().getCancel().getColor(), is(Color.secondary.name()));
    }

    @Test
    void generateButton() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/toolbar/generateButton.page.xml")
                .get(new PageContext("generateButton"));

        assertThat(page.getWidget().getToolbar().size(), is(1));
        assertThat(page.getWidget().getToolbar().get("topLeft").size(), is(1));
        assertThat(page.getWidget().getToolbar().get("topLeft").get(0).getButtons().size(), is(1));

        PerformButton button = (PerformButton) page.getWidget().getToolbar().get("topLeft").get(0).getButtons().get(0);
        assertThat(button.getId(), is("close"));
        assertThat(button.getLabel(), is("Закрыть"));
        assertThat(button.getAction(), instanceOf(LinkAction.class));
        assertThat(button.getProperties().size(), is(1));
        assertThat(button.getProperties().get("label"), is("newLabel"));
    }
}
