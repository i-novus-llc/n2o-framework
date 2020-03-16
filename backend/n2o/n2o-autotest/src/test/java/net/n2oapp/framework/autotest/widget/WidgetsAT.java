package net.n2oapp.framework.autotest.widget;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.cell.BadgeCell;
import net.n2oapp.framework.autotest.api.component.cell.ImageCell;
import net.n2oapp.framework.autotest.api.component.cell.TextCell;
import net.n2oapp.framework.autotest.api.component.control.InputControl;
import net.n2oapp.framework.autotest.api.component.control.SelectControl;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.list.ListWidget;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.impl.component.button.N2oDropdownButton;
import net.n2oapp.framework.autotest.impl.component.button.N2oStandardButton;
import net.n2oapp.framework.autotest.impl.component.control.N2oInputControl;
import net.n2oapp.framework.autotest.impl.component.page.N2oSimplePage;
import net.n2oapp.framework.autotest.impl.component.widget.N2oFormWidget;
import net.n2oapp.framework.autotest.impl.component.widget.table.N2oTableWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oHeaderPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Автотест виджетов
 */
public class WidgetsAT extends AutoTestBase {
    @BeforeClass
    public static void beforeClass() {
        configureSelenide();
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oHeaderPack(), new N2oAllPagesPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));
    }

    @Test
    public void testForm() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/form/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/form/testForm.object.xml"));
        SimplePage page = open(N2oSimplePage.class);
        page.shouldExists();
        page.single().shouldHaveSize(1);
        FormWidget form = page.single().widget(N2oFormWidget.class);
        form.fields().shouldHaveSize(2);
        StandardField surname = form.fields().field("Фамилия");
        surname.labelShouldHave(Condition.text("Фамилия"));
        surname.control(N2oInputControl.class).val("test");

        StandardField name = form.fields().field("Имя");
        name.shouldBeRequired();
        name.shouldHaveValidationMessage(Condition.text("Поле обязательно для заполнения"));
        name.control(N2oInputControl.class).val("1");
        surname.control(N2oInputControl.class).val("test");
        name.shouldHaveValidationMessage(Condition.text("Имя должно быть test"));
        name.control(N2oInputControl.class).val("test");
        surname.control(N2oInputControl.class).val("test");
        name.shouldHaveValidationMessage(Condition.empty);
    }

    @Test
    public void testTable() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/table/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/testTable.query.xml"));
        SimplePage page = open(N2oSimplePage.class);
        page.shouldExists();

        TableWidget widget = page.single().widget(N2oTableWidget.class);
        widget.filters().shouldBeVisible();
        widget.filters().toolbar().button("searchLabel").shouldBeEnabled();
        widget.filters().toolbar().button("resetLabel").shouldBeEnabled();
        widget.filters().fields().field("Имя").control(InputControl.class).val("test");
        widget.filters().fields().field("Пол").control(SelectControl.class).openOptions();
        widget.filters().fields().field("Пол").control(SelectControl.class).select(Condition.text("Мужской"));
        widget.filters().toolbar().button("resetLabel").click();
        widget.filters().fields().field("Имя").control(InputControl.class).shouldHaveValue("test");


        widget.toolbar().topRight().button(0, N2oStandardButton.class).click();
        widget.filters().shouldBeInvisible();
        widget.toolbar().topRight().button(0, N2oStandardButton.class).click();

        widget.columns().rows().row(0).cell(0).element().parent().shouldHave(Condition.cssClass("bg-danger"));
        widget.columns().rows().row(1).cell(0).element().parent().shouldHave(Condition.cssClass("bg-info"));
        widget.columns().rows().row(2).cell(0).element().parent().shouldHave(Condition.cssClass("bg-success"));
        widget.columns().headers().header(0).titleShouldHave(Condition.text("Имя"));
        widget.columns().headers().header(1).titleShouldHave(Condition.text("Фамилия"));
        widget.columns().headers().header(2).titleShouldHave(Condition.text("Дата рождения"));

        widget.toolbar().topRight().button(1, N2oDropdownButton.class).click();
        widget.toolbar().topRight().button(1, N2oDropdownButton.class).menuItem("Имя").click();
        widget.columns().headers().header(0).titleShouldHave(Condition.empty);
        widget.toolbar().topRight().button(1, N2oDropdownButton.class).menuItem("Имя").click();
        widget.columns().headers().header(0).titleShouldHave(Condition.text("Имя"));
    }

    @Test
    public void testList() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/list/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/list/form.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/list/testList.query.xml"));
        SimplePage page = open(N2oSimplePage.class);
        ListWidget listWidget = page.single().widget(ListWidget.class);
        listWidget.shouldHaveSize(10);
        listWidget.content(0).body(TextCell.class).textShouldHave("body1");
        listWidget.content(0).leftTop(ImageCell.class).srcShouldBe(getBaseUrl() + "/favicon.ico");
        listWidget.content(0).leftBottom(TextCell.class).textShouldHave("leftBottom1");
        listWidget.content(0).subHeader(BadgeCell.class).colorShouldBe(Colors.SUCCESS);
        listWidget.paging().totalElementsShouldBe(11);
        listWidget.paging().clickNext();
        listWidget.shouldHaveSize(1);
        listWidget.paging().clickPrev();
        listWidget.shouldHaveSize(10);

        listWidget.content(0).click();
        SimplePage openPage = N2oSelenide.page(SimplePage.class);
        openPage.shouldExists();
        FormWidget form = openPage.single().widget(FormWidget.class);
        form.shouldExists();
        form.fields().field("body").control(InputControl.class).shouldHaveValue("body1");

        page.shouldExists();
        page.shouldExists();

    }
}
