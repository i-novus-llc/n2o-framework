package net.n2oapp.framework.autotest.widget.cards;

import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.cell.*;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.Paging;
import net.n2oapp.framework.autotest.api.component.widget.cards.Card;
import net.n2oapp.framework.autotest.api.component.widget.cards.CardsWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oHeaderPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест для виджета CardsWidget
 */
public class CardsAT extends AutoTestBase {

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
        builder.packs(new N2oHeaderPack(), new N2oAllPagesPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/cards/test.query.xml"));
    }

    @Test
    public void testCardsOne(){
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/cards/page1/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/cards/page1/modal.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("CardsWidget1");

        CardsWidget cardsWidget = page.widget(CardsWidget.class);
        cardsWidget.shouldExists();
        cardsWidget.shouldHaveItems(2);
        cardsWidget.paging().totalElementsShouldBe(2);

        Card card = cardsWidget.card(0);
        card.shouldExists();

        card.columns().shouldHaveSize(1);
        card.columns().column(0).shouldHaveWidth(5);
        card.columns().column(0).blocks().shouldHaveSize(4);

        TextCell textCell = card.columns().column(0).blocks().cell(0, TextCell.class);
        textCell.textShouldHave("Hamburg");

        ImageCell imageCell = card.columns().column(0).blocks().cell(1, ImageCell.class);
        imageCell.shouldExists();
        imageCell.imageShouldBe(getBaseUrl() +"/images/hamburg-3846525__340.jpg");

        BadgeCell badgeCell = card.columns().column(0).blocks().cell(2, BadgeCell.class);
        badgeCell.textShouldHave("Germany");

        ToolbarCell toolbarCell = card.columns().column(0).blocks().cell(3, ToolbarCell.class);
        toolbarCell.toolbar().button("Info").shouldHaveColor(Colors.SUCCESS);
        toolbarCell.toolbar().button("Info").click();

        Modal modal = N2oSelenide.modal();
        modal.shouldExists();
        modal.shouldHaveTitle("ModalPage");
        modal.close();
        modal.shouldNotExists();

        card = cardsWidget.card(1);
        card.shouldExists();

        card.columns().shouldHaveSize(1);
        card.columns().column(0).shouldHaveWidth(5);
        card.columns().column(0).blocks().shouldHaveSize(4);

        textCell = card.columns().column(0).blocks().cell(0, TextCell.class);
        textCell.textShouldHave("Paris");

        imageCell = card.columns().column(0).blocks().cell(1, ImageCell.class);
        imageCell.shouldExists();
        imageCell.imageShouldBe(getBaseUrl() +"/images/paris-3193674__340.jpg");

        badgeCell = card.columns().column(0).blocks().cell(2, BadgeCell.class);
        badgeCell.textShouldHave("France");

        toolbarCell = card.columns().column(0).blocks().cell(3, ToolbarCell.class);
        toolbarCell.toolbar().button("Info").shouldHaveColor(Colors.SUCCESS);
    }

    @Test
    public void testCardsTwo(){
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/cards/page2/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("CardsWidget2");

        CardsWidget cardsWidget = page.widget(CardsWidget.class);
        cardsWidget.shouldExists();
        cardsWidget.shouldHaveItems(2);
        cardsWidget.paging().totalElementsShouldBe(2);

        Card card = cardsWidget.card(0);
        card.shouldExists();

        card.columns().shouldHaveSize(2);
        card.columns().column(0).shouldHaveWidth(3);
        card.columns().column(0).blocks().shouldHaveSize(2);
        card.columns().column(1).shouldHaveWidth(2);
        card.columns().column(1).blocks().shouldHaveSize(5);

        TextCell textCell = card.columns().column(0).blocks().cell(0, TextCell.class);
        textCell.textShouldHave("Hamburg");

        ImageCell imageCell = card.columns().column(0).blocks().cell(1, ImageCell.class);
        imageCell.shouldExists();
        imageCell.imageShouldBe(getBaseUrl() +"/images/hamburg-3846525__340.jpg");

        IconCell iconCell = card.columns().column(1).blocks().cell(0, IconCell.class);
        iconCell.textShouldHave("ship");

        BadgeCell badgeCell = card.columns().column(1).blocks().cell(1, BadgeCell.class);
        badgeCell.textShouldHave("Germany");

        ProgressBarCell progressBarCell = card.columns().column(1).blocks().cell(2, ProgressBarCell.class);
        progressBarCell.valueShouldBe("50");

        RatingCell ratingCell = card.columns().column(1).blocks().cell(3, RatingCell.class);
        ratingCell.valueShouldBe("4");

        CheckboxCell checkboxCell = card.columns().column(1).blocks().cell(4, CheckboxCell.class);
        checkboxCell.shouldBeChecked();

        card = cardsWidget.card(1);
        card.shouldExists();

        card.columns().shouldHaveSize(2);
        card.columns().column(0).shouldHaveWidth(3);
        card.columns().column(0).blocks().shouldHaveSize(2);
        card.columns().column(1).shouldHaveWidth(2);
        card.columns().column(1).blocks().shouldHaveSize(5);

        textCell = card.columns().column(0).blocks().cell(0, TextCell.class);
        textCell.textShouldHave("Paris");

        imageCell = card.columns().column(0).blocks().cell(1, ImageCell.class);
        imageCell.shouldExists();
        imageCell.imageShouldBe(getBaseUrl() +"/images/paris-3193674__340.jpg");

        iconCell = card.columns().column(1).blocks().cell(0, IconCell.class);
        iconCell.textShouldHave("bicycle");

        badgeCell = card.columns().column(1).blocks().cell(1, BadgeCell.class);
        badgeCell.textShouldHave("France");

        progressBarCell = card.columns().column(1).blocks().cell(2, ProgressBarCell.class);
        progressBarCell.valueShouldBe("70");

        ratingCell = card.columns().column(1).blocks().cell(3, RatingCell.class);
        ratingCell.valueShouldBe("4.5");

        checkboxCell = card.columns().column(1).blocks().cell(4, CheckboxCell.class);
        checkboxCell.shouldBeUnchecked();
    }

    @Test
    public void testPaging() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/cards/paging/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/cards/paging/test.query.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        CardsWidget cards = page.regions().region(0, SimpleRegion.class).content().widget(CardsWidget.class);
        cards.shouldExists();

        Paging paging = cards.paging();
        paging.totalElementsShouldBe(8);
        paging.prevShouldNotExist();
        paging.nextShouldNotExist();
        paging.firstShouldExist();
        paging.firstShouldHaveIcon("fa-angle-double-left");
        paging.lastShouldNotExist();

        paging.activePageShouldBe("1");
        cards.card(0).columns().column(0).blocks().cell(0, TextCell.class).textShouldHave("test1");
        cards.shouldHaveItems(3);
        paging.selectPage("3");
        paging.activePageShouldBe("3");
        cards.shouldHaveItems(2);
        cards.card(0).columns().column(0).blocks().cell(0, TextCell.class).textShouldHave("test7");
        paging.selectFirst();
        paging.activePageShouldBe("1");
        cards.card(0).columns().column(0).blocks().cell(0, TextCell.class).textShouldHave("test1");


        CardsWidget cards2 = page.regions().region(0, SimpleRegion.class).content().widget(1, CardsWidget.class);
        paging = cards2.paging();
        paging.totalElementsShouldNotExist();
        paging.prevShouldExist();
        paging.prevShouldHaveLabel("Prev");
        paging.prevShouldHaveIcon("fa-angle-down");
        paging.nextShouldExist();
        paging.nextShouldHaveLabel("Next");
        paging.nextShouldHaveIcon("fa-angle-up");
        paging.firstShouldExist();
        paging.firstShouldHaveLabel("First");
        paging.firstShouldHaveIcon("fa-angle-double-down");
        paging.lastShouldExist();
        paging.lastShouldHaveLabel("Last");
        paging.lastShouldHaveIcon("fa-angle-double-up");

        paging.activePageShouldBe("1");
        cards2.card(0).columns().column(0).blocks().cell(0, TextCell.class).textShouldHave("test1");
        paging.selectNext();
        paging.activePageShouldBe("2");
        cards2.card(0).columns().column(0).blocks().cell(0, TextCell.class).textShouldHave("test4");
        paging.selectPrev();
        paging.activePageShouldBe("1");
        paging.selectLast();
        cards2.shouldHaveItems(2);
        cards2.card(0).columns().column(0).blocks().cell(0, TextCell.class).textShouldHave("test7");
    }

}
