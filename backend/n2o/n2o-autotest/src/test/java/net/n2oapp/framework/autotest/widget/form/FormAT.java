package net.n2oapp.framework.autotest.widget.form;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
import net.n2oapp.framework.autotest.api.component.page.Page;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.RegionItems;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест для виджета Форма
 */
public class FormAT extends AutoTestBase {
    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oApplicationPack(), new N2oAllPagesPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"));
    }

    @Test
    public void testForm() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/form/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/form/testForm.object.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        FormWidget form = page.widget(FormWidget.class);
        form.fields().shouldHaveSize(2);

        StandardField surname = form.fields().field("Фамилия");
        surname.labelShouldHave(Condition.text("Фамилия"));
        surname.control(InputText.class).val("test");

        StandardField name = form.fields().field("Имя");
        name.shouldBeRequired();
        name.shouldHaveValidationMessage(Condition.text("Поле обязательно для заполнения"));

        name.control(InputText.class).val("1");
        surname.control(InputText.class).val("test");
        name.shouldHaveValidationMessage(Condition.text("Имя должно быть test"));
        name.control(InputText.class).val("test");
        surname.control(InputText.class).val("test");
        name.shouldHaveValidationMessage(Condition.empty);
    }

    @Test
    public void testToolbar() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/form/toolbar/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Page.Tooltip tooltip = page.tooltip();

        FormWidget form = page.widget(FormWidget.class);
        form.fields().shouldHaveSize(1);
        InputText name = form.fields().field("Имя").control(InputText.class);

        // проверка, что при разном состоянии доступности кнопок отображаются разные подсказки
        StandardButton button1 = form.toolbar().bottomLeft().button("Кнопка1");
        StandardButton button2 = form.toolbar().bottomLeft().button("Кнопка2");

        // подсказка при недоступности кнопки1 и кнопки2
        button1.shouldBeDisabled();
        button1.hover();
        tooltip.shouldHaveText("Заполните имя");
        button2.shouldBeDisabled();
        button2.hover();
        tooltip.shouldHaveText("Заполните имя");

        name.val("test");
        // подсказка при доступности кнопки1 и кнопки2
        button1.shouldBeEnabled();
        button1.hover();
        tooltip.shouldHaveText("Описание");
        button2.shouldBeEnabled();
        button2.hover();
        // у кнопки2 не должно быть подсказки, т.к. не указан description
        tooltip.shouldNotBeExist();
    }


    //https://jira.i-novus.ru/browse/NNO-7339
    @Test
    public void testMode() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/form/mode/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/form/mode/test.query.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        RegionItems content = page.regions().region(0, SimpleRegion.class).content();
        InputText masterName = content.widget(FormWidget.class).fields().field("master-name")
                .control(InputText.class);
        InputText childName = content.widget(1, FormWidget.class).fields().field("child-name")
                .control(InputText.class);
        InputText master2Name = content.widget(2, FormWidget.class).fields().field("master2-name")
                .control(InputText.class);
        InputText child2Name = content.widget(3, FormWidget.class).fields().field("child2-name")
                .control(InputText.class);

        masterName.shouldHaveValue("test");
        childName.shouldHaveValue("test");
        master2Name.shouldHaveValue("test");
        child2Name.shouldHaveValue("test");

        childName.val("123");
        child2Name.val("123");
        childName.shouldHaveValue("123");
        child2Name.shouldHaveValue("123");

        masterName.val("test1");
        master2Name.val("test1");

        // one-model (запрос будет -> значение поля вернется к исходному)
        childName.shouldHaveValue("test");
        // two-models (запроса не будет -> значение поля не изменится)
        child2Name.shouldHaveValue("123");
    }
}
