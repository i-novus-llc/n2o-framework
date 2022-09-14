package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.badge.BadgeUtil;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.PerformButton;
import org.springframework.stereotype.Component;

/**
 * Связывание действия ссылки в PerformButton с данными
 */
@Component
public class PerformButtonBinder extends ActionComponentBinder<PerformButton> {

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return PerformButton.class;
    }

    @Override
    public PerformButton bind(PerformButton compiled, BindProcessor p) {
        BadgeUtil.bindSimpleBadge(compiled.getBadge(), p);
        return super.bind(compiled, p);
    }
}
