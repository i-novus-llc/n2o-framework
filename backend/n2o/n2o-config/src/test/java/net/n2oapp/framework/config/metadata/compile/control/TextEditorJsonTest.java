package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.JsonMetadataTestBase;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import org.junit.Before;
import org.junit.Test;

/**
 * Тестирование маппинга java модели в json text-editor
 */
public class TextEditorJsonTest extends JsonMetadataTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        ((SimplePropertyResolver) builder.getEnvironment()
                .getSystemProperties()).setProperty("n2o.api.control.text_editor.toolbar_url", "net/n2oapp/framework/config/mapping/toolbar.json");
        builder.ios()
                .packs(new N2oWidgetsPack(), new N2oActionsPack(),
                        new N2oFieldSetsPack(), new N2oAllDataPack(), new N2oControlsV2IOPack())
                .compilers(new TextEditorCompiler());
    }

    @Test
    public void testTextEditor() {
        check("net/n2oapp/framework/config/mapping/testTextEditor.widget.xml",
                "components/controls/TextEditor/TextEditor.meta.json")
                .cutXml("form.fieldsets[0].rows[0].cols[0].fields[0].control")
                .exclude("src", "id", "disabled", "name", "visible")
                .assertEquals(new WidgetContext("testTextEditor"));
    }


}