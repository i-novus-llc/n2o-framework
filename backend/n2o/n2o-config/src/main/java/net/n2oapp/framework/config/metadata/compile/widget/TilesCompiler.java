package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oBlock;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oTiles;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oTextCell;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.widget.Tiles;
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
 * Компиляция виджета Плитки
 */
@Component
public class TilesCompiler extends BaseListWidgetCompiler<Tiles, N2oTiles> {
    @Override
    protected String getPropertyWidgetSrc() {
        return "n2o.api.widget.tiles.src";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oTiles.class;
    }

    @Override
    public Tiles compile(N2oTiles source, CompileContext<?, ?> context, CompileProcessor p) {
        Tiles tiles = new Tiles();
        compileBaseWidget(tiles, source, context, p);
        N2oAbstractDatasource datasource = getDatasourceById(source.getDatasourceId(), p);
        CompiledObject object = getObject(source, datasource, p);
        WidgetScope widgetScope = new WidgetScope(source.getId(), source.getDatasourceId(), ReduxModel.resolve, p);
        MetaActions widgetActions = initMetaActions(source, p);
        compileToolbarAndAction(tiles, source, context, p, widgetScope, widgetActions, object, null);

        tiles.setColsSm(castDefault(source.getColsSm(), p.resolve(property("n2o.api.widget.tiles.colsSm"), Integer.class)));
        tiles.setColsMd(castDefault(source.getColsMd(), p.resolve(property("n2o.api.widget.tiles.colsMd"), Integer.class)));
        tiles.setColsLg(castDefault(source.getColsLg(), p.resolve(property("n2o.api.widget.tiles.colsLg"), Integer.class)));
        tiles.setHeight(prepareSizeAttribute(castDefault(source.getHeight(),
                () -> p.resolve(property("n2o.api.widget.tiles.height"), String.class))));
        tiles.setWidth(prepareSizeAttribute(castDefault(source.getWidth(),
                () -> p.resolve(property("n2o.api.widget.tiles.width"), String.class))));

        List<Tiles.Tile> tls = new ArrayList<>(source.getContent().length);
        for (N2oBlock block : source.getContent())
            tls.add(compileBlock(block, context, p, object, widgetScope, widgetActions));
        tiles.setTile(tls);
        tiles.setPaging(compilePaging(source, p.resolve(property("n2o.api.widget.tiles.size"), Integer.class), p, widgetScope));
        return tiles;
    }

    private Tiles.Tile compileBlock(N2oBlock source, CompileContext<?, ?> context, CompileProcessor p,
                                    Object... scopes) {
        Tiles.Tile tile = new Tiles.Tile();
        source.setId(castDefault(source.getId(), source.getTextFieldId()));
        tile.setId(source.getId());
        tile.setClassName(source.getCssClass());
        tile.setStyle(StylesResolver.resolveStyles(source.getStyle()));
        N2oCell cell = source.getComponent();
        if (cell == null)
            cell = new N2oTextCell();
        tile.setComponent(p.compile(cell, context, p, new IndexScope(), new ComponentScope(source), scopes));
        return tile;
    }
}
