package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oActionCell;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import net.n2oapp.framework.config.metadata.compile.action.ActionComponentBinder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Связывание с данными любых ячеек с действием
 */
@Component
public class ActionCellBinder extends ActionComponentBinder<N2oActionCell> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return N2oActionCell.class;
    }

}