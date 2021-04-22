package net.n2oapp.framework.access.integration.metadata.transform;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.control.ButtonField;
import org.springframework.stereotype.Component;

/**
 * Трансформатор доступа ButtonField
 */
@Component
public class ButtonFieldAccessTransformer extends BaseAccessTransformer<ButtonField, CompileContext<?, ?>> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return ButtonField.class;
    }

    @Override
    public ButtonField transform(ButtonField compiled, CompileContext<?, ?> context, CompileProcessor p) {
        transfer(compiled.getAction(), compiled);
        return compiled;
    }
}
