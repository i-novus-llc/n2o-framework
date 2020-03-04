package net.n2oapp.framework.autotest.controls;

import com.codeborne.selenide.CollectionCondition;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.*;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.dataprovider.TestDataProviderIOv1;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Автотесты дополнительных полей ввода
 */
public class InputControlsAT extends AutoTestBase {

    private SimplePage simplePage;

    @BeforeClass
    public static void beforeClass() {
        configureSelenide();
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/controls/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/default.header.xml"));

        simplePage = open(SimplePage.class);
        simplePage.shouldExists();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oHeaderPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oControlsPack(), new N2oControlsV2IOPack(), new N2oQueriesPack());
        builder.ios(new TestDataProviderIOv1());
    }

    @Test
    public void inputMoneyTest() {
        InputMoneyControl inputMoney = getFields().field("InputMoney").control(InputMoneyControl.class);
        inputMoney.shouldBeEnabled();
        inputMoney.shouldHaveValue("");
        inputMoney.shouldHavePlaceholder("");

        inputMoney.val("100500,999");
        inputMoney.shouldHaveValue("100 500,99 rub");
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

    @Test
    public void maskedInputTest() {
        MaskedInputControl maskedInput = getFields().field("MaskedInput").control(MaskedInputControl.class);
        maskedInput.shouldBeEnabled();
        maskedInput.shouldHavePlaceholder("+7 (___) ___-__-__");
        maskedInput.shouldHaveValue("");
        maskedInput.val("A7$h-F835-#$7sd fr8!93+2~sr0");
        maskedInput.shouldHaveValue("+7 (783) 578-93-20");
    }

    @Test
    public void textAreaTest() {
        TextArea textArea = getFields().field("TextArea").control(TextArea.class);
        textArea.shouldBeEnabled();
        textArea.shouldHaveValue("");
        textArea.val("1\n2\n3\n4\n5\n6\n7");
        textArea.shouldHaveValue("1\n2\n3\n4\n5\n6\n7");
    }

    @Test
    public void fileUploadTest() {
        InputControl inputControl = getFields().field("FileUpload").control(InputControl.class);
        inputControl.shouldBeEnabled();
    }

    @Test
    public void inputSelectTreeTest() {
        InputSelectTree inputSelectTree = getFields().field("InputSelectTree").control(InputSelectTree.class);
        inputSelectTree.shouldHavePlaceholder("SelectOption");
        inputSelectTree.shouldBeUnselected();
        inputSelectTree.toggleOptions();
        inputSelectTree.shouldDisplayedOptions(CollectionCondition.size(4));
        inputSelectTree.setFilter("three");

        inputSelectTree.selectOption(0);
        inputSelectTree.selectOption(2);
        inputSelectTree.selectOption(1);

        inputSelectTree.toggleOptions();

        inputSelectTree.shouldBeSelected(0, "one");
        inputSelectTree.shouldBeSelected(1, "two");
        inputSelectTree.shouldBeSelected(2, "three");

        inputSelectTree.removeOption(1);

        inputSelectTree.shouldBeSelected(0, "one");
        inputSelectTree.shouldBeSelected(1, "three");

        inputSelectTree.removeAllOptions();
        inputSelectTree.shouldBeUnselected();
    }

    private Fields getFields() {
        return simplePage.single().widget(FormWidget.class).fields();
    }
}
