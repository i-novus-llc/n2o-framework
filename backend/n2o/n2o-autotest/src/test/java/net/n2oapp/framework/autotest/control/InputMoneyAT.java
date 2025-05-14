package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.InputMoneyControl;
import net.n2oapp.framework.autotest.api.component.field.ButtonField;
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
 * Автотесты компонента ввода денежных единиц
 */
class InputMoneyAT extends AutoTestBase {

    private SimplePage simplePage;

    @BeforeAll
    static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();

        simplePage = open(SimplePage.class);
        simplePage.shouldExists();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oControlsPack(), new N2oControlsV2IOPack());
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/money/index.page.xml"));
    }

    @Test
    void inputMoneyTest() {
        InputMoneyControl inputMoney = getFields().field("InputMoney").control(InputMoneyControl.class);
        inputMoney.shouldBeEnabled();
        inputMoney.shouldHaveValue("");
        inputMoney.shouldHavePlaceholder("");

        inputMoney.setValue("");
        getFields().field("Кнопка", ButtonField.class).click();
        inputMoney.shouldHaveValue("");

        inputMoney.setValue("100500,999");
        inputMoney.shouldHaveValue("100 500,99 rub");
    }


    private Fields getFields() {
        return simplePage.widget(FormWidget.class).fields();
    }
}
