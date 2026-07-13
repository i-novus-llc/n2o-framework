package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import net.n2oapp.framework.api.criteria.Restriction;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.datasource.AbstractDatasource;
import net.n2oapp.framework.api.metadata.datasource.StandardDatasource;
import net.n2oapp.framework.api.metadata.meta.BindLink;
import net.n2oapp.framework.api.metadata.meta.BreadcrumbList;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.Models;
import net.n2oapp.framework.api.metadata.meta.control.DefaultValues;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;

import java.util.*;

import static net.n2oapp.framework.api.StringUtils.hasLink;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Базовое связывание данных на странице
 */
public abstract class PageBinder<D extends Page> implements BaseMetadataBinder<D> {
    public D bindPage(D page, BindProcessor p, List<Widget<?>> widgets) {
        bindDatasources(page, p);
        processRouteParameters(page, p);
        collectFiltersToModels(page, widgets, p);
        updateModelsFromQueryMapping(page, p);
        resolveModelLinks(page, p);
        processBreadcrumb(page, p);
        processPageProperty(page, p);
        return page;
    }

    private void processPageProperty(D page, BindProcessor p) {
        if (page.getPageProperty() != null) {
            page.getPageProperty().setTitle(tryToResolve(page.getPageProperty().getTitle(),
                    page.getPageProperty().getModelLinks(), p, page));
            page.getPageProperty().setHtmlTitle(tryToResolve(page.getPageProperty().getHtmlTitle(),
                    page.getPageProperty().getModelLinks(), p, page));
            page.getPageProperty().setModalHeaderTitle(tryToResolve(page.getPageProperty().getModalHeaderTitle(),
                    page.getPageProperty().getModelLinks(), p, page));
        }
    }

    private void processBreadcrumb(D page, BindProcessor p) {
        if (page.getBreadcrumb() != null) {
            page.setBreadcrumb(new BreadcrumbList(page.getBreadcrumb()));
            page.getBreadcrumb()
                    .forEach(b -> {
                        b.setPath(p.resolveUrl(b.getPath()));
                        b.setLabel(tryToResolve(b.getLabel(), b.getModelLinks(), p, page));
                    });
        }
    }

    private void processRouteParameters(D page, BindProcessor p) {
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
                            page.getModels().add(modelLink.getModel(), modelLink.getDatasource(), modelLink.getSuffix(), modelLink.getFieldId(), modelLink);
                        });
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
        if (widgets == null) return;
        widgets.stream()
                .filter(w -> w.getFiltersDatasourceId() != null)
                .forEach(w -> {
                            AbstractDatasource filterDatasource = page.getDatasources().get(w.getFiltersDatasourceId());
                            if (!(filterDatasource instanceof StandardDatasource datasource)) return;
                            DataSet data = p.executeQuery(datasource.getQueryId(), getFilterDefaultValueCriteria(datasource));
                            if (data != null) {
                                data.forEach((k, v) -> {
                                    ModelLink existParam = page.getModels().get(ReduxModelEnum.FILTER, w.getDatasource(), k);
                                    if (v != null && (existParam == null || existParam.getParam() == null || !p.canResolveParam(existParam.getParam()))) {
                                        ModelLink modelLink = new ModelLink(v);
                                        modelLink.setDatasource(w.getDatasource());
                                        page.getModels().add(ReduxModelEnum.FILTER, w.getDatasource(), k, modelLink);
                                    }
                                });
                            }
                        }
                );
    }

    private void updateModelsFromQueryMapping(D page, BindProcessor p) {
        // Сохраняем все значения по виджетам (не только дефолтные), группируя по datasource из ModelLink
        Map<String, Map<String, ModelLink>> widgetAllValues = new HashMap<>();
        for (Map.Entry<String, ModelLink> entry : page.getModels().entrySet()) {
            String key = entry.getKey();
            ModelLink modelLink = entry.getValue();
            String datasource = modelLink.getDatasource();
            if (datasource != null)
                widgetAllValues.computeIfAbsent(datasource, k -> new HashMap<>()).put(key, modelLink);
        }
        page.getModels().clear();
        if (page.getRoutes() != null && page.getRoutes().getQueryMapping() != null) {
            // Собираем виджеты, у которых есть параметры в URL
            // Добавляем в models все queryMapping, которые присутствуют в URL
            Set<String> widgetsWithParams = addQueryMappingToModels(page, p);
            addValuesToModels(page, widgetAllValues, widgetsWithParams);
        }
    }

    private void resolveModelLinks(D page, BindProcessor p) {
        if (page.getModels() != null) {
            //разрешение контекстных значений в моделях
            page.getModels().values().forEach(bl -> {
                if (bl.getValue() instanceof String blStr) {
                    bl.setValue(resolveContextOrText(blStr, p));
                } else if (bl.getValue() instanceof DefaultValues dv) {
                    for (String key : dv.getValues().keySet()) {
                        if (dv.getValues().get(key) instanceof String str) {
                            dv.getValues().put(key, resolveContextOrText(str, p));
                        }
                    }
                }
            });
            resolveLinks(page.getModels(), p);
        }
    }

    /**
     * Добавляет в models все queryMapping, которые присутствуют в URL
     *
     * @param page страница
     * @param p    процессор связывания
     * @return множество идентификаторов источников данных, у которых есть параметры в URL
     */
    private Set<String> addQueryMappingToModels(D page, BindProcessor p) {
        Set<String> widgetsWithParams = new HashSet<>();
        for (Map.Entry<String, PageRoutes.Query> entry : page.getRoutes().getQueryMapping().entrySet()) {
            if (p.canResolveParam(entry.getKey())) {
                BindLink onSet = entry.getValue().getOnSet();
                if (onSet instanceof ModelLink modelLink) {
                    widgetsWithParams.add(modelLink.getDatasource());
                    page.getModels().add(modelLink.getModel(), modelLink.getDatasource(), modelLink.getSuffix(), modelLink.getFieldId(), modelLink);
                }
            }
        }
        return widgetsWithParams;
    }


    /**
     * Для виджетов с параметрами в URL добавляет только дефолтные значения (DefaultValues)
     * Для виджетов, для которых не были заданы параметры в URL, добавляет в models все их значения (с сохранением полных ключей)
     *
     * @param page              страница
     * @param widgetAllValues   все значения виджетов
     * @param widgetsWithParams источники данных, у которых есть параметры в URL
     */
    private void addValuesToModels(D page, Map<String, Map<String, ModelLink>> widgetAllValues, Set<String> widgetsWithParams) {
        for (Map.Entry<String, Map<String, ModelLink>> entry : widgetAllValues.entrySet()) {
            String datasource = entry.getKey();
            boolean hasParams = widgetsWithParams.contains(datasource);
            for (Map.Entry<String, ModelLink> keyEntry : entry.getValue().entrySet()) {
                ModelLink value = keyEntry.getValue();
                // Для виджетов с параметрами в URL восстанавливаем только дефолтные значения
                if (!hasParams || value.getValue() instanceof DefaultValues) {
                    page.getModels().put(keyEntry.getKey(), value);
                }
            }
        }
    }


    private static Object resolveContextOrText(String value, BindProcessor p) {
        return StringUtils.isContext(value) ? p.resolve(value) : p.resolveText(value);
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

    private String tryToResolve(String value, List<ModelLink> modelLinks, BindProcessor p, D page) {
        String pageTitleResolvingMode = p.resolve(property("n2o.api.page.title.resolving"), String.class);

        // Если включено новое поведение И в странице есть datasource И есть ссылки в значении
        // В остальных случаях разрешаем на сервере
        String resolved = ("new".equals(pageTitleResolvingMode) && hasLink(value) &&
                page.getPageProperty() != null && page.getPageProperty().getDatasource() != null)
                ? value
                : p.resolveText(value, modelLinks); //TODO использовать только один modelLink https://jira.i-novus.ru/browse/NNO-8532

        return hasLink(resolved) ? ScriptProcessor.resolveLinks(resolved) : resolved;
    }

    private void resolveLinks(Models models, BindProcessor p) {
        // 1. Обрабатываем неконстантные ссылки
        new HashSet<>(models.entrySet()).stream()
                .filter(e -> !e.getValue().isConst())
                .forEach(e -> {
                            ModelLink link = models.get(e.getKey());
                            ModelLink newLink;
                            if (link.getFieldValue() == null) {
                                newLink = new ModelLink(link.getModel(), link.getDatasource(), p.resolveTextByParams(link.getFieldId()), link.getSuffix());
                            } else {
                                // значит fieldId пустой, а значение задано js выражением в value
                                newLink = new ModelLink(link.getModel(), link.getDatasource(), null, link.getSuffix());
                            }
                            newLink.copyAttributes(link);
                            ModelLink resolvedLink = (ModelLink) p.resolveLink(newLink, true, false);
                            models.put(e.getKey(), resolvedLink);
                        }
                );
        // 2. Обрабатываем константные ссылки с subModelQuery
        new HashSet<>(models.entrySet()).stream()
                .filter(e -> e.getValue().isConst() && e.getValue().getSubModelLink() != null)
                .forEach(e -> {
                    ModelLink link = models.get(e.getKey());
                    ModelLink resolvedSubModelLink = p.resolveSubModels(link);
                    models.add(link.getSubModelLink(), resolvedSubModelLink);
                });

        // 3. Обрабатываем DefaultValues объекты и простые значения
        new HashSet<>(models.entrySet()).stream()
                .filter(e -> !e.getValue().isConst())
                .forEach(e -> {
                    ModelLink link = models.get(e.getKey());
                    if (link.getValue() instanceof String str && StringUtils.isJs(str)) {
                        Object resolved = p.resolve(str);
                        if (resolved != null) {
                            link.setValue(resolved);
                            models.put(e.getKey(), link);
                        }
                    }
                });
    }

    private void bindDatasources(D page, BindProcessor p) {
        if (page.getDatasources() != null) {
            page.getDatasources().values().forEach(p::bind);
        }
    }
}