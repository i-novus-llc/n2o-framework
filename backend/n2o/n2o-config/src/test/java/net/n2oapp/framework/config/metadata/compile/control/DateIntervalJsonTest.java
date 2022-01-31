package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.JsonMetadataTestBase;
import org.junit.Before;
import org.junit.Test;

public class DateIntervalJsonTest extends JsonMetadataTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oActionsPack(), new N2oAllDataPack(), new N2oControlsV2IOPack());
        builder.compilers(new DateIntervalCompiler());
    }

    @Test
    public void tabsDateInterval() {
        check("net/n2oapp/framework/config/mapping/testDateInterval.page.xml",
                "components/controls/DatePicker/DateInterval.meta.json")
                .cutXml("widget.form.fieldsets[0].rows[0].cols[0].fields[0].control")
                .exclude("src", "id", "openOnFocus", "value", "defaultValue", "outputFormat", "placeholder", "readOnly", "popupPlacement", "disabled", "locale", "value", "timeFormat", "defaultTime") /// TODO: ждем, когда будет реализовано на сервере
                .assertEquals();
    }
}
