package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.badge.BadgeUtil;
import net.n2oapp.framework.api.metadata.meta.control.ButtonField;
import org.springframework.stereotype.Component;

@Component
public class ButtonFieldBinder extends ActionComponentBinder<ButtonField> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return ButtonField.class;
    }

    @Override
    public ButtonField bind(ButtonField compiled, BindProcessor p) {
        BadgeUtil.bindSimpleBadge(compiled.getBadge(), p);
        return super.bind(compiled, p);
    }
}
