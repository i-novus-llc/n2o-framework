package net.n2oapp.framework.autotest.widget.form;

import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.Select;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

/**
 * Автотест Форма как фильтры таблицы
 */
class FormAsFilterAT extends AutoTestBase {

    @BeforeAll
    static void beforeClass() {
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
    void openWithoutParam() {
        setResourcePath("net/n2oapp/framework/autotest/widget/form/filter/simple");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/form/filter/simple/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/form/filter/simple/test.query.xml"));
        StandardPage page = open(StandardPage.class);
        Selenide.refresh();
        page.breadcrumb().crumb(0).shouldHaveLabel("Форма как фильтры таблицы");

        FormWidget form = page.regions().region(0, SimpleRegion.class).content().widget(0, FormWidget.class);
        form.shouldExists();
        form.fields().shouldHaveSize(2);

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(1, TableWidget.class);
        table.shouldExists();
        table.columns().rows().shouldHaveSize(4);

        page.shouldHaveUrlMatches(".*/#/");

        Select select = form.fields().field("Period").control(Select.class);
        select.openPopup();
        select.dropdown().selectItem(0);
        select.shouldSelected("Week");

        table.columns().rows().shouldHaveSize(1);
        page.shouldHaveUrlMatches(".*/#/\\?period=WEEK");

        select.clear();

        table.columns().rows().shouldHaveSize(4);
        page.shouldHaveUrlMatches(".*/#/");

        InputText inputText = form.fields().field("Uid").control(InputText.class);
        inputText.click();
        inputText.setValue("1");

        table.columns().rows().shouldHaveSize(1);
        page.shouldHaveUrlMatches(".*/#/\\?uid=1");

        inputText.click();
        inputText.clear();
        table.columns().rows().shouldHaveSize(4);
        page.shouldHaveUrlMatches(".*/#/");
    }

    @Test
    void openWithIdParam() {
        setResourcePath("net/n2oapp/framework/autotest/widget/form/filter/simple");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/form/filter/simple/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/form/filter/simple/test.query.xml"));
        StandardPage page = open(StandardPage.class, "/", Collections.singletonMap("uid", "3"));
        Selenide.refresh();
        page.breadcrumb().crumb(0).shouldHaveLabel("Форма как фильтры таблицы");

        FormWidget form = page.regions().region(0, SimpleRegion.class).content().widget(0, FormWidget.class);
        form.shouldExists();
        form.fields().shouldHaveSize(2);

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(1, TableWidget.class);
        table.shouldExists();
        table.columns().rows().shouldHaveSize(1);

        page.shouldHaveUrlMatches(".*/#/\\?uid=3");

        InputText inputText = form.fields().field("Uid").control(InputText.class);
        inputText.shouldHaveValue("3");
        inputText.click();
        inputText.clear();
        table.columns().rows().shouldHaveSize(4);
    }

    @Test
    void openWithPeriodParam() {
        setResourcePath("net/n2oapp/framework/autotest/widget/form/filter/simple");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/form/filter/simple/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/form/filter/simple/test.query.xml"));
        StandardPage page = open(StandardPage.class, "/", Collections.singletonMap("period", "MONTH"));
        Selenide.refresh();
        page.breadcrumb().crumb(0).shouldHaveLabel("Форма как фильтры таблицы");

        FormWidget form = page.regions().region(0, SimpleRegion.class).content().widget(0, FormWidget.class);
        form.shouldExists();
        form.fields().shouldHaveSize(2);

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(1, TableWidget.class);
        table.shouldExists();
        table.columns().rows().shouldHaveSize(1);

        page.shouldHaveUrlMatches(".*/#/\\?period=MONTH");

        InputText inputText = form.fields().field("Uid").control(InputText.class);
        inputText.shouldBeEmpty();

        Select select = form.fields().field("Period").control(Select.class);
        select.shouldSelected("Month");
        select.clear();
        table.columns().rows().shouldHaveSize(4);
    }

    @Test
    void filterByButtonClick() {
        setResourcePath("net/n2oapp/framework/autotest/widget/form/filter/button_click");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/form/filter/button_click/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/form/filter/button_click/test.query.xml"));

        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Фильтрация по нажатию кнопки");

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(1, TableWidget.class);
        table.columns().rows().shouldHaveSize(4);

        FormWidget form = page.regions().region(0, SimpleRegion.class).content().widget(0, FormWidget.class);
        form.shouldExists();

        InputText searchField = form.fields().field("Поиск").control(InputText.class);
        searchField.click();
        searchField.setValue("test2");
        form.toolbar().topLeft().button("Найти").click();
        table.columns().rows().shouldHaveSize(1);
        table.columns().rows().row(0).cell(0).shouldHaveText("test2");

        form.toolbar().topLeft().button("Очистить").click();
        searchField.shouldHaveValue("");
        table.columns().rows().shouldHaveSize(4);
    }
}
