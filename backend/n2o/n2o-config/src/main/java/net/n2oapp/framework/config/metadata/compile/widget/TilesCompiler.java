package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oTiles;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.widget.Tiles;
import net.n2oapp.framework.api.metadata.meta.widget.table.Pagination;
import net.n2oapp.framework.config.metadata.compile.PageRoutesScope;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

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

        tiles.setColsSm(p.cast(source.getColsSm(), p.resolve(property("n2o.api.widget.tiles.colsSm"), Integer.class)));
        tiles.setColsMd(p.cast(source.getColsMd(), p.resolve(property("n2o.api.widget.tiles.colsMd"), Integer.class)));
        tiles.setColsLg(p.cast(source.getColsLg(), p.resolve(property("n2o.api.widget.tiles.colsLg"), Integer.class)));
        tiles.setHeight(p.cast(source.getHeight(), p.resolve(property("n2o.api.widget.tiles.height"), Integer.class)));
        tiles.setWidth(p.cast(source.getWidth(), p.resolve(property("n2o.api.widget.tiles.width"), Integer.class)));

        List<Tiles.Tile> tls = new ArrayList<>(source.getContent().length);
        for (N2oTiles.Block block : source.getContent()) {
            tls.add(p.compile(block, context, p));
        }
        tiles.setTile(tls);
        tiles.setPaging(compilePaging(source, p.resolve(property("n2o.api.widget.tiles.size"), Integer.class)));
        return tiles;
    }

    /**
     * Компиляция паджинации
     */
    private Pagination compilePaging(N2oTiles source, Integer size) {
        Pagination pagination = new Pagination();
        pagination.setSize(source.getSize() != null ? source.getSize() : size);
        pagination.setPrev(source.getPagination() != null ? source.getPagination().getPrev() : null);
        pagination.setNext(source.getPagination() != null ? source.getPagination().getNext() : null);
        return pagination;
    }
}
