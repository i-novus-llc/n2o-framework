package net.n2oapp.framework.sandbox.autotest.examples;

import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.page.LeftRightPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
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

@SpringBootTest(properties = {"n2o.engine.test.classpath=/examples/crud/"},
        classes = AutoTestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MasterDetailAT extends AutoTestBase {

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
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("/examples/master_detail/index.page.xml"),
                new CompileInfo("/examples/master_detail/test.query.xml"));
    }

    @Test
    public void crudTest() {
        LeftRightPage page = open(LeftRightPage.class);
        page.shouldExists();
        page.header().brandNameShouldBe("N2O");
        page.breadcrumb().crumb(0).shouldHaveLabel("Связь Master Detail");

        TableWidget table = page.left().region(0, SimpleRegion.class).content().widget(TableWidget.class);
        table.shouldExists();
        table.columns().headers().shouldHaveSize(2);
        TableWidget.Rows rows = table.columns().rows();
        table.paging().totalElementsShouldBe(4);
        rows.columnShouldHaveTexts(0, Arrays.asList("1", "2", "3", "4"));
        rows.columnShouldHaveTexts(1, Arrays.asList("test1", "test2", "test3", "test4"));

        FormWidget form = page.right().region(0, SimpleRegion.class).content().widget(FormWidget.class);
        InputText id = form.fields().field("id").control(InputText.class);
        id.shouldExists();
        InputText name = form.fields().field("name").control(InputText.class);
        name.shouldExists();

        rows.row(0).click();
        id.shouldHaveValue("1");
        name.shouldHaveValue("test1");
        rows.row(1).click();
        rows.shouldBeSelected(1);
        id.shouldHaveValue("2");
        name.shouldHaveValue("test2");
        rows.row(2).click();
        rows.shouldBeSelected(2);
        id.shouldHaveValue("3");
        name.shouldHaveValue("test3");
        rows.row(3).click();
        rows.shouldBeSelected(3);
        id.shouldHaveValue("4");
        name.shouldHaveValue("test4");
    }

}
