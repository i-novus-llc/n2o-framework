package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.compile.enums.Color;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ConfirmType;
import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.api.metadata.meta.action.confirm.ConfirmAction;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkActionImpl;
import net.n2oapp.framework.api.metadata.meta.action.multi.MultiAction;
import net.n2oapp.framework.api.metadata.meta.control.ButtonField;
import net.n2oapp.framework.api.metadata.meta.control.EnablingDependency;
import net.n2oapp.framework.api.metadata.meta.control.ValidationType;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тестирование компиляции ButtonField компонента
 */
class ButtonFieldCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() throws Exception {
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
        assertThat(field.getId(), is("btn1"));
        assertThat(field.getAction(), instanceOf(LinkAction.class));
        assertThat(field.getSrc(), is("ButtonField"));
        assertThat(field.getLabel(), is("delete"));
        assertThat(field.getIcon(), nullValue());
        assertThat(field.getColor(), is("`name`"));
        assertThat(field.getValidate().get(0), is("testButtonFieldCompile_ds"));
        assertThat(field.getDatasource(), is("testButtonFieldCompile_ds"));
        assertThat(field.getDependencies().get(0).getExpression(), is("test == null"));
        assertThat(field.getDependencies().get(0).getOn().get(0), is("url"));
        assertThat(field.getDependencies().get(0).getType(), is(ValidationType.enabled));
        assertThat(((EnablingDependency) field.getDependencies().get(0)).getMessage(), is("test message"));

        assertThat(((LinkAction) field.getAction()).getUrl(), is("/testButtonFieldCompile/test2/:param1/:param2?param3=:param3"));
        assertThat(((LinkAction) field.getAction()).getTarget(), is(Target.application));
        assertThat(((LinkAction) field.getAction()).getPathMapping().size(), is(2));
        assertThat(((LinkAction) field.getAction()).getPathMapping().get("param1").getBindLink(), is("models.resolve['testButtonFieldCompile_ds']"));
        assertThat(((LinkAction) field.getAction()).getPathMapping().get("param1").getValue(), is("`field1`"));
        assertThat(((LinkAction) field.getAction()).getPathMapping().get("param2").getValue(), is("1"));
        assertThat(((LinkAction) field.getAction()).getQueryMapping().size(), is(1));
        assertThat(((LinkAction) field.getAction()).getQueryMapping().get("param3").getBindLink(), is("models.resolve['testButtonFieldCompile_ds']"));
        assertThat(((LinkAction) field.getAction()).getQueryMapping().get("param3").getValue(), is("`field3`"));

        field = (ButtonField) form.getComponent().getFieldsets().get(0).getRows().get(2).getCols().get(0).getFields().get(0);
        assertThat(field.getId(), is("btn2"));
        assertThat(field.getAction(), instanceOf(LinkAction.class));
        assertThat(((LinkAction) field.getAction()).getUrl(), is("http://ya.ru"));
        assertThat(field.getSrc(), is("ButtonField"));
        assertThat(field.getLabel(), nullValue());
        assertThat(field.getIcon(), is("fa fa-pencil"));

        field = (ButtonField) form.getComponent().getFieldsets().get(0).getRows().get(3).getCols().get(0).getFields().get(0);
        assertThat(field.getId(), is("btn3"));
        assertThat(field.getSrc(), is("ButtonField"));
        assertThat(field.getLabel(), is("load"));
        assertThat(field.getIcon(), is("fa fa-download"));

        field = (ButtonField) form.getComponent().getFieldsets().get(0).getRows().get(5).getCols().get(0).getFields().get(0);
        assertThat(field.getId(), is("btn5"));
        assertThat(field.getSrc(), is("ButtonField"));

        field = (ButtonField) form.getComponent().getFieldsets().get(0).getRows().get(6).getCols().get(0).getFields().get(0);
        assertThat(field.getDescription(), is("`description`"));
        assertThat(field.getHint(), is("`description`"));

        field = (ButtonField) form.getComponent().getFieldsets().get(0).getRows().get(7).getCols().get(0).getFields().get(0);
        assertThat(field.getAction().getClass(), is(MultiAction.class));
        assertThat(((MultiAction) field.getAction()).getPayload().getActions().get(0).getClass(), is(ConfirmAction.class));
        assertThat(((MultiAction) field.getAction()).getPayload().getActions().get(1).getClass(), is(LinkActionImpl.class));
        ConfirmAction confirm = (ConfirmAction) ((MultiAction) field.getAction()).getPayload().getActions().get(0);
        assertThat(confirm.getPayload().getMode(), is(ConfirmType.MODAL));
        assertThat(confirm.getPayload().getTitle(), is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.title")));
        assertThat(confirm.getPayload().getText(), is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.text")));
        assertThat(confirm.getPayload().getOk().getLabel(), is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.ok_label")));
        assertThat(confirm.getPayload().getOk().getColor(), is(Color.primary.name()));
        assertThat(confirm.getPayload().getCancel().getLabel(), is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.cancel_label")));
        assertThat(confirm.getPayload().getCancel().getColor(), is(Color.secondary.name()));

        field = (ButtonField) form.getComponent().getFieldsets().get(0).getRows().get(8).getCols().get(0).getFields().get(0);
        confirm = (ConfirmAction)  field.getAction();

        assertThat(confirm.getPayload().getTitle(), is("Предупреждение"));
        assertThat(confirm.getPayload().getText(), is("Подтвердите действие"));
        assertThat(confirm.getPayload().getOk().getLabel(), is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.ok_label")));
        assertThat(confirm.getPayload().getOk().getColor(), is(Color.danger.name()));
        assertThat(confirm.getPayload().getCancel().getLabel(), is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.cancel_label")));
        assertThat(confirm.getPayload().getCancel().getColor(), is(Color.primary.name()));
    }
}
