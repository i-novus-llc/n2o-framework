package net.n2oapp.framework.autotest.app_datasource;

import net.n2oapp.framework.autotest.api.component.application.Sidebar;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AppDatasourceAT extends AutoTestBase {

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
    }

    @Test
    public void testSimpleDS() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/datasources/app_datasource/datasource/side.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/app_datasource/datasource/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/app_datasource/datasource/page2.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/app_datasource/datasource/test.query.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("App datasource. Ссылка на источник данных, объявленный в application.xml");

        Sidebar sidebar = page.sidebar();
        InputText id = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class)
                .fields().field("id").control(InputText.class);
        InputText name = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class)
                .fields().field("name").control(InputText.class);
        InputText step2 = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class)
                .fields().field("step2").control(InputText.class);

        id.shouldHaveValue("1");
        name.shouldHaveValue("test1");
        sidebar.nav().anchor(1).click();
        step2.shouldHaveValue("step2");
        sidebar.nav().anchor(0).click();
        id.shouldHaveValue("1");
        name.shouldHaveValue("test1");
    }
}
