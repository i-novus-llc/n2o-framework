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
public class PerformButtonBinder implements BaseMetadataBinder<PerformButton> {
    @Override
    public PerformButton bind(PerformButton button, BindProcessor p) {
        String url = p.resolveUrl(button.getUrl(), button.getPathMapping(), button.getQueryMapping());
        if (button.getQueryMapping() != null) {
            Map<String, ModelLink> result = new HashMap<>();
            button.getQueryMapping().forEach((k, v) -> result.put(k, (ModelLink) p.resolveLink(v)));
            button.setQueryMapping(result);
        }
        button.setUrl(url);
        return button;
    }

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return PerformButton.class;
    }
}
