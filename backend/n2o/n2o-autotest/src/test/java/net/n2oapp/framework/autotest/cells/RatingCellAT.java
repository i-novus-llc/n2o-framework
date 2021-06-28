package net.n2oapp.framework.autotest.cells;

import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.collection.Alerts;
import net.n2oapp.framework.autotest.api.component.cell.RatingCell;
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
public class RatingCellAT extends AutoTestBase {

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/cells/rating/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/cells/rating/rating.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/cells/rating/rating.object.xml"));
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oHeaderPack(), new N2oWidgetsPack(),
                new N2oCellsPack(), new N2oAllDataPack(), new N2oActionsPack());
    }

    @Test
    public void ratingCellTest() {
        SimplePage simplePage = open(SimplePage.class);
        simplePage.shouldExists();

        TableWidget.Rows rows = simplePage.widget(TableWidget.class).columns().rows();
        rows.shouldHaveSize(2);

        //проверка не редактируемых ячеек
        rows.row(0).cell(0, RatingCell.class).maxShouldBe(10);
        rows.row(0).cell(0, RatingCell.class).valueShouldBe("10");
        rows.row(0).cell(0, RatingCell.class).value("5");
        rows.row(0).cell(0, RatingCell.class).valueShouldBe("10");

        rows.row(1).cell(0, RatingCell.class).valueShouldBe("3");
        rows.row(1).cell(0, RatingCell.class).value("2");
        rows.row(1).cell(0, RatingCell.class).valueShouldBe("3");

        //проверка редактируемых ячеек
        rows.row(0).cell(1, RatingCell.class).maxShouldBe(10);
        rows.row(0).cell(1, RatingCell.class).value("5");
        Alerts.Alert alert = simplePage.alerts().alert(0);
        alert.shouldHaveText("Данные сохранены");
        alert.shouldHaveColor(Colors.SUCCESS);
        rows.row(0).cell(1, RatingCell.class).valueShouldBe("5");
        rows.row(1).cell(1, RatingCell.class).value("8");
        alert.shouldHaveText("Данные сохранены");
        alert.shouldHaveColor(Colors.SUCCESS);
        rows.row(1).cell(1, RatingCell.class).valueShouldBe("8");
        //проверка что значение сохранилось на бэке
        Selenide.refresh();
        simplePage.shouldExists();
        rows.row(0).cell(1, RatingCell.class).valueShouldBe("5");
        rows.row(1).cell(1, RatingCell.class).valueShouldBe("8");
    }

}
