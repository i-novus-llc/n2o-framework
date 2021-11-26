package net.n2oapp.framework.autotest.widget.cards;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import net.n2oapp.framework.autotest.api.component.cell.TextCell;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.cards.Card;
import net.n2oapp.framework.autotest.api.component.widget.cards.CardsWidget;
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
 * Автотест для высоты блоков и центрирование текста
 */
public class CardsHeightCenteringAT extends AutoTestBase {

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
//        Configuration.headless = false;
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oApplicationPack(), new N2oAllPagesPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/cards/block_height_and_text_centering/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/cards/block_height_and_text_centering/test.query.xml"));
    }

    @Test
    public void testCardsHeightCentering() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        page.breadcrumb().titleShouldHaveText("Высота блоков и центрирование текста");

        CardsWidget cardsWidget = page.widget(CardsWidget.class);
        cardsWidget.shouldExists();
        cardsWidget.shouldHaveItems(2);

        Card card = cardsWidget.card(0);
        card.shouldExists();
//        card.columns().column(0).blocks().shouldHaveStyle("height: 100%");
        card.columns().column(0).blocks().cell(0, TextCell.class).shouldHave; .element().should(Condition.attribute("style", "height: 100%"));
//                .shouldHaveCssClass("align-items-center");
//        card.columns().column(0).blocks().shouldBeEmpty();
//        card.columns().column(0).blocks().cell(0, TextCell.class);

    }

}
