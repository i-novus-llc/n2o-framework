package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oCards;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oTextCell;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.widget.Cards;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.util.StylesResolver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция виджета Карточки
 */
@Component
public class CardsCompiler extends BaseListWidgetCompiler<Cards, N2oCards> {
    @Override
    protected String getPropertyWidgetSrc() {
        return "n2o.api.widget.cards.src";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oCards.class;
    }

    @Override
    public Cards compile(N2oCards source, CompileContext<?, ?> context, CompileProcessor p) {
        Cards cards = new Cards();
        N2oDatasource datasource = initInlineDatasource(cards, source, p);
        CompiledObject object = getObject(source, datasource, p);
        compileBaseWidget(cards, source, context, p, object);
        WidgetScope widgetScope = new WidgetScope();
        widgetScope.setWidgetId(source.getId());
        widgetScope.setClientWidgetId(cards.getId());
        widgetScope.setDatasourceId(source.getDatasourceId());
        MetaActions widgetActions = initMetaActions(source, p);
        compileToolbarAndAction(cards, source, context, p, widgetScope, widgetActions, object, null);

        if (source.getContent() != null)
            cards.setCards(compileCols(source.getContent(), context, p, object, widgetScope, widgetActions));
        cards.setVerticalAlign(p.cast(source.getVerticalAlign(),
                p.resolve(property("n2o.api.widget.cards.vertical_align"), Cards.Position.class)));
        cards.setHeight(p.cast(source.getHeight(),
                p.resolve(property("n2o.api.widget.cards.height"), String.class)));
        cards.setPaging(compilePaging(cards, source, p.resolve(property("n2o.api.widget.cards.size"), Integer.class), p));
        return cards;
    }

    private List<Cards.Card> compileCols(N2oCards.Col[] source, CompileContext<?, ?> context, CompileProcessor p,
                                         CompiledObject object, WidgetScope widgetScope, MetaActions widgetActions) {
        List<Cards.Card> cards = new ArrayList<>(source.length);
        for (N2oCards.Col col : source) {
            Cards.Card card = new Cards.Card();
            card.setSize(col.getSize());
            if (col.getBlocks() != null)
                card.setContent(compileBlock(col.getBlocks(), context, p, object, widgetScope, widgetActions));
            cards.add(card);
        }
        return cards;
    }

    private List<Cards.Block> compileBlock(N2oCards.Block[] source, CompileContext<?, ?> context, CompileProcessor p,
                                       Object... scopes) {
        List<Cards.Block> blocks = new ArrayList<>(source.length);
        for (N2oCards.Block block : source) {
            Cards.Block clientBlock = new Cards.Block();
            block.setId(p.cast(block.getId(), block.getTextFieldId()));
            clientBlock.setId(block.getId());
            clientBlock.setClassName(block.getCssClass());
            clientBlock.setStyle(StylesResolver.resolveStyles(block.getStyle()));
            N2oCell cell = block.getComponent();
            if (cell == null)
                cell = new N2oTextCell();
            clientBlock.setComponent(p.compile(cell, context, p, new IndexScope(), new ComponentScope(block), scopes));
            blocks.add(clientBlock);
        }
        return blocks;
    }
}
