package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeTypeEnum;
import net.n2oapp.framework.api.metadata.meta.control.DefaultValues;
import net.n2oapp.framework.api.metadata.meta.control.ImageUpload;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;

/**
 * Тестирование компиляции компонента загрузки изображений
 */
class ImageUploadCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oAllDataPack());
    }

    @Test
    void testImageUpload() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/control/testImageUpload.page.xml")
                .get(new PageContext("testImageUpload"));
        Form form = (Form) page.getWidget();

        ImageUpload imageUpload = (ImageUpload) ((StandardField<?>) form.getComponent().getFieldsets().get(0).getRows().get(0)
                .getCols().get(0).getFields().get(0)).getControl();
        assertThat(imageUpload, allOf(
                hasProperty("src", is("ImageUploader")),
                hasProperty("listType", is(ImageUpload.ListTypeEnum.CARD)),
                hasProperty("canLightbox", is(true)),
                hasProperty("canDelete", is(false)),
                hasProperty("width", is("500px")),
                hasProperty("height", is("400px")),
                hasProperty("icon", is("fa fa-plus")),
                hasProperty("iconSize", is("150px")),
                hasProperty("showTooltip", is(false)),
                hasProperty("shape", is(ShapeTypeEnum.CIRCLE)),
                hasProperty("ajax", is(false)),
                hasProperty("multi", is(true)),
                hasProperty("showSize", is(false)),
                hasProperty("showName", is(true)),
                hasProperty("accept", is(".img,.png")),
                hasProperty("uploadUrl", is("/upload")),
                hasProperty("deleteUrl", is("/delete")),
                hasProperty("valueFieldId", is("valueId")),
                hasProperty("labelFieldId", is("labelId")),
                hasProperty("urlFieldId", is("urlId")),
                hasProperty("responseFieldId", is("messageId")),
                hasProperty("requestParam", is("param"))
        ));
        DefaultValues values = (DefaultValues) page.getModels().get("resolve['testImageUpload_w1'].imageUpload").getValue();
        assertThat(values.getValues().get("id"), is("value1"));
        assertThat(values.getValues().get("filename"), is("file"));
        assertThat(values.getValues().get("url"), is("/test"));

        ImageUpload imageUpload2 = (ImageUpload) ((StandardField<?>) form.getComponent().getFieldsets().get(0).getRows().get(1)
                .getCols().get(0).getFields().get(0)).getControl();
        assertThat(imageUpload2.getListType(), is(ImageUpload.ListTypeEnum.IMAGE));
        assertThat(imageUpload2.getCanLightbox(), is(false));
        assertThat(imageUpload2.getAjax(), is(true));
        assertThat(imageUpload2.getMulti(), is(false));
        assertThat(imageUpload2.getShowSize(), is(true));
        assertThat(imageUpload2.getShowName(), is(false));
        assertThat(imageUpload2.getValueFieldId(), is("id"));
        assertThat(imageUpload2.getLabelFieldId(), is("name"));
        assertThat(imageUpload2.getUrlFieldId(), is("url"));
        assertThat(imageUpload2.getResponseFieldId(), is("message"));
        assertThat(imageUpload2.getRequestParam(), is("file"));
        assertThat(imageUpload2.getCanDelete(), is(true));
        assertThat(imageUpload2.getShowTooltip(), is(true));
        assertThat(imageUpload2.getShape(), is(ShapeTypeEnum.ROUNDED));
        assertThat(imageUpload2.getWidth(), nullValue());
        assertThat(imageUpload2.getHeight(), nullValue());
        assertThat(imageUpload2.getIconSize(), nullValue());
    }
}
