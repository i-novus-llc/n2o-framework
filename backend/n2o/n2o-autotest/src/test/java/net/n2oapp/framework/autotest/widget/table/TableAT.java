package net.n2oapp.framework.autotest.widget.table;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.component.button.DropdownButton;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.cell.TextCell;
import net.n2oapp.framework.autotest.api.component.cell.ToolbarCell;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.Select;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.Paging;
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
        table.filters().shouldBeHidden();
        table.toolbar().topRight().button(0, StandardButton.class).click();

        table.columns().rows().row(0).shouldHaveColor(Colors.DANGER);
        table.columns().rows().row(1).shouldHaveColor(Colors.INFO);
        table.columns().rows().row(2).shouldHaveColor(Colors.SUCCESS);

        for (int i = 0; i < 3; i++) {
            table.columns().rows().row(i).cell(0).shouldBeHidden();
            table.columns().rows().row(i).cell(1).shouldBeVisible();
            table.columns().rows().row(i).cell(2).shouldBeVisible();
            table.columns().rows().row(i).cell(2).shouldHaveIcon("fa-plus");
            table.columns().rows().row(i).cell(3).shouldBeVisible();
        }

        table.columns().headers().header(0).shouldBeHidden();
        table.columns().headers().header(1).shouldBeVisible();
        table.columns().headers().header(1).shouldHaveTitle("Имя");
        table.columns().headers().header(1).shouldHaveStyle("color: red");
        table.columns().headers().header(2).shouldBeVisible();
        table.columns().headers().header(2).shouldHaveTitle("Фамилия");
        table.columns().headers().header(2).shouldHaveCssClass("font-italic");
        table.columns().headers().header(2).shouldHaveIcon("fa-plus");
        table.columns().headers().header(3).shouldBeVisible();
        table.columns().headers().header(3).shouldHaveTitle("Дата рождения");

        table.toolbar().topRight().button(1, DropdownButton.class).click();
        table.toolbar().topRight().button(1, DropdownButton.class).menuItem("Фамилия").click();

        table.columns().headers().header(0).shouldBeHidden();
        table.columns().headers().header(1).shouldBeVisible();
        table.columns().headers().header(2).shouldBeHidden();
        table.columns().headers().header(3).shouldBeVisible();
        for (int i = 0; i < 3; i++) {
            table.columns().rows().row(i).cell(0).shouldBeHidden();
            table.columns().rows().row(i).cell(1).shouldBeVisible();
            table.columns().rows().row(i).cell(2).shouldBeHidden();
            table.columns().rows().row(i).cell(2).shouldNotHaveIcon();
            table.columns().rows().row(i).cell(3).shouldBeVisible();
        }

        table.toolbar().topRight().button(1, DropdownButton.class).menuItem("Фамилия").click();

        table.columns().headers().header(0).shouldBeHidden();
        table.columns().headers().header(1).shouldBeVisible();
        table.columns().headers().header(2).shouldBeVisible();
        table.columns().headers().header(2).shouldHaveIcon("fa-plus");
        table.columns().headers().header(3).shouldBeVisible();
        for (int i = 0; i < 3; i++) {
            table.columns().rows().row(i).cell(0).shouldBeHidden();
            table.columns().rows().row(i).cell(1).shouldBeVisible();
            table.columns().rows().row(i).cell(2).shouldBeVisible();
            table.columns().rows().row(i).cell(2).shouldHaveIcon("fa-plus");
            table.columns().rows().row(i).cell(3).shouldBeVisible();
        }
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

    @Test
    public void testHideOnBlur() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/table/toolbar/hide_on_blur/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/toolbar/test.object.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/toolbar/test.query.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        TableWidget table = page.widget(TableWidget.class);
        TableWidget.Rows rows = table.columns().rows();
        rows.shouldHaveSize(3);

        StandardButton button = rows.row(0).cell(2, ToolbarCell.class).toolbar().button("Кнопка");
        button.shouldNotExists();
        rows.row(0).hover();
        button.shouldBeEnabled();
        button.click();
        page.alerts().alert(0).shouldHaveText("echo");

        button = rows.row(1).cell(2, ToolbarCell.class).toolbar().button("Кнопка");
        button.shouldNotExists();
        rows.row(1).hover();
        button.shouldExists();
        button.shouldBeDisabled();
        button = rows.row(2).cell(2, ToolbarCell.class).toolbar().button("Кнопка");
        button.shouldNotExists();
    }

    @Test
    public void testPaging() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/table/paging/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/paging/test.query.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class);
        Paging paging = table.paging();
        paging.totalElementsShouldBe(8);
        paging.prevShouldNotExist();
        paging.nextShouldNotExist();
        paging.firstShouldExist();
        paging.lastShouldNotExist();

        paging.activePageShouldBe("1");
        table.columns().rows().row(0).cell(0, TextCell.class).textShouldHave("test1");
        paging.selectPage("3");
        paging.activePageShouldBe("3");
        table.columns().rows().row(0).cell(0, TextCell.class).textShouldHave("test7");
        paging.selectFirst();
        paging.activePageShouldBe("1");


        TableWidget table2 = page.regions().region(1, SimpleRegion.class).content().widget(TableWidget.class);
        paging = table2.paging();
        paging.totalElementsShouldNotExist();
        paging.prevShouldExist();
        paging.nextShouldExist();
        paging.firstShouldNotExist();
        paging.lastShouldExist();

        paging.activePageShouldBe("1");
        table2.columns().rows().row(0).cell(0, TextCell.class).textShouldHave("test1");
        paging.selectNext();
        paging.activePageShouldBe("2");
        table2.columns().rows().row(0).cell(0, TextCell.class).textShouldHave("test4");
        paging.selectPrev();
        paging.activePageShouldBe("1");
        paging.selectLast();
        table2.columns().rows().row(0).cell(0, TextCell.class).textShouldHave("test7");
    }
}
