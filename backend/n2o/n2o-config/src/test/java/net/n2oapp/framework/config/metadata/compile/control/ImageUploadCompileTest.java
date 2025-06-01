package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeTypeEnum;
import net.n2oapp.framework.api.metadata.meta.control.DefaultValues;
import net.n2oapp.framework.api.metadata.meta.control.ImageUpload;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oControlsV2IOPack;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

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
        builder.packs(new N2oPagesPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsV2IOPack());
        builder.compilers(new ImageUploadCompiler());
    }

    @Test
    void testImageUpload() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/control/testImageUpload.page.xml")
                .get(new PageContext("testImageUpload"));
        Form form = (Form) page.getWidget();

        ImageUpload imageUpload = (ImageUpload) ((StandardField) form.getComponent().getFieldsets().get(0).getRows().get(0)
                .getCols().get(0).getFields().get(0)).getControl();
        assertThat(imageUpload.getSrc(), is("ImageUploader"));
        assertThat(imageUpload.getListType(), is(ImageUpload.ListTypeEnum.CARD));
        assertThat(imageUpload.getCanLightbox(), is(true));
        assertThat(imageUpload.getCanDelete(), is(false));
        assertThat(imageUpload.getWidth(), is("500px"));
        assertThat(imageUpload.getHeight(), is("400px"));
        assertThat(imageUpload.getIcon(), is("fa fa-plus"));
        assertThat(imageUpload.getIconSize(), is("150px"));
        assertThat(imageUpload.getShowTooltip(), is(false));
        assertThat(imageUpload.getShape(), is(ShapeTypeEnum.CIRCLE));
        assertThat(imageUpload.getAjax(), is(false));
        assertThat(imageUpload.getMulti(), is(true));
        assertThat(imageUpload.getShowSize(), is(false));
        assertThat(imageUpload.getShowName(), is(true));
        assertThat(imageUpload.getAccept(), is(".img,.png"));
        assertThat(imageUpload.getUploadUrl(), is("/upload"));
        assertThat(imageUpload.getDeleteUrl(), is("/delete"));
        assertThat(imageUpload.getValueFieldId(), is("valueId"));
        assertThat(imageUpload.getLabelFieldId(), is("labelId"));
        assertThat(imageUpload.getUrlFieldId(), is("urlId"));
        assertThat(imageUpload.getResponseFieldId(), is("messageId"));
        assertThat(imageUpload.getRequestParam(), is("param"));
        DefaultValues values = (DefaultValues) page.getModels().get("resolve['testImageUpload_w1'].imageUpload").getValue();
        assertThat(values.getValues().get("id"), is("value1"));
        assertThat(values.getValues().get("filename"), is("file"));
        assertThat(values.getValues().get("url"), is("/test"));

        ImageUpload imageUpload2 = (ImageUpload) ((StandardField) form.getComponent().getFieldsets().get(0).getRows().get(1)
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
