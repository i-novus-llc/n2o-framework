package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.meta.control.DefaultValues;
import net.n2oapp.framework.api.metadata.meta.control.FileUpload;
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
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование компиляции компонента загрузки файлов
 */
public class FileUploadCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsV2IOPack());
        builder.compilers(new FileUploadCompiler());
    }

    @Test
    void testFileUpload() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/control/testFileUpload.page.xml")
                .get(new PageContext("testFileUpload"));
        Form form = (Form) page.getWidget();

        FileUpload fileUpload = (FileUpload) ((StandardField) form.getComponent().getFieldsets().get(0).getRows().get(0)
                .getCols().get(0).getFields().get(0)).getControl();
        assertThat(fileUpload.getSrc(), is("ButtonUploader"));
        assertThat(fileUpload.getAjax(), is(false));
        assertThat(fileUpload.getMulti(), is(true));
        assertThat(fileUpload.getShowSize(), is(false));
        assertThat(fileUpload.getAccept(), is(".img,.png"));
        assertThat(fileUpload.getUploadUrl(), is("/upload"));
        assertThat(fileUpload.getDeleteUrl(), is("/delete"));
        assertThat(fileUpload.getValueFieldId(), is("valueId"));
        assertThat(fileUpload.getLabelFieldId(), is("labelId"));
        assertThat(fileUpload.getUrlFieldId(), is("urlId"));
        assertThat(fileUpload.getResponseFieldId(), is("messageId"));
        assertThat(fileUpload.getRequestParam(), is("param"));
        DefaultValues values = (DefaultValues) page.getModels().get("resolve['testFileUpload_main'].fileUpload").getValue();
        assertThat(values.getValues().get("id"), is("value1"));
        assertThat(values.getValues().get("filename"), is("file"));
        assertThat(values.getValues().get("url"), is("/test"));

        FileUpload fileUpload2 = (FileUpload) ((StandardField) form.getComponent().getFieldsets().get(0).getRows().get(1)
                .getCols().get(0).getFields().get(0)).getControl();
        assertThat(fileUpload2.getAjax(), is(true));
        assertThat(fileUpload2.getMulti(), is(false));
        assertThat(fileUpload2.getShowSize(), is(true));
        assertThat(fileUpload2.getValueFieldId(), is("id"));
        assertThat(fileUpload2.getLabelFieldId(), is("name"));
        assertThat(fileUpload2.getUrlFieldId(), is("url"));
        assertThat(fileUpload2.getResponseFieldId(), is("message"));
        assertThat(fileUpload2.getRequestParam(), is("file"));
    }
}
