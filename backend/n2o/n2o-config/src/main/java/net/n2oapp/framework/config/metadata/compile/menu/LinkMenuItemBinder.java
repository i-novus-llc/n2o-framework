package net.n2oapp.framework.config.metadata.compile.menu;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.menu.LinkMenuItem;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LinkMenuItemBinder implements BaseMetadataBinder<LinkMenuItem> {

    @Override
    public LinkMenuItem bind(LinkMenuItem compiled, BindProcessor p) {
        Map<String, ModelLink> pathMapping = compiled.getPathMapping();
        Map<String, ModelLink> queryMapping = compiled.getQueryMapping();
        compiled.setUrl(p.resolveUrl(compiled.getUrl(), pathMapping, queryMapping));
        if (pathMapping != null) {
            pathMapping.forEach((k, v) -> pathMapping.put(k, (ModelLink) p.resolveLink(v)));
        }
        if (queryMapping != null) {
            queryMapping.forEach((k, v) -> queryMapping.put(k, (ModelLink) p.resolveLink(v)));
        }
        return compiled;
    }

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return LinkMenuItem.class;
    }
}
