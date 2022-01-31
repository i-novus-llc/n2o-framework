package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.meta.control.ControlDependency;
import net.n2oapp.framework.api.metadata.meta.control.Text;
import net.n2oapp.framework.api.metadata.meta.control.ValidationType;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование компиляции TextField
 */
public class TextFieldCompileTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack(),
                new N2oFieldSetsPack(), new N2oAllDataPack(), new N2oControlsPack());
    }

    @Test
    public void testTextField(){
        SimplePage page =(SimplePage) compile("net/n2oapp/framework/config/metadata/compile/control/testTextFieldCompile.page.xml")
                .get(new PageContext("testTextFieldCompile"));
        Form form = (Form) page.getWidget();
        Text field = (Text)form.getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0);

        assertThat(field.getSrc(), is("TextField"));
        assertThat(field.getText(), is("`'Hello, '+username`"));
        assertThat(field.getFormat(), is("formatTest"));
        assertThat(field.getClassName(), is("testClass"));
        assertThat(field.getVisible(), is(false));

        ControlDependency dependency = field.getDependencies().get(0);
        assertThat(dependency.getType(),is(ValidationType.visible));
        assertThat(dependency.getExpression(), is("false"));
        assertThat(dependency.getApplyOnInit(), is(true));

        dependency = field.getDependencies().get(1);
        assertThat(dependency.getType(),is(ValidationType.reRender));
        assertThat(dependency.getOn().get(0), is("type"));
        assertThat(dependency.getExpression(), nullValue());
        assertThat(dependency.getApplyOnInit(), nullValue());
    }

}
