package net.n2oapp.framework.autotest.cells;

import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ImageShape;
import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.component.cell.*;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Автотест ячеек таблицы
 */
public class CellsAT extends AutoTestBase {

    private TableWidget.Rows rows;

    @BeforeClass
    public static void beforeClass() {
        configureSelenide();
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/cells/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/default.header.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/cells/testTable.query.xml"));

        SimplePage simplePage = open(SimplePage.class);
        simplePage.shouldExists();

        rows = simplePage.single().widget(TableWidget.class).columns().rows();
        rows.shouldHaveSize(4);
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oHeaderPack(), new N2oWidgetsPack(),
                new N2oCellsPack(), new N2oAllDataPack());
    }

    @Test
    public void badgeCellTest() {
        int col = 0;

        rows.row(0).cell(col, BadgeCell.class).textShouldHave("Male");
        rows.row(0).cell(col, BadgeCell.class).colorShouldBe(Colors.DANGER);
        rows.row(3).cell(col, BadgeCell.class).textShouldHave("Female");
        rows.row(3).cell(col, BadgeCell.class).colorShouldBe(Colors.SUCCESS);
    }

    @Test
    public void checkboxCellTest() {
        int col = 1;

        rows.row(0).cell(col, CheckboxCell.class).shouldBeChecked();
        rows.row(1).cell(col, CheckboxCell.class).shouldBeUnchecked();
        rows.row(2).cell(col, CheckboxCell.class).shouldBeUnchecked();
        rows.row(0).cell(col, CheckboxCell.class).setChecked(false);
        rows.row(1).cell(col, CheckboxCell.class).setChecked(true);
        rows.row(2).cell(col, CheckboxCell.class).setChecked(true);
        rows.row(0).cell(col, CheckboxCell.class).shouldBeUnchecked();
        rows.row(1).cell(col, CheckboxCell.class).shouldBeChecked();
        rows.row(2).cell(col, CheckboxCell.class).shouldBeUnchecked();
    }

    @Test
    public void iconCellTest() {
        int col = 2;

        rows.row(0).cell(col, IconCell.class).iconShouldBe("fa-phone");
        rows.row(0).cell(col, IconCell.class).textShouldHave("fa fa-phone");
        rows.row(3).cell(col, IconCell.class).iconShouldBe("fa-minus");
        rows.row(3).cell(col, IconCell.class).textShouldHave("fa fa-minus");
    }

    @Test
    public void imageCellTest() {
        int col = 3;

        rows.row(0).cell(col, ImageCell.class).imageShouldBe(getBaseUrl()+"/favicon.ico");
        rows.row(0).cell(col, ImageCell.class).shapeShouldBe(ImageShape.circle);
    }

    @Test
    public void linkCellTest() {
        int col = 4;

        rows.row(0).cell(col, LinkCell.class).textShouldHave("test1");
        rows.row(0).cell(col, LinkCell.class).hrefShouldHave(getBaseUrl()+"/1/update");
        rows.row(0).cell(col, LinkCell.class).shouldHaveIcon("fa-link");

        rows.row(3).cell(col, LinkCell.class).textShouldHave("test4");
        rows.row(3).cell(col, LinkCell.class).hrefShouldHave(getBaseUrl()+"/4/update");
        rows.row(3).cell(col, LinkCell.class).shouldHaveIcon("fa-link");
    }

    @Test   //TODO не работает
    public void listCellTest() {
        int col = -1;

//        Selenide.sleep(500);
    }

    @Test
    public void progressBarCellTest() {
        int col = 5;

        rows.row(0).cell(col, ProgressBarCell.class).colorShouldBe(Colors.SUCCESS);
        rows.row(3).cell(col, ProgressBarCell.class).colorShouldBe(Colors.SUCCESS);

        rows.row(0).cell(col, ProgressBarCell.class).valueShouldBe("-50");
        rows.row(1).cell(col, ProgressBarCell.class).valueShouldBe("0");
        rows.row(2).cell(col, ProgressBarCell.class).valueShouldBe("100");
        rows.row(3).cell(col, ProgressBarCell.class).valueShouldBe("150");

        rows.row(0).cell(col, ProgressBarCell.class).sizeShouldBe(ProgressBarCell.Size.normal);
        rows.row(1).cell(col, ProgressBarCell.class).sizeShouldBe(ProgressBarCell.Size.normal);

        rows.row(1).cell(col, ProgressBarCell.class).shouldBeAnimated();
        rows.row(2).cell(col, ProgressBarCell.class).shouldBeAnimated();

        rows.row(2).cell(col, ProgressBarCell.class).shouldBeStriped();
        rows.row(3).cell(col, ProgressBarCell.class).shouldBeStriped();
    }

    @Test
    public void ratingCellTest() {
        int col = 6;

        rows.row(0).cell(col, RatingCell.class).maxShouldBe(10);
//        rows.row(0).cell(col, RatingCell.class).checkedShouldBe(0); val:-1
        rows.row(1).cell(col, RatingCell.class).checkedShouldBe(0);
        rows.row(2).cell(col, RatingCell.class).checkedShouldBe(5);
//        rows.row(3).cell(col, RatingCell.class).checkedShouldBe(10); val:15

        rows.row(0).cell(col, RatingCell.class).check(2);
        rows.row(0).cell(col, RatingCell.class).checkedShouldBe(2);

        rows.row(0).cell(col, RatingCell.class).check(7);
        rows.row(0).cell(col, RatingCell.class).checkedShouldBe(7);
    }

    @Test
    public void toolbarCellTest() {
        int col = 7;

        rows.row(0).cell(col, ToolbarCell.class).itemsShouldBe(3);
        rows.row(1).cell(col, ToolbarCell.class).itemsTextShouldBe(2, "three");
        rows.row(2).cell(col, ToolbarCell.class).clickMenu("two");
    }
}
