package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.AutoComplete;
import net.n2oapp.framework.autotest.api.component.control.InputText;
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

        autoComplete = page.widget(FormWidget.class).fields().field("AutoComplete4")
                .control(AutoComplete.class);
        autoComplete.shouldExists();

        autoComplete.click();
        autoComplete.setValue("Иванов");
        autoComplete.chooseDropdownOption("Иванов К.Л.");
        autoComplete.shouldHaveValue("12");
        autoComplete.setValue("А.А.");
        autoComplete.chooseDropdownOption("Соколова А.А.");
        autoComplete.shouldHaveValue("15");

        autoComplete = page.widget(FormWidget.class).fields().field("AutoComplete5")
                .control(AutoComplete.class);
        autoComplete.click();
        autoComplete.setValue("Иванов");
        autoComplete.chooseDropdownOption("Иванов К.Л.");
        autoComplete.shouldHaveTags(new String[]{"12"});
        autoComplete.setValue("А.А.");
        autoComplete.chooseDropdownOption("Соколова А.А.");
        autoComplete.shouldHaveTags(new String[]{"12", "15"});
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
        autoComplete.shouldBeOpened();
        autoComplete.shouldHaveDropdownOptions(new String[]{"test4", "test5"});

        select.clear();
        select.shouldBeEmpty();
        autoComplete.shouldHaveValue("test");
        autoComplete.click();
        autoComplete.shouldBeOpened();
        autoComplete.shouldHaveDropdownOptions(new String[]{"test1", "test2", "test3", "test4", "test5", "test6"});
        autoComplete.setValue("3");
        autoComplete.shouldHaveDropdownOptions(new String[]{"test3"});
    }

    @Test
    void testMask() {
        setResourcePath("net/n2oapp/framework/autotest/control/auto_complete/mask");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/auto_complete/mask/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/auto_complete/mask/test.query.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Fields fields = page.widget(FormWidget.class).fields();

        // ========== 1. ПРОВЕРКА FREE РЕЖИМА ==========
        // mask-paste-mode="free"
        AutoComplete autoCompleteFree = fields.field("namesFree").control(AutoComplete.class);
        autoCompleteFree.shouldExists();
        InputText input = fields.field("input").control(InputText.class);

        // 1.1 Атрибут mask - ручной ввод по маске 99-999-99-99
        autoCompleteFree.click();
        autoCompleteFree.setValue("123456789");
        autoCompleteFree.shouldHaveValue("12-345-67-89");

        // Проверка, что больше 9 цифр не вставляется
        autoCompleteFree.clear();
        autoCompleteFree.setValue("12345678901");
        autoCompleteFree.shouldHaveValue("12-345-67-89");

        // 1.2 Атрибут mask - поиск по частично заполненному значению
        autoCompleteFree.clear();
        autoCompleteFree.setValue("11");
        autoCompleteFree.shouldHaveDropdownOptions(new String[]{"11-111-11-26"});

        // 1.3 Атрибут mask-paste-mode="free" - вставка с невалидными символами (сохраняется как есть)
        autoCompleteFree.clear();
        autoCompleteFree.click();
        // ручной ввод
        autoCompleteFree.setValue("1a2b3c4d5e6f7g8h9i");
        autoCompleteFree.shouldHaveValue("12-345-67-89");
        // копировать из буфера
        input.setValue("1a2b3c4d5e6f7g8h9i");
        input.copyValue();
        autoCompleteFree.pasteValue();
        autoCompleteFree.shouldHaveValue("1a2b3c4d5e6f7g8h9i");

        // 1.4 Выбор из выпадающего списка с маской
        autoCompleteFree.clear();
        autoCompleteFree.setValue("33");
        autoCompleteFree.shouldHaveDropdownOptions(new String[]{"33-333-33-26"});
        autoCompleteFree.chooseDropdownOption("33-333-33-26");
        autoCompleteFree.shouldHaveTags(new String[]{"33-333-33-..."});

        // ========== 2. ПРОВЕРКА STRICT РЕЖИМА ==========
        // mask-paste-mode="strict"
        AutoComplete autoCompleteStrict = fields.field("namesStrict").control(AutoComplete.class);
        autoCompleteStrict.shouldExists();

        // 2.1 Атрибут mask - ручной ввод по маске
        autoCompleteStrict.click();
        // ручной ввод
        autoCompleteStrict.setValue("123456789");
        autoCompleteStrict.shouldHaveValue("12-345-67-89");
        // копировать из буфера
        input.setValue("1a2b3c4d5e6f7g8h9i");
        input.copyValue();
        autoCompleteStrict.pasteValue();
        autoCompleteStrict.shouldHaveValue("12-345-67-89");

        // 2.2 Атрибут mask-paste-mode="strict" - вставка фильтруется по маске (только цифры)
        autoCompleteStrict.clear();
        autoCompleteStrict.setValue("1a2b3c4d5e6f7g8h9i");
        autoCompleteStrict.shouldHaveValue("12-345-67-89");

        // 2.3 Выбор из выпадающего списка
        autoCompleteStrict.clear();
        autoCompleteStrict.setValue("44");
        autoCompleteStrict.shouldHaveDropdownOptions(new String[]{"44-444-44-26"});
        autoCompleteStrict.chooseDropdownOption("44-444-44-26");
        autoCompleteStrict.shouldHaveTags(new String[]{"44-444-44-..."});

        // если ввести цифру, её возможно удалить или изменить
        AutoComplete auto = fields.field("auto").control(AutoComplete.class);
        auto.shouldExists();
        auto.click();
        auto.setValue("987654321");
        auto.shouldHaveValue("98-765-43-21");
        auto.clear();
        auto.shouldBeEmpty();
        auto.setValue("123456789");
        auto.shouldHaveValue("12-345-67-89");
    }
}
