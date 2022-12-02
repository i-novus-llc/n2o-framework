package net.n2oapp.framework.autotest.action;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
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

/**
 * Автотест для действия перехода по ссылке
 */
public class AnchorAT extends AutoTestBase {

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
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/action/anchor/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/anchor/test.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/anchor/test.query.xml"));
    }


    @Test
    public void testAnchorAction() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Тестирование действия перехода по ссылке");

        TableWidget table = page.widget(TableWidget.class);
        table.columns().rows().row(2).click();
        table.toolbar().topLeft().button("Открыть").click();

        SimplePage open = N2oSelenide.page(SimplePage.class);
        page.breadcrumb().crumb(1).shouldHaveLabel("Вторая страница");
        open.toolbar().bottomRight().button("Ссылка").click();

        open.urlShouldMatches(getBaseUrl() + "/link/3/");
    }
}
