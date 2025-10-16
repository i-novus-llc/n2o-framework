package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oAbstractListWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oPagination;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oRowClick;
import net.n2oapp.framework.api.metadata.global.view.widget.table.Place;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShowCountType;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.Filter;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.ReduxAction;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.action.RoutablePayload;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.metadata.meta.widget.table.Pagination;
import net.n2oapp.framework.api.metadata.meta.widget.table.RowClick;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.datasource.DataSourcesScope;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.util.StylesResolver;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.metadata.compile.action.ActionCompileStaticProcessor.compileAction;
import static net.n2oapp.framework.config.metadata.compile.action.ActionCompileStaticProcessor.initActions;
import static net.n2oapp.framework.config.metadata.compile.redux.Redux.createRoutablePagingLink;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

/**
 * Компиляция абстрактного спискового виджета
 */
public abstract class BaseListWidgetCompiler<D extends Widget, S extends N2oAbstractListWidget> extends BaseWidgetCompiler<D, S> {

    /**
     * Компиляция паджинации
     */
    protected Pagination compilePaging(N2oAbstractListWidget source, Integer size, CompileProcessor p, WidgetScope widgetScope) {
        Pagination pagination = new Pagination();
        pagination.setSize(source.getSize() != null ? source.getSize() : size);
        N2oPagination sourcePagination = source.getPagination() != null ? source.getPagination() : new N2oPagination();
        pagination.setPrev(castDefault(sourcePagination.getPrev(),
                () -> p.resolve(property("n2o.api.widget.list.paging.prev"), Boolean.class)));
        pagination.setNext(castDefault(sourcePagination.getNext(),
                () -> p.resolve(property("n2o.api.widget.list.paging.next"), Boolean.class)));
        pagination.setShowLast(castDefault(sourcePagination.getShowLast(),
                () -> p.resolve(property("n2o.api.widget.list.paging.show_last"), Boolean.class)));
        pagination.setShowCount(castDefault(sourcePagination.getShowCount(),
                () -> p.resolve(property("n2o.api.widget.list.paging.show_count"), ShowCountType.class)));
        pagination.setSrc(sourcePagination.getSrc());
        pagination.setPrevLabel(castDefault(sourcePagination.getPrevLabel(),
                () -> p.resolve(property("n2o.api.widget.list.paging.prev_label"), String.class)));
        pagination.setPrevIcon(castDefault(sourcePagination.getPrevIcon(),
                () -> p.resolve(property("n2o.api.widget.list.paging.prev_icon"), String.class)));
        pagination.setNextLabel(castDefault(sourcePagination.getNextLabel(),
                () -> p.resolve(property("n2o.api.widget.list.paging.next_label"), String.class)));
        pagination.setNextIcon(castDefault(sourcePagination.getNextIcon(),
                () -> p.resolve(property("n2o.api.widget.list.paging.next_icon"), String.class)));
        pagination.setClassName(castDefault(sourcePagination.getClassName(),
                () -> p.resolve(property("n2o.api.widget.list.paging.className"), String.class)));
        pagination.setStyle(StylesResolver.resolveStyles(sourcePagination.getStyle()));
        pagination.setPlace(castDefault(sourcePagination.getPlace(),
                () -> p.resolve(property("n2o.api.widget.list.paging.place"), Place.class)));
        if (Boolean.TRUE.equals(sourcePagination.getRoutable()))
            initRoute(p, widgetScope);
        if (ShowCountType.BY_REQUEST.equals(pagination.getShowCount())) {
            addAdditionalMapping(widgetScope, p);
            pagination.setCountDataProvider(initCountDataProvider(widgetScope, p));
        } else if (ShowCountType.NEVER.equals(pagination.getShowCount()) && Boolean.FALSE.equals(pagination.getShowLast()))
            addAdditionalMapping(widgetScope, p);
        return pagination;
    }

    private void initRoute(CompileProcessor p, WidgetScope widgetScope) {
        PageRoutes pageRoutes = p.getScope(PageRoutes.class);
        String pageIndex;
        String sizeIndex;
        if (p.getScope(PageScope.class).getWidgetIdSourceDatasourceMap().size() > 1) {
            pageIndex = widgetScope.getWidgetId().concat("_page");
            sizeIndex = widgetScope.getWidgetId().concat("_size");
        } else {
            pageIndex = "page";
            sizeIndex = "size";
        }
        addPagingQueryMapping(pageRoutes, pageIndex, widgetScope, RoutablePayload.Paging.page, p);
        addPagingQueryMapping(pageRoutes, sizeIndex, widgetScope, RoutablePayload.Paging.size, p);
    }

    /**
     * Инициализация withCount = false в провайдере datasource
     *
     * @param widgetScope Информация о виджете
     * @param p           Процессор сборки метаданных
     */
    private void addAdditionalMapping(WidgetScope widgetScope, CompileProcessor p) {
        DataSourcesScope dataSourcesScope = p.getScope(DataSourcesScope.class);
        if (dataSourcesScope != null && dataSourcesScope.get(widgetScope.getDatasourceId()) != null) {
            N2oAbstractDatasource datasource = dataSourcesScope.get(widgetScope.getDatasourceId());
            Map<String, ModelLink> mapping = datasource.getAdditionalQueryMapping();
            if (mapping == null) {
                mapping = new HashMap<>();
                datasource.setAdditionalQueryMapping(mapping);
            }
            mapping.putIfAbsent("withCount", new ModelLink(false));
        }
    }

    /**
     * Инициализация провайдера пагинации для получения количества записей по запросу
     *
     * @param widgetScope Информация о виджете
     * @param p           Процессор сборки метаданных
     * @return Провайдер данных для получения количества записей по запросу
     */
    private ClientDataProvider initCountDataProvider(WidgetScope widgetScope, CompileProcessor p) {
        ClientDataProvider countDataProvider = new ClientDataProvider();
        countDataProvider.setUrl(p.resolve(property("n2o.api.widget.list.paging.count.url"), String.class));
        FiltersScope filtersScope = p.getScope(FiltersScope.class);
        if (filtersScope != null) {
            List<Filter> filters = filtersScope.getFilters(widgetScope.getDatasourceId());
            if (!CollectionUtils.isEmpty(filters)) {
                countDataProvider.setQueryMapping(new LinkedHashMap<>());
                filtersScope.getFilters(widgetScope.getDatasourceId()).forEach(filter ->
                        countDataProvider.getQueryMapping().put(filter.getParam(), filter.getLink()));
            }
        }
        return countDataProvider;
    }

    private void addPagingQueryMapping(PageRoutes pageRoutes, String param, WidgetScope widgetScope, RoutablePayload.Paging paramType, CompileProcessor p) {
        RoutablePayload payload = new RoutablePayload();
        payload.setId(widgetScope.getClientDatasourceId());
        HashMap<String, String> paging = new HashMap<>();
        paging.put("paging.".concat(paramType.name()), ":".concat(param));
        payload.setParams(paging);
        ReduxAction onGet = new ReduxAction(p.resolve(property("n2o.api.widget.list.paging.routable.type"), String.class), payload);
        pageRoutes.addQueryMapping(param, onGet, createRoutablePagingLink(widgetScope.getClientDatasourceId(), paramType));
    }

    /**
     * Компиляция действия клика по строке
     */
    protected RowClick compileRowClick(N2oAbstractListWidget source, CompileContext<?, ?> context,
                                       CompileProcessor p, WidgetScope widgetScope,
                                       CompiledObject object) {
        RowClick rc = null;
        if (source.getRows() != null && source.getRows().getRowClick() != null) {
            N2oRowClick rowClick = source.getRows().getRowClick();
            Object enabledCondition = ScriptProcessor.resolveExpression(rowClick.getEnabled());
            if (enabledCondition == null || enabledCondition instanceof String || Boolean.TRUE.equals(enabledCondition)) {
                rowClick.setActions(initActions(rowClick, p));
                if (isNotEmpty(rowClick.getActions())) {
                    Action compiledAction = compileAction(rowClick, context, p, object, widgetScope);
                    rc = new RowClick(compiledAction);
                    rc.setProperties(p.mapAttributes(rowClick));
                    if (compiledAction != null && StringUtils.isJs(enabledCondition)) {
                        rc.setEnablingCondition((String) ScriptProcessor.removeJsBraces(enabledCondition));
                    }
                }
            }
        }
        return rc;
    }
}
