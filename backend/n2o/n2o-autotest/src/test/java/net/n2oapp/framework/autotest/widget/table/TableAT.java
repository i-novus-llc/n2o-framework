package net.n2oapp.framework.autotest.widget.table;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.button.DropdownButton;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.cell.ToolbarCell;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.Select;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oHeaderPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест для виджета Таблица
 */
public class TableAT extends AutoTestBase {
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
        builder.packs(new N2oHeaderPack(), new N2oAllPagesPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));
    }

    @Test
    public void testTable() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/table/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/testTable.query.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        TableWidget table = page.widget(TableWidget.class);
        table.filters().shouldBeVisible();
        table.filters().toolbar().button("searchLabel").shouldBeEnabled();
        table.filters().toolbar().button("resetLabel").shouldBeEnabled();
        table.filters().fields().field("Имя").control(InputText.class).val("test");
        table.filters().fields().field("Пол").control(Select.class).select(Condition.text("Мужской"));
        table.filters().toolbar().button("resetLabel").click();
        table.filters().fields().field("Имя").control(InputText.class).shouldHaveValue("test");

        table.toolbar().topRight().button(0, StandardButton.class).click();
        table.filters().shouldBeInvisible();
        table.toolbar().topRight().button(0, StandardButton.class).click();

        table.columns().rows().row(0).cell(0).element().parent().shouldHave(Condition.cssClass("bg-danger"));
        table.columns().rows().row(1).cell(0).element().parent().shouldHave(Condition.cssClass("bg-info"));
        table.columns().rows().row(2).cell(0).element().parent().shouldHave(Condition.cssClass("bg-success"));
        table.columns().headers().header(0).shouldHaveTitle("Имя");
        table.columns().headers().header(1).shouldHaveTitle("Фамилия");
        table.columns().headers().header(2).shouldHaveTitle("Дата рождения");

        table.toolbar().topRight().button(1, DropdownButton.class).click();
        table.toolbar().topRight().button(1, DropdownButton.class).menuItem("Имя").click();
        table.columns().headers().header(0).shouldNotHaveTitle();
        table.toolbar().topRight().button(1, DropdownButton.class).menuItem("Имя").click();
        table.columns().headers().header(0).shouldHaveTitle("Имя");
    }

    @Test
    public void testToolbar() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/table/toolbar/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/toolbar/test.query.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        TableWidget table = page.widget(TableWidget.class);
        TableWidget.Rows rows = table.columns().rows();
        rows.shouldHaveSize(3);

        StandardButton button = rows.row(0).cell(2, ToolbarCell.class).toolbar().button("Кнопка");
        button.shouldExists();
        button.shouldBeEnabled();
        button = rows.row(1).cell(2, ToolbarCell.class).toolbar().button("Кнопка");
        button.shouldExists();
        button.shouldBeDisabled();
        button = rows.row(2).cell(2, ToolbarCell.class).toolbar().button("Кнопка");
        button.shouldNotExists();
    }
}
