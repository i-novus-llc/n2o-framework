package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.JsonMetadataTestBase;
import org.junit.Before;
import org.junit.Test;

public class SliderJsonTest extends JsonMetadataTestBase {
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
        builder.compilers(new SliderCompiler());
    }

    @Test
    public void tabsSlider() {
        check("net/n2oapp/framework/config/mapping/testSlider.widget.xml",
                "components/controls/Slider/Slider.meta.json")
                .cutXml("form.fieldsets[0].rows[0].cols[0].fields[0].control")
                .exclude("src", "id", "tooltipPlacement", "disabled", "showTooltip", "dots", "marks",
                        "pushable", "valueFieldId", "labelFieldId", "tooltipFormatter", "sortFieldId") /// TODO: поведение formatter должно быть корректно
                .assertEquals();
    }

}
