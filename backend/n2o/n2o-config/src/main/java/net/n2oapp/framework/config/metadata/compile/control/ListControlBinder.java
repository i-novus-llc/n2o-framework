package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.control.ListControl;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import net.n2oapp.framework.config.util.BindUtil;
import org.springframework.stereotype.Component;

@Component
public class ListControlBinder implements BaseMetadataBinder<ListControl> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return ListControl.class;
    }

    @Override
    public ListControl bind(ListControl control, BindProcessor p) {
        if (control.getDataProvider() != null) {
            BindUtil.bindDataProvider(control.getDataProvider(), p);
        }
        return control;
    }
}
