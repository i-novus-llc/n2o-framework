package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.*;
import net.n2oapp.framework.api.metadata.meta.control.DefaultValues;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import net.n2oapp.framework.config.metadata.compile.redux.Redux;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Базовое связывание данных на странице
 */
public abstract class PageBinder<D extends Page> implements BaseMetadataBinder<D> {
    public D bindPage(D page, BindProcessor p, List<Widget> widgets) {
        if (widgets != null)
            widgets.forEach(p::bind);
        bindRoutes(page, p);
        if (page.getBreadcrumb() != null)
            page.getBreadcrumb().stream().filter(b -> b.getPath() != null)
                    .forEach(b -> {
                        b.setPath(p.resolveUrl(b.getPath()));
                        b.setLabel(p.resolveText(b.getLabel(), b.getModelLink()));
                    });
        bindModels(page, p, widgets);
        bindProperty(page, p);
        if (page.getBreadcrumb() != null) {
            for (Breadcrumb crumb : page.getBreadcrumb()) {
                crumb.setLabel(p.resolveText(crumb.getLabel(), crumb.getModelLink()));
            }
        }
        return page;
    }

    private void bindModels(D page, BindProcessor p, List<Widget> widgets) {
        if (page.getModels() != null) {
            page.getModels().values().forEach(bl -> {
                Object value = p.getLinkValue(bl);
                if (value != null)
                    bl.setValue(value);
                else if (bl.getValue() instanceof String) {
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
            //порядок вызова функций важен, сначала разрешаются submodels, потом удаляются значения по умолчанию которые резолвятся из url
            collectFiltersToModels(page.getModels(), widgets, p);
            resolveLinks(page.getModels(), p);
        }
    }

    private void bindRoutes(D page, BindProcessor p) {
        if (page.getRoutes() != null) {
            Map<String, BindLink> pathMappings = new HashMap<>();
            page.getRoutes().getPathMapping().forEach((k, v) -> pathMappings.put(k, Redux.createBindLink(v)));
            for (PageRoutes.Route route : page.getRoutes().getList()) {
                route.setPath(p.resolveUrl(route.getPath(), pathMappings, null));
            }
            if (page.getRoutes().getQueryMapping() != null) {
                if (page.getModels() == null) {
                    page.setModels(new Models());
                }
                HashMap<String, ModelLink> resolvedModelLinks = new HashMap<>();
                page.getRoutes().getQueryMapping().keySet().stream()
                        .filter(param -> page.getRoutes().getQueryMapping().get(param).getOnSet() instanceof ModelLink)
                        .forEach(param -> resolvedModelLinks.put(param, (ModelLink) p.resolveLink(page.getRoutes().getQueryMapping().get(param).getOnSet())));
                resolvedModelLinks.keySet().stream().filter(param -> (resolvedModelLinks.get(param).isConst() && resolvedModelLinks.get(param).isLink()))
                        .forEach(param -> {
                            ModelLink modelLink = resolvedModelLinks.get(param);
                            page.getModels().add(modelLink.getModel(), modelLink.getDatasource(), modelLink.getFieldId(), modelLink);
                        });
            }
        }
    }

    private void bindProperty(D page, BindProcessor p) {
        if (page.getPageProperty() != null) {
            page.getPageProperty().setTitle(p.resolveText(page.getPageProperty().getTitle(),
                    page.getPageProperty().getModelLink()));
            page.getPageProperty().setHtmlTitle(p.resolveText(page.getPageProperty().getHtmlTitle(),
                    page.getPageProperty().getModelLink()));
            page.getPageProperty().setModalHeaderTitle(p.resolveText(page.getPageProperty().getModalHeaderTitle(),
                    page.getPageProperty().getModelLink()));

        }
    }

    private void collectFiltersToModels(Models models, List<Widget> widgets, BindProcessor p) {
        if (widgets != null)
            for (Widget w : widgets)
                if (w.getFilters() != null)
                    for (Filter f : (List<Filter>) w.getFilters())
                        if (Boolean.TRUE.equals(f.getRoutable()))
                            if (f.getLink().getSubModelQuery() != null)
                                addSubModelLinkToModels(models, f);
                            else if (w instanceof Table && ((Table) w).getFiltersDefaultValuesQueryId() != null)
                                addDefaultFilterValueLinkToModels(models, f, p);


    }

    private void addSubModelLinkToModels(Models models, Filter f) {
        ModelLink link = constructLink(models, f.getLink(), f.getLink().getSubModelQuery().getSubModel());
        link.setParam(f.getParam());
        link.setSubModelQuery(f.getLink().getSubModelQuery());

        if (link.getValue() == null)
            link.setValue(f.getLink().getValue());
        models.add(link.getModel(), link.getDatasource(), link.getFieldId(), link);
    }

    private void addDefaultFilterValueLinkToModels(Models models, Filter f, BindProcessor p) {
        ModelLink link = constructLink(models, f.getLink(), f.getFilterId());
        link.setParam(link.getDatasource() + "_" + f.getFilterId());

        Object linkValue = p.getLinkValue(link);
        link.setValue(linkValue != null ? linkValue : f.getLink().getValue());
        models.add(link.getModel(), link.getDatasource(), link.getFieldId(), link);
    }

    private ModelLink constructLink(Models models, ModelLink filterLink, String fieldId) {
        ReduxModel model = filterLink.getModel();
        String widgetId = filterLink.getDatasource();
        ModelLink link = new ModelLink(model, widgetId, fieldId);

        ModelLink pageLink = models.get(model, widgetId, fieldId);
        if (pageLink != null)
            link.setValue(pageLink.getValue());
        return link;
    }

    private void resolveLinks(Models models, BindProcessor p) {
        models.keySet().forEach(param -> {
                    models.put(param, (ModelLink) p.resolveLink(models.get(param)));
                    p.resolveSubModels(models.get(param));
                }
        );
    }
}
