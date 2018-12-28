package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.MetadataBinder;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetDataProvider;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import net.n2oapp.framework.config.register.route.RouteUtil;
import org.springframework.stereotype.Component;

/**
 * Связывание вызова запросов с данными
 */
@Component
public class InvokeActionBinder implements BaseMetadataBinder<InvokeAction> {
    @Override
    public InvokeAction bind(InvokeAction action, CompileProcessor p) {
        WidgetDataProvider dataProvider = action.getOptions().getPayload().getDataProvider();
        String url = p.resolveUrl(dataProvider.getUrl(), dataProvider.getPathMapping(), dataProvider.getQueryMapping());
        dataProvider.setUrl(url);
        return action;
    }

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return InvokeAction.class;
    }
}
