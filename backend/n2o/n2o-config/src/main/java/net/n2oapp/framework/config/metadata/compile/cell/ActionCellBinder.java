package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.meta.cell.ActionCell;
import net.n2oapp.framework.config.metadata.compile.action.ActionComponentBinder;
import org.springframework.stereotype.Component;

/**
 * Связывание с данными любых ячеек с действием
 */
@Component
public class ActionCellBinder extends ActionComponentBinder<ActionCell> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return ActionCell.class;
    }

}