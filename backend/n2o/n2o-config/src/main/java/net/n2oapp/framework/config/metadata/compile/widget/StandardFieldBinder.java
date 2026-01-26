package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Group;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import net.n2oapp.framework.config.util.BindUtil;
import org.springframework.stereotype.Component;

/**
 * Связывание стандартных полей с данными
 */
@Component
public class StandardFieldBinder implements BaseMetadataBinder<StandardField<?>> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return StandardField.class;
    }

    @Override
    public StandardField<?> bind(StandardField<?> compiled, BindProcessor p) {
        BindUtil.bindDataProvider(compiled.getDataProvider(), p);
        if (compiled.getControl() != null) {
            p.bind(compiled.getControl());
        }
        return compiled;
    }
}
