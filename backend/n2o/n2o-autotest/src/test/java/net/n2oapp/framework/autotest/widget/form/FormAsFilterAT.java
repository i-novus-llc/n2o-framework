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
import net.n2oapp.framework.config.metadata.pack.N2oHeaderPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

/**
 * Автотест Форма как фильтры таблицы
 */
public class FormAsFilterAT extends AutoTestBase {

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
        builder.packs(new N2oHeaderPack(), new N2oAllPagesPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/form/filter/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/form/filter/test.query.xml"));
    }

    @Test
    public void openWithoutParam() {
        StandardPage page = open(StandardPage.class);
        Selenide.refresh();
        page.breadcrumb().titleShouldHaveText("Форма как фильтры таблицы");

        FormWidget form = page.regions().region(0, SimpleRegion.class).content().widget(0, FormWidget.class);
        form.shouldExists();
        form.fields().shouldHaveSize(2);

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(1, TableWidget.class);
        table.shouldExists();
        table.columns().rows().shouldHaveSize(4);

        page.urlShouldMatches(getBaseUrl() + "/#/");

        Select select = form.fields().field("Period").control(Select.class);
        select.select(0);
        select.shouldSelected("Week");

        table.columns().rows().shouldHaveSize(1);
        page.urlShouldMatches(getBaseUrl() + "/#/\\?period=WEEK");

        select.clear();

        table.columns().rows().shouldHaveSize(4);
        page.urlShouldMatches(getBaseUrl() + "/#/");

        InputText inputText = form.fields().field("Uid").control(InputText.class);
        inputText.val("1");

        table.columns().rows().shouldHaveSize(1);
        page.urlShouldMatches(getBaseUrl() + "/#/\\?uid=1");

        inputText.clear();
        table.columns().rows().shouldHaveSize(4);
        page.urlShouldMatches(getBaseUrl() + "/#/");
    }

    @Test
    public void openWithIdParam() {
        StandardPage page = open(StandardPage.class, "/", Collections.singletonMap("uid", "3"));  //"uid=3"
        Selenide.refresh();
        page.breadcrumb().titleShouldHaveText("Форма как фильтры таблицы");

        FormWidget form = page.regions().region(0, SimpleRegion.class).content().widget(0, FormWidget.class);
        form.shouldExists();
        form.fields().shouldHaveSize(2);

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(1, TableWidget.class);
        table.shouldExists();
        table.columns().rows().shouldHaveSize(1);

        page.urlShouldMatches(getBaseUrl() + "/#/\\?uid=3");

        InputText inputText = form.fields().field("Uid").control(InputText.class);
        inputText.shouldHaveValue("3");
        inputText.clear();
        table.columns().rows().shouldHaveSize(4);
    }

    @Test
    public void openWithPeriodParam() {
        StandardPage page = open(StandardPage.class, "/", Collections.singletonMap("period", "MONTH")); //"period=MONTH"
        Selenide.refresh();
        page.breadcrumb().titleShouldHaveText("Форма как фильтры таблицы");

        FormWidget form = page.regions().region(0, SimpleRegion.class).content().widget(0, FormWidget.class);
        form.shouldExists();
        form.fields().shouldHaveSize(2);

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(1, TableWidget.class);
        table.shouldExists();
        table.columns().rows().shouldHaveSize(1);

        page.urlShouldMatches(getBaseUrl() + "/#/\\?period=MONTH");

        InputText inputText = form.fields().field("Uid").control(InputText.class);
        inputText.shouldBeEmpty();

        Select select = form.fields().field("Period").control(Select.class);
        select.shouldSelected("Month");
        select.clear();
        table.columns().rows().shouldHaveSize(4);
    }
}
