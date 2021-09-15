package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.widget.Tiles;
import org.springframework.stereotype.Component;

/**
 * Связывание данных в виджете Плитки
 */
@Component
public class TilesBinder extends BaseListWidgetBinder<Tiles> {

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return Tiles.class;
    }

    @Override
    public Tiles bind(Tiles compiled, BindProcessor p) {
        compiled.getTile().forEach(x -> p.bind(x.getComponent()));
        return compiled;
    }
}
