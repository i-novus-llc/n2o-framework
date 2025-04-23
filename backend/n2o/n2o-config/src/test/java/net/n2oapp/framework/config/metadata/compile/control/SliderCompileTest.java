package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.meta.control.Field;
import net.n2oapp.framework.api.metadata.meta.control.Slider;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class SliderCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oActionsPack(), new N2oAllDataPack(), new N2oControlsPack());
    }

    @Test
    void testSlider() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/control/testSlider.page.xml")
                .get(new PageContext("testSlider"));
        Form form = (Form) page.getWidget();
        Field field = form.getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0);
        Slider slider = (Slider) ((StandardField) field).getControl();
        assertThat(slider.getVertical(), is(true));
        assertThat(slider.getMultiple(), is(true));
        assertThat(slider.getShowTooltip(), is(true));
        assertThat(slider.getTooltipPlacement(), is("left"));
        assertThat(slider.getTooltipFormatter(), is("${this}%"));
        assertThat(slider.getMin(), is(0));
        assertThat(slider.getMax(), is(100));
        assertThat(slider.getStep(), is(2));

        field = form.getComponent().getFieldsets().get(0).getRows().get(1).getCols().get(0).getFields().get(0);
        slider = (Slider) ((StandardField) field).getControl();
        assertThat(slider.getVertical(), is(false));
        assertThat(slider.getMultiple(), is(false));
        assertThat(slider.getStep(), is(1));
        assertThat(slider.getTooltipPlacement(), is("top"));
    }
}
