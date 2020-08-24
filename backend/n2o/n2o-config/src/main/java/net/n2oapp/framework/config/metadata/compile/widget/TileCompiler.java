package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oTiles;
import net.n2oapp.framework.api.metadata.meta.widget.Tiles;
import net.n2oapp.framework.config.metadata.compile.ComponentCompiler;
import net.n2oapp.framework.config.util.StylesResolver;
import org.springframework.stereotype.Component;

/**
 * Компиляция компонента Плитка
 */
@Component
public class TileCompiler<D extends Tiles.Tile, S extends N2oTiles.Block> extends ComponentCompiler<D, S> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oTiles.Block.class;
    }

    @Override
    public D compile(S source, CompileContext<?, ?> context, CompileProcessor p) {
        Tiles.Tile tile = new Tiles.Tile();
        tile.setId(source.getId());
        tile.setStyle(StylesResolver.resolveStyles(source.getStyle()));
        tile.setSrc(source.getSrc());
        tile.setClassName(source.getCssClass());
        tile.setComponent(p.compile(source.getComponent(), context, p));
        return (D) tile;
    }

}
