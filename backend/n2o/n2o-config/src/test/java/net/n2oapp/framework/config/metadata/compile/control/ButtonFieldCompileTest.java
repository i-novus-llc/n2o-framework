package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.meta.control.ButtonField;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

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
        Form form = (Form) compile("net/n2oapp/framework/config/metadata/compile/control/testButtonFieldCompile.widget.xml")
                .get(new WidgetContext("testButtonFieldCompile"));

        ButtonField field = (ButtonField) form.getComponent().getFieldsets().get(0).getRows().get(1).getCols().get(0).getFields().get(0);
        assertThat(field.getId(), is("btn1"));
        assertThat(field.getAction(), notNullValue());
        assertThat(field.getSrc(), is("ButtonField"));
        assertThat(field.getLabel(), is("delete"));
        assertThat(field.getIcon(), nullValue());
        assertThat(field.getColor(), is("danger"));

        assertThat(field.getUrl(), is("/test2/:param1/:param2?param3=:param3"));
        assertThat(field.getTarget(), is(Target.application));
        assertThat(field.getPathMapping().size(), is(2));
        assertThat(field.getPathMapping().get("param1").getBindLink(), is("models.resolve['$testButtonFieldCompile']"));
        assertThat(field.getPathMapping().get("param1").getValue(), is("`field1`"));
        assertThat(field.getPathMapping().get("param2").getValue(), is("1"));
        assertThat(field.getQueryMapping().size(), is(1));
        assertThat(field.getQueryMapping().get("param3").getBindLink(), is("models.resolve['$testButtonFieldCompile']"));
        assertThat(field.getQueryMapping().get("param3").getValue(), is("`field3`"));

        field = (ButtonField) form.getComponent().getFieldsets().get(0).getRows().get(2).getCols().get(0).getFields().get(0);
        assertThat(field.getId(), is("btn2"));
        assertThat(field.getAction(), notNullValue());
        assertThat(field.getUrl(), is("http://ya.ru"));
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
        assertThat(field.getUrl(), is("`url`"));
    }
}
