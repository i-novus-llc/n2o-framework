package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.global.view.widget.table.ShowCountTypeEnum;
import net.n2oapp.framework.api.metadata.meta.cell.Cell;
import net.n2oapp.framework.api.metadata.meta.cell.ImageCell;
import net.n2oapp.framework.api.metadata.meta.cell.TextCell;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.Cards;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование компиляции виджета Карточки
 */
class CardsCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(),
                new N2oCellsPack(), new N2oCellsIOPack());
    }

    @Test
    void testCards() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testCardsCompile.page.xml")
                .get(new PageContext("testCardsCompile"));
        Cards cards = (Cards) page.getRegions().get("single").get(0).getContent().get(0);
        assertThat(cards.getSrc(), is("CardsTest"));
        assertThat(cards.getVerticalAlign(), is(Cards.PositionEnum.center));
        assertThat(cards.getHeight(), is("400px"));
        assertThat(cards.getCards().size(), is(2));

        Cards.Card card = cards.getCards().get(0);
        assertThat(card.getSize(), is(6));
        assertThat(card.getContent().size(), is(2));
        assertThat(card.getContent().get(0).getClassName(), is("font-weight-bold"));
        assertThat(card.getContent().get(0).getStyle().get("color"), is("red"));

        Cell cell = card.getContent().get(0).getComponent();
        assertThat(cell, instanceOf(TextCell.class));
        assertThat(cell.getSrc(), is("TextCell"));
        assertThat(cell.getId(), is("11"));
        assertThat(((TextCell) cell).getFieldKey(), is("test1"));
        assertThat(((TextCell) cell).getTooltipFieldId(), is("tooltip"));

        cell = card.getContent().get(1).getComponent();
        assertThat(cell, instanceOf(ImageCell.class));
        assertThat(cell.getSrc(), is("cell12"));
        assertThat(cell.getId(), is("12"));

        card = cards.getCards().get(1);
        assertThat(card.getSize(), is(nullValue()));
        assertThat(card.getContent().size(), is(1));

        cell = card.getContent().get(0).getComponent();
        assertThat(cell, instanceOf(TextCell.class));
        assertThat(cell.getSrc(), is("TextCell"));

        assertThat(cards.getPaging().getNext(), is(true));
        assertThat(cards.getPaging().getPrev(), is(true));
        assertThat(cards.getPaging().getShowCount(), is(ShowCountTypeEnum.NEVER));
        assertThat(cards.getPaging().getSize(), is(5));
        assertThat(cards.getPaging().getSrc(), is("pagingSrc"));

        //второй виджет карточек
        cards = (Cards) page.getRegions().get("single").get(0).getContent().get(1);
        assertThat(cards.getSrc(), is("CardsWidget"));
        assertThat(cards.getVerticalAlign(), is(Cards.PositionEnum.top));
        assertThat(cards.getHeight(), is("300px"));
        assertThat(cards.getCards().size(), is(1));

        card = cards.getCards().get(0);
        assertThat(card.getSize(), is(nullValue()));
        assertThat(card.getContent().size(), is(1));

        cell = card.getContent().get(0).getComponent();
        assertThat(cell, instanceOf(ImageCell.class));
        assertThat(cell.getSrc(), is("ImageCell"));
        assertThat(cell.getId(), is("31"));

        assertThat(cards.getPaging().getNext(), is(false));
        assertThat(cards.getPaging().getPrev(), is(false));
        assertThat(cards.getPaging().getShowCount(), is(ShowCountTypeEnum.ALWAYS));
        assertThat(cards.getPaging().getSize(), is(10));
    }
}
