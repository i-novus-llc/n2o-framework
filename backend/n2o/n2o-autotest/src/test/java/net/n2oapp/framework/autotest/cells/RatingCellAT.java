package net.n2oapp.framework.autotest.cells;

import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.ColorsEnum;
import net.n2oapp.framework.autotest.api.collection.Alerts;
import net.n2oapp.framework.autotest.api.component.cell.RatingCell;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.snippet.Alert;
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
class RatingCellAT extends AutoTestBase {

    @BeforeAll
    static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();

        setResourcePath("/net/n2oapp/framework/autotest/cells/rating");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/cells/rating/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/cells/rating/rating.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/cells/rating/rating.object.xml"));
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(),
                new N2oCellsPack(), new N2oAllDataPack(), new N2oActionsPack());
    }

    @Test
    void ratingCellTest() {
        SimplePage simplePage = open(SimplePage.class);
        simplePage.shouldExists();

        TableWidget.Rows rows = simplePage.widget(TableWidget.class).columns().rows();
        rows.shouldHaveSize(1);

        //проверка не редактируемых ячеек
        rows.row(0).cell(0, RatingCell.class).shouldHaveMax(10);
        rows.row(0).cell(0, RatingCell.class).shouldHaveValue("8");
        rows.row(0).cell(0, RatingCell.class).value("5");
        rows.row(0).cell(0, RatingCell.class).shouldHaveValue("8");

        //проверка редактируемых ячеек
        RatingCell cell = rows.row(0).cell(1, RatingCell.class);
        cell.shouldHaveMax(10);
        Alerts alerts = simplePage.alerts(Alert.PlacementEnum.top);

        cell.value("5");
        cell.shouldExists();
        cell.shouldHaveValue("5");
        alerts.shouldHaveSize(1);
        alerts.alert(0).shouldHaveColor(ColorsEnum.SUCCESS);

        //проверка что значение сохранилось на бэке
        Selenide.refresh();
        simplePage.shouldExists();
        cell.shouldExists();
        cell.shouldHaveValue("5");
    }

}
