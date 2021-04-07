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
public class AutoCompleteAT extends AutoTestBase {

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
        builder.packs(new N2oPagesPack(), new N2oHeaderPack(), new N2oWidgetsPack(),
                new N2oFieldSetsPack(), new N2oControlsPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));
    }

    @Test
    public void testAutoComplete() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/auto_complete/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        AutoComplete autoComplete = page.widget(FormWidget.class).fields().field("AutoComplete1")
                .control(AutoComplete.class);
        autoComplete.shouldExists();

        autoComplete.shouldBeEmpty();
        autoComplete.val("c");
        autoComplete.shouldHaveValue("c");
        autoComplete.shouldHaveDropdownOptions("abc", "ccc");
        autoComplete.chooseDropdownOption("ccc");
        autoComplete.shouldHaveValue("ccc");
        autoComplete.val("ab");
        autoComplete.shouldHaveDropdownOptions("abc");
        autoComplete.chooseDropdownOption("abc");
        autoComplete.shouldHaveValue("abc");
        autoComplete.val("d");
        autoComplete.shouldNotHaveDropdownOptions();
    }

    @Test
    public void testTags() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/auto_complete/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        AutoComplete autoComplete = page.widget(FormWidget.class).fields().field("AutoComplete2")
                .control(AutoComplete.class);
        autoComplete.shouldExists();

        autoComplete.addTag("item1");
        autoComplete.shouldHaveTags("item1");

        autoComplete.val("ab");
        autoComplete.shouldHaveDropdownOptions("abc");
        autoComplete.chooseDropdownOption("abc");
        autoComplete.shouldHaveTags("item1", "abc");

        autoComplete.addTag("item2");
        autoComplete.shouldHaveTags("item1", "abc", "item2");

        autoComplete.removeTag("item1");
        autoComplete.removeTag("item2");
        autoComplete.shouldHaveTags("abc");
        autoComplete.removeTag("abc");
        autoComplete.shouldBeEmpty();
    }

    @Test
    public void testPrefilters() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/auto_complete/pre_filters/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/auto_complete/pre_filters/test.query.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Fields fields = page.widget(FormWidget.class).fields();
        Select select = fields.field("type").control(Select.class);
        AutoComplete autoComplete = fields.field("auto").control(AutoComplete.class);
        autoComplete.shouldExists();

        select.shouldHaveValue("type1");
        autoComplete.shouldBeEmpty();
        autoComplete.click();
        autoComplete.shouldHaveDropdownOptions("test1", "test2", "test3");
        autoComplete.val("2");
        autoComplete.shouldHaveDropdownOptions("test2");

        select.select(1);
        select.shouldHaveValue("type2");
        autoComplete.val("test");
        autoComplete.shouldHaveDropdownOptions("test4", "test5");

        select.clear();
        autoComplete.click();
        autoComplete.shouldHaveDropdownOptions("test1", "test2", "test3", "test4", "test5", "test6");
        autoComplete.val("3");
        autoComplete.shouldHaveDropdownOptions("test3");
    }
}
