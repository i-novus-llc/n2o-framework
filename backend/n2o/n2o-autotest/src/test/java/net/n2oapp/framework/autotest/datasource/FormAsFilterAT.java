package net.n2oapp.framework.autotest.datasource;

import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.control.InputText;
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

import java.util.Arrays;


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
        builder.packs(new N2oApplicationPack(), new N2oAllPagesPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"));
    }

    @Test
    public void testFormAsFilter() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/datasource/formAsFilter/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasource/formAsFilter/test.query.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        InputText filterId = page.regions().region(0, SimpleRegion.class).content()
                .widget(0, FormWidget.class).fields().field("Фильтр по id").control(InputText.class);
        InputText filterName = page.regions().region(0, SimpleRegion.class).content()
                .widget(0, FormWidget.class).fields().field("Фильтр по name").control(InputText.class);
        Button searchButton = page.regions().region(0, SimpleRegion.class).content()
                .widget(0, FormWidget.class).toolbar().topLeft().button("Найти");
        Button clearButton = page.regions().region(0, SimpleRegion.class).content()
                .widget(0, FormWidget.class).toolbar().topLeft().button("Очистить");

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(1, TableWidget.class);

        table.columns().rows().shouldHaveSize(6);
        filterId.val("3");
        searchButton.click();
        table.columns().rows().shouldHaveSize(1);
        table.columns().rows().row(0).cell(0).textShouldHave("3");
        table.columns().rows().row(0).cell(1).textShouldHave("test3");
        filterName.val("test1");
        searchButton.click();
        table.columns().rows().shouldHaveSize(0);
        clearButton.click();
        table.columns().rows().shouldHaveSize(6);

        filterId.clear();
        filterName.val("test4");
        searchButton.click();
        table.columns().rows().shouldHaveSize(1);
        table.columns().rows().row(0).cell(0).textShouldHave("4");
        table.columns().rows().row(0).cell(1).textShouldHave("test4");
        clearButton.click();

        filterId.val("1");
        filterName.val("test1");
        searchButton.click();
        table.columns().rows().shouldHaveSize(1);
        table.columns().rows().row(0).cell(0).textShouldHave("1");
        table.columns().rows().row(0).cell(1).textShouldHave("test1");
        clearButton.click();

        filterId.val("1");
        filterName.val("test2");
        searchButton.click();
        table.columns().rows().shouldHaveSize(0);
        clearButton.click();

        filterId.clear();
        filterName.val("repeatTest");
        searchButton.click();
        table.columns().rows().shouldHaveSize(2);
        table.columns().rows().columnShouldHaveTexts(1, Arrays.asList("repeatTest", "repeatTest"));
        clearButton.click();

        filterId.val("6");
        filterName.val("repeatTest");
        searchButton.click();
        table.columns().rows().shouldHaveSize(1);
    }
}
