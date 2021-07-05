package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.CheckboxGroup;
import net.n2oapp.framework.autotest.api.component.control.InputSelect;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.RadioGroup;
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
 * Тестирование фильтрации списковых полей
 */
public class ListFieldFiltrationAT extends AutoTestBase {
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
        builder.packs(new N2oPagesPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oControlsPack(), new N2oHeaderPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/list/filter/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/list/filter/test.query.xml"));
    }

    @Test
    public void listFieldFiltrationTest() {
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
        filteredByLink.shouldHaveOptions("test1", "test2");
        filteredByConstant.shouldHaveOptions("test1", "test2");
        filteredByLinkWithCache.shouldHaveOptions("test1", "test2");
        radioGroup.shouldHaveOptions("test1", "test2");
        checkboxGroup.shouldHaveOptions("test1", "test2");

        type.val("2");
        filteredByLink.shouldHaveOptions("test3", "test4");
        filteredByConstant.shouldHaveOptions("test1", "test2");
        filteredByLinkWithCache.shouldHaveOptions("test3", "test4");
        radioGroup.shouldHaveOptions("test3", "test4");
        checkboxGroup.shouldHaveOptions("test3", "test4");
    }
}
