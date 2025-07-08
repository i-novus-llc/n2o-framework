package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.compile.enums.ColorEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ConfirmTypeEnum;
import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.api.metadata.meta.action.condition.ConditionAction;
import net.n2oapp.framework.api.metadata.meta.action.confirm.ConfirmAction;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.action.multi.MultiAction;
import net.n2oapp.framework.api.metadata.meta.badge.PositionEnum;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.PerformButton;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Тестирование компиляции базовой кнопки
 */
class BaseButtonCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
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
        Table<?> t = (Table<?>) page.getRegions().get("single").get(0).getContent().get(0);

        AbstractButton btn = page.getToolbar().getButton("btn1");
        assertThat(btn, allOf(
                hasProperty("label", is("delete")),
                hasProperty("icon", nullValue()),
                hasProperty("color", is("`name`")),
                hasProperty("hintPosition", is("right")),
                hasProperty("className", is("Button")),
                hasProperty("style", allOf(
                        aMapWithSize(1),
                        hasEntry("color", "red")
                )),
                hasProperty("hint", is("hint")),
                hasProperty("datasource", is("testButton_table")),
                hasProperty("action", allOf(
                        instanceOf(InvokeAction.class),
                        hasProperty("payload", hasProperty("datasource", is("testButton_table")))
                ))
        ));

        btn = page.getToolbar().getButton("btn2");
        assertThat(btn, allOf(
                hasProperty("label", nullValue()),
                hasProperty("datasource", nullValue()),
                hasProperty("icon", is("fa fa-pencil")),
                hasProperty("iconPosition", is(PositionEnum.RIGHT))
        ));

        btn = page.getToolbar().getButton("btn3");
        assertThat(btn, allOf(
                hasProperty("label", is("load")),
                hasProperty("icon", is("fa fa-download")),
                hasProperty("iconPosition", is(PositionEnum.LEFT))
        ));

        btn = page.getToolbar().getButton("btn4");
        assertThat(btn.getHint(), is("`description`"));

        btn = t.getToolbar().getButton("btn5");
        assertThat(btn.getDatasource(), is("testButton_table"));

        btn = page.getToolbar().getButton("btn6");
        assertThat(btn.getModel(), is(ReduxModelEnum.DATASOURCE));

        btn = page.getToolbar().getButton("btn7");
        assertThat(btn.getAction(), allOf(
                instanceOf(MultiAction.class),
                hasProperty("payload", hasProperty("actions", contains(
                                instanceOf(ConfirmAction.class),
                                instanceOf(InvokeAction.class)
                        ))
                )));
        ConfirmAction confirm = (ConfirmAction) ((MultiAction) btn.getAction()).getPayload().getActions().get(0);
        assertThat(confirm.getPayload(), allOf(
                hasProperty("mode", is(ConfirmTypeEnum.MODAL)),
                hasProperty("title", is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.title"))),
                hasProperty("text", is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.text")))
        ));
        assertThat(confirm.getPayload().getOk(), allOf(
                hasProperty("label", is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.ok_label"))),
                hasProperty("color", is(ColorEnum.PRIMARY.getId()))
        ));
        assertThat(confirm.getPayload().getCancel(), allOf(
                hasProperty("label", is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.cancel_label"))),
                hasProperty("color", is(ColorEnum.SECONDARY.getId()))
        ));

        btn = page.getToolbar().getButton("btn8");
        confirm = (ConfirmAction) btn.getAction();

        assertThat(confirm.getPayload(), allOf(
                hasProperty("title", is("Предупреждение")),
                hasProperty("text", is("Подтвердите действие"))
        ));
        assertThat(confirm.getPayload().getOk(), allOf(
                hasProperty("label", is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.ok_label"))),
                hasProperty("color", is(ColorEnum.DANGER.getId()))
        ));
        assertThat(confirm.getPayload().getCancel(), allOf(
                hasProperty("label", is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.cancel_label"))),
                hasProperty("color", is(ColorEnum.PRIMARY.getId()))
        ));

        btn = page.getToolbar().getButton("btn9");
        assertThat(btn.getAction().getClass(), is(ConditionAction.class));
        ConditionAction conditionAction = (ConditionAction) btn.getAction();
        assertThat(conditionAction.getPayload(), allOf(
                hasProperty("condition", is("test")),
                hasProperty("datasource", is("testButton_test")),
                hasProperty("fail", nullValue())
        ));
        assertThat(conditionAction.getPayload().getSuccess().getClass(), is(ConfirmAction.class));

        confirm = (ConfirmAction) conditionAction.getPayload().getSuccess();
        assertThat(confirm.getPayload(), allOf(
                hasProperty("mode", is(ConfirmTypeEnum.MODAL)),
                hasProperty("title", is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.title"))),
                hasProperty("text", is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.text")))
        ));
        assertThat(confirm.getPayload().getOk(), allOf(
                hasProperty("label", is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.ok_label"))),
                hasProperty("color", is(ColorEnum.PRIMARY.getId()))
        ));
        assertThat(confirm.getPayload().getCancel(), allOf(
                hasProperty("label", is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.cancel_label"))),
                hasProperty("color", is(ColorEnum.SECONDARY.getId()))
        ));
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
