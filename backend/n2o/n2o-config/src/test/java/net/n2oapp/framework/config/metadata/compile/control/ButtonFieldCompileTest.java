package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ConfirmType;
import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.api.metadata.meta.control.ButtonField;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тестирование компиляции ButtonField компонента
 */
public class ButtonFieldCompileTest extends SourceCompileTestBase {
    @Override
    @Before
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
    public void testField() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/control/testButtonFieldCompile.page.xml")
                .get(new PageContext("testButtonFieldCompile"));
        Form form = (Form) page.getRegions().get("single").get(0).getContent().get(0);
        ButtonField field = (ButtonField) form.getComponent().getFieldsets().get(0).getRows().get(1).getCols().get(0).getFields().get(0);
        assertThat(field.getId(), is("btn1"));
        assertThat(field.getAction(), instanceOf(LinkAction.class));
        assertThat(field.getSrc(), is("ButtonField"));
        assertThat(field.getLabel(), is("delete"));
        assertThat(field.getIcon(), nullValue());
        assertThat(field.getColor(), is("danger"));
        assertThat(field.getValidate().get(0), is("testButtonFieldCompile_ds"));
        assertThat(field.getDatasource(), is("testButtonFieldCompile_ds"));

        assertThat(((LinkAction) field.getAction()).getUrl(), is("/testButtonFieldCompile/test2/:param1/:param2?param3=:param3"));
        assertThat(((LinkAction) field.getAction()).getTarget(), is(Target.application));
        assertThat(((LinkAction) field.getAction()).getPathMapping().size(), is(2));
        assertThat(((LinkAction) field.getAction()).getPathMapping().get("param1").getBindLink(), is("models.resolve['testButtonFieldCompile_main']"));
        assertThat(((LinkAction) field.getAction()).getPathMapping().get("param1").getValue(), is("`field1`"));
        assertThat(((LinkAction) field.getAction()).getPathMapping().get("param2").getValue(), is("1"));
        assertThat(((LinkAction) field.getAction()).getQueryMapping().size(), is(1));
        assertThat(((LinkAction) field.getAction()).getQueryMapping().get("param3").getBindLink(), is("models.resolve['testButtonFieldCompile_main']"));
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
        assertThat(field.getConfirm().getText(), is("Нажмите \"Да\", если Вы уверены в совершаемом действии. Или \"Нет\", если ещё хотите обдумать совершаемое действие."));
        assertThat(field.getConfirm().getTitle(), is("Предупреждение"));
        assertThat(field.getConfirm().getOk().getLabel(), is("Да"));
        assertThat(field.getConfirm().getOk().getColor(), is("primary"));
        assertThat(field.getConfirm().getCancel().getLabel(), is("Нет"));
        assertThat(field.getConfirm().getCancel().getColor(), is("secondary"));
        assertThat(field.getConfirm().getReverseButtons(), is(false));
        assertThat(field.getConfirm().getCloseButton(), is(false));
        assertThat(field.getConfirm().getMode(), is(ConfirmType.MODAL));
        assertThat(field.getConfirm().getCondition(), is("`true`"));
        assertThat(field.getConfirm().getModelLink(), is("models.resolve['testButtonFieldCompile_main']"));

        field = (ButtonField) form.getComponent().getFieldsets().get(0).getRows().get(7).getCols().get(0).getFields().get(0);
        assertThat(field.getConfirm().getText(), is("Зарегистрировать заявление?"));
        assertThat(field.getConfirm().getTitle(), is("Предупреждение"));
        assertThat(field.getConfirm().getOk().getLabel(), is("Зарегистрировать"));
        assertThat(field.getConfirm().getOk().getColor(), is("dark"));
        assertThat(field.getConfirm().getCancel().getLabel(), is("Отмена"));
        assertThat(field.getConfirm().getCancel().getColor(), is("light"));
        assertThat(field.getConfirm().getReverseButtons(), is(false));
        assertThat(field.getConfirm().getCloseButton(), is(false));
        assertThat(field.getConfirm().getMode(), is(ConfirmType.MODAL));
        assertThat(field.getConfirm().getCondition(), is("`test === '1'|| test === '2'`"));
        assertThat(field.getConfirm().getModelLink(), is("models.resolve['testButtonFieldCompile_main']"));

        field = (ButtonField) form.getComponent().getFieldsets().get(0).getRows().get(8).getCols().get(0).getFields().get(0);
        assertThat(field.getDescription(), is("`description`"));
        assertThat(field.getHint(), is("`description`"));
    }
}
