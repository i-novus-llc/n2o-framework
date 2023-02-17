package net.n2oapp.framework.autotest.cells;

import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.api.metadata.meta.cell.ImageStatusElementPlace;
import net.n2oapp.framework.api.metadata.meta.control.TextPosition;
import net.n2oapp.framework.autotest.api.component.cell.ImageCell;
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
 * Автотест ячейки таблицы с изображением
 */
public class ImageCellAT extends AutoTestBase {

    private TableWidget.Rows rows;

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
    public void imageCellTest() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/cells/image/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/cells/testTable.query.xml"));

        SimplePage simplePage = open(SimplePage.class);
        simplePage.shouldExists();

        rows = simplePage.widget(TableWidget.class).columns().rows();
        rows.shouldHaveSize(4);

        ImageCell cell = rows.row(0).cell(0, ImageCell.class);
        cell.shouldHaveSrc(getBaseUrl() + "/favicon.ico");
        cell.shouldHaveWidth(15);
        cell.shouldHaveShape(ShapeType.CIRCLE);
    }

    @Test
    public void imageCellWithTitleTest() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/cells/image/title/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/cells/image/title/test.query.xml"));

        SimplePage simplePage = open(SimplePage.class);
        simplePage.shouldExists();

        rows = simplePage.widget(TableWidget.class).columns().rows();
        rows.shouldHaveSize(2);

        ImageCell cell = rows.row(0).cell(0, ImageCell.class);
        cell.shouldExists();
        cell.shouldHaveSrc("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACoAAAAlCAMAAAADS4u8AAAAnFBMVEX////ZbFrptDj////ptDj///9YuKvZbFrptDj///9YuKvZbFrptDj///9YuKvptDj////ZbFr////ptDj///9YuKvZbFrptDj///9YuKvptDj////ZbFrptDj///9YuKvptDj///9YuKvZbFrptDj///9YuKvZbFr///9YuKvptDj///9YuKvZbFrptDj///9YuKvZbFrptDj///90gGlOAAAAMHRSTlMAEBAQICAwMDAwQEBAQFBQUGBgcHCAgICAkJCQoKCgsLCwwMDAwNDQ0ODg4PDw8PDEXJ+/AAAA+UlEQVQYGc3B61aCQBSA0U/ICtGki4ZhGKmlYXI57/9uzbgwRqHF/KnV3hBl0rTxUHrL4mjeg1BaZX3gvajNIZN2D+AXph7ygxD8wuSTSrs7uCoMnxBIq40LzIvaFAiSdVPsok2XR7f8B7O8026CNsttTFB2uY0tSm4HZZvbeEO5yW0M0AaL1y6LS36TOzJQcYaKw6lIToRoT+XBysEQyZkQeCwrKwyZnEmBfXl0QU0agPLbkFoqZxLgo6zsHWqBnMo8YFxW7jGN4rUh9tCun1fKy5i/0B91cTlwY+kWoUViI0QRKymK2EFJxUaCEoiFzEPz4nWXuI/yBYSItStrEp20AAAAAElFTkSuQmCC");
        cell.shouldHaveWidth(40);
        cell.shouldHaveTitle("Заголовок1");
        cell.shouldHaveDescription("Описание1");
        cell.shouldHaveTextPosition(TextPosition.left);

        cell = rows.row(1).cell(0, ImageCell.class);
        cell.shouldHaveSrc(getBaseUrl() + "/images/hamburg-3846525__340.jpg");
        cell.shouldHaveTitle("Заголовок2");
        cell.shouldHaveDescription("Описание2");
    }

    @Test
    public void imageCellWithStatusTest() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/cells/image/status/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/cells/image/status/test.query.xml"));

        SimplePage simplePage = open(SimplePage.class);
        simplePage.shouldExists();

        TableWidget tableWidget = simplePage.widget(TableWidget.class);
        tableWidget.shouldExists();
        rows = tableWidget.columns().rows();
        rows.shouldHaveSize(2);

        ImageCell cell = rows.row(0).cell(0, ImageCell.class);
        cell.shouldExists();
        cell.shouldHaveStatus(ImageStatusElementPlace.topRight, 0, "Статус1");
        cell.shouldHaveStatusIcon(ImageStatusElementPlace.topRight, 0, ".fa.fa-plus");

        cell = rows.row(1).cell(0, ImageCell.class);
        cell.shouldHaveStatus(ImageStatusElementPlace.topRight, 0, "Статус2");
    }
}
