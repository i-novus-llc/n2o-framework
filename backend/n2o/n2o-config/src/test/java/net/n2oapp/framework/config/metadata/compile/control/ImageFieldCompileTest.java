package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeTypeEnum;
import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.api.metadata.meta.cell.ImageStatusElementPlaceEnum;
import net.n2oapp.framework.api.metadata.meta.control.ControlDependency;
import net.n2oapp.framework.api.metadata.meta.control.ImageField;
import net.n2oapp.framework.api.metadata.meta.control.TextPositionEnum;
import net.n2oapp.framework.api.metadata.meta.control.ValidationTypeEnum;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.hasProperty;

/**
 * Тестирование компиляции компонента вывода изображения
 */
class ImageFieldCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack(), new N2oActionsPack());
    }

    @Test
    void testImageField() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/control/testImageFieldCompile.page.xml")
                .get(new PageContext("testImageFieldCompile"));
        Form form = (Form) page.getWidget();
        ImageField field = (ImageField) form.getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0);
        assertThat(field, allOf(
                hasProperty("id", is("testId")),
                hasProperty("src", is("testSrc")),
                hasProperty("label", is("testLabel")),
                hasProperty("data", is("`'data:image/jpeg;base64'+image`")),
                hasProperty("title", is("`title`")),
                hasProperty("description", is("`Description`")),
                hasProperty("textPosition", is(TextPositionEnum.TOP)),
                hasProperty("shape", is(ShapeTypeEnum.CIRCLE)),
                hasProperty("width", is("500px")),
                hasProperty("statuses", arrayWithSize(2))));
        assertThat(field.getStatuses()[0].getSrc(), Matchers.is("testSrc"));
        assertThat(field.getStatuses()[0].getFieldId(), Matchers.is("status1"));
        assertThat(field.getStatuses()[0].getIcon(), Matchers.is("`icon1`"));
        assertThat(field.getStatuses()[0].getPlace(), Matchers.is(ImageStatusElementPlaceEnum.TOP_RIGHT));
        assertThat(field.getStatuses()[1].getSrc(), Matchers.is("Status"));
        assertThat(field.getStatuses()[1].getFieldId(), Matchers.is("id"));
        assertThat(field.getStatuses()[1].getIcon(), Matchers.is(nullValue()));
        assertThat(field.getStatuses()[1].getPlace(), Matchers.is(ImageStatusElementPlaceEnum.TOP_LEFT));
        assertThat(field.getAction(), notNullValue());
        assertThat(((LinkAction) field.getAction()).getUrl(), is("http://example.com"));

        ControlDependency dependency = field.getDependencies().get(0);
        assertThat(dependency.getType(), is(ValidationTypeEnum.RE_RENDER));
        assertThat(dependency.getOn().get(0), is("name"));
        assertThat(dependency.getOn().get(1), is("type"));

        field = (ImageField) form.getComponent().getFieldsets().get(0).getRows().get(1).getCols().get(0).getFields().get(0);
        assertThat(field.getId(), is("testId2"));
        assertThat(field.getSrc(), is("ImageField"));
        assertThat(field.getTextPosition(), is(TextPositionEnum.RIGHT));
        assertThat(field.getShape(), is(ShapeTypeEnum.ROUNDED));

        field = (ImageField) form.getComponent().getFieldsets().get(0).getRows().get(2).getCols().get(0).getFields().get(0);
        assertThat(field.getId(), is("testId3"));
        assertThat(field.getSrc(), is("ImageField"));
        assertThat(field.getData(), is("testImage"));
        assertThat(field.getWidth(), is("135px"));
    }
}
