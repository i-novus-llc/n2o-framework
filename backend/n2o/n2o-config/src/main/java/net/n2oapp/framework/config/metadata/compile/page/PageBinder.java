package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.datasource.AbstractDatasource;
import net.n2oapp.framework.api.metadata.datasource.StandardDatasource;
import net.n2oapp.framework.api.metadata.meta.*;
import net.n2oapp.framework.api.metadata.meta.control.DefaultValues;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import net.n2oapp.framework.config.metadata.compile.redux.Redux;
import net.n2oapp.framework.config.util.BindUtil;

import java.util.*;
import java.util.stream.Collectors;

import static net.n2oapp.framework.api.StringUtils.hasLink;

/**
 * Базовое связывание данных на странице
 */
public abstract class PageBinder<D extends Page> implements BaseMetadataBinder<D> {
    public D bindPage(D page, BindProcessor p, List<Widget<?>> widgets) {
        if (widgets != null)
            widgets.forEach(p::bind);
        bindDatasources(page, p);
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
                            removeDuplicateModels(page, modelLink);
                            page.getModels().add(modelLink.getModel(), modelLink.getDatasource(), modelLink.getFieldId(), modelLink);
                        });
            }
        }

        if (page.getBreadcrumb() != null) {
            page.setBreadcrumb(new BreadcrumbList(page.getBreadcrumb()));
            page.getBreadcrumb()
                    .forEach(b -> {
                        b.setPath(p.resolveUrl(b.getPath()));
                        b.setLabel(tryToResolve(b.getLabel(), b.getModelLinks(), p));
                    });
        }

        collectFiltersToModels(page, widgets, p);

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
            page.getPageProperty().setTitle(tryToResolve(page.getPageProperty().getTitle(),
                    page.getPageProperty().getModelLinks(), p));
            page.getPageProperty().setHtmlTitle(tryToResolve(page.getPageProperty().getHtmlTitle(),
                    page.getPageProperty().getModelLinks(), p));
            page.getPageProperty().setModalHeaderTitle(tryToResolve(page.getPageProperty().getModalHeaderTitle(),
                    page.getPageProperty().getModelLinks(), p));

        }
        if (page.getBreadcrumb() != null) {
            for (Breadcrumb crumb : page.getBreadcrumb()) {
                crumb.setLabel(p.resolveText(crumb.getLabel(), crumb.getModelLinks()));
            }
        }
        return page;
    }

    private void removeDuplicateModels(D page, ModelLink modelLink) {
        String modelLinkValue = StringUtils.unwrapJs((String) modelLink.getValue());
        if (Objects.isNull(modelLinkValue))
            return;
        String modelId = modelLink.getBindLink().split("\\.")[1] + "." + modelLinkValue.split("\\.")[0];
        ModelLink link = page.getModels().get(modelId);
        if (link != null && link.getValue() instanceof DefaultValues) {
            DefaultValues defaultValues = (DefaultValues) link.getValue();
            defaultValues.getValues().remove(modelLinkValue.split("\\.")[1]);
            if (defaultValues.getValues().isEmpty()) {
                page.getModels().remove(modelId);
            }
        }
    }

    /**
     * Добавление значений фильтров таблицы из выборки в модели
     *
     * @param page    страница
     * @param widgets Виджеты
     * @param p       Процессор связывания
     */
    private void collectFiltersToModels(D page, List<Widget<?>> widgets, BindProcessor p) {
        if (widgets != null)
            for (Widget<?> w : widgets)
                if (w.getFiltersDatasourceId() != null) {
                    AbstractDatasource filterDatasource = page.getDatasources().get(w.getFiltersDatasourceId());
                    if (!(filterDatasource instanceof StandardDatasource)) continue;
                    DataSet data = p.executeQuery(((StandardDatasource) filterDatasource).getQueryId());
                    if (data != null) {
                        data.forEach((k, v) -> {
                            //todo NNO-7523   && !p.canResolveParam(f.getParam())
                            if (v != null) {
                                page.getModels().add(ReduxModel.filter, w.getDatasource(), k, new ModelLink(v));
                            }
                        });
                    }
                }
    }

    private String tryToResolve(String value, List<ModelLink> modelLinks, BindProcessor p) {
        String resolved = p.resolveText(value, modelLinks); //TODO использовать только один modelLink https://jira.i-novus.ru/browse/NNO-8532
        return hasLink(resolved) ? ScriptProcessor.resolveLinks(resolved) : resolved;
    }

    private void resolveLinks(Models models, BindProcessor p) {
        new HashSet<>(models.entrySet()).stream().filter(e -> !e.getValue().isConst()).forEach(e -> {
                    ModelLink link = models.get(e.getKey());
                    ModelLink resolvedLink = (ModelLink) p.resolveLink(link, true, false);
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

    private void bindDatasources(D page, BindProcessor p) {
        if (page.getDatasources() != null) {
            List<StandardDatasource> datasources = page.getDatasources().values().stream().
                    filter(StandardDatasource.class::isInstance).
                    map(StandardDatasource.class::cast).
                    collect(Collectors.toList());
            datasources.stream().filter(ds -> ds.getProvider() != null)
                    .forEach(ds -> BindUtil.bindDataProvider(ds.getProvider(), p));
            datasources.stream().filter(ds -> ds.getSubmit() != null)
                    .forEach(ds -> BindUtil.bindDataProvider(ds.getSubmit(), p));
            page.getDatasources().values().forEach(p::bind);
        }
    }
}
