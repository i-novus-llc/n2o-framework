package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.control.ButtonField;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Связывание действия ссылки в ButtonField с данными
 */
@Component
public class ButtonFieldBinder implements BaseMetadataBinder<ButtonField> {
    @Override
    public ButtonField bind(ButtonField button, BindProcessor p) {
        String url = p.resolveUrl(button.getUrl(), button.getPathMapping(), button.getQueryMapping());
        if (button.getQueryMapping() != null) {
            Map<String, ModelLink> result = new HashMap<>();
            button.getQueryMapping().forEach((k, v) -> result.put(k, (ModelLink) p.resolveLink(v)));
            button.setQueryMapping(result);
        }
        button.setUrl(url);
        p.bind(button.getAction());
        return button;
    }

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return ButtonField.class;
    }
}
