package net.n2oapp.framework.autotest.widget.cards;

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
 * Автотест для стилей блоков карточек
 */
public class CardsBlockStyleAT extends AutoTestBase {

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
                new CompileInfo("net/n2oapp/framework/autotest/widget/cards/style/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/cards/style/test.query.xml"));
    }

    @Test
    public void testCardBlockStyles() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        CardsWidget cardsWidget = page.widget(CardsWidget.class);
        cardsWidget.shouldExists();
        cardsWidget.shouldHaveItems(2);

        Card card = cardsWidget.card(0);
        card.shouldExists();

        Card.Block ratingCellBlock = card.columns().column(0).blocks().block(2);
        ratingCellBlock.shouldHaveCssClass("align-self-center");

        Card.Block textCellBlock = card.columns().column(1).blocks().block(0);
        textCellBlock.shouldHaveCssClass("align-self-center");
        textCellBlock.shouldHaveStyle("color: green");

        Card.Block buttonCellBlock = card.columns().column(2).blocks().block(1);
        buttonCellBlock.shouldHaveCssClass("align-self-end");
    }
}
