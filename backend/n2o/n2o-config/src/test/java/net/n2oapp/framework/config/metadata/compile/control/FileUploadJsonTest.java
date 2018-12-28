package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oControlsPack;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.JsonMetadataTestBase;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import org.junit.Before;
import org.junit.Test;

public class FileUploadJsonTest extends JsonMetadataTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.format.date.client", "DD-MMM-YY HH:mm");
        super.configure(builder);
        builder.packs(new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
    }

    @Test
    public void testFileUpload() {
        check("net/n2oapp/framework/config/mapping/testFileUpload.widget.xml",
                "components/controls/FileUploader/DropZone.meta.json")
                .cutXml("form.fieldsets[0].rows[0].cols[0].fields[0].control")
                .exclude("disabled", "ajax", "label", "name", "statusFieldId", "responseFieldId", "src", "readOnly", "sizeFieldId", "autoUpload")
                .assertEquals();
    }

}