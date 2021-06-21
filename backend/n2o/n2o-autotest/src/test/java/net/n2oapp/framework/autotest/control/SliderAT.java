package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.component.control.Slider;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
/**
 * Автотест для компонента ползунка
 */
public class SliderAT extends AutoTestBase {

    private SimplePage page;

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/slider/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"));

        page = open(SimplePage.class);
        page.shouldExists();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
    }

    @Test
    public void testSlider() {
        Slider slider = page.widget(FormWidget.class).fields().field("Slider1")
                .control(Slider.class);
        slider.shouldExists();

        slider.shouldHaveValue("-10");
        slider.val("4", 2);
        slider.shouldHaveValue("4");
        slider.val("-8", 2);
        slider.shouldHaveValue("-8");
    }

    @Test
    public void testVerticalSlider() {
        Slider slider = page.widget(FormWidget.class).fields().field("Slider2")
                .control(Slider.class);
        slider.shouldExists();

        slider.shouldHaveValue("-10");
        slider.val("4");
        slider.shouldHaveValue("4");
        slider.val("-8");
        slider.shouldHaveValue("-8");
    }

    @Test
    public void testRangeModeSlider() {
        Slider slider = page.widget(FormWidget.class).fields().field("Slider3")
                .control(Slider.class);
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
