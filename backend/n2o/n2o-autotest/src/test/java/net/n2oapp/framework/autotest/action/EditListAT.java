package net.n2oapp.framework.autotest.action;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.cell.ToolbarCell;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
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

public class EditListAT extends AutoTestBase {

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
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack());
    }

    @Test
    public void testCRUD() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/edit_list/crud_table/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/edit_list/crud_table/modal.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/edit_list/crud_table/update.page.xml"));

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class);
        Button addButton = page.regions().region(0, SimpleRegion.class).content().widget(1, FormWidget.class).toolbar().topLeft().button("Добавить документ");

        Modal modalPage = N2oSelenide.modal();
        InputText description = modalPage.content(SimplePage.class).widget(FormWidget.class).fields().field("description").control(InputText.class);
        InputText url = modalPage.content(SimplePage.class).widget(FormWidget.class).fields().field("url").control(InputText.class);
        Button saveButton = modalPage.content(SimplePage.class).widget(FormWidget.class).toolbar().topLeft().button("Сохранить");


        table.columns().rows().shouldHaveSize(0);
        addButton.click();
        modalPage.shouldExists();
        description.val("test2");
        url.val("test2url");
        saveButton.click();
        modalPage.close();

        table.columns().rows().shouldHaveSize(1);
        table.columns().rows().row(0).cell(0).textShouldHave("test2");
        table.columns().rows().row(0).cell(1).textShouldHave("test2url");

        addButton.click();
        modalPage.shouldExists();
        description.val("test1");
        url.val("test1url");
        saveButton.click();
        modalPage.close();

        table.columns().rows().shouldHaveSize(2);
        table.columns().rows().row(0).cell(0).textShouldHave("test2");
        table.columns().rows().row(0).cell(1).textShouldHave("test2url");
        table.columns().rows().row(1).cell(0).textShouldHave("test1");
        table.columns().rows().row(1).cell(1).textShouldHave("test1url");

        table.columns().rows().row(0).cell(2, ToolbarCell.class).toolbar().button("update").click();
        modalPage.shouldExists();
        description.shouldHaveValue("test2");
        url.shouldHaveValue("test2url");
        description.val("update-test2");
        url.val("update-test2url");
        saveButton.click();
        modalPage.close();

        table.columns().rows().shouldHaveSize(2);
        table.columns().rows().row(0).cell(0).textShouldHave("update-test2");
        table.columns().rows().row(0).cell(1).textShouldHave("update-test2url");
        table.columns().rows().row(1).cell(0).textShouldHave("test1");
        table.columns().rows().row(1).cell(1).textShouldHave("test1url");

        table.columns().rows().row(1).cell(2, ToolbarCell.class).toolbar().button("delete").click();
        table.columns().rows().shouldHaveSize(1);
        table.columns().rows().row(0).cell(0).textShouldHave("update-test2");
        table.columns().rows().row(0).cell(1).textShouldHave("update-test2url");
    }
}
