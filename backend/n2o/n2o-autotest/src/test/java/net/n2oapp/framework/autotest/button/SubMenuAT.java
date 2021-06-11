package net.n2oapp.framework.autotest.button;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.api.collection.TableHeaders;
import net.n2oapp.framework.autotest.api.component.button.DropdownButton;
import net.n2oapp.framework.autotest.api.component.cell.ToolbarCell;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
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
 * Автотест для кнопки с выпадающим меню
 */
public class SubMenuAT extends AutoTestBase {

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
        Configuration.headless = false;
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oHeaderPack(), new N2oAllPagesPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/button/submenu/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/button/submenu/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));
    }

    @Test
    public void testSubmenu() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        DropdownButton dropdown = page.widget(FormWidget.class).toolbar().topLeft().dropdown();
        dropdown.shouldHaveItems(3);

        dropdown.click();
        dropdown.shouldBeExpanded();
        dropdown.menuItem("Изменить").click();
        dropdown.shouldBeCollapsed();
    }

    @Test
    public void testSubMenuInCell() {
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(1, SimpleRegion.class).content().widget(TableWidget.class);
        table.shouldExists();
        TableWidget.Rows rows = table.columns().rows();
        rows.shouldHaveSize(1);

        DropdownButton dropdown = rows.row(0).cell(1, ToolbarCell.class).toolbar().dropdown();
        dropdown.shouldHaveItems(3);
        dropdown.click();
        dropdown.shouldBeExpanded();
        dropdown.menuItem("Сохранить").click();
        dropdown.shouldBeCollapsed();
    }
}

