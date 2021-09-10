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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Базовое связывание данных на странице
 */
public abstract class PageBinder<D extends Page> implements BaseMetadataBinder<D> {
    public D bindPage(D page, BindProcessor p, List<Widget<?>> widgets) {
        if (widgets != null)
            widgets.forEach(p::bind);

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
                //копирование ссылок из квери параметров роутера в модель, если такой квери параметр есть в запросе
                page.getRoutes().getQueryMapping().keySet().stream()
                        .filter(p::canResolveParam)
                        .filter(param -> page.getRoutes().getQueryMapping().get(param).getOnSet() instanceof ModelLink)
                        .forEach(param -> {
                            ModelLink modelLink = (ModelLink) page.getRoutes().getQueryMapping().get(param).getOnSet();
                            page.getModels().add(modelLink.getModel(), modelLink.getWidgetId(), modelLink.getFieldId(), modelLink);
                        });
            }
        }

        if (page.getBreadcrumb() != null)
            page.getBreadcrumb().stream().filter(b -> b.getPath() != null)
                    .forEach(b -> {
                        b.setPath(p.resolveUrl(b.getPath()));
                        b.setLabel(p.resolveText(b.getLabel(), b.getModelLink()));
                    });


        if (page.getModels() != null) {
            //разрешение контекстных значений в моделях
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
            resolveLinks(page.getModels(), p);
        }
        if (page.getPageProperty() != null) {
            page.getPageProperty().setTitle(p.resolveText(page.getPageProperty().getTitle(),
                    page.getPageProperty().getModelLink()));
            page.getPageProperty().setHtmlTitle(p.resolveText(page.getPageProperty().getHtmlTitle(),
                    page.getPageProperty().getModelLink()));
            page.getPageProperty().setModalHeaderTitle(p.resolveText(page.getPageProperty().getModalHeaderTitle(),
                    page.getPageProperty().getModelLink()));

        }
        if (page.getBreadcrumb() != null) {
            for (Breadcrumb crumb : page.getBreadcrumb()) {
                crumb.setLabel(p.resolveText(crumb.getLabel(), crumb.getModelLink()));
            }
        }
        return page;
    }

    /**
     * Добавление значений фильтров таблицы в модели
     *
     * @param models
     * @param widgets
     * @param p
     */
    private void collectFiltersToModels(Models models, List<Widget<?>> widgets, BindProcessor p) {
        if (widgets != null)
            for (Widget<?> w : widgets)
                if (w.getFilters() != null)
                    for (Filter f : w.getFilters())
                        if (f.getRoutable())
                            if (f.getLink().getSubModelQuery() != null)
                                addSubModelLinkToModels(models, f);
    }

    private void addSubModelLinkToModels(Models models, Filter f) {
        ModelLink link = constructLink(models, f.getLink(), f.getLink().getSubModelQuery().getSubModel());
        link.setParam(f.getParam());
        link.setSubModelQuery(f.getLink().getSubModelQuery());

        if (link.getValue() == null)
            link.setValue(f.getLink().getValue());
        models.add(link.getModel(), link.getWidgetId(), link.getFieldId(), link);
    }

    private void addDefaultFilterValueLinkToModels(Models models, Filter f, BindProcessor p) {
        ModelLink link = constructLink(models, f.getLink(), f.getFilterId());
        link.setParam(link.getWidgetId() + "_" + f.getFilterId());

        Object linkValue = p.resolveLinkValue(link);
        link.setValue(linkValue != null ? linkValue : f.getLink().getValue());
        models.add(link.getModel(), link.getWidgetId(), link.getFieldId(), link);
    }

    private ModelLink constructLink(Models models, ModelLink filterLink, String fieldId) {
        ReduxModel model = filterLink.getModel();
        String widgetId = filterLink.getWidgetId();
        ModelLink link = new ModelLink(model, widgetId, fieldId);

        ModelLink pageLink = models.get(model, widgetId, fieldId);
        if (pageLink != null)
            link.setValue(pageLink.getValue());
        return link;
    }

    private void resolveLinks(Models models, BindProcessor p) {
        new HashSet<>(models.entrySet()).stream().filter(e -> !e.getValue().isConst()).forEach(e -> {
                    ModelLink link = models.get(e.getKey());
                    ModelLink resolvedLink = (ModelLink) p.resolveLink(link);
                    models.put(e.getKey(), resolvedLink);
                }
        );
        new HashSet<>(models.entrySet()).stream().filter(e -> e.getValue().isConst() && e.getValue().getSubModelLink() != null).forEach(e -> {
                    ModelLink link = models.get(e.getKey());
                    ModelLink resolvedSubModelLink = p.resolveSubModels(link);
                    models.add(link.getSubModelLink(), resolvedSubModelLink);
                }
        );
    }
}
