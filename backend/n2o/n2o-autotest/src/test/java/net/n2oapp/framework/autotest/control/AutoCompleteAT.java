package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.AutoComplete;
import net.n2oapp.framework.autotest.api.component.control.Select;
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
 * Автотест для компонента ввода текста с автозаполнением
 */
class AutoCompleteAT extends AutoTestBase {

    @BeforeAll
    static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(
                new N2oPagesPack(),
                new N2oApplicationPack(),
                new N2oWidgetsPack(),
                new N2oFieldSetsPack(),
                new N2oControlsPack(),
                new N2oAllDataPack()
        );
    }

    @Test
    void testAutoComplete() {
        setResourcePath("net/n2oapp/framework/autotest/control/auto_complete/simple");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/auto_complete/simple/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/auto_complete/simple/test.query.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        AutoComplete autoComplete = page.widget(FormWidget.class).fields().field("AutoComplete1")
                .control(AutoComplete.class);
        autoComplete.shouldExists();

        autoComplete.shouldBeEmpty();
        autoComplete.setValue("c");
        autoComplete.shouldHaveValue("c");
        autoComplete.shouldHaveDropdownOptions(new String[]{"abc", "ccc"});
        autoComplete.chooseDropdownOption("ccc");
        autoComplete.shouldHaveValue("ccc");
        autoComplete.click();
        autoComplete.clear();
        autoComplete.setValue("ab");
        autoComplete.shouldHaveDropdownOptions(new String[]{"abc"});
        autoComplete.chooseDropdownOption("abc");
        autoComplete.shouldHaveValue("abc");
        autoComplete.click();
        autoComplete.clear();
        autoComplete.setValue("d");
        autoComplete.shouldNotHaveDropdownOptions();
    }

    @Test
    void testTags() {
        setResourcePath("net/n2oapp/framework/autotest/control/auto_complete/simple");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/auto_complete/simple/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/auto_complete/simple/test.query.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        AutoComplete autoComplete = page.widget(FormWidget.class).fields().field("AutoComplete2")
                .control(AutoComplete.class);
        autoComplete.shouldExists();

        autoComplete.click();
        autoComplete.setValue("item1");
        autoComplete.enter();
        autoComplete.shouldHaveTags(new String[]{"item1"});

        autoComplete.click();
        autoComplete.setValue("ab");
        autoComplete.shouldHaveDropdownOptions(new String[]{"abc"});
        autoComplete.chooseDropdownOption("abc");
        autoComplete.shouldHaveTags(new String[]{"item1", "abc"});

        autoComplete.click();
        autoComplete.setValue("item2");
        autoComplete.enter();
        autoComplete.shouldHaveTags(new String[]{"item1", "abc", "item2"});

        autoComplete.removeTag("item1");
        autoComplete.removeTag("item2");
        autoComplete.shouldHaveTags(new String[]{"abc"});
        autoComplete.removeTag("abc");
        autoComplete.shouldBeEmpty();

        autoComplete = page.widget(FormWidget.class).fields().field("AutoComplete3")
                .control(AutoComplete.class);
        autoComplete.click();
        autoComplete.setValue("Ив");
        autoComplete.chooseDropdownOption("Иванов П.И.");
        autoComplete.click();
        autoComplete.setValue("К.Л.");
        autoComplete.chooseDropdownOption("Иванченко К.Л.");
        autoComplete.click();
        autoComplete.setValue("Иванов К.Л.");
        autoComplete.chooseDropdownOption("Иванов К.Л.");
        autoComplete.shouldHaveTags(new String[]{"Иванов П.И...", "Иванченко ...", "Иванов К.Л..."});

        // проверяем, что нажатие Enter не создает пустой тэг
        autoComplete.clear();
        autoComplete.click();
        autoComplete.enter();
        autoComplete.shouldHaveTags(new String[]{"Иванов П.И...", "Иванченко ...", "Иванов К.Л..."});
    }

    @Test
    void testPrefilters() {
        setResourcePath("net/n2oapp/framework/autotest/control/auto_complete/preFilters");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/auto_complete/preFilters/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/auto_complete/preFilters/test.query.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Fields fields = page.widget(FormWidget.class).fields();
        Select select = fields.field("type").control(Select.class);
        AutoComplete autoComplete = fields.field("auto").control(AutoComplete.class);
        autoComplete.shouldExists();

        select.shouldHaveValue("type1");
        autoComplete.shouldBeEmpty();
        autoComplete.click();
        autoComplete.shouldHaveDropdownOptions(new String[]{"test1", "test2", "test3"});
        autoComplete.click();
        autoComplete.setValue("2");
        autoComplete.shouldHaveDropdownOptions(new String[]{"test2"});

        select.openPopup();
        select.dropdown().selectItem(1);
        select.shouldHaveValue("type2");
        autoComplete.click();
        autoComplete.setValue("test");
        autoComplete.shouldHaveDropdownOptions(new String[]{"test4", "test5"});

        select.clear();
        autoComplete.click();
        autoComplete.shouldHaveDropdownOptions(new String[]{"test1", "test2", "test3", "test4", "test5", "test6"});
        autoComplete.setValue("3");
        autoComplete.shouldHaveDropdownOptions(new String[]{"test3"});
    }
}
