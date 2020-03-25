package net.n2oapp.framework.autotest.control;

import com.codeborne.selenide.CollectionCondition;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.*;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Автотесты компонента ввода с выбором в выпадающем списке в виде дерева
 */
public class InputSelectTreeAT extends AutoTestBase {

    private SimplePage simplePage;

    @BeforeClass
    public static void beforeClass() {
        configureSelenide();
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/controls/selecttree/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/default.header.xml"));

        simplePage = open(SimplePage.class);
        simplePage.shouldExists();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oHeaderPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oControlsPack(), new N2oControlsV2IOPack());
    }

    @Test
    public void inputSelectTreeTest() {
        InputSelectTree inputSelectTree = getFields().field("InputSelectTree").control(InputSelectTree.class);
        inputSelectTree.shouldHavePlaceholder("SelectOption");
        inputSelectTree.shouldBeUnselected();
        inputSelectTree.toggleOptions();
        inputSelectTree.shouldDisplayedOptions(CollectionCondition.size(4));
        inputSelectTree.setFilter("three");

        inputSelectTree.selectOption(0);
        inputSelectTree.selectOption(2);
        inputSelectTree.selectOption(1);

        inputSelectTree.toggleOptions();

        inputSelectTree.shouldBeSelected(0, "one");
        inputSelectTree.shouldBeSelected(1, "two");
        inputSelectTree.shouldBeSelected(2, "three");

        inputSelectTree.removeOption(1);

        inputSelectTree.shouldBeSelected(0, "one");
        inputSelectTree.shouldBeSelected(1, "three");

        inputSelectTree.removeAllOptions();
        inputSelectTree.shouldBeUnselected();
    }

    private Fields getFields() {
        return simplePage.single().widget(FormWidget.class).fields();
    }
}
