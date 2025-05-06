package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oBlock;
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

import static net.n2oapp.framework.api.StringUtils.prepareSizeAttribute;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.metadata.compile.action.ActionCompileStaticProcessor.initMetaActions;

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
        compileBaseWidget(cards, source, context, p);
        N2oAbstractDatasource datasource = getDatasourceById(source.getDatasourceId(), p);
        CompiledObject object = getObject(source, datasource, p);
        WidgetScope widgetScope = new WidgetScope(source.getId(), source.getDatasourceId(), ReduxModelEnum.resolve, p);
        MetaActions widgetActions = initMetaActions(source, p);
        compileToolbarAndAction(cards, source, context, p, widgetScope, widgetActions, object, null);

        if (source.getContent() != null)
            cards.setCardList(compileCols(source.getContent(), context, p, object, widgetScope, widgetActions));
        cards.setVerticalAlign(castDefault(source.getVerticalAlign(),
                () -> p.resolve(property("n2o.api.widget.cards.vertical_align"), Cards.PositionEnum.class)));
        cards.setHeight(prepareSizeAttribute(castDefault(source.getHeight(),
                () -> p.resolve(property("n2o.api.widget.cards.height"), String.class))));
        cards.setPaging(compilePaging(source, p.resolve(property("n2o.api.widget.cards.size"), Integer.class), p, widgetScope));
        return cards;
    }

    private List<Cards.Card> compileCols(N2oCards.N2oCol[] source, CompileContext<?, ?> context, CompileProcessor p,
                                         CompiledObject object, WidgetScope widgetScope, MetaActions widgetActions) {
        List<Cards.Card> cards = new ArrayList<>(source.length);
        for (N2oCards.N2oCol col : source) {
            Cards.Card card = new Cards.Card();
            card.setSize(col.getSize());
            if (col.getBlocks() != null)
                card.setContent(compileBlock(col.getBlocks(), context, p, object, widgetScope, widgetActions));
            cards.add(card);
        }
        return cards;
    }

    private List<Cards.Block> compileBlock(N2oBlock[] source, CompileContext<?, ?> context, CompileProcessor p,
                                           Object... scopes) {
        List<Cards.Block> blocks = new ArrayList<>(source.length);
        for (N2oBlock block : source) {
            Cards.Block clientBlock = new Cards.Block();
            block.setId(castDefault(block.getId(), block.getTextFieldId()));
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
