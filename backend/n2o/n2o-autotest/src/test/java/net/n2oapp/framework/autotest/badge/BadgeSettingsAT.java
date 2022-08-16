package net.n2oapp.framework.autotest.badge;

import com.codeborne.selenide.Configuration;
import net.n2oapp.framework.autotest.api.component.DropDown;
import net.n2oapp.framework.autotest.api.component.DropDownTree;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.cell.BadgeCell;
import net.n2oapp.framework.autotest.api.component.control.InputSelect;
import net.n2oapp.framework.autotest.api.component.control.InputSelectTree;
import net.n2oapp.framework.autotest.api.component.control.Select;
import net.n2oapp.framework.autotest.api.component.header.AnchorMenuItem;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BadgeSettingsAT extends AutoTestBase {
    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
//        Configuration.headless=false;
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oApplicationPack(), new N2oAllPack());
    }

    @Test
    public void testSelects() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/badge/badge_settings/badge_in_selectors/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/badge/badge_settings/badge_in_selectors/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/badge/badge_settings/badge_in_selectors/tree.query.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        FormWidget formWidget = page.widget(FormWidget.class);
        InputSelect inputSelect = formWidget.fields().field("Ввод с выпадающим списком").control(InputSelect.class);
        inputSelect.shouldBeExpandable();
        inputSelect.expand();
        inputSelect.shouldBeExpanded();
        DropDown dropDownInputSelect = inputSelect.dropdown();
        dropDownInputSelect.shouldExists();
        dropDownInputSelect.shouldHaveItems(4);
        DropDown.DropDownItem itemDropDownInputSelect = dropDownInputSelect.item(0);
        itemDropDownInputSelect.badgeShouldBeExists();
        itemDropDownInputSelect.badgeShouldBeShape("square");
        itemDropDownInputSelect.badgeShouldHaveImage();
        itemDropDownInputSelect.badgeImageShouldBeShape("circle");
        itemDropDownInputSelect.badgeImageShouldBePosition("left");

        Select select = formWidget.fields().field("Выпадающий список").control(Select.class);
        select.shouldBeExpandable();
        select.expand();
        select.shouldBeExpanded();
        DropDown dropDownSelect = select.dropdown();
        dropDownSelect.shouldExists();
        dropDownSelect.shouldHaveItems(4);
        DropDown.DropDownItem itemDropDownSelect = dropDownSelect.item(0);
        itemDropDownSelect.badgeShouldBeExists();
        itemDropDownSelect.badgeShouldBeShape("rounded");
        itemDropDownSelect.badgeShouldHaveImage();
        itemDropDownSelect.badgeImageShouldBeShape("rounded");
        itemDropDownSelect.badgeImageShouldBePosition("right");

        InputSelectTree selectTree = formWidget.fields().field("Дерево").control(InputSelectTree.class);
        selectTree.shouldBeExpandable();
        selectTree.expand();
        selectTree.shouldBeExpanded();
        DropDownTree dropDownTreeSelect = selectTree.dropdowntree();
        DropDownTree.DropDownTreeItem firstItemTree = dropDownTreeSelect.item(0);
        firstItemTree.shouldBeExpandable();
        firstItemTree.expand();
        firstItemTree.shouldBeExpanded();
        dropDownTreeSelect.shouldHaveItems(4);
        DropDownTree.DropDownTreeItem thirdItemTree = dropDownTreeSelect.item(1);
        thirdItemTree.shouldBeExpandable();
        thirdItemTree.expand();
        thirdItemTree.shouldBeExpanded();
        dropDownTreeSelect.shouldHaveItems(5);
        DropDownTree.DropDownTreeItem fourthTreeItem = dropDownTreeSelect.item(2);
        fourthTreeItem.badgeShouldBeExists();
        fourthTreeItem.badgeShouldBeShape("circle");
        fourthTreeItem.badgeShouldHaveImage();
        fourthTreeItem.badgeImageShouldBeShape("square");
        fourthTreeItem.badgeImageShouldBePosition("right");
    }

    @Test
    public void testCell() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/badge/badge_settings/badge_in_cell/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/badge/badge_settings/badge_in_cell/test.query.xml"));

        SimplePage simplePage = open(SimplePage.class);
        simplePage.shouldExists();

        TableWidget.Rows rows = simplePage.widget(TableWidget.class).columns().rows();
        rows.shouldHaveSize(3);
        int col = 3;
        BadgeCell cell = rows.row(0).cell(col, BadgeCell.class);
        cell.badgeShouldBeExists();
        cell.badgeShouldBeShape("rounded");
        cell.badgeShouldHaveImage();
        cell.badgeImageShouldBePosition("right");
        cell.badgeImageShouldBeShape("rounded");
    }

    @Test
    public void testButton() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/badge/badge_settings/badge_in_button/index.page.xml"));

        SimplePage simplePage = open(SimplePage.class);
        simplePage.shouldExists();

        StandardButton firstBtn = simplePage.toolbar().topLeft().button("Первая");
        firstBtn.shouldBeEnabled();
        firstBtn.badgeShouldBeExists();
        firstBtn.badgeShouldBeShape("circle");
        firstBtn.badgeShouldHaveImage();
        firstBtn.badgeImageShouldBeShape("circle");
        firstBtn.badgeImageShouldBePosition("left");

        StandardButton secondBtn = simplePage.toolbar().topLeft().button("Вторая");
        firstBtn.shouldBeEnabled();
        secondBtn.badgeShouldBeExists();
        secondBtn.badgeShouldBeShape("rounded");
        secondBtn.badgeShouldHaveImage();
        secondBtn.badgeImageShouldBeShape("rounded");
        secondBtn.badgeImageShouldBePosition("right");

        StandardButton thirdBtn = simplePage.toolbar().topLeft().button("Третья");
        firstBtn.shouldBeEnabled();
        thirdBtn.badgeShouldBeExists();
        thirdBtn.badgeShouldBeShape("square");
        thirdBtn.badgeShouldHaveImage();
        thirdBtn.badgeImageShouldBeShape("square");
        thirdBtn.badgeImageShouldBePosition("right");
    }

    @Test
    public void testTree() {

    }

    @Test
    public void testMenu() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/badge/badge_settings/badge_in_menu/app.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/badge/badge_settings/badge_in_menu/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/badge/badge_settings/badge_in_menu/test.page.xml"));
        builder.properties("n2o.application.id=app");

        SimplePage simplePage = open(SimplePage.class);
        simplePage.shouldExists();

        simplePage.breadcrumb().titleShouldHaveText("Меню с баджем");
        simplePage.header().brandNameShouldBe("Хедер");
        simplePage.header().nav().shouldHaveSize(1);

        AnchorMenuItem menuItem = simplePage.header().nav().anchor(0);
        menuItem.shouldHaveIcon();
        menuItem.iconShouldHaveCssClass("fa fa-bell");
        menuItem.badgeShouldBeExists();
        menuItem.badgeShouldBeShape("square");
        menuItem.badgeShouldHaveImage();
        menuItem.badgeImageShouldBeShape("square");
        menuItem.badgeImageShouldBePosition("right");
    }
}
