package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.impl.component.control.N2oInputText;
import net.n2oapp.framework.autotest.impl.component.page.N2oSimplePage;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Автотест поля для ввода текста
 */
public class InputTextAT extends AutoTestBase {

    @BeforeClass
    public static void beforeClass() {
        configureSelenide();
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oHeaderPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
    }

    @Test
    public void testInputText() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/input_text/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));

        N2oSimplePage page = open(N2oSimplePage.class);
        page.shouldExists();

        N2oInputText input = page.single().widget(FormWidget.class).fields().field("InputText")
                .control(N2oInputText.class);
        input.shouldExists();

        input.shouldHavePlaceholder("Введите текст");
        input.shouldHaveLength("10");
        input.shouldHaveValue("default");
        input.val("test-value");
        input.shouldHaveValue("test-value");
        // превышение указанной длины
        input.val("test1test2test3");
        input.shouldHaveValue("test1test2");
    }

    @Test
    public void testInputNumber() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/input_text/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));

        N2oSimplePage page = open(N2oSimplePage.class);
        page.shouldExists();

        N2oInputText input = page.single().widget(FormWidget.class).fields().field("InputNumber")
                .control(N2oInputText.class);
        input.shouldExists();

        input.shouldHaveMin("-100");
        input.shouldHaveMax("100");
        input.shouldHaveStep("2");

        input.shouldHaveValue("5");
        input.val("10");
        input.shouldHaveValue("10");
        // проверка работы кнопок (+step, -step)
        input.clickPlusStepButton();
        input.shouldHaveValue("12");
        input.clickMinusStepButton();
        input.shouldHaveValue("10");
        // проверка, что значение, измененное кнопками не выходит за границу
        input.val("99");
        input.clickPlusStepButton();
        input.shouldHaveValue("99");
        input.val("-99");
        input.clickMinusStepButton();
        input.shouldHaveValue("-99");
        // ввод некорректного значения (вне границ)
        input.val("-101");
        input.shouldHaveValue("");
    }

    @Test
    public void testInputFloat() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/input_text/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));

        N2oSimplePage page = open(N2oSimplePage.class);
        page.shouldExists();

        N2oInputText input = page.single().widget(FormWidget.class).fields().field("InputFloat")
                .control(N2oInputText.class);
        input.shouldExists();

        input.val("7.7");
        input.shouldHaveValue("7.7");
        // проверка работы кнопок (+step, -step)
        input.clickPlusStepButton();
        input.shouldHaveValue("8.2");
        input.clickMinusStepButton();
        /// TODO 7.69999999999 должно ли так быть или должно округляться?
//        input.shouldHaveValue("7.7");
        // проверка, что значение, измененное кнопками не выходит за границу
        input.val("9.9");
        input.clickPlusStepButton();
        input.shouldHaveValue("9.9");
        input.val("-9.9");
        input.clickMinusStepButton();
        input.shouldHaveValue("-9.9");
        // ввод некорректного значения (вне границ)
        input.val("-10.1");
        input.shouldHaveValue("");
    }
}