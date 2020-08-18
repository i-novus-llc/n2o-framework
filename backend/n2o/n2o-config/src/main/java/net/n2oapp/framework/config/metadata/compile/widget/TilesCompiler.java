package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oTiles;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.widget.Tiles;
import net.n2oapp.framework.config.metadata.compile.PageRoutesScope;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * Компиляция виджета Плитки
 */
@Component
public class TilesCompiler extends BaseWidgetCompiler<Tiles, N2oTiles> {
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
        CompiledObject object = getObject(source, p);
        compileWidget(tiles, source, context, p, object);
        ParentRouteScope widgetRoute = initWidgetRouteScope(tiles, context, p);
        PageRoutesScope pageRoutesScope = p.getScope(PageRoutesScope.class);
        if (pageRoutesScope != null) {
            pageRoutesScope.put(tiles.getId(), widgetRoute);
        }
        compileDataProviderAndRoutes(tiles, source, context, p, null, widgetRoute, null, null, object);
        WidgetScope widgetScope = new WidgetScope();
        widgetScope.setWidgetId(source.getId());
        widgetScope.setQueryId(source.getQueryId());
        widgetScope.setClientWidgetId(tiles.getId());
        MetaActions widgetActions = new MetaActions();
        compileToolbarAndAction(tiles, source, context, p, widgetScope, widgetRoute, widgetActions, object, null);

        tiles.setColsSm(source.getColsSm());
        tiles.setColsMd(source.getColsMd());
        tiles.setColsLg(source.getColsLg());
        tiles.setSrc(source.getSrc());
        List<Tiles.Tile> tls = new LinkedList<>();
        for (N2oTiles.Block block : source.getContent()) {
            Tiles.Tile tile = new Tiles.Tile();
            tile.setId(block.getId());
            tile.setStyle(block.getStyle());
            tile.setSrc(block.getSrc()); //todo
            tile.setClassName(block.getClassName());
            tile.setComponent(block.getComponent()); //todo
            tls.add(tile);
        }
        tiles.setTile(tls);
        return tiles;
    }

}
