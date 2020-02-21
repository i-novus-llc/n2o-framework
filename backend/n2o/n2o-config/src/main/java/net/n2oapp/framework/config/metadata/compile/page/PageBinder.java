package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.*;
import net.n2oapp.framework.api.metadata.meta.control.DefaultValues;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import net.n2oapp.framework.config.metadata.compile.redux.Redux;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Базовое связывание данных на странице
 */
public abstract class PageBinder<D extends Page> implements BaseMetadataBinder<D> {
    public D bindPage(D page, BindProcessor p, Map<String, Widget> widgets) {
        if (widgets != null) {
            widgets.values().forEach(p::bind);
        }

        if (page.getRoutes() != null) {
            Map<String, BindLink> pathMappings = new HashMap<>();
            page.getRoutes().getPathMapping().forEach((k, v) -> pathMappings.put(k, Redux.createBindLink(v)));
            for (PageRoutes.Route route : page.getRoutes().getList()) {
                route.setPath(p.resolveUrl(route.getPath(), pathMappings, null));
            }
        }
        if (page.getBreadcrumb() != null)
            page.getBreadcrumb().stream().filter(b -> b.getPath() != null)
                    .forEach(b -> {
                        b.setPath(p.resolveUrl(b.getPath()));
                        b.setLabel(p.resolveText(b.getLabel(), b.getModelLink()));
                    });
        if (page.getModels() != null) {
            page.getModels().values().forEach(bl -> {
                if (bl.getValue() instanceof String) {
                    bl.setValue(p.resolveText((String) bl.getValue()));
                } else if (bl.getValue() instanceof DefaultValues) {
                    DefaultValues dv = (DefaultValues) bl.getValue();
                    for (String key : dv.getValues().keySet()) {
                        if (dv.getValues().get(key) instanceof String) {
                            dv.getValues().put(key, p.resolveText((String) dv.getValues().get(key)));
                        }
                    }
                }
            });
            //порядок вызова функций важен, сначала удаляются значения по умолчанию которые резолвятся из url, потом разрешаются submodels
            resolveParams(page, p);
            List<ModelLink> filterLinks = collectFilterLinks(page.getModels(), widgets);
            resolveLinks(page.getModels(), filterLinks, p);
        }
        if (page.getPageProperty() != null) {
            page.getPageProperty().setTitle(p.resolveText(page.getPageProperty().getTitle(),
                    page.getPageProperty().getModelLink()));
        }
        if (page.getBreadcrumb() != null) {
            for (Breadcrumb crumb : page.getBreadcrumb()) {
                crumb.setLabel(p.resolveText(crumb.getLabel(), crumb.getModelLink()));
            }
        }
        return page;
    }

    private void resolveParams(D page, BindProcessor p) {
        if (page.getRoutes() != null && page.getRoutes().getQueryMapping() != null) {
            page.getRoutes().getQueryMapping().forEach((k, v) -> {
                if (v.getOnGet() != null && v.getOnGet().getPayload() != null &&
                        v.getOnGet().getPayload().containsKey("value") &&
                        p.canResolveParam(v.getOnGet().getPayload().get("value").toString().replace(":", ""))) {
                    page.getModels().remove(
                            createModelKey(v.getOnGet().getPayload().get("prefix").toString(),
                                    v.getOnGet().getPayload().get("key").toString(),
                                    v.getOnGet().getPayload().get("field").toString()));
                }
            });
        }
    }

    private List<ModelLink> collectFilterLinks(Models models, Map<String, Widget> widgets) {
        List<ModelLink> links = new ArrayList<>();
        if (widgets != null) {
            for (Widget w : widgets.values()) {
                if (w.getFilters() != null) {
                    for (Filter f : (List<Filter>) w.getFilters()) {
                        if (f.getRoutable() && f.getLink().getSubModelQuery() != null) {
                            createLink(links, models, f);
                        }
                    }
                }
            }
        }
        return links;
    }

    private void createLink(List<ModelLink> links, Models models, Filter f) {
        ReduxModel model = f.getLink().getModel();
        String widgetId = f.getLink().getWidgetId();
        String fieldId = f.getLink().getSubModelQuery().getSubModel();
        ModelLink link = new ModelLink(model, widgetId, fieldId);
        f.getLink().setParam(f.getParam());
        link.setSubModelQuery(f.getLink().getSubModelQuery());
        if (models.get(model, widgetId, fieldId) != null)
            link.setValue(models.get(model, widgetId, fieldId).getValue());
        if (link.getValue() == null || link.isConst())
            models.add(model, widgetId, fieldId, link);
        links.add(f.getLink());
    }

    private void resolveLinks(Models models, List<ModelLink> filterLinks, BindProcessor p) {
        models.keySet().forEach(param -> {
                    models.put(param, (ModelLink) p.resolveLink(models.get(param)));
                    p.resolveSubModels(models.get(param), filterLinks);
                }
        );
    }

    private String createModelKey(String prefix, String key, String field) {
        if(field != null) {
            return String.format("%s['%s'].%s", prefix, key, field);
        }
        return String.format("%s['%s']", prefix, key);
    }
}
