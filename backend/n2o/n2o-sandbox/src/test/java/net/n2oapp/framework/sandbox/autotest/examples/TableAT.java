package net.n2oapp.framework.sandbox.autotest.examples;

import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.run.AutoTestApplication;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest(properties = {"server.servlet.context-path=/", "n2o.engine.test.classpath=/examples/table/"},
        classes = AutoTestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TableAT extends AutoTestBase {

    private SimplePage page;

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        page = open(SimplePage.class);
        page.shouldExists();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/default/default.application.xml"),
                new CompileInfo("/examples/table/test.query.xml"),
                new CompileInfo("/examples/table/index.page.xml"));
    }

    @Test
    public void testTableBase() {
        page.header().shouldHaveBrandName("N2O");
        page.breadcrumb().crumb(0).shouldHaveLabel("Таблица");

        TableWidget table = page.widget(TableWidget.class);
        table.shouldExists();
        table.columns().headers().shouldHaveSize(2);
        table.columns().rows().shouldHaveSize(4);
    }

    @Test
    public void testTableData() {
        TableWidget table = page.widget(TableWidget.class);
        TableWidget.Rows rows = table.columns().rows();
        rows.columnShouldHaveTexts(0, Arrays.asList("test1", "test2", "test3", "test4"));
        rows.columnShouldHaveTexts(1, Arrays.asList("1", "1", "2", "2"));
    }

    @Test
    public void testTableClick() {
        TableWidget table = page.widget(TableWidget.class);

        TableWidget.Rows rows = table.columns().rows();
        //todo вернуть rows.shouldNotHaveSelectedRows();
        rows.row(1).click();
        rows.shouldBeSelected(1);
        rows.row(2).click();
        rows.shouldBeSelected(2);
        rows.row(3).click();
        rows.shouldBeSelected(3);
    }

}
