package net.n2oapp.framework.config.metadata.compile.widget.table;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.MoveModeEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oAbstractColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oDndColumn;
import net.n2oapp.framework.api.metadata.meta.widget.table.DndColumn;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция drag-n-drop столбца таблицы
 */
@Component
public class DndColumnCompiler<S extends N2oDndColumn> implements BaseSourceCompiler<DndColumn, S, CompileContext<?, ?>> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oDndColumn.class;
    }

    @Override
    public DndColumn compile(S source, CompileContext<?, ?> context, CompileProcessor p) {
        DndColumn compiled = new DndColumn();
        compiled.setSrc(castDefault(source.getSrc(), () -> p.resolve(property("n2o.api.widget.column.dnd.src"), String.class)));
        compiled.setMoveMode(castDefault(source.getMoveMode(), () -> p.resolve(property("n2o.api.widget.column.dnd.move_mode"), MoveModeEnum.class)));
        compiled.setChildren(new ArrayList<>());
        for (N2oAbstractColumn subColumn : source.getChildren()) {
            compiled.getChildren().add(p.compile(subColumn, context, p));
        }
        return compiled;
    }
}
