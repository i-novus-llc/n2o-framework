package net.n2oapp.framework.autotest.datasource;

import net.n2oapp.framework.autotest.api.component.button.Button;
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

public class SaveManyFormOneButtonAT extends AutoTestBase {

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
    public void testSaveManyFormOneButton(){
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/datasource/saveManyFormOneButton/index.page.xml"),
                        new CompileInfo("net/n2oapp/framework/autotest/datasource/saveManyFormOneButton/saveForm.page.xml"),
                        new CompileInfo("net/n2oapp/framework/autotest/datasource/saveManyFormOneButton/test.query.xml"),
                        new CompileInfo("net/n2oapp/framework/autotest/datasource/saveManyFormOneButton/test.object.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class);

        page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class)
                        .toolbar().topLeft().button("Создать").click();
        page.breadcrumb().titleShouldHaveText("Создание записи");

        InputText nameInput = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class)
                                    .fields().field("Имя").control(InputText.class);
        InputText surnameInput = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class)
                                    .fields().field("Фамилия").control(InputText.class);
        Button saveButton = page.toolbar().bottomRight().button("Сохранить");

        TabsRegion tabs = page.regions().region(1, TabsRegion.class);
        InputText addressInput = tabs.tab(0).content().widget(FormWidget.class)
                                .fields().field("Адрес").control(InputText.class);
        InputText organizationInput = tabs.tab(1).content().widget(FormWidget.class)
                                .fields().field("Название организации").control(InputText.class);

        nameInput.val("Сергей");
        surnameInput.val("Катеев");
        addressInput.val("ул. Есенина 54");
        tabs.tab(1).click();
        tabs.tab(0).click();
        saveButton.click();
        tabs.tab(1).shouldBeActive();

        tabs.tab(1).click();
        organizationInput.shouldBeEnabled();
        organizationInput.val("Сбербанк");
        saveButton.click();
        page.breadcrumb().titleShouldHaveText("Datasource");

        table.columns().rows().row(0).cell(0).textShouldHave("Сергей");
        table.columns().rows().row(0).click();

        page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class)
                .toolbar().topLeft().button("Изменить").click();
        page.breadcrumb().titleShouldHaveText("Создание записи");

        nameInput.shouldHaveValue("Сергей");
        tabs.tab(1).click();
        organizationInput.shouldHaveValue("Сбербанк");
    }
}
