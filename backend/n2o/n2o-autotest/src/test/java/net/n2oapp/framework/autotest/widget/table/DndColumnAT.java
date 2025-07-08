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
        String[] labels = new String[]{"id", "firstName", "lastName", "phone", "birthday"};
        headers.shouldHaveSize(5);
        for (int i = 0; i < 5; i++) {
            headers.header(i).shouldHaveTitle(labels[i]);
            headers.header(i).shouldNotHaveDndIcon();
            headers.header(i).shouldNotBeDraggable();
        }

        // Проверка dnd-колонок в настройках
        Toolbar toolbar = table.toolbar().topLeft();
        N2oDropdownButton button = toolbar.button(0, N2oDropdownButton.class);
        button.click();
        button.shouldHaveItems(5);
        button.menuItem(0).shouldHaveLabel("id");
        button.menuItem(0).shouldNotBeDraggable();
        button.menuItem(0).shouldNotHaveDndIcon();
        for (int i = 1; i < 5; i++) {
            button.menuItem(i).shouldHaveLabel(labels[i]);
            button.menuItem(i).shouldBeDraggable();
            button.menuItem(i).shouldHaveDndIcon();
        }

        // Драг-н-дроп в настройках: переносим столбец "lastName" после "birthday"
        button.menuItem(2).dragAndDropTo(button.menuItem(4));

        labels = new String[]{"id", "firstName", "phone", "birthday", "lastName"};
        for (int i = 0; i < 5; i++) {
            // Проверка dnd-колонок в настройках
            button.menuItem(i).shouldHaveLabel(labels[i]);
            // Проверка dnd-колонок в таблице
            headers.header(i).shouldHaveTitle(labels[i]);
        }

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
        String[] labels = new String[]{"id", "firstName", "lastName", "phone", "birthday"};
        headers.header(0).shouldHaveTitle("id");
        headers.header(0).shouldNotHaveDndIcon();
        headers.header(0).shouldNotBeDraggable();
        for (int i = 1; i < 5; i++) {
            headers.header(i).shouldHaveTitle(labels[i]);
            headers.header(i).shouldHaveDndIcon();
            headers.header(i).shouldBeDraggable();
        }

        // Проверка dnd-колонок в настройках
        Toolbar toolbar = table.toolbar().topLeft();
        N2oDropdownButton button = toolbar.button(0, N2oDropdownButton.class);
        button.click();
        button.shouldHaveItems(5);
        for (int i = 0; i < 5; i++) {
            button.menuItem(i).shouldHaveLabel(labels[i]);
            button.menuItem(i).shouldNotBeDraggable();
            button.menuItem(i).shouldNotHaveDndIcon();
        }
        button.click();

        // Драг-н-дроп в таблице: переносим столбец "firstName" после "phone"
        headers.header(1).dragAndDropTo(headers.header(3));
        button.click();
        labels = new String[]{"id", "lastName", "phone", "firstName", "birthday"};
        for (int i = 0; i < 5; i++) {
            // Проверка dnd-колонок в таблице
            headers.header(i).shouldHaveTitle(labels[i]);
            // Проверка dnd-колонок в настройках
            button.menuItem(i).shouldHaveLabel(labels[i]);
        }

        Selenide.clearBrowserLocalStorage();
    }
}
