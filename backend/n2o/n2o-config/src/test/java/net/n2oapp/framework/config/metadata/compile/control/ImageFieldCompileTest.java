package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.meta.control.*;
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
 * Тестирование компиляции Image с заголовком и подзаголовком
 */
public class ImageFieldCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
    }

    @Test
    public void testImageField() {
        Form form = (Form) compile("net/n2oapp/framework/config/metadata/compile/control/testImageFieldCompile.widget.xml")
                .get(new WidgetContext("testImageFieldCompile"));
        ImageField field = (ImageField) form.getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0);
        assertThat(field.getId(), is("testId"));
        assertThat(field.getSrc(), is("testSrc"));
        assertThat(field.getLabel(), is("testLabel"));
        assertThat(field.getUrl(), is("testUrl"));
        assertThat(field.getData(), is("testData"));
        assertThat(field.getTitle(), is("testTitle"));
        assertThat(field.getDescription(), is("testDescription"));
        assertThat(field.getTextPosition(), is("testPosition"));

        ControlDependency dependency = field.getDependencies().get(0);
        assertThat(dependency.getType(), is(ValidationType.reRender));
        assertThat(dependency.getOn().get(0), is("name"));
        assertThat(dependency.getOn().get(1), is("type"));

        field = (ImageField) form.getComponent().getFieldsets().get(0).getRows().get(1).getCols().get(0).getFields().get(0);
        assertThat(field.getId(), is("testId2"));
        assertThat(field.getSrc(), is("ImageField"));
        assertThat(field.getTextPosition(), is("right"));
    }

}
