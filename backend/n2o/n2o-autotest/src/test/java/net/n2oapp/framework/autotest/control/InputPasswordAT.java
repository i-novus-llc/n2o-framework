package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.PasswordControl;
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
 * Автотесты поля ввода пароля
 */
public class InputPasswordAT extends AutoTestBase {

    private SimplePage simplePage;

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/password/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"));

        simplePage = open(SimplePage.class);
        simplePage.shouldExists();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oControlsPack(), new N2oControlsV2IOPack());
    }

    @Test
    public void inputPasswordTest() {
        PasswordControl password = getFields().field("InputPassword").control(PasswordControl.class);
        password.shouldBeEnabled();
        password.shouldHavePlaceholder("EnterPassword");
        password.shouldHaveValue("");
        password.passwordShouldNotBeVisible();
        password.val("S!e@c#r&e*t5%$----");
        password.shouldHaveValue("S!e@c#r&e*t5%$-");
        password.clickEyeButton();
        password.passwordShouldBeVisible();
        password.clickEyeButton();
        password.passwordShouldNotBeVisible();
    }

    private Fields getFields() {
        return simplePage.widget(FormWidget.class).fields();
    }
}
