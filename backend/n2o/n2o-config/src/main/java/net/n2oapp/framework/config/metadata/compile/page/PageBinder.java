package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.*;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import net.n2oapp.framework.config.metadata.compile.redux.Redux;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Базовое связывание данных на странице
 */
@Component
public class PageBinder implements BaseMetadataBinder<Page> {
    @Override
    public Page bind(Page page, CompileProcessor p) {
        if (page.getWidgets() != null) {
            page.getWidgets().values().forEach(p::bind);
        }
        if (page.getActions() != null)
            page.getActions().values().forEach(p::bind);
        if (page.getRoutes() != null) {
            Map<String, BindLink> pathMappings = new HashMap<>();
            page.getRoutes().getPathMapping().forEach((k, v) -> pathMappings.put(k, Redux.createBindLink(v)));
            for (PageRoutes.Route route : page.getRoutes().getList()) {
                route.setPath(p.resolveUrl(route.getPath(), pathMappings, null));
            }
        }
        if (page.getBreadcrumb() != null)
            page.getBreadcrumb().stream().filter(b -> b.getPath() != null)
                    .forEach(b -> b.setPath(p.resolveUrl(b.getPath(), null, null)));
        if (page.getModels() != null) {
            page.getModels().values().forEach(bl -> {
                if (bl.getValue() instanceof String) {
                    bl.setValue(p.resolveText((String) bl.getValue()));
                }
            });
            resolveLinks(page.getModels(), page.getWidgets(), p);
        }
        if (page.getProperties() != null) {
            page.getProperties().setTitle(p.resolveText(page.getProperties().getTitle(), page.getProperties().getModelLink()));
        }
        if (page.getBreadcrumb() != null) {
            for (Breadcrumb crumb : page.getBreadcrumb()) {
                crumb.setLabel(p.resolveText(crumb.getLabel(), crumb.getModelLink()));
            }
        }
        return page;
    }

    private void resolveLinks(Models linkMap, Map<String, Widget> widgets, CompileProcessor p) {
        if (widgets != null) {
            for (Widget w : widgets.values()) {
                if (w.getFilters() != null) {
                    for (Filter f : (List<Filter>) w.getFilters()) {
                        if (f.getReloadable() && f.getLink().getSubModelQuery() != null) {
                            ModelLink link = new ModelLink(
                                    f.getLink().getModel(),
                                    f.getLink().getWidgetId(),
                                    f.getLink().getSubModelQuery()
                            );
                            if (linkMap.get(ReduxModel.FILTER, w.getId(), f.getLink().getSubModelQuery().getSubModel()) != null)
                                link.setValue(linkMap.get(ReduxModel.FILTER, w.getId(), f.getLink().getSubModelQuery().getSubModel()).getValue());

                            linkMap.add(ReduxModel.FILTER, w.getId(), f.getLink().getSubModelQuery().getSubModel(), link);
                        }
                    }
                }
            }
        }
        Iterator<String> it = linkMap.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            ModelLink link = p.resolveLink(linkMap.get(key));
            if (link.getValue() != null)
                linkMap.put(key, link);
            else
                it.remove();
        }
    }

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return Page.class;
    }
}
