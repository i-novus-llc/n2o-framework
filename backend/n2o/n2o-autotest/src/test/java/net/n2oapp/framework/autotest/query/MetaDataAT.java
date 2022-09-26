package net.n2oapp.framework.autotest.query;

import net.n2oapp.framework.autotest.api.component.control.Select;
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

public class MetaDataAT extends AutoTestBase {

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
    public void testDynamic() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/query/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/query/dynamic.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"));

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        Select select1 = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class).fields().field("Поле 1").control(Select.class);
        Select select2 = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class).fields().field("Поле 2").control(Select.class);

        select1.shouldHaveOptions("1", "test2", "test3", "test4");
        select1.click();

        select2.shouldHaveOptions("2", "test2", "test3", "test4");
    }
}