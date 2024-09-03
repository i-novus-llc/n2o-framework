package net.n2oapp.framework.autotest.control;

import com.codeborne.selenide.CollectionCondition;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.DropDownTree;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputSelectTree;
import net.n2oapp.framework.autotest.api.component.fieldset.SimpleFieldSet;
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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Автотесты компонента ввода с выбором в выпадающем списке в виде дерева
 */
class InputSelectTreeAT extends AutoTestBase {

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
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oControlsPack(), new N2oControlsV2IOPack(), new N2oAllDataPack(), new N2oRegionsPack());
    }

    @Test
    void inputSelectTreeTest() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/select_tree/simple/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        InputSelectTree inputSelectTree = page.widget(FormWidget.class).fields().field("InputSelectTree")
                .control(InputSelectTree.class);
        inputSelectTree.shouldHavePlaceholder("SelectOption");
        inputSelectTree.shouldBeUnselected();
        inputSelectTree.click();
        inputSelectTree.shouldDisplayedOptions(CollectionCondition.size(4));

//        Вернуть после исправлений
//        inputSelectTree.clearSearchField();
//        inputSelectTree.setFilter("three");

        inputSelectTree.click();
        inputSelectTree.click();
        inputSelectTree.selectOption(0);
        inputSelectTree.selectOption(3);
        inputSelectTree.selectOption(1);

        inputSelectTree.shouldBeSelected(0, "one");
        inputSelectTree.shouldBeSelected(1, "two");
        inputSelectTree.shouldBeSelected(2, "long message");

        inputSelectTree.removeOption(1);

        inputSelectTree.shouldBeSelected(0, "one");
        inputSelectTree.shouldBeSelected(1, "long message");

        inputSelectTree.removeAllOptions();
        inputSelectTree.shouldBeUnselected();
    }

    @Test
    void readFromQueryTest() {
        setJsonPath("net/n2oapp/framework/autotest/control/select_tree/nodes");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/select_tree/nodes/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/select_tree/nodes/test.query.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        InputSelectTree inputSelectTree = page.widget(FormWidget.class).fields().field("InputSelectTree")
                .control(InputSelectTree.class);

        inputSelectTree.shouldBeUnselected();
        inputSelectTree.click();
        inputSelectTree.shouldDisplayedOptions(CollectionCondition.size(2));
        inputSelectTree.expandParentOptions(0);
        inputSelectTree.shouldDisplayedOptions(CollectionCondition.size(5));

        inputSelectTree.selectOption(1);
        inputSelectTree.selectOption(3);
        inputSelectTree.selectOption(2);

        inputSelectTree.shouldBeSelected(0, "one");
        inputSelectTree.shouldBeSelected(1, "message");
        inputSelectTree.shouldBeSelected(2, "long message");
        inputSelectTree.shouldBeSelected(3, "very long message");

        inputSelectTree.removeAllOptions();
        inputSelectTree.shouldBeUnselected();
    }

    @Test
    void testSearchMinLength() {
        setJsonPath("net/n2oapp/framework/autotest/control/select_tree/throttle_delay");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/select_tree/throttle_delay/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/select_tree/throttle_delay/test.query.xml"));

        SimplePage simplePage = open(SimplePage.class);
        simplePage.shouldExists();

        InputSelectTree inputSelectTree = simplePage.widget(FormWidget.class)
                .fields().field("Input-select-tree min-length=4").control(InputSelectTree.class);
        inputSelectTree.openPopup();
        inputSelectTree.shouldHaveDropdownMessage("Введите не менее 4 символов");
        DropDownTree dropdown = inputSelectTree.dropdown();
        dropdown.shouldHaveItems(0);

        dropdown.setValue("a");
        dropdown.shouldHaveItems(0);

        dropdown.clear();
        inputSelectTree.openPopup();
        dropdown.setValue("aud");
        dropdown.shouldHaveItems(0);
        inputSelectTree.shouldHaveDropdownMessage("Введите не менее 4 символов");

        dropdown.clear();
        inputSelectTree.openPopup();
        dropdown.setValue("audi");
        dropdown.shouldHaveItems(1);

        dropdown.clear();
        inputSelectTree.openPopup();
        dropdown.setValue("merc");
        inputSelectTree.shouldHaveDropdownMessage("Нет данных для отображения");
    }

    @Test
    @Disabled
    void testMaxTagsCount() {
        setJsonPath("net/n2oapp/framework/autotest/control/select_tree/max_count");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/select_tree/max_count/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/select_tree/max_count/test.query.xml"));

        SimplePage simplePage = open(SimplePage.class);
        simplePage.shouldExists();

        InputSelectTree inputSelectTree1 = simplePage.widget(FormWidget.class)
                .fields().field("one").control(InputSelectTree.class);
        InputSelectTree inputSelectTree2 = simplePage.widget(FormWidget.class)
                .fields().field("few").control(InputSelectTree.class);
        InputSelectTree inputSelectTree3 = simplePage.widget(FormWidget.class)
                .fields().field("unlimited").control(InputSelectTree.class);

        inputSelectTree1.click();
        inputSelectTree1.selectOption(0);
        inputSelectTree1.shouldSelectedMulti(new String[]{"Выбрано: 1"});
        inputSelectTree1.selectOption(1);
        inputSelectTree1.selectOption(2);
        inputSelectTree1.shouldSelectedMulti(new String[]{"Выбрано: 3"});
        inputSelectTree1.click();

        inputSelectTree2.click();
        inputSelectTree2.expandParentOptions(2);
        inputSelectTree2.selectOption(8);
        inputSelectTree2.selectOption(9);
        inputSelectTree2.shouldSelectedMulti(new String[]{"Александр ...", "Иван Алекс..."});
        inputSelectTree2.selectOption(7);
        inputSelectTree2.shouldSelectedMulti(new String[]{"Александр ...", "Иван Алекс...", "Александр ..."});
        inputSelectTree2.selectOption(6);
        inputSelectTree2.shouldSelectedMulti(new String[]{"Александр ...", "Александр ...", "Александр ...", "+ 1..."});
        inputSelectTree2.selectOption(10);
        inputSelectTree2.selectOption(11);
        inputSelectTree2.shouldSelectedMulti(new String[]{"Александр ...", "Александр ...", "Александр ...", "+ 3..."});
        inputSelectTree2.click();

        inputSelectTree3.click();
        inputSelectTree3.expandParentOptions(2);
        inputSelectTree3.selectOption(8);
        inputSelectTree3.selectOption(9);
        inputSelectTree3.selectOption(10);
        inputSelectTree3.selectOption(11);
        inputSelectTree3.selectOption(6);
        inputSelectTree3.selectOption(7);
        inputSelectTree3.shouldSelectedMultiSize(6);
    }

    @Test
    void testCheckingStrategy() {
        setJsonPath("net/n2oapp/framework/autotest/control/select_tree/checking_strategy");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/select_tree/checking_strategy/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/select_tree/checking_strategy/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/select_tree/checking_strategy/tree.query.xml")
        );

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        Fields fields = page.regions().region(0, SimpleRegion.class).content().widget(0, FormWidget.class).fieldsets().fieldset(0, SimpleFieldSet.class).fields();
        StandardButton uncheck = page.regions().region(0, SimpleRegion.class).content().widget(0, FormWidget.class).toolbar().topRight().button("uncheck");
        InputSelectTree selectedLessThanTwo = fields.field("Дерево с ограничением отображения выбранных = 2").control(InputSelectTree.class);
        InputSelectTree childStrategy = fields.field("Дерево со стратегией выбора child").control(InputSelectTree.class);
        InputSelectTree parentStrategy = fields.field("Дерево со стратегией выбора parent").control(InputSelectTree.class);

        InputSelectTree selectedNotRoot = page.regions()
                .region(0, SimpleRegion.class)
                .content()
                .widget(1, FormWidget.class)
                .fieldsets()
                .fieldset(0, SimpleFieldSet.class)
                .fields()
                .field("Дерево с выбранным не рутовым элементом")
                .control(InputSelectTree.class);

        selectedLessThanTwo.shouldSelectedMulti(new String[]{"11", "12"});
        childStrategy.shouldSelectedMulti(new String[]{"11", "12"});
        parentStrategy.shouldSelectedMulti(new String[]{"11", "12"});
        selectedNotRoot.shouldSelectedMulti(new String[]{"11"});

        selectedNotRoot.click();
        parentStrategy.click();
        childStrategy.click();
        selectedLessThanTwo.click();

        selectedNotRoot.shouldSelectedMulti(new String[]{"11", "111"});
        parentStrategy.shouldSelectedMulti(new String[]{"1"});
        childStrategy.shouldSelectedMulti(new String[]{"12", "111"});
        selectedLessThanTwo.shouldSelectedMulti(new String[]{"11", "12", "+ 2..."});

        selectedNotRoot.click();
        selectedNotRoot.dropdown().item(0).shouldNotBeSelected();
        selectedNotRoot.dropdown().item(0).expand();
        selectedNotRoot.dropdown().item(1).shouldBeSelected();
        selectedNotRoot.dropdown().item(1).expand();
        selectedNotRoot.dropdown().item(2).shouldBeSelected();
        selectedNotRoot.dropdown().item(3).shouldNotBeSelected();
        uncheck.click();

        parentStrategy.click();
        parentStrategy.dropdown().item(0).shouldBeSelected();
        parentStrategy.dropdown().item(0).expand();
        parentStrategy.dropdown().item(1).shouldBeSelected();
        parentStrategy.dropdown().item(1).expand();
        parentStrategy.dropdown().item(2).shouldBeSelected();
        parentStrategy.dropdown().item(3).shouldBeSelected();
        uncheck.click();

        childStrategy.click();
        childStrategy.dropdown().item(0).shouldBeSelected();
        childStrategy.dropdown().item(0).expand();
        childStrategy.dropdown().item(1).shouldBeSelected();
        childStrategy.dropdown().item(1).expand();
        childStrategy.dropdown().item(2).shouldBeSelected();
        childStrategy.dropdown().item(3).shouldBeSelected();
        uncheck.click();

        selectedLessThanTwo.click();
        selectedLessThanTwo.dropdown().item(0).shouldBeSelected();
        selectedLessThanTwo.dropdown().item(0).expand();
        selectedLessThanTwo.dropdown().item(1).shouldBeSelected();
        selectedLessThanTwo.dropdown().item(1).expand();
        selectedLessThanTwo.dropdown().item(2).shouldBeSelected();
        selectedLessThanTwo.dropdown().item(3).shouldBeSelected();
        uncheck.click();
    }

    @Test
    void testEnabledFieldId() {
        setJsonPath("net/n2oapp/framework/autotest/control/select_tree/enabled_field_id");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/select_tree/enabled_field_id/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/select_tree/enabled_field_id/test.query.xml")
        );

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        InputSelectTree singleTree = page.regions()
                .region(0, SimpleRegion.class)
                .content()
                .widget(0, FormWidget.class)
                .fieldsets()
                .fieldset(0, SimpleFieldSet.class)
                .fields()
                .field("Сингл")
                .control(InputSelectTree.class);
        InputSelectTree multiTree = page.regions()
                .region(0, SimpleRegion.class)
                .content()
                .widget(0, FormWidget.class)
                .fieldsets()
                .fieldset(0, SimpleFieldSet.class)
                .fields()
                .field("мульти")
                .control(InputSelectTree.class);

        singleTree.click();
        singleTree.dropdown().item(0).shouldBeDisabled();
        singleTree.dropdown().item(0).expand();
        singleTree.dropdown().item(1).shouldBeDisabled();
        singleTree.dropdown().item(1).click();
        singleTree.shouldBeEmpty();
        singleTree.dropdown().item(1).expand();
        singleTree.dropdown().item(2).shouldBeEnabled();
        singleTree.dropdown().item(2).click();
        singleTree.shouldHaveValue("child1");

        multiTree.click();
        multiTree.dropdown().item(0).shouldBeDisabled();
        multiTree.dropdown().item(0).expand();
        multiTree.dropdown().item(1).shouldBeDisabled();
        multiTree.dropdown().item(1).click();
        multiTree.shouldBeEmpty();
        multiTree.dropdown().item(1).expand();
        multiTree.dropdown().item(2).shouldBeEnabled();
        multiTree.dropdown().item(2).click();
        multiTree.dropdown().item(3).shouldBeEnabled();
        multiTree.dropdown().item(3).click();
        multiTree.shouldSelectedMulti(new String[]{"child1", "child2"});
    }
}
