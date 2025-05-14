package net.n2oapp.framework.autotest.widget.table;

import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.api.collection.TableHeaders;
import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.impl.component.button.N2oDropdownButton;
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
 * Автотест для drag-n-drop столбца таблицы
 */
class DndColumnAT extends AutoTestBase {

    @BeforeAll
    static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() {
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
    }

    @Test
    void testThroughSettings() {
        setResourcePath("net/n2oapp/framework/autotest/widget/table/dnd_column/through_settings");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/dnd_column/through_settings/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/dnd_column/through_settings/test.query.xml")
        );
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class);

        TableWidget.Rows rows = table.columns().rows();
        rows.shouldHaveSize(4);

        // Проверка dnd-колонок в таблице
        TableHeaders headers = table.columns().headers();
        headers.shouldHaveSize(5);
        headers.header(0).shouldHaveTitle("id");
        headers.header(0).shouldNotHaveDndIcon();
        headers.header(0).shouldNotBeDraggable();

        headers.header(1).shouldHaveTitle("firstName");
        headers.header(1).shouldNotHaveDndIcon();
        headers.header(1).shouldNotBeDraggable();

        headers.header(2).shouldHaveTitle("lastName");
        headers.header(2).shouldNotHaveDndIcon();
        headers.header(2).shouldNotBeDraggable();

        headers.header(3).shouldHaveTitle("phone");
        headers.header(3).shouldNotHaveDndIcon();
        headers.header(3).shouldNotBeDraggable();

        headers.header(4).shouldHaveTitle("birthday");
        headers.header(4).shouldNotHaveDndIcon();
        headers.header(4).shouldNotBeDraggable();

        // Проверка dnd-колонок в настройках
        Toolbar toolbar = table.toolbar().topLeft();
        N2oDropdownButton button = toolbar.button(0, N2oDropdownButton.class);
        button.click();
        button.shouldHaveItems(5);
        button.menuItem(0).shouldHaveLabel("id");
        button.menuItem(0).shouldNotBeDraggable();
        button.menuItem(0).shouldNotHaveDndIcon();

        button.menuItem(1).shouldHaveLabel("firstName");
        button.menuItem(1).shouldBeDraggable();
        button.menuItem(1).shouldHaveDndIcon();

        button.menuItem(2).shouldHaveLabel("lastName");
        button.menuItem(2).shouldBeDraggable();
        button.menuItem(2).shouldHaveDndIcon();

        button.menuItem(3).shouldHaveLabel("phone");
        button.menuItem(3).shouldBeDraggable();
        button.menuItem(3).shouldHaveDndIcon();

        button.menuItem(4).shouldHaveLabel("birthday");
        button.menuItem(4).shouldBeDraggable();
        button.menuItem(4).shouldHaveDndIcon();

        // Драг-н-дроп в настройках: переносим столбец "lastName" после "birthday"
        button.menuItem(2).dragAndDropTo(button.menuItem(4));

        // Проверка dnd-колонок в настройках
        button.menuItem(0).shouldHaveLabel("id");
        button.menuItem(1).shouldHaveLabel("firstName");
        button.menuItem(2).shouldHaveLabel("phone");
        button.menuItem(3).shouldHaveLabel("birthday");
        button.menuItem(4).shouldHaveLabel("lastName");

        // Проверка dnd-колонок в таблице
        headers.header(0).shouldHaveTitle("id");
        headers.header(1).shouldHaveTitle("firstName");
        headers.header(2).shouldHaveTitle("phone");
        headers.header(3).shouldHaveTitle("birthday");
        headers.header(4).shouldHaveTitle("lastName");

        Selenide.clearBrowserLocalStorage();
    }

    @Test
    void testThroughTable() {
        setResourcePath("net/n2oapp/framework/autotest/widget/table/dnd_column/through_table");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/dnd_column/through_table/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/dnd_column/through_table/test.query.xml")
        );
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class);

        // Проверка dnd-колонок в таблице
        TableHeaders headers = table.columns().headers();
        headers.shouldHaveSize(5);
        headers.header(0).shouldHaveTitle("id");
        headers.header(0).shouldNotHaveDndIcon();
        headers.header(0).shouldNotBeDraggable();

        headers.header(1).shouldHaveTitle("firstName");
        headers.header(1).shouldHaveDndIcon();
        headers.header(1).shouldBeDraggable();

        headers.header(2).shouldHaveTitle("lastName");
        headers.header(2).shouldHaveDndIcon();
        headers.header(2).shouldBeDraggable();

        headers.header(3).shouldHaveTitle("phone");
        headers.header(3).shouldHaveDndIcon();
        headers.header(3).shouldBeDraggable();

        headers.header(4).shouldHaveTitle("birthday");
        headers.header(4).shouldHaveDndIcon();
        headers.header(4).shouldBeDraggable();

        // Проверка dnd-колонок в настройках
        Toolbar toolbar = table.toolbar().topLeft();
        N2oDropdownButton button = toolbar.button(0, N2oDropdownButton.class);
        button.click();
        button.shouldHaveItems(5);
        button.menuItem(0).shouldHaveLabel("id");
        button.menuItem(0).shouldNotBeDraggable();
        button.menuItem(0).shouldNotHaveDndIcon();

        button.menuItem(1).shouldHaveLabel("firstName");
        button.menuItem(1).shouldNotBeDraggable();
        button.menuItem(1).shouldNotHaveDndIcon();

        button.menuItem(2).shouldHaveLabel("lastName");
        button.menuItem(2).shouldNotBeDraggable();
        button.menuItem(2).shouldNotHaveDndIcon();

        button.menuItem(3).shouldHaveLabel("phone");
        button.menuItem(3).shouldNotBeDraggable();
        button.menuItem(3).shouldNotHaveDndIcon();

        button.menuItem(4).shouldHaveLabel("birthday");
        button.menuItem(4).shouldNotBeDraggable();
        button.menuItem(4).shouldNotHaveDndIcon();
        button.click();

        // Драг-н-дроп в таблице: переносим столбец "firstName" после "phone"
        headers.header(1).dragAndDropTo(headers.header(3));

        // Проверка dnd-колонок в таблице
        headers.header(0).shouldHaveTitle("id");
        headers.header(1).shouldHaveTitle("lastName");
        headers.header(2).shouldHaveTitle("phone");
        headers.header(3).shouldHaveTitle("firstName");
        headers.header(4).shouldHaveTitle("birthday");

        // Проверка dnd-колонок в настройках
        button.click();
        button.menuItem(0).shouldHaveLabel("id");
        button.menuItem(1).shouldHaveLabel("lastName");
        button.menuItem(2).shouldHaveLabel("phone");
        button.menuItem(3).shouldHaveLabel("firstName");
        button.menuItem(4).shouldHaveLabel("birthday");

        Selenide.clearBrowserLocalStorage();
    }
}
