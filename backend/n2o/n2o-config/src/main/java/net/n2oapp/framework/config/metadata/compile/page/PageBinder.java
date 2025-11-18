package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import net.n2oapp.framework.api.criteria.Restriction;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.datasource.AbstractDatasource;
import net.n2oapp.framework.api.metadata.datasource.StandardDatasource;
import net.n2oapp.framework.api.metadata.meta.Breadcrumb;
import net.n2oapp.framework.api.metadata.meta.BreadcrumbList;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.Models;
import net.n2oapp.framework.api.metadata.meta.control.DefaultValues;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

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
            if (page.getRoutes().getPath() != null)
                page.getRoutes().setPath(p.resolveUrl(page.getRoutes().getPath()));
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
                if (bl.getValue() instanceof String blStr) {
                    bl.setValue(p.resolveText(blStr));
                } else if (bl.getValue() instanceof DefaultValues dv) {
                    for (String key : dv.getValues().keySet()) {
                        if (dv.getValues().get(key) instanceof String str) {
                            dv.getValues().put(key, p.resolveText(str));
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
        String modelId = modelLink.getLink().split("\\.")[1] + "." + modelLinkValue.split("\\.")[0];
        ModelLink link = page.getModels().get(modelId);
        if (link != null && link.getValue() instanceof DefaultValues defaultValues) {
            defaultValues.getValues().remove(modelLinkValue.split("\\.")[1]);
            page.getModels().remove(modelId);
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
        if (widgets == null) return;
        widgets.stream().filter(w -> w.getFiltersDatasourceId() != null).forEach(w -> {
            AbstractDatasource filterDatasource = page.getDatasources().get(w.getFiltersDatasourceId());
            if (!(filterDatasource instanceof StandardDatasource datasource)) return;
            DataSet data = p.executeQuery(datasource.getQueryId(), getFilterDefaultValueCriteria(datasource));
            if (data != null) {
                data.forEach((k, v) -> {
                    ModelLink existParam = page.getModels().get(ReduxModelEnum.FILTER, w.getDatasource(), k);
                    if (v != null && (existParam == null || !p.canResolveParam(existParam.getParam()))) {
                        page.getModels().add(ReduxModelEnum.FILTER, w.getDatasource(), k, new ModelLink(v));
                    }
                });
            }
        });
    }

    private static N2oPreparedCriteria getFilterDefaultValueCriteria(StandardDatasource datasource) {
        N2oPreparedCriteria criteria = new N2oPreparedCriteria();
        if (datasource.getProvider() == null || datasource.getProvider().getQueryMapping() == null)
            return criteria;

        datasource.getProvider().getQueryMapping().forEach((param, modelLink) -> {
            if (modelLink.getValue() != null && !StringUtils.isJs(modelLink.getValue()))
                criteria.addRestriction(new Restriction(modelLink.getQueryParam(), modelLink.getValue()));
        });
        return criteria;
    }

    private String tryToResolve(String value, List<ModelLink> modelLinks, BindProcessor p) {
        String resolved = p.resolveText(value, modelLinks); //TODO использовать только один modelLink https://jira.i-novus.ru/browse/NNO-8532
        return hasLink(resolved) ? ScriptProcessor.resolveLinks(resolved) : resolved;
    }

    private void resolveLinks(Models models, BindProcessor p) {
        new HashSet<>(models.entrySet()).stream().filter(e -> !e.getValue().isConst()).forEach(e -> {
                    ModelLink link = models.get(e.getKey());
                    ModelLink newLink;
                    if (link.getFieldValue() == null) {
                        newLink = new ModelLink(link.getModel(), link.getDatasource(), p.resolveTextByParams(link.getFieldId()));
                    } else {
                        // значит fieldId пустой, а значение задано js выражением в value
                        newLink = new ModelLink(link.getModel(), link.getDatasource());
                    }
                    newLink.copyAttributes(link);
                    ModelLink resolvedLink = (ModelLink) p.resolveLink(newLink, true, false);
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
            page.getDatasources().values().forEach(p::bind);
        }
    }
}
