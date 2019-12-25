package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkAction;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Связывание действия ссылки с данными
 */
@Component
public class LinkActionBinder implements BaseMetadataBinder<LinkAction> {
    @Override
    public LinkAction bind(LinkAction action, BindProcessor p) {
        String url = p.resolveUrl(action.getOptions().getPath(), action.getOptions().getPathMapping(), action.getOptions().getQueryMapping());
        if (action.getOptions().getQueryMapping() != null) {
            Map<String, ModelLink> result = new HashMap<>();
            action.getOptions().getQueryMapping().forEach((k, v) -> result.put(k, (ModelLink) p.resolveLink(v)));
            action.getOptions().setQueryMapping(result);
        }
        action.getOptions().setPath(url);
        return action;
    }

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return LinkAction.class;
    }
}
