package net.n2oapp.framework.autotest.badge;

import net.n2oapp.framework.autotest.BadgePosition;
import net.n2oapp.framework.autotest.BadgeShape;
import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.collection.FieldSets;
import net.n2oapp.framework.autotest.api.component.DropDown;
import net.n2oapp.framework.autotest.api.component.DropDownTree;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.cell.BadgeCell;
import net.n2oapp.framework.autotest.api.component.control.InputSelect;
import net.n2oapp.framework.autotest.api.component.control.InputSelectTree;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.Select;
import net.n2oapp.framework.autotest.api.component.fieldset.LineFieldSet;
import net.n2oapp.framework.autotest.api.component.fieldset.MultiFieldSet;
import net.n2oapp.framework.autotest.api.component.fieldset.SimpleFieldSet;
import net.n2oapp.framework.autotest.api.component.header.AnchorMenuItem;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.TreeWidget;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест для проверки работы баджей с картинками
 */

public class BadgeAT extends AutoTestBase {
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
        builder.packs(new N2oApplicationPack(), new N2oAllPack());
    }

    @Test
    public void testSelects() {
        setJsonPath("net/n2oapp/framework/autotest/badge/selectors");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/badge/selectors/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/badge/selectors/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/badge/selectors/tree.query.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        FormWidget formWidget = page.widget(FormWidget.class);
        InputSelect inputSelect = formWidget.fields().field("Ввод с выпадающим списком").control(InputSelect.class);
        inputSelect.openPopup();
        inputSelect.shouldBeOpened();
        DropDown dropDownInputSelect = inputSelect.dropdown();
        dropDownInputSelect.shouldExists();
        dropDownInputSelect.shouldHaveOptions(4);
        DropDown.DropDownItem itemDropDownInputSelect = dropDownInputSelect.item(0);
        itemDropDownInputSelect.badgeShouldExists();
        itemDropDownInputSelect.badgeShouldHaveText("Проект 1");
        itemDropDownInputSelect.badgeShouldHaveShape(BadgeShape.SQUARE);
        itemDropDownInputSelect.badgeShouldHaveImage("hamburg-3846525__340.jpg");
        itemDropDownInputSelect.badgeShouldHaveImageShape(BadgeShape.CIRCLE);
        itemDropDownInputSelect.badgeShouldHaveImagePosition(BadgePosition.LEFT);

        Select select = formWidget.fields().field("Выпадающий список").control(Select.class);
        select.openPopup();
        select.shouldBeOpened();
        DropDown dropDownSelect = select.dropdown();
        dropDownSelect.shouldExists();
        dropDownSelect.shouldHaveOptions(4);
        DropDown.DropDownItem itemDropDownSelect = dropDownSelect.item(0);
        itemDropDownSelect.badgeShouldExists();
        itemDropDownSelect.badgeShouldHaveText("Проект 1");
        itemDropDownSelect.badgeShouldHaveShape(BadgeShape.ROUNDED);
        itemDropDownSelect.badgeShouldHaveImage("static/hamburg-3846525__340.jpg");
        itemDropDownSelect.badgeShouldHaveImageShape(BadgeShape.ROUNDED);
        itemDropDownSelect.badgeShouldHaveImagePosition(BadgePosition.RIGHT);

        InputSelectTree selectTree = formWidget.fields().field("Дерево").control(InputSelectTree.class);
        selectTree.openPopup();
        selectTree.shouldBeOpened();
        DropDownTree dropDownTreeSelect = selectTree.dropdown();
        DropDownTree.DropDownTreeItem firstItemTree = dropDownTreeSelect.item(0);
        firstItemTree.expand();
        firstItemTree.shouldBeExpanded();
        dropDownTreeSelect.shouldHaveItems(4);
        dropDownTreeSelect.shouldHaveOption("11");
        DropDownTree.DropDownTreeItem thirdItemTree = dropDownTreeSelect.item(1);
        thirdItemTree.expand();
        thirdItemTree.shouldBeExpanded();
        dropDownTreeSelect.shouldHaveItems(5);
        DropDownTree.DropDownTreeItem fourthTreeItem = dropDownTreeSelect.item(2);
        fourthTreeItem.badgeShouldHaveText("Проект 1.1.1");
        fourthTreeItem.badgeShouldExists();
        fourthTreeItem.badgeShouldHaveShape(BadgeShape.CIRCLE);
        fourthTreeItem.badgeShouldHaveImage("static/hamburg-3846525__340.jpg");
        fourthTreeItem.badgeShouldHaveImageShape(BadgeShape.SQUARE);
        fourthTreeItem.badgeShouldHaveImagePosition(BadgePosition.RIGHT);
    }

    @Test
    public void testCell() {
        setJsonPath("net/n2oapp/framework/autotest/badge/cell");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/badge/cell/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/badge/cell/test.query.xml"));

        SimplePage simplePage = open(SimplePage.class);
        simplePage.shouldExists();

        TableWidget.Rows rows = simplePage.widget(TableWidget.class).columns().rows();
        rows.shouldHaveSize(3);
        int col = 3;
        BadgeCell cell = rows.row(0).cell(col, BadgeCell.class);
        cell.badgeShouldExists();
        cell.badgeShouldHaveShape(BadgeShape.ROUNDED);
        cell.badgeShouldHaveText("Проект 3");
        cell.badgeShouldHaveImage("static/hamburg-3846525__340.jpg");
        cell.badgeShouldHaveImagePosition(BadgePosition.RIGHT);
        cell.badgeShouldHaveImageShape(BadgeShape.ROUNDED);
    }

    @Test
    public void testButton() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/badge/button/index.page.xml"));

        SimplePage simplePage = open(SimplePage.class);
        simplePage.shouldExists();

        StandardButton firstBtn = simplePage.toolbar().topLeft().button("Первая");
        firstBtn.shouldBeEnabled();
        firstBtn.badgeShouldExists();
        firstBtn.badgeShouldHaveShape(BadgeShape.CIRCLE);
        firstBtn.badgeShouldHaveText("new");
        firstBtn.badgeShouldHaveImage("static/hamburg-3846525__340.jpg");
        firstBtn.badgeShouldHaveImageShape(BadgeShape.CIRCLE);
        firstBtn.badgeShouldHaveImagePosition(BadgePosition.LEFT);

        StandardButton secondBtn = simplePage.toolbar().topLeft().button("Вторая");
        firstBtn.shouldBeEnabled();
        secondBtn.badgeShouldExists();
        secondBtn.badgeShouldHaveShape(BadgeShape.ROUNDED);
        secondBtn.badgeShouldHaveText("new");
        secondBtn.badgeShouldHaveImage("static/hamburg-3846525__340.jpg");
        secondBtn.badgeShouldHaveImageShape(BadgeShape.ROUNDED);
        secondBtn.badgeShouldHaveImagePosition(BadgePosition.RIGHT);

        StandardButton thirdBtn = simplePage.toolbar().topLeft().button("Третья");
        firstBtn.shouldBeEnabled();
        thirdBtn.badgeShouldExists();
        thirdBtn.badgeShouldHaveShape(BadgeShape.SQUARE);
        thirdBtn.badgeShouldHaveText("new");
        thirdBtn.badgeShouldHaveImage("static/hamburg-3846525__340.jpg");
        thirdBtn.badgeShouldHaveImageShape(BadgeShape.SQUARE);
        thirdBtn.badgeShouldHaveImagePosition(BadgePosition.RIGHT);
    }

    @Test
    public void testTree() {
        setJsonPath("net/n2oapp/framework/autotest/badge/tree");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/badge/tree/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/badge/tree/test.query.xml"));

        SimplePage simplePage = open(SimplePage.class);
        simplePage.shouldExists();

        TreeWidget treeWidget = simplePage.widget(TreeWidget.class);
        treeWidget.shouldExists();

        treeWidget.shouldHaveItems(2);
        TreeWidget.TreeItem firstItemTree = treeWidget.item(0);
        firstItemTree.expand();
        treeWidget.shouldHaveItems(4);
        treeWidget.shouldHaveItem("11");

        TreeWidget.TreeItem secondItemTree = treeWidget.item(1);
        secondItemTree.badgeShouldExists();
        secondItemTree.badgeShouldHaveShape(BadgeShape.ROUNDED);
        secondItemTree.badgeShouldHaveText("Проект 1.1");
        secondItemTree.badgeShouldHaveImage("static/hamburg-3846525__340.jpg");
        secondItemTree.badgeShouldHaveImagePosition(BadgePosition.LEFT);
        secondItemTree.badgeShouldHaveImageShape(BadgeShape.SQUARE);
    }

    @Test
    public void testMenu() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/badge/menu/app.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/badge/menu/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/badge/menu/test.page.xml"));

        SimplePage simplePage = open(SimplePage.class);
        simplePage.shouldExists();

        simplePage.breadcrumb().crumb(0).shouldHaveLabel("Меню с баджем");
        simplePage.header().shouldHaveBrandName("Хедер");
        simplePage.header().nav().shouldHaveSize(1);

        AnchorMenuItem menuItem = simplePage.header().nav().anchor(0);
        menuItem.shouldHaveIcon();
        menuItem.shouldHaveIconCssClass("fa fa-bell");
        menuItem.badgeShouldExists();
        menuItem.badgeShouldHaveShape(BadgeShape.SQUARE);
        menuItem.badgeShouldHaveImage("static/hamburg-3846525__340.jpg");
        menuItem.badgeShouldHaveImageShape(BadgeShape.SQUARE);
        menuItem.badgeShouldHaveImagePosition(BadgePosition.RIGHT);
    }

    @Test
    public void testLineFieldset() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/badge/fieldset/line/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        FieldSets fieldsets = page.widget(FormWidget.class).fieldsets();

        LineFieldSet fieldset = fieldsets.fieldset(0, LineFieldSet.class);
        fieldset.badgeShouldHaveText("12");
        fieldset.badgeShouldHaveShape(BadgeShape.ROUNDED);

        fieldset = fieldsets.fieldset(1, LineFieldSet.class);
        fieldset.badgeShouldHaveText("Humburg");
        fieldset.badgeShouldHaveShape(BadgeShape.SQUARE);
        fieldset.badgeShouldHaveImage("static/hamburg-3846525__340.jpg");
        fieldset.badgeShouldHaveImagePosition(BadgePosition.RIGHT);
        fieldset.badgeShouldHaveImageShape(BadgeShape.SQUARE);
        fieldset.badgeShouldHaveColor(Colors.DANGER);

        fieldset = fieldsets.fieldset(2, LineFieldSet.class);
        fieldset.badgeShouldNotExists();
        fieldset.expand();
        fieldset.fields().field("count").control(InputText.class).setValue("27");
        fieldset.badgeShouldHaveText("27");
        fieldset.fields().field("count").control(InputText.class).setValue("54");
        fieldset.badgeShouldHaveText("54");
        fieldset.fields().field("count").control(InputText.class).clear();
        fieldset.badgeShouldNotExists();
    }
    @Test
    public void testSimpleFieldset() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/badge/fieldset/simple/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        FieldSets fieldsets = page.widget(FormWidget.class).fieldsets();

        SimpleFieldSet fieldset = fieldsets.fieldset(0, SimpleFieldSet.class);
        fieldset.badgeShouldHaveText("12");
        fieldset.badgeShouldHaveShape(BadgeShape.ROUNDED);

        fieldset = fieldsets.fieldset(1, SimpleFieldSet.class);
        fieldset.badgeShouldHaveText("Humburg");
        fieldset.badgeShouldHaveShape(BadgeShape.SQUARE);
        fieldset.badgeShouldHaveImage("static/hamburg-3846525__340.jpg");
        fieldset.badgeShouldHaveImagePosition(BadgePosition.RIGHT);
        fieldset.badgeShouldHaveImageShape(BadgeShape.SQUARE);
        fieldset.badgeShouldHaveColor(Colors.DANGER);

        fieldset = fieldsets.fieldset(2, SimpleFieldSet.class);
        fieldset.badgeShouldNotExists();
        fieldset.fields().field("count").control(InputText.class).setValue("27");
        fieldset.badgeShouldHaveText("27");
        fieldset.fields().field("count").control(InputText.class).setValue("54");
        fieldset.badgeShouldHaveText("54");
        fieldset.fields().field("count").control(InputText.class).clear();
        fieldset.badgeShouldNotExists();
    }

    @Test
    public void testMultiFieldset() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/badge/fieldset/multi/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        FieldSets fieldsets = page.widget(FormWidget.class).fieldsets();

        MultiFieldSet fieldset = fieldsets.fieldset(0, MultiFieldSet.class);
        fieldset.badgeShouldHaveText("12");
        fieldset.badgeShouldHaveShape(BadgeShape.ROUNDED);

        fieldset = fieldsets.fieldset(1, MultiFieldSet.class);
        fieldset.badgeShouldHaveText("Humburg");
        fieldset.badgeShouldHaveShape(BadgeShape.SQUARE);
        fieldset.badgeShouldHaveImage("static/hamburg-3846525__340.jpg");
        fieldset.badgeShouldHaveImagePosition(BadgePosition.RIGHT);
        fieldset.badgeShouldHaveImageShape(BadgeShape.SQUARE);
        fieldset.badgeShouldHaveColor(Colors.DANGER);

        fieldset = fieldsets.fieldset(2, MultiFieldSet.class);
        fieldset.badgeShouldNotExists();
        fieldset.clickAddButton();
        fieldset.item(0).fields().field("count").control(InputText.class).setValue("27");
        fieldset.badgeShouldHaveText("27");
        fieldset.item(0).fields().field("count").control(InputText.class).setValue("54");
        fieldset.badgeShouldHaveText("54");
        fieldset.item(0).fields().field("count").control(InputText.class).clear();
        fieldset.badgeShouldNotExists();
    }
}
