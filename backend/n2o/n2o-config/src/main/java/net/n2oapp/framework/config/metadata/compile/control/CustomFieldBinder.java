package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.control.CustomField;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

@Component
public class CustomFieldBinder implements BaseMetadataBinder<CustomField> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return CustomField.class;
    }

    @Override
    public CustomField bind(CustomField compiled, BindProcessor p) {
        if (compiled.getControl() != null) {
            p.bind(compiled.getControl());
        }
        if (compiled.getControls() != null) {
            compiled.getControls().forEach(p::bind);
        }
        return compiled;
    }
}
