package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.meta.control.ButtonField;
import net.n2oapp.framework.api.metadata.meta.control.Field;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
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

        Field field = form.getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0);
        assertThat(field.getId(), is("btn1"));
        assertThat(field, instanceOf(ButtonField.class));
        assertThat(((ButtonField)field).getAction(), notNullValue());
        assertThat(field.getSrc(), is("ButtonField"));

        assertThat(((ButtonField)field).getUrl(), Matchers.is("/test2/:param1/:param2?param3=:param3"));
        assertThat(((ButtonField)field).getTarget(), Matchers.is(Target.application));
        assertThat(((ButtonField)field).getPathMapping().size(), Matchers.is(2));
        assertThat(((ButtonField)field).getPathMapping().get("param1").getBindLink(), Matchers.is("models.resolve['$testButtonFieldCompile']"));
        assertThat(((ButtonField)field).getPathMapping().get("param1").getValue(), Matchers.is("`field1`"));
        assertThat(((ButtonField)field).getPathMapping().get("param2").getBindLink(), Matchers.is("models.resolve['$testButtonFieldCompile']"));
        assertThat(((ButtonField)field).getPathMapping().get("param2").getValue(), Matchers.is("`field2`"));
        assertThat(((ButtonField)field).getQueryMapping().size(), Matchers.is(1));
        assertThat(((ButtonField)field).getQueryMapping().get("param3").getBindLink(), Matchers.is("models.resolve['$testButtonFieldCompile']"));
        assertThat(((ButtonField)field).getQueryMapping().get("param3").getValue(), Matchers.is("`field3`"));

        field = form.getComponent().getFieldsets().get(0).getRows().get(1).getCols().get(0).getFields().get(0);
        assertThat(field.getId(), is("btn2"));
        assertThat(field, instanceOf(ButtonField.class));
        assertThat(((ButtonField)field).getAction(), notNullValue());
        assertThat(((ButtonField)field).getUrl(), is("http://ya.ru"));
        assertThat(field.getSrc(), is("ButtonField"));
    }

}
