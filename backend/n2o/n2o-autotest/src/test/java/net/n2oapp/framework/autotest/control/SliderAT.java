package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.impl.component.control.N2oSlider;
import net.n2oapp.framework.autotest.impl.component.page.N2oSimplePage;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Автотест для компонента ползунка
 */
public class SliderAT extends AutoTestBase {

    private SimplePage page;

    @BeforeClass
    public static void beforeClass() {
        configureSelenide();
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/slider/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));

        page = open(N2oSimplePage.class);
        page.shouldExists();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oHeaderPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
    }

    @Test
    public void testSlider() {
        N2oSlider slider = page.single().widget(FormWidget.class).fields().field("Slider1")
                .control(N2oSlider.class);
        slider.shouldExists();

        slider.shouldHaveValue("-10");
        slider.val("4", 2);
        slider.shouldHaveValue("4");
        slider.val("-8", 2);
        slider.shouldHaveValue("-8");
    }

    @Test
    public void testVerticalSlider() {
        N2oSlider slider = page.single().widget(FormWidget.class).fields().field("Slider2")
                .control(N2oSlider.class);
        slider.shouldExists();

        slider.shouldHaveValue("-10");
        slider.val("4");
        slider.shouldHaveValue("4");
        slider.val("-8");
        slider.shouldHaveValue("-8");
    }

    @Test
    public void testRangeModeSlider() {
        N2oSlider slider = page.single().widget(FormWidget.class).fields().field("Slider3")
                .control(N2oSlider.class);
        slider.shouldExists();

        slider.shouldHaveLeftValue("-10");
        slider.shouldHaveRightValue("-10");
        slider.valRight("8");
        slider.shouldHaveRightValue("8");
        slider.valLeft("-2");
        slider.shouldHaveLeftValue("-2");

        // пересекаем ползунки
        // проверяем, что первый всегда будет наименьшим
        slider.valRight("-3");
        slider.shouldHaveLeftValue("-3");
        slider.shouldHaveRightValue("-2");
    }
}
