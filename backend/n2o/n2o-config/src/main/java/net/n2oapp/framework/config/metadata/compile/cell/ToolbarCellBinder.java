package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.cell.ToolbarCell;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

@Component
public class ToolbarCellBinder implements BaseMetadataBinder<ToolbarCell> {

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return ToolbarCell.class;
    }

    @Override
    public ToolbarCell bind(ToolbarCell compiled, BindProcessor p) {
        if (compiled.getToolbar() != null) {
            compiled.getToolbar().forEach(t ->
                    t.getButtons().forEach(b -> {
                        if (b.getAction() != null)
                            p.bind(b.getAction());
                    })
            );
        }
        return compiled;
    }
}
