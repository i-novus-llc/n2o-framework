package net.n2oapp.framework.autotest.cells;

import net.n2oapp.framework.autotest.api.component.cell.LinkCell;
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
class LinkCellAT extends AutoTestBase {

    @BeforeAll
    static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();

        setResourcePath("net/n2oapp/framework/autotest/cells/link");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/cells/link/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/cells/link/test.query.xml"));
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(),
                new N2oCellsPack(), new N2oAllDataPack());
    }

    @Test
    void linkCellTest() {
        SimplePage simplePage = open(SimplePage.class);
        simplePage.shouldExists();

        TableWidget widget = simplePage.widget(TableWidget.class);
        widget.shouldExists();
        TableWidget.Rows rows = widget.columns().rows();
        rows.shouldHaveSize(1);

        LinkCell cell = rows.row(0).cell(0, LinkCell.class);
        cell.shouldHaveText("Text");
        cell.shouldHaveHref(getBaseUrl() + "/123");
        cell.shouldNotHaveIcon();

        cell = rows.row(0).cell(1, LinkCell.class);
        cell.shouldHaveText("Text2");
        cell.shouldHaveHref(getBaseUrl() + "/123");
        cell.shouldHaveIcon("fa-plus");

        cell = rows.row(0).cell(2, LinkCell.class);
        cell.shouldNotHaveText();
        cell.shouldHaveHref(getBaseUrl() + "/123");
        cell.shouldHaveIcon("fa-plus");

        cell.click();
        simplePage.shouldHaveUrlMatches(getBaseUrl() + "/#/123");
    }
}
