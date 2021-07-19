package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.global.view.widget.table.ImageShape;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.ImageStatusElement;
import net.n2oapp.framework.api.metadata.meta.control.ControlDependency;
import net.n2oapp.framework.api.metadata.meta.control.ImageField;
import net.n2oapp.framework.api.metadata.meta.control.TextPosition;
import net.n2oapp.framework.api.metadata.meta.control.ValidationType;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.N2oControlsPack;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование компиляции компонента вывода изображения
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
        assertThat(field.getUrl(), is("`Url`"));
        assertThat(field.getData(), is("`'data:image/jpeg;base64'+image`"));
        assertThat(field.getTitle(), is("`title`"));
        assertThat(field.getDescription(), is("`Description`"));
        assertThat(field.getTextPosition(), is(TextPosition.top));
        assertThat(field.getShape(), is(ImageShape.circle));
        assertThat(field.getWidth(), is("500px"));
        assertThat(field.getStatuses().length, is(2));
        assertThat(field.getStatuses()[0].getSrc(), Matchers.is("testSrc"));
        assertThat(field.getStatuses()[0].getFieldId(), Matchers.is("status1"));
        assertThat(field.getStatuses()[0].getIcon(), Matchers.is("`icon1`"));
        assertThat(field.getStatuses()[0].getPlace(), Matchers.is(ImageStatusElement.Place.topRight));
        assertThat(field.getStatuses()[1].getSrc(), Matchers.is("Status"));
        assertThat(field.getStatuses()[1].getFieldId(), Matchers.is("id"));
        assertThat(field.getStatuses()[1].getIcon(), Matchers.is(nullValue()));
        assertThat(field.getStatuses()[1].getPlace(), Matchers.is(ImageStatusElement.Place.topLeft));

        ControlDependency dependency = field.getDependencies().get(0);
        assertThat(dependency.getType(), is(ValidationType.reRender));
        assertThat(dependency.getOn().get(0), is("name"));
        assertThat(dependency.getOn().get(1), is("type"));

        field = (ImageField) form.getComponent().getFieldsets().get(0).getRows().get(1).getCols().get(0).getFields().get(0);
        assertThat(field.getId(), is("testId2"));
        assertThat(field.getSrc(), is("ImageField"));
        assertThat(field.getTextPosition(), is(TextPosition.right));
    }
}
