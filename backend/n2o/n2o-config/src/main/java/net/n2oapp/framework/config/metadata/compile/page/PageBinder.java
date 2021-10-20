package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.criteria.dataset.DataSet;
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
                            page.getModels().add(modelLink.getModel(), modelLink.getDatasource(), modelLink.getFieldId(), modelLink);
                        });
            }
        }

        if (page.getBreadcrumb() != null) {
            page.setBreadcrumb(new BreadcrumbList(page.getBreadcrumb()));
            page.getBreadcrumb().stream().filter(b -> b.getPath() != null)
                    .forEach(b -> {
                        b.setPath(p.resolveUrl(b.getPath()));
                        b.setLabel(p.resolveText(b.getLabel(), b.getModelLink()));
                    });
        }

        collectFiltersToModels(page.getModels(), widgets, p);

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
     * Добавление значений фильтров таблицы из выборки в модели
     *
     * @param models Модели
     * @param widgets Виджеты
     * @param p Процессор связывания
     */
    private void collectFiltersToModels(Models models, List<Widget<?>> widgets, BindProcessor p) {
        if (widgets != null)
            for (Widget<?> w : widgets)
                if (w.getFilters() != null && w.getFiltersDefaultValuesQueryId() != null) {
                    DataSet data = p.executeQuery(w.getFiltersDefaultValuesQueryId());
                    if (data != null) {
                        for (Filter filter : w.getFilters()) {
                            addDefaultFilterValueLinkToModels(models, w.getId(), filter, data, p);
                        }
                    }
                }
    }

    private void addDefaultFilterValueLinkToModels(Models models, String widgetId, Filter f, DataSet data, BindProcessor p) {
        Object value = data.get(f.getFilterId());
        if (value != null && !p.canResolveParam(f.getParam())) {
            ModelLink link = new ModelLink(f.getLink());
            link.setValue(value);
            link.setParam(link.getDatasource() + "_" + f.getFilterId());
            f.setLink(link);
            models.add(ReduxModel.FILTER, widgetId, f.getFilterId(), f.getLink());
        }
    }


    private void resolveLinks(Models models, BindProcessor p) {
        new HashSet<>(models.entrySet()).stream().filter(e -> !e.getValue().isConst()).forEach(e -> {
                    ModelLink link = models.get(e.getKey());
                    ModelLink resolvedLink = (ModelLink) p.resolveLink(link, true);
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
