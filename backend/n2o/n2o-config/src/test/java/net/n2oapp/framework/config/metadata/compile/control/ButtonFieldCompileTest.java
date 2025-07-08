package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.compile.enums.ColorEnum;
import net.n2oapp.framework.api.metadata.global.view.action.control.TargetEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ConfirmTypeEnum;
import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.api.metadata.meta.action.confirm.ConfirmAction;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkActionImpl;
import net.n2oapp.framework.api.metadata.meta.action.multi.MultiAction;
import net.n2oapp.framework.api.metadata.meta.control.ButtonField;
import net.n2oapp.framework.api.metadata.meta.control.EnablingDependency;
import net.n2oapp.framework.api.metadata.meta.control.ValidationTypeEnum;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Тестирование компиляции ButtonField компонента
 */
class ButtonFieldCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack(),
                new N2oFieldSetsPack(), new N2oAllDataPack(), new N2oControlsV2IOPack(), new N2oControlsPack());
    }

    @Test
    void testField() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/control/testButtonFieldCompile.page.xml")
                .get(new PageContext("testButtonFieldCompile"));
        Form form = (Form) page.getRegions().get("single").get(0).getContent().get(0);
        ButtonField field = (ButtonField) form.getComponent().getFieldsets().get(0).getRows().get(1).getCols().get(0).getFields().get(0);

        assertThat(field, allOf(
                hasProperty("id", is("btn1")),
                hasProperty("action", instanceOf(LinkAction.class)),
                hasProperty("src", is("ButtonField")),
                hasProperty("label", is("delete")),
                hasProperty("icon", nullValue()),
                hasProperty("rounded", is(false)),
                hasProperty("color", is("`name`")),
                hasProperty("validate", contains("testButtonFieldCompile_ds")),
                hasProperty("datasource", is("testButtonFieldCompile_ds"))
        ));
        assertThat(field.getDependencies().get(0).getExpression(), is("test == null"));
        assertThat(field.getDependencies().get(0).getOn().get(0), is("url"));
        assertThat(field.getDependencies().get(0).getType(), is(ValidationTypeEnum.ENABLED));
        assertThat(((EnablingDependency) field.getDependencies().get(0)).getMessage(), is("test message"));

        assertThat(((LinkAction) field.getAction()).getUrl(), is("/test2/:param1/:param2?param3=:param3"));
        assertThat(((LinkAction) field.getAction()).getTarget(), is(TargetEnum.APPLICATION));
        assertThat(((LinkAction) field.getAction()).getPathMapping().size(), is(2));
        assertThat(((LinkAction) field.getAction()).getPathMapping().get("param1").getLink(), is("models.resolve['testButtonFieldCompile_ds']"));
        assertThat(((LinkAction) field.getAction()).getPathMapping().get("param1").getValue(), is("`field1`"));
        assertThat(((LinkAction) field.getAction()).getPathMapping().get("param2").getValue(), is("1"));
        assertThat(((LinkAction) field.getAction()).getQueryMapping().size(), is(1));
        assertThat(((LinkAction) field.getAction()).getQueryMapping().get("param3").getLink(), is("models.resolve['testButtonFieldCompile_ds']"));
        assertThat(((LinkAction) field.getAction()).getQueryMapping().get("param3").getValue(), is("`field3`"));

        field = (ButtonField) form.getComponent().getFieldsets().get(0).getRows().get(2).getCols().get(0).getFields().get(0);
        assertThat(field, allOf(
                hasProperty("id", is("btn2")),
                hasProperty("action", allOf(
                        instanceOf(LinkAction.class),
                        hasProperty("url", is("http://ya.ru"))
                )),
                hasProperty("src", is("ButtonField")),
                hasProperty("label", nullValue()),
                hasProperty("icon", is("fa fa-pencil")),
                hasProperty("rounded", is(true))
        ));

        field = (ButtonField) form.getComponent().getFieldsets().get(0).getRows().get(3).getCols().get(0).getFields().get(0);
        assertThat(field, allOf(
                hasProperty("id", is("btn3")),
                hasProperty("src", is("ButtonField")),
                hasProperty("label", is("load")),
                hasProperty("icon", is("fa fa-download"))
        ));

        field = (ButtonField) form.getComponent().getFieldsets().get(0).getRows().get(5).getCols().get(0).getFields().get(0);
        assertThat(field, allOf(
                hasProperty("id", is("btn5")),
                hasProperty("src", is("ButtonField"))
        ));

        field = (ButtonField) form.getComponent().getFieldsets().get(0).getRows().get(6).getCols().get(0).getFields().get(0);
        assertThat(field, allOf(
                hasProperty("description", is("`description`")),
                hasProperty("hint", is("`description`"))
        ));

        field = (ButtonField) form.getComponent().getFieldsets().get(0).getRows().get(7).getCols().get(0).getFields().get(0);
        assertThat(field.getAction(), allOf(
                instanceOf(MultiAction.class),
                hasProperty("payload", hasProperty("actions", contains(
                                instanceOf(ConfirmAction.class),
                                instanceOf(LinkActionImpl.class)
                        ))
                )));

        ConfirmAction confirm = (ConfirmAction) ((MultiAction) field.getAction()).getPayload().getActions().get(0);
        assertThat(confirm.getPayload(), allOf(
                hasProperty("mode", is(ConfirmTypeEnum.MODAL)),
                hasProperty("title", is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.title"))),
                hasProperty("text", is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.text"))),
                hasProperty("ok", allOf(
                        hasProperty("label", is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.ok_label"))),
                        hasProperty("color", is(ColorEnum.PRIMARY.getId()))
                )),
                hasProperty("cancel", allOf(
                        hasProperty("label", is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.cancel_label"))),
                        hasProperty("color", is(ColorEnum.SECONDARY.getId()))
                ))
        ));

        field = (ButtonField) form.getComponent().getFieldsets().get(0).getRows().get(8).getCols().get(0).getFields().get(0);
        confirm = (ConfirmAction) field.getAction();
        assertThat(confirm.getPayload(), allOf(
                hasProperty("title", is("Предупреждение")),
                hasProperty("text", is("Подтвердите действие")),
                hasProperty("ok", allOf(
                        hasProperty("label", is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.ok_label"))),
                        hasProperty("color", is(ColorEnum.DANGER.getId()))
                )),
                hasProperty("cancel", allOf(
                        hasProperty("label", is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.cancel_label"))),
                        hasProperty("color", is(ColorEnum.PRIMARY.getId()))
                ))
        ));
    }
}
