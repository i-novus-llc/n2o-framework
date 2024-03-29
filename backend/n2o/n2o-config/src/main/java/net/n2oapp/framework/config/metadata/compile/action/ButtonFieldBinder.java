package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.badge.BadgeUtil;
import net.n2oapp.framework.api.metadata.meta.control.ButtonField;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

/**
 * Связывание полей ButtonField с данными
 */
@Component
public class ButtonFieldBinder implements BaseMetadataBinder<ButtonField> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return ButtonField.class;
    }

    @Override
    public ButtonField bind(ButtonField compiled, BindProcessor p) {
        BadgeUtil.bindSimpleBadge(compiled.getBadge(), p);
        return compiled;
    }
}
