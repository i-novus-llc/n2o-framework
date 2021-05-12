package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.MaskedInput;
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
 * Автотесты компоненты ввода текста с маской
 */
public class MaskedInputAT extends AutoTestBase {

    private SimplePage simplePage;

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/masked/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));

        simplePage = open(SimplePage.class);
        simplePage.shouldExists();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oHeaderPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oControlsPack(), new N2oControlsV2IOPack());
    }

    @Test
    public void maskedInputTest() {
        MaskedInput maskedInput = getFields().field("MaskedInput").control(MaskedInput.class);
        maskedInput.shouldBeEnabled();
        maskedInput.shouldHavePlaceholder("+7 (___) ___-__-__");
        maskedInput.shouldHaveValue("");
        maskedInput.val("A7$h-F835-#$7sd fr8!93+2~sr0");
        maskedInput.shouldHaveValue("+7 (783) 578-93-20");
        // проверка меры измерения
        maskedInput.shouldHaveMeasure();
        maskedInput.measureShouldHaveText("тел.");

        MaskedInput maskedInputWithoutClear = getFields().field("MaskedInputWithoutClear").control(MaskedInput.class);
        maskedInputWithoutClear.shouldBeEnabled();
        maskedInput.val("123");
        maskedInputWithoutClear.val("783");
        maskedInput.shouldHaveValue("");
        //вводим значение в первое поле, чтобы покинуть второе
        maskedInput.val("123");
        maskedInputWithoutClear.shouldHaveValue("+7 (783)");
    }

    private Fields getFields() {
        return simplePage.widget(FormWidget.class).fields();
    }
}
