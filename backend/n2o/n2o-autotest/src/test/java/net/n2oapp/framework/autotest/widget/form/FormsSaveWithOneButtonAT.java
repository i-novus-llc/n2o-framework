package net.n2oapp.framework.autotest.widget.form;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
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
        builder.packs(
                new N2oApplicationPack(),
                new N2oAllPagesPack(),
                new N2oAllDataPack()
        );
        setResourcePath("net/n2oapp/framework/autotest/widget/form/save_with_one_button");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/form/save_with_one_button/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/form/save_with_one_button/test.object.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/form/save_with_one_button/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/form/save_with_one_button/saveForm.page.xml"));
    }

    @Test
    public void testForm() {
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget tableWidget = page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class);
        StandardButton createButton = tableWidget.toolbar().topLeft().button("Создать");
        createButton.shouldExists();
        createButton.click();

        StandardPage open = N2oSelenide.page(StandardPage.class);
        FormWidget mainForm = open.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class);
        mainForm.shouldExists();
        StandardField name = mainForm.fields().field("Имя");
        StandardField surname = mainForm.fields().field("Фамилия");
        name.shouldExists();
        name.control(InputText.class).click();
        name.control(InputText.class).setValue("Александр");
        surname.control(InputText.class).click();
        surname.control(InputText.class).setValue("Цой");
        TabsRegion tabs = open.regions().region(1, TabsRegion.class);
        FormWidget addressForm = tabs.tab(0).content().widget(FormWidget.class);
        StandardField address = addressForm.fields().field("Адрес");
        addressForm.shouldExists();
        address.control(InputText.class).click();
        address.control(InputText.class).setValue("г.Казань, ул.Качалова, д.75");

        tabs.tab(1).click();
        FormWidget orgForm = tabs.tab(1).content().widget(FormWidget.class);
        orgForm.shouldExists();
        StandardField org = orgForm.fields().field("Название организации");
        org.shouldExists();
        org.control(InputText.class).click();
        org.control(InputText.class).setValue("Ай-новус");

        open.toolbar().bottomRight().button("Сохранить").click();

        tableWidget.columns().rows().row(0).cell(0).shouldHaveText("Александр");
        tableWidget.columns().rows().row(0).cell(1).shouldHaveText("Цой");
        tableWidget.columns().rows().row(0).cell(2).shouldHaveText("г.Казань, ул.Качалова, д.75");
        tableWidget.columns().rows().row(0).cell(3).shouldHaveText("Ай-новус");
        tableWidget.columns().rows().row(0).click();

        StandardButton updateButton = tableWidget.toolbar().topLeft().button("Изменить");
        updateButton.shouldExists();
        updateButton.click();
        mainForm = open.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class);
        mainForm.shouldExists();
        name.shouldExists();
        name.control(InputText.class).shouldHaveValue("Александр");
        name.control(InputText.class).click();
        name.control(InputText.class).setValue("Иван");
        surname.control(InputText.class).shouldHaveValue("Цой");
        surname.control(InputText.class).click();
        surname.control(InputText.class).setValue("Лебедев");

        tabs = open.regions().region(1, TabsRegion.class);
        addressForm = tabs.tab(0).content().widget(FormWidget.class);
        addressForm.shouldExists();
        address.control(InputText.class).shouldHaveValue("г.Казань, ул.Качалова, д.75");
        address.control(InputText.class).click();
        address.control(InputText.class).setValue("г.Казань, ул.Салимжанова, д.5");

        tabs.tab(1).click();
        orgForm = tabs.tab(1).content().widget(FormWidget.class);
        orgForm.shouldExists();
        org.control(InputText.class).shouldHaveValue("Ай-новус");
        org.control(InputText.class).click();
        org.control(InputText.class).setValue("КИР");

        open.toolbar().bottomRight().button("Сохранить").click();

        tableWidget.columns().rows().row(0).cell(0).shouldHaveText("Иван");
        tableWidget.columns().rows().row(0).cell(1).shouldHaveText("Лебедев");
        tableWidget.columns().rows().row(0).cell(2).shouldHaveText("г.Казань, ул.Салимжанова, д.5");
        tableWidget.columns().rows().row(0).cell(3).shouldHaveText("КИР");
    }
}
