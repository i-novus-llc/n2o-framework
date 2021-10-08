package net.n2oapp.framework.autotest.widget.form;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.region.TabsRegion;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
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
 * Автотест сохранение нескольких форм одной кнопкой
 */
public class FormsSaveWithOneButtonAT extends AutoTestBase {
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
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/form/save_with_one_button/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/form/save_with_one_button/test.object.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/form/save_with_one_button/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/form/save_with_one_button/saveForm.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget tableWidget = page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class);
        StandardButton createButton = tableWidget.toolbar().topLeft().button("Создать");
        createButton.shouldExists();
        createButton.click();

        StandardPage open = N2oSelenide.page(StandardPage.class);
        FormWidget mainForm = open.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class);
        mainForm.shouldExists();
        mainForm.fields().field("Имя").control(InputText.class).val("Александр");
        mainForm.fields().field("Фамилия").control(InputText.class).val("Цой");
        TabsRegion tabs = open.regions().region(1, TabsRegion.class);
        FormWidget addressForm = tabs.tab(0).content().widget(FormWidget.class);
        addressForm.shouldExists();
        addressForm.fields().field("Адрес").control(InputText.class).val("г.Казань, ул.Качалова, д.75");

        tabs.tab(1).click();
        FormWidget orgForm = tabs.tab(1).content().widget(FormWidget.class);
        orgForm.shouldExists();
        orgForm.fields().field("Название организации").control(InputText.class).val("Ай-новус");

        open.toolbar().bottomRight().button("Сохранить").click();

        tableWidget.columns().rows().row(0).cell(0).textShouldHave("Александр");
        tableWidget.columns().rows().row(0).cell(1).textShouldHave("Цой");
        tableWidget.columns().rows().row(0).cell(2).textShouldHave("г.Казань, ул.Качалова, д.75");
        tableWidget.columns().rows().row(0).cell(3).textShouldHave("Ай-новус");

        StandardButton updateButton = tableWidget.toolbar().topLeft().button("Изменить");
        updateButton.shouldExists();
        updateButton.click();
        mainForm = open.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class);
        mainForm.shouldExists();
        mainForm.fields().field("Имя").control(InputText.class).shouldHaveValue("Александр");
        mainForm.fields().field("Имя").control(InputText.class).val("Иван");
        mainForm.fields().field("Фамилия").control(InputText.class).shouldHaveValue("Цой");
        mainForm.fields().field("Фамилия").control(InputText.class).val("Лебедев");

        tabs = open.regions().region(1, TabsRegion.class);
        addressForm = tabs.tab(0).content().widget(FormWidget.class);
        addressForm.shouldExists();
        addressForm.fields().field("Адрес").control(InputText.class).shouldHaveValue("г.Казань, ул.Качалова, д.75");
        addressForm.fields().field("Адрес").control(InputText.class).val("г.Казань, ул.Салимжанова, д.5");

        tabs.tab(1).click();
        orgForm = tabs.tab(1).content().widget(FormWidget.class);
        orgForm.shouldExists();
        orgForm.fields().field("Название организации").control(InputText.class).shouldHaveValue("Ай-новус");
        orgForm.fields().field("Название организации").control(InputText.class).val("КИР");

        open.toolbar().bottomRight().button("Сохранить").click();

        tableWidget.columns().rows().row(0).cell(0).textShouldHave("Иван");
        tableWidget.columns().rows().row(0).cell(1).textShouldHave("Лебедев");
        tableWidget.columns().rows().row(0).cell(2).textShouldHave("г.Казань, ул.Салимжанова, д.5");
        tableWidget.columns().rows().row(0).cell(3).textShouldHave("КИР");
    }
}
