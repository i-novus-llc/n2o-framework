package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.control.*;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Тестирование фильтрации списковых полей
 */
public class ListFieldAT extends AutoTestBase {

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
        builder.packs(new N2oApplicationPack(), new N2oAllPagesPack(), new N2oAllDataPack());
    }

    @Test
    public void listFieldFiltrationTest() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/list/filter/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/list/filter/test.query.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        Fields fields = page.widget(FormWidget.class).fields();

        InputText type = fields.field("type").control(InputText.class);

        InputSelect filteredByLink = fields.field("Фильтр по type").control(InputSelect.class);
        InputSelect filteredByConstant = fields.field("Фильтр по константе").control(InputSelect.class);
        InputSelect filteredByLinkWithCache = fields.field("Фильтр по ссылке с кэшированием").control(InputSelect.class);
        RadioGroup radioGroup = fields.field("Фильтр по радио кнопкам").control(RadioGroup.class);
        CheckboxGroup checkboxGroup = fields.field("Фильтр по чекбоксам").control(CheckboxGroup.class);

        type.shouldHaveValue("1");
        filteredByLink.openPopup();
        filteredByLink.dropdown().shouldHaveOptions("test1", "test2");
        filteredByConstant.openPopup();
        filteredByConstant.dropdown().shouldHaveOptions("test1", "test2");
        filteredByLinkWithCache.openPopup();
        filteredByLinkWithCache.dropdown().shouldHaveOptions("test1", "test2");
        radioGroup.shouldHaveOptions("test1", "test2");
        checkboxGroup.shouldHaveOptions("test1", "test2");

        type.click();
        type.setValue("2");
        filteredByLink.openPopup();
        filteredByLink.dropdown().shouldHaveOptions("test3", "test4");
        filteredByConstant.openPopup();
        filteredByConstant.dropdown().shouldHaveOptions("test1", "test2");
        filteredByLinkWithCache.openPopup();
        filteredByLinkWithCache.dropdown().shouldHaveOptions("test3", "test4");
        radioGroup.shouldHaveOptions("test3", "test4");
        checkboxGroup.shouldHaveOptions("test3", "test4");
    }

    @Test
    public void testOptionsFromDS() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/list/options_from_ds/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/list/options_from_ds/test.query.xml"));

        StandardPage page = open(StandardPage.class);
        Fields fields = page.regions().region(0, SimpleRegion.class).content().widget(1, FormWidget.class).fields();
        Select select = fields.field("select").control(Select.class);
        InputSelect inputSelect = fields.field("input_select").control(InputSelect.class);
        AutoComplete autoComplete = fields.field("auto_complete").control(AutoComplete.class);
        RadioGroup radioGroup = fields.field("rdg").control(RadioGroup.class);
        CheckboxGroup checkboxGroup = fields.field("chg").control(CheckboxGroup.class);
        Pills pills = fields.field("pills").control(Pills.class);

        select.shouldSelected("test1");
        select.openPopup();
        select.dropdown().shouldHaveOptions("test1", "test2", "test3", "test4");
        inputSelect.shouldBeEmpty();
        inputSelect.openPopup();
        inputSelect.dropdown().shouldHaveOptions("test1", "test2", "test3", "test4");
        autoComplete.shouldBeEmpty();
        autoComplete.click();
        autoComplete.shouldHaveDropdownOptions("test1", "test2", "test3", "test4");
        radioGroup.shouldHaveOptions("test1", "test2", "test3", "test4");
        checkboxGroup.shouldBeChecked("test1");
        checkboxGroup.shouldBeUnchecked("test2");
        checkboxGroup.shouldHaveOptions("test1", "test2", "test3", "test4");
        pills.shouldHaveOptions("test1", "test2", "test3", "test4");
    }

    @Test
    public void testOptionFromParentDS() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/list/option_from_parent_ds/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/list/option_from_parent_ds/createDoc.page.xml"));

        StandardPage page = open(StandardPage.class);
        Button showModal = page.regions().region(0, SimpleRegion.class).content().widget(2, FormWidget.class).toolbar().bottomLeft().button("Добавить документ");

        showModal.click();
        Modal modal = N2oSelenide.modal();
        InputSelect inputSelect = modal.content(StandardPage.class).regions()
                .region(0, SimpleRegion.class).content().widget(FormWidget.class).fields().field("Группа документов").control(InputSelect.class);

        inputSelect.openPopup();
        inputSelect.dropdown().shouldHaveOptions("Документы, подтверждающие обучение", "Документы, подтверждающие размер заработной платы");
    }

    @Test
    public void testFetchValueDependencies() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/list/fetch_value/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/list/fetch_value/test.query.xml"));

        SimplePage page = open(SimplePage.class);
        Fields fields = page.widget(FormWidget.class).fields();
        fields.field("size 1").control(InputSelect.class).shouldBeEmpty();
        fields.field("size 2").control(InputSelect.class).shouldBeEmpty();
        fields.field("size 3").control(InputSelect.class).shouldBeEmpty();
        fields.field("size 4").control(InputSelect.class).shouldBeEmpty();
        fields.field("size 5").control(InputSelect.class).shouldBeEmpty();

        fields.field("def 1").control(InputSelect.class).shouldSelectedMulti("test1");
        fields.field("def 2").control(InputSelect.class).shouldSelectedMulti("test1", "test2");
        fields.field("def 3").control(InputSelect.class).shouldSelectedMulti("test1", "test2", "test3");
        fields.field("def 4").control(InputSelect.class).shouldSelectedMulti("test1", "test2", "test3", "test4");
        fields.field("def 5").control(InputSelect.class).shouldSelectedMulti("test1", "test2", "test3", "test4");

        fields.field("check").control(Checkbox.class).setChecked(true);
        fields.field("size 1").control(InputSelect.class).shouldSelectedMulti("test1");
        fields.field("size 2").control(InputSelect.class).shouldSelectedMulti("test1", "test2");
        fields.field("size 3").control(InputSelect.class).shouldSelectedMulti("test1", "test2", "test3");
        fields.field("size 4").control(InputSelect.class).shouldSelectedMulti("test1", "test2", "test3", "test4");
        fields.field("size 5").control(InputSelect.class).shouldSelectedMulti("test1", "test2", "test3", "test4");
    }
}
