package net.n2oapp.framework.autotest.cells;

import net.n2oapp.framework.autotest.api.component.cell.TooltipListCell;
import net.n2oapp.framework.autotest.api.component.page.Page;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест ячейки с тултипом и раскрывающимся текстовым списком
 */
public class TooltipListCellAT extends AutoTestBase {

    private StandardPage page;

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/cells/tooltip_list/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/cells/testTable.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"));

        page = open(StandardPage.class);
        page.shouldExists();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oApplicationPack(), new N2oWidgetsPack(),
                new N2oCellsPack(), new N2oAllDataPack());
    }


    @Test
    public void testDefaultLabelFormat() {
        TableWidget.Rows rows = page.regions().region(0, SimpleRegion.class).content()
                .widget(0, TableWidget.class).columns().rows();

        Page.Tooltip tooltip = page.tooltip();

        // заодно проверяем hover и содержимое тултипа
        // проверяем снизу вверх, чтобы тултип не перекрывал ячейки
        TooltipListCell cell4 = rows.row(3).cell(0, TooltipListCell.class);
        cell4.shouldHaveText("Объектов 5 шт.");
        cell4.labelShouldNotBeDashed();
        cell4.hover();
        tooltip.shouldBeExist();
        tooltip.shouldHaveText("val1", "val2", "val3", "val4", "val5");

        TooltipListCell cell3 = rows.row(2).cell(0, TooltipListCell.class);
        cell3.shouldBeEmpty();
        cell3.hover();
        tooltip.shouldNotBeExist();

        TooltipListCell cell2 = rows.row(1).cell(0, TooltipListCell.class);
        cell2.shouldHaveText("Объектов 3 шт.");
        cell2.labelShouldNotBeDashed();
        cell2.hover();
        tooltip.shouldBeExist();
        tooltip.shouldHaveText("val1", "val2", "val3");

        // если значение одно, то оно и отображается в поле без тултипа
        TooltipListCell cell1 = rows.row(0).cell(0, TooltipListCell.class);
        cell1.shouldHaveText("val1");
        cell1.labelShouldNotBeDashed();
        cell1.hover();
        tooltip.shouldNotBeExist();
    }

    @Test
    public void testOtherLabelFormats() {
        TableWidget.Rows rows0 = page.regions().region(0, SimpleRegion.class).content()
                .widget(0, TableWidget.class).columns().rows();
        TableWidget.Rows rows = page.regions().region(0, SimpleRegion.class).content()
                .widget(1, TableWidget.class).columns().rows();

        Page.Tooltip tooltip = page.tooltip();

        // заодно проверяем click и содержимое тултипа
        // проверяем снизу вверх, чтобы тултип не перекрывал ячейки
        TooltipListCell cell4 = rows.row(3).cell(0, TooltipListCell.class);
        cell4.shouldHaveText("5 объектов");
        cell4.labelShouldBeDashed();
        cell4.click();
        tooltip.shouldBeExist();
        tooltip.shouldHaveText("val1", "val2", "val3", "val4", "val5");
        // проверяем, что при наведении тултип не появляется
        rows0.row(0).cell(0, TooltipListCell.class).click();
        cell4.hover();
        tooltip.shouldNotBeExist();

        TooltipListCell cell3 = rows.row(2).cell(0, TooltipListCell.class);
        cell3.shouldBeEmpty();
        cell3.click();
        tooltip.shouldNotBeExist();

        TooltipListCell cell2 = rows.row(1).cell(0, TooltipListCell.class);
        cell2.shouldHaveText("3 объекта");
        cell2.labelShouldBeDashed();
        cell2.click();
        tooltip.shouldBeExist();
        tooltip.shouldHaveText("val1", "val2", "val3");

        // если значение одно, то оно и отображается в поле без тултипа
        TooltipListCell cell1 = rows.row(0).cell(0, TooltipListCell.class);
        cell1.shouldHaveText("val1");
        cell1.labelShouldNotBeDashed();
        cell1.click();
        tooltip.shouldNotBeExist();
    }

    @Test
    public void testWithoutLabel() {
        TableWidget.Rows rows = page.regions().region(0, SimpleRegion.class).content()
                .widget(2, TableWidget.class).columns().rows();

        Page.Tooltip tooltip = page.tooltip();

        TooltipListCell cell1 = rows.row(0).cell(0, TooltipListCell.class);
        cell1.shouldHaveText("val1");
        cell1.labelShouldNotBeDashed();
        TooltipListCell cell2 = rows.row(1).cell(0, TooltipListCell.class);
        cell2.shouldBeEmpty();
    }
}
