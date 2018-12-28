package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.JsonMetadataTestBase;
import org.junit.Before;
import org.junit.Test;

public class FormJsonTest extends JsonMetadataTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllDataPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack())
                .sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/widgets/testJsonForm.object.xml"));
    }

    @Test
    public void form() {
        check("net/n2oapp/framework/config/metadata/compile/widgets/testJsonForm.widget.xml",
                "components/widgets/Form/FormWidget.meta.json")
                .cutJson("Page_Form.form.validation")
                .exclude("email", "name[0].validationKey", "name[1].validationKey")
                .cutXml("form.validation")
                .assertEquals();

    }
}
