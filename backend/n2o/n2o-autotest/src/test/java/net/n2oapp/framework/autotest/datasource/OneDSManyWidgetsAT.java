package net.n2oapp.framework.autotest.datasource;

import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.cards.CardsWidget;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.api.component.widget.tiles.TilesWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест для одного датасоурса
 * на несколько виджетов
 */
public class OneDSManyWidgetsAT extends AutoTestBase {

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
    public void testOneDSManyWidgets() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/datasource/testOneDSManyWidgets/index.page.xml"),
                        new CompileInfo("net/n2oapp/framework/autotest/datasource/testOneDSManyWidgets/testOneDSManyWidgets.query.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(0, TableWidget.class);
        TilesWidget tiles = page.regions().region(0, SimpleRegion.class).content().widget(1, TilesWidget.class);
        CardsWidget cards = page.regions().region(0, SimpleRegion.class).content().widget(2, CardsWidget.class);

        table.columns().rows().shouldHaveSize(4);
        tiles.paging().totalElementsShouldBe(4);
        cards.paging().totalElementsShouldBe(4);
    }
}
