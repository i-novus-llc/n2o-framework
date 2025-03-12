package net.n2oapp.framework.autotest.cells;

import net.n2oapp.framework.autotest.api.component.cell.ListCell;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест ячеек таблицы
 */
public class ListCellAT extends AutoTestBase {

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
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(),
                new N2oCellsPack(), new N2oAllDataPack());
    }

    @Test
    void testSimple() {
        setResourcePath("net/n2oapp/framework/autotest/cells/list/simple");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/cells/list/simple/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/cells/list/simple/testTable.query.xml"));

        SimplePage simplePage = open(SimplePage.class);
        simplePage.shouldExists();

        TableWidget.Rows rows;
        rows = simplePage.widget(TableWidget.class).columns().rows();
        rows.shouldHaveSize(4);

        int col = 0;
        rows.row(0).cell(col, ListCell.class).shouldHaveSize(1);
        rows.row(1).cell(col, ListCell.class).shouldHaveSize(3);
        rows.row(2).cell(col, ListCell.class).shouldHaveSize(0);
        rows.row(3).cell(col, ListCell.class).shouldHaveSize(3);

        rows.row(0).cell(col, ListCell.class).shouldNotBeExpandable();
        rows.row(1).cell(col, ListCell.class).shouldNotBeExpandable();
        rows.row(2).cell(col, ListCell.class).shouldNotBeExpandable();
        rows.row(3).cell(col, ListCell.class).shouldBeExpandable();

        rows.row(0).cell(col, ListCell.class).shouldHaveText(0, "val1");
        rows.row(1).cell(col, ListCell.class).shouldHaveText(1, "val2");
        rows.row(3).cell(col, ListCell.class).shouldHaveText(2, "val3");

        rows.row(3).cell(col, ListCell.class).expand();
        rows.row(3).cell(col, ListCell.class).shouldHaveSize(5);
        rows.row(3).cell(col, ListCell.class).shouldHaveText(3, "val4");
        rows.row(3).cell(col, ListCell.class).shouldHaveText(4, "val5");
        rows.row(3).cell(col, ListCell.class).shouldBeExpandable();
        rows.row(3).cell(col, ListCell.class).expand();
        rows.row(3).cell(col, ListCell.class).shouldHaveSize(3);
    }

    @Test
    void testInnerCells() {
        setResourcePath("net/n2oapp/framework/autotest/cells/list/inner_cells");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/cells/list/inner_cells/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/cells/list/inner_cells/test.query.xml"));

        SimplePage simplePage = open(SimplePage.class);
        simplePage.shouldExists();

        TableWidget.Rows rows;
        rows = simplePage.widget(TableWidget.class).columns().rows();
        rows.shouldHaveSize(1);

        ListCell cell = rows.row(0).cell(0, ListCell.class);
        cell.shouldExists();
        cell.shouldHaveInnerText(0, "text1");
        cell.shouldHaveInnerText(1, "text2");
        cell.shouldHaveInnerText(2, "text3");
        cell.shouldNotBeInline();
        cell.shouldNotBeExpandable();

        cell = rows.row(0).cell(1, ListCell.class);
        cell.shouldExists();
        cell.shouldHaveInnerText(0, "text-1");
        cell.shouldHaveInnerText(1, "text-2");
        cell.shouldHaveInnerText(2, "text-3");
        cell.shouldBeInline();
        cell.shouldHaveSeparator(", ");
        cell.shouldNotBeExpandable();

        cell = rows.row(0).cell(2, ListCell.class);
        cell.shouldExists();
        cell.shouldNotBeInline();
        cell.shouldHaveInnerLink(0, "link1");
        cell.shouldHaveInnerLink(1, "link2");
        cell.shouldBeExpandable();
        cell.shouldHaveInnerLinksSize(2);
        cell.expand();
        cell.shouldHaveInnerLinksSize(5);
        cell.shouldHaveInnerLink(2, "link3");
        cell.shouldHaveInnerLink(3, "link4");
        cell.shouldHaveInnerLink(4, "link5");
        cell.shouldHaveHref(0, "https://example.com/");
        cell.shouldHaveHref(4, "https://mail.ru/");


        cell = rows.row(0).cell(3, ListCell.class);
        cell.shouldExists();
        cell.shouldNotBeInline();
        cell.shouldNotBeExpandable();
        cell.shouldHaveInnerBadgesSize(2);
        cell.shouldHaveInnerBadge(0, "badge1");
        cell.shouldHaveInnerBadge(1, "badge2");
    }
}
