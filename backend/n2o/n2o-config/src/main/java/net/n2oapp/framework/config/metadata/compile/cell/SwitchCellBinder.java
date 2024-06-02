package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.cell.SwitchCell;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

@Component
public class SwitchCellBinder implements BaseMetadataBinder<SwitchCell> {

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return SwitchCell.class;
    }

    @Override
    public SwitchCell bind(SwitchCell compiled, BindProcessor p) {
        if (compiled.getSwitchDefault() != null) {
            p.bind(compiled.getSwitchDefault());
        }
        if (compiled.getSwitchList() != null) {
            compiled.getSwitchList().values().forEach(p::bind);
        }
        return compiled;
    }
}
