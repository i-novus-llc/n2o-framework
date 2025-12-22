package net.n2oapp.framework.config.metadata.merge;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oCards;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.widget.v5.CardsWidgetIOV5;
import net.n2oapp.framework.config.metadata.merge.widget.N2oCardsMerger;
import net.n2oapp.framework.config.metadata.pack.N2oCellsPack;
import net.n2oapp.framework.config.test.SourceMergerTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тест слияния виджетов Карточки
 */
class N2oCardsMergerTest extends SourceMergerTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oCellsPack())
                .ios(new CardsWidgetIOV5())
                .mergers(new N2oCardsMerger());
    }

    @Test
    void mergeCards() {
        N2oCards cards = merge("net/n2oapp/framework/config/metadata/merge/widget/parentCardsMerger.widget.xml",
                "net/n2oapp/framework/config/metadata/merge/widget/childCardsMerger.widget.xml")
                .get("parentCardsMerger", N2oCards.class);

        assertThat(cards, notNullValue());
        assertThat(cards.getHeight(), is("200px"));
        assertThat(cards.getVerticalAlign().name(), is("CENTER"));
        assertThat(cards.getPagination().getClassName(), is("childPagination"));
        assertThat(cards.getContent(), notNullValue());
        assertThat(cards.getContent().length, is(2));
        assertThat(cards.getContent()[0].getBlocks()[0].getId(), is("childBlock"));
        assertThat(cards.getContent()[1].getBlocks()[0].getId(), is("parentBlock"));
    }
}

