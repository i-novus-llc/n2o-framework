package net.n2oapp.framework.autotest.control;

import com.codeborne.selenide.CollectionCondition;
import net.n2oapp.framework.autotest.api.component.DropDownTree;
import net.n2oapp.framework.autotest.api.component.control.InputSelectTree;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
/**
 * Автотесты компонента ввода с выбором в выпадающем списке в виде дерева
 */
public class InputSelectTreeAT extends AutoTestBase {

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
                new N2oControlsPack(), new N2oControlsV2IOPack(), new N2oAllDataPack());
    }

    @Test
    public void inputSelectTreeTest() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/select_tree/simple/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        InputSelectTree inputSelectTree = page.widget(FormWidget.class).fields().field("InputSelectTree")
                .control(InputSelectTree.class);
        inputSelectTree.shouldHavePlaceholder("SelectOption");
        inputSelectTree.shouldBeUnselected();
        inputSelectTree.expandOptions();
        inputSelectTree.shouldDisplayedOptions(CollectionCondition.size(4));

//        Вернуть после исправлений
//        inputSelectTree.clearSearchField();
//        inputSelectTree.setFilter("three");

        inputSelectTree.selectOption(0);
        inputSelectTree.selectOption(3);
        inputSelectTree.selectOption(1);

        inputSelectTree.expandOptions();

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
    public void readFromQueryTest() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/select_tree/nodes/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/select_tree/nodes/test.query.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        InputSelectTree inputSelectTree = page.widget(FormWidget.class).fields().field("InputSelectTree")
                .control(InputSelectTree.class);

        inputSelectTree.shouldBeUnselected();
        inputSelectTree.expandOptions();
        inputSelectTree.shouldDisplayedOptions(CollectionCondition.size(2));
        inputSelectTree.expandParentOptions(0);
        inputSelectTree.shouldDisplayedOptions(CollectionCondition.size(5));

        inputSelectTree.selectOption(1);
        inputSelectTree.selectOption(3);
        inputSelectTree.selectOption(2);

        inputSelectTree.shouldBeSelected(0, "one");
        inputSelectTree.shouldBeSelected(1, "message");
        inputSelectTree.shouldBeSelected(2, "long message");
        // проверяем, что длина названий ограничена 15 символами
        inputSelectTree.shouldBeSelected(3, "very long messa...");

        inputSelectTree.removeAllOptions();
        inputSelectTree.shouldBeUnselected();
    }

    @Test
    public void testSearchMinLength() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/select_tree/throttle_delay/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/select_tree/throttle_delay/test.query.xml"));

        SimplePage simplePage = open(SimplePage.class);
        simplePage.shouldExists();

        InputSelectTree inputSelectTree = simplePage.widget(FormWidget.class)
                .fields().field("Input-select-tree min-length=4").control(InputSelectTree.class);
        inputSelectTree.expand();
        DropDownTree dropdown = inputSelectTree.dropdown();
        dropdown.shouldHaveItems(3);

        dropdown.setValue("a");
        dropdown.shouldHaveItems(3);

        dropdown.clear();
        inputSelectTree.openPopup();
        dropdown.setValue("aud");
        dropdown.shouldHaveItems(3);

        dropdown.clear();
        inputSelectTree.openPopup();
        dropdown.setValue("audi");
        dropdown.shouldHaveItems(1);
    }
}
