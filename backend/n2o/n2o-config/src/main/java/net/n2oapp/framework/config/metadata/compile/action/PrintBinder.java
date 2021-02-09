package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.print.Print;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Связывание действия печати с данными
 */
@Component
public class PrintBinder implements BaseMetadataBinder<Print> {
    @Override
    public Print bind(Print print, BindProcessor p) {
        print.getPayload().setUrl(p.resolveUrl(print.getPayload().getUrl(), print.getPayload().getPathMapping(), print.getPayload().getQueryMapping()));
        if (print.getPayload().getQueryMapping() != null) {
            Map<String, ModelLink> result = new HashMap<>();
            print.getPayload().getQueryMapping().forEach((k, v) -> result.put(k, (ModelLink) p.resolveLink(v)));
            print.getPayload().setQueryMapping(result);
        }
        return print;
    }

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return Print.class;
    }
}
