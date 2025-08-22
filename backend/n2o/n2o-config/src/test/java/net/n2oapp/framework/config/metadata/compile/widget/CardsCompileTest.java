package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.global.view.widget.table.ShowCountTypeEnum;
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
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;

/**
 * Тестирование компиляции виджета Карточки
 */
class CardsCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() {
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
        assertThat(cards, allOf(
                hasProperty("src", is("CardsTest")),
                hasProperty("verticalAlign", is(Cards.PositionEnum.CENTER)),
                hasProperty("height", is("400px")),
                hasProperty("content", hasSize(2))
        ));

        Cards.Card card = cards.getContent().get(0);
        assertThat(card, allOf(
                hasProperty("size", is(6)),
                hasProperty("content", hasSize(2))
        ));
        assertThat(card.getContent().get(0).getComponent(), allOf(
                instanceOf(TextCell.class),
                hasProperty("src", is("TextCell")),
                hasProperty("id", is("11")),
                hasProperty("fieldKey", is("test1")),
                hasProperty("tooltipFieldId", is("tooltip"))
        ));

        assertThat(card.getContent().get(1).getComponent(), allOf(
                instanceOf(ImageCell.class),
                hasProperty("src", is("cell12")),
                hasProperty("id", is("12"))
        ));

        card = cards.getContent().get(1);
        assertThat(card, allOf(
                hasProperty("size", nullValue()),
                hasProperty("content", hasSize(1))
        ));

        assertThat(card.getContent().get(0).getComponent(), allOf(
                instanceOf(TextCell.class),
                hasProperty("src", is("TextCell"))
        ));

        assertThat(cards.getPaging(), allOf(
                hasProperty("next", is(true)),
                hasProperty("prev", is(true)),
                hasProperty("showCount", is(ShowCountTypeEnum.NEVER)),
                hasProperty("size", is(10)),
                hasProperty("src", is("pagingSrc"))
        ));

        //второй виджет карточек
        cards = (Cards) page.getRegions().get("single").get(0).getContent().get(1);
        assertThat(cards, allOf(
                hasProperty("src", is("CardsWidget")),
                hasProperty("verticalAlign", is(Cards.PositionEnum.TOP)),
                hasProperty("height", is("300px")),
                hasProperty("content", hasSize(1))
        ));

        card = cards.getContent().get(0);
        assertThat(card, allOf(
                hasProperty("size", nullValue()),
                hasProperty("content", hasSize(1))
        ));

        assertThat(card.getContent().get(0).getComponent(), allOf(
                instanceOf(ImageCell.class),
                hasProperty("src", is("ImageCell")),
                hasProperty("id", is("31"))
        ));

        assertThat(cards.getPaging(), allOf(
                hasProperty("next", is(false)),
                hasProperty("prev", is(false)),
                hasProperty("showCount", is(ShowCountTypeEnum.ALWAYS)),
                hasProperty("size", is(10))
        ));
    }
}
