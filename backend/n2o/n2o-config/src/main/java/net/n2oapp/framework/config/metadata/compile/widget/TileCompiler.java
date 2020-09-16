package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oTiles;
import net.n2oapp.framework.api.metadata.meta.widget.Tiles;
import net.n2oapp.framework.config.metadata.compile.ComponentCompiler;
import org.springframework.stereotype.Component;

/**
 * Компиляция компонента Плитка
 */
@Component
public class TileCompiler extends ComponentCompiler<Tiles.Tile, N2oTiles.Block> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oTiles.Block.class;
    }

    @Override
    public Tiles.Tile compile(N2oTiles.Block source, CompileContext<?, ?> context, CompileProcessor p) {
        Tiles.Tile tile = new Tiles.Tile();
        tile.setId(source.getId());
        compileComponent(tile, source, context, p);
        tile.setComponent(p.compile(source.getComponent(), context, p));
        return tile;
    }

}
