package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.PerformButton;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Связывание действия ссылки в PerformButton с данными
 */
@Component
public class PerformButtonBinder extends ActionComponentBinder<PerformButton> {

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return PerformButton.class;
    }
}
