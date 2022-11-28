package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Связывание вызова запросов с данными
 */
@Component
public class InvokeActionBinder implements BaseMetadataBinder<InvokeAction> {
    @Override
    public InvokeAction bind(InvokeAction action, BindProcessor p) {
        ClientDataProvider dataProvider = action.getPayload().getDataProvider();
        String url = p.resolveUrl(dataProvider.getUrl(), dataProvider.getPathMapping(), dataProvider.getQueryMapping());
        if (dataProvider.getQueryMapping() != null) {
            Map<String, ModelLink> result = new HashMap<>();
            dataProvider.getQueryMapping().forEach((k, v) -> result.put(k, (ModelLink) p.resolveLink(v)));
            dataProvider.setQueryMapping(result);
        }
        dataProvider.setUrl(url);
        return action;
    }

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return InvokeAction.class;
    }
}
