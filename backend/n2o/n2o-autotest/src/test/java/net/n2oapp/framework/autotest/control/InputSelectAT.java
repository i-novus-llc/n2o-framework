package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputSelect;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static net.n2oapp.framework.autotest.N2oSelenide.page;

/**
 * Автотест компонента ввода текста с выбором из выпадающего списка
 */
public class InputSelectAT extends AutoTestBase {


    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oHeaderPack(), new N2oWidgetsPack(), new N2oActionsPack(),
                new N2oFieldSetsPack(), new N2oControlsPack(), new N2oAllDataPack(), new N2oCellsPack());
    }

    @Test
    public void testSingle() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/input_select/simple/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Fields fields = page.widget(FormWidget.class).fields();
        InputSelect input = fields.field("InputSelect").control(InputSelect.class);
        input.shouldExists();

        // close popup by icon
        input.expand();
        input.shouldBeExpanded();
        input.collapse();
        input.shouldBeCollapsed();

        input.shouldBeEmpty();
        input.shouldHaveOptions("One", "Two", "Three");
        // close popup by click on options
        input.select(1);
        input.shouldBeCollapsed();
        input.shouldSelected("Two");
        input.clear();
        input.shouldBeEmpty();

        input.val("Three");
        input.shouldHaveValue("Three");
        input.collapse();

        InputSelect input2 = fields.field("InputSelect1").control(InputSelect.class);
        input2.itemShouldBeEnabled(true, "One");
        input2.itemShouldBeEnabled(true, "Two");
        input2.itemShouldBeEnabled(false, "Three");
        input2.itemShouldBeEnabled(true, "Four");

        // close popup by click on outside area
        input2.expand();
        input2.shouldBeExpanded();
        input.click();
        input2.shouldBeCollapsed();
    }

    @Test
    public void testColorStatus() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/input_select/simple/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        InputSelect input = page.widget(FormWidget.class).fields().field("InputSelect2")
                .control(InputSelect.class);
        input.shouldExists();

        input.itemShouldHaveStatusColor("One", Colors.SUCCESS);
        input.itemShouldHaveStatusColor("Two", Colors.PRIMARY);
        input.itemShouldHaveStatusColor("Three", Colors.DANGER);
    }

    @Test
    public void testMulti() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/input_select/multi/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        InputSelect input = page.widget(FormWidget.class).fields().field("InputSelect")
                .control(InputSelect.class);
        input.shouldExists();

        input.shouldBeEmpty();
        input.shouldHaveOptions("One", "Two", "Three");
        input.selectMulti(1, 2);
        input.shouldSelectedMulti("Two", "Three");
        input.clear();
        input.shouldBeEmpty();

        input.selectMulti(2, 1, 0);
        input.shouldSelectedMulti("Three", "Two", "One");

        // удаление выбранных значений
        input.clearItems("Three");
        input.shouldSelectedMulti("Two", "One");
        input.clearItems("Two", "One");
        input.shouldBeEmpty();
    }

    @Test
    public void testCheckboxes() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/input_select/checkboxes/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        InputSelect input = page.widget(FormWidget.class).fields().field("InputSelect")
                .control(InputSelect.class);
        input.shouldExists();

        input.shouldBeEmpty();
        input.shouldHaveOptions("One", "Two", "Three");
        input.selectMulti(1, 2);
        input.shouldSelectedMulti("Two", "Three");
        input.clear();
        input.shouldBeEmpty();

        input.selectMulti(2, 1, 0);
        input.shouldSelectedMulti("Three", "Two", "One");

        // удаление выбранных значений
        input.clearItems("Three");
        input.shouldSelectedMulti("Two", "One");
        input.clearItems("Two", "One");
        input.shouldBeEmpty();

        input.collapse();
        input = page.widget(FormWidget.class).fields().field("InputSelect3")
                .control(InputSelect.class);
        input.itemShouldBeEnabled(true, "One");
        input.itemShouldBeEnabled(true, "Two");
        input.itemShouldBeEnabled(false, "Three");
        input.itemShouldBeEnabled(true, "Four");
    }

    @Test
    public void testReadFromQuery() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/input_select/query/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/query/test.query.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        InputSelect input = page.widget(FormWidget.class).fields().field("InputSelect1")
                .control(InputSelect.class);
        input.shouldExists();

        input.shouldBeEmpty();
        input.shouldHaveOptions("name1", "name2", "name3");
        input.optionShouldHaveDescription("name1", "desc1");
        input.optionShouldHaveDescription("name3", "desc3");
        input.select(1);
        input.shouldSelected("name2");
        input.clear();
        input.shouldBeEmpty();
        input.val("name3");
        input.shouldHaveValue("name3");
        // сворачиваем popup, чтобы не накладывался на нижний контрол
        input.collapse();


        InputSelect input2 = page.widget(FormWidget.class).fields().field("InputSelect2")
                .control(InputSelect.class);
        input2.shouldExists();

        input2.shouldBeEmpty();
        input2.optionShouldHaveDescription("name1", "desc1");
        input2.optionShouldHaveDescription("name3", "desc3");
        input2.shouldHaveOptions("name1", "name2", "name3");
        input2.selectMulti(1, 2);
        input2.shouldSelectedMulti("name2", "name3");
        input2.clear();
        input2.shouldBeEmpty();
    }

    @Test
    public void testPrefilterByQueryParam() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/input_select/prefilter/by_query_param/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/prefilter/modalForm.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/prefilter/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/prefilter/gender.query.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        TableWidget tableWidget = page.widget(TableWidget.class);
        tableWidget.shouldExists();
        StandardButton open = tableWidget.toolbar().topLeft().button("Open");
        open.shouldExists();
        open.click();

        SimplePage new_page = page(SimplePage.class);
        new_page.shouldExists();
        FormWidget formWidget = page.widget(FormWidget.class);
        formWidget.shouldExists();
        InputSelect gender = formWidget.fields().field("Gender").control(InputSelect.class);
        InputSelect gender_with_const = formWidget.fields().field("Gender with const").control(InputSelect.class);
        gender.shouldExists();
        gender.shouldSelected("Мужской");
        gender.shouldHaveOptions("Мужской");
        gender_with_const.shouldExists();
        gender_with_const.shouldSelected("Женский");
        gender_with_const.shouldHaveOptions("Женский");
    }

    @Test
    public void testPrefilterByPathParam() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/input_select/prefilter/by_path_param/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/prefilter/modalForm.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/prefilter/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/prefilter/gender.query.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        TableWidget tableWidget = page.widget(TableWidget.class);
        tableWidget.shouldExists();
        StandardButton open = tableWidget.toolbar().topLeft().button("Open");
        open.shouldExists();
        open.click();

        SimplePage new_page = page(SimplePage.class);
        new_page.shouldExists();
        FormWidget formWidget = page.widget(FormWidget.class);
        formWidget.shouldExists();
        InputSelect gender = formWidget.fields().field("Gender").control(InputSelect.class);
        InputSelect gender_with_const = formWidget.fields().field("Gender with const").control(InputSelect.class);
        gender.shouldExists();
        gender.shouldSelected("Мужской");
        gender.shouldHaveOptions("Мужской");
        gender_with_const.shouldExists();
        gender_with_const.shouldSelected("Женский");
        gender_with_const.shouldHaveOptions("Женский");
    }

}
