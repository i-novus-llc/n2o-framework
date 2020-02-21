package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.BindLink;
import net.n2oapp.framework.api.metadata.meta.control.ListControl;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Связывание данных в списковом компоненте
 */
@Component
public class ListControlBinder implements BaseMetadataBinder<ListControl> {
    @Override
    public Class<ListControl> getCompiledClass() {
        return ListControl.class;
    }

    @Override
    public ListControl bind(ListControl compiled, BindProcessor p) {
        if (compiled.getDataProvider() != null) {
            Map<String, BindLink> pathMapping = compiled.getDataProvider().getPathMapping();
            Map<String, BindLink> queryMapping = compiled.getDataProvider().getQueryMapping();
            compiled.getDataProvider().setUrl(p.resolveUrl(compiled.getDataProvider().getUrl(), pathMapping, queryMapping));
            pathMapping.forEach((k, v) -> pathMapping.put(k, p.resolveLink(v)));
            queryMapping.forEach((k, v) -> queryMapping.put(k, p.resolveLink(v)));
        }
        return compiled;
    }
}
