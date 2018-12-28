package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.JsonMetadataTestBase;
import org.junit.Before;
import org.junit.Test;

/**
 * Тестирвоание маппинга java модели в json input-text
 */
public class CodeEditorJsonTest extends JsonMetadataTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.ios()
                .packs(new N2oWidgetsPack(), new N2oActionsPack(),
                        new N2oFieldSetsPack(), new N2oAllDataPack(), new N2oControlsV2IOPack())
                .compilers(new CodeEditorCompiler());
    }

    @Test
    public void testCodeEditor() {
        check("net/n2oapp/framework/config/mapping/testCodeEditor.widget.xml",
                "components/controls/CodeEditor/CodeEditor.meta.json")
                .cutXml("form.fieldsets[0].rows[0].cols[0].fields[0].control")
                .exclude("src", "disabled", "visible")
                .assertEquals(new WidgetContext("testCodeEditor"));
    }


}