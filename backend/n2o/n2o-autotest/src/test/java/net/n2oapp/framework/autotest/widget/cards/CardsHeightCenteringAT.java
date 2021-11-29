package net.n2oapp.framework.autotest.widget.cards;

import net.n2oapp.framework.autotest.api.component.cell.BadgeCell;
import net.n2oapp.framework.autotest.api.component.cell.RatingCell;
import net.n2oapp.framework.autotest.api.component.cell.TextCell;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
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

        RatingCell ratingCell = card.columns().column(0).blocks().cell(2, RatingCell.class);
        ratingCell.shouldHaveCssClass("align-self-center");

        TextCell textCell = card.columns().column(1).blocks().cell(0, TextCell.class);
        textCell.shouldHaveCssClass("align-self-center");
        textCell.shouldHaveStyle("height: 100%;");

        BadgeCell buttonCell = card.columns().column(2).blocks().cell(3, BadgeCell.class);
        buttonCell.shouldHaveCssClass("align-self-end");
    }

}
