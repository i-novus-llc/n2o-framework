package net.n2oapp.framework.autotest.alert;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.Select;
import net.n2oapp.framework.autotest.api.component.fieldset.MultiFieldSet;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Configuration.headless;
import static com.codeborne.selenide.Configuration.timeout;


public class AlertStackAT extends AutoTestBase {

    public static void configureSelenide() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
        System.setProperty("chromeoptions.args", "--no-sandbox,--verbose,--whitelisted-ips=''");
        headless = Boolean.parseBoolean(System.getProperty("selenide.headless", "false"));
        timeout = Long.parseLong(System.getProperty("selenide.timeout", "5000"));
    }

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
        ScriptProcessor.getScriptEngine();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oCellsPack(), new N2oActionsPack(), new N2oControlsPack(), new N2oAllDataPack());

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/alert/stack/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/alert/stack/alert.object.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"));
    }

    @Test
    public void test() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Button send = page.widget(FormWidget.class).toolbar().bottomRight().button("Отправить уведомления");

        MultiFieldSet multiFieldSet = page.widget(FormWidget.class).fieldsets().fieldset(MultiFieldSet.class);
        multiFieldSet.clickAddButton();
        InputText text = multiFieldSet.item(0).fields().field("Текст сообщения").control(InputText.class);
        InputText position = multiFieldSet.item(0).fields().field("Позиция уведомления").control(InputText.class);

        text.val("Алерт 1");
        position.val("top");

        send.click();
        page.alerts("top").alert(0).shouldHaveText("Алерт 1");
    }
}
