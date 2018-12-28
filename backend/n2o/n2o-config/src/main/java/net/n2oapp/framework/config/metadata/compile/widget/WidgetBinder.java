package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.MetadataBinder;
import net.n2oapp.framework.api.metadata.meta.BindLink;
import net.n2oapp.framework.api.metadata.meta.Filter;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Базовое связывание виджета с данными
 */
@Component
public class WidgetBinder implements BaseMetadataBinder<Widget> {
    @Override
    public Widget bind(Widget widget, CompileProcessor p) {
        if (widget.getActions() != null)
            ((Map<String, Action>) widget.getActions()).values().forEach(p::bind);
        if (widget.getDataProvider() != null) {
            Map<String, BindLink> pathMapping = widget.getDataProvider().getPathMapping();
            Map<String, BindLink> queryMapping = widget.getDataProvider().getQueryMapping();
            widget.getDataProvider().setUrl(p.resolveUrl(widget.getDataProvider().getUrl(), pathMapping, queryMapping));
            pathMapping.keySet().forEach(param -> pathMapping.put(param, p.resolveLink((ModelLink) pathMapping.get(param))));
            queryMapping.keySet().forEach(param -> queryMapping.put(param, p.resolveLink((ModelLink) queryMapping.get(param))));
        }
        if (widget.getFilters() != null) {
            ((List<Filter>) widget.getFilters()).forEach(f ->
                    f.setLink(p.resolveLink(f.getLink())));
        }
        return widget;
    }

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return Widget.class;
    }
}
