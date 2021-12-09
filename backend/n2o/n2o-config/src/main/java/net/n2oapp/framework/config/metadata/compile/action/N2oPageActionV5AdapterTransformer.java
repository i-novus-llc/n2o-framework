package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.compile.SourceTransformer;
import net.n2oapp.framework.api.metadata.event.action.N2oAbstractPageAction;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.register.route.RouteUtil.normalize;

/**
 * Трансфомация route в действиях(перенос route из виджета)
 */
@Component
public class N2oPageActionV5AdapterTransformer implements SourceTransformer<N2oAbstractPageAction>, SourceClassAware {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oAbstractPageAction.class;
    }

    @Override
    public N2oAbstractPageAction transform(N2oAbstractPageAction source, SourceProcessor p) {
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (widgetScope != null && widgetScope.getOldRoute() != null) {
            if (source.getRoute() == null) {
                source.setRoute(widgetScope.getOldRoute() + normalize(source.getId()));
            } else {
                source.setRoute(widgetScope.getOldRoute() + source.getRoute());
            }
        }
        return source;
    }
}
