package net.n2oapp.framework.config.metadata.compile.context;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.view.ActionBar;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.meta.Breadcrumb;
import net.n2oapp.framework.api.metadata.meta.page.Page;

import java.util.*;

@Getter
@Setter
public class PageContext extends BaseCompileContext<Page, N2oPage> {

    /**
     * Список действий на странице
     */
    private Map<String, ActionBar> actions;

    private List<Breadcrumb> breadcrumbs;

    /**
     * Задан ли бредкрамб на родительской странице
     */
    private Boolean breadcrumbFromParent;
    /**
     * Маршрут родителя
     */
    private String parentRoute;
    /**
     * Родительский виджет (клиентский), в котором находилось действие
     */
    @Deprecated
    private String parentClientWidgetId;
    /**
     * Родительский локальный источник данных, в котором находилось действие
     */
    private String parentLocalDatasourceId;
    /**
     * Клиентский идентификатор родительской страницы, в которой находилось действие
     */
    private String parentClientPageId;
    /**
     * Наименование страницы
     */
    private String pageName;
    /**
     * Идентификаторы источников данных, которые необходимо обновить после успешной отправки формы
     */
    private List<String> refreshClientDataSourceIds;
    /**
     * Обновить данные родительского виджета после закрытия страницы
     */
    private Boolean refreshOnClose;
    /**
     * Предупредить о несохраненных данных на форме при закрытии?
     */
    private Boolean unsavedDataPromptOnClose;
    /**
     * Список источников данных открываемой страницы
     */
    private List<N2oAbstractDatasource> datasources;
    /**
     * Соответствия идентификаторов источников данных родительской страницы с клиентскими идентификаторами
     */
    private Map<String, String> parentDatasourceIdsMap;
    /**
     * Клиентский идентификатор страницы
     */
    private String clientPageId;
    /**
     * Соответствия идентификаторов виджета с источником данных в родительском виджете
     */
    private Map<String, String> parentWidgetIdDatasourceMap;
    /**
     * Список идентификаторов таб регионов
     */
    private Set<String> parentTabIds;
    /**
     * Список всех родительских маршрутов
     */
    private List<String> parentRoutes;
    /**
     * Список тулбаров открываемой страницы
     */
    private List<N2oToolbar> toolbars;

    public PageContext(String sourcePageId) {
        super(sourcePageId, N2oPage.class, Page.class);
    }

    public PageContext(String sourcePageId, String route) {
        super(route, sourcePageId, N2oPage.class, Page.class);
    }


    public void setBreadcrumbs(List<Breadcrumb> breadcrumbs) {
        if (breadcrumbs != null)
            this.breadcrumbs = Collections.unmodifiableList(breadcrumbs);
        else
            this.breadcrumbs = null;
    }

    @Deprecated
    public List<N2oPreFilter> getPreFilters() {
        List<N2oPreFilter> filters = new ArrayList<>();
        if (datasources != null)
            datasources.stream()
                    .filter(N2oStandardDatasource.class::isInstance)
                    .forEach(ds -> filters.addAll(Arrays.asList(((N2oStandardDatasource) ds).getFilters())));
        return filters;
    }

    public void addParentRoute(String route, PageContext context) {
        if (this.parentRoutes == null)
            this.parentRoutes = new ArrayList<>();

        if (context.getParentRoutes() != null)
            this.parentRoutes.addAll(context.getParentRoutes());
        this.parentRoutes.add(route);
    }

    @Override
    public boolean isIdentical(CompileContext<Page, N2oPage> obj) {
        if (obj == null || getClass() != obj.getClass())
            return false;
        PageContext that = (PageContext) obj;

        return super.isIdentical(that) &&
                Objects.equals(actions, that.actions) &&
                Objects.equals(breadcrumbs, that.breadcrumbs) &&
                Objects.equals(breadcrumbFromParent, that.breadcrumbFromParent) &&
                Objects.equals(parentRoute, that.parentRoute) &&
                Objects.equals(parentClientWidgetId, that.parentClientWidgetId) &&
                Objects.equals(parentLocalDatasourceId, that.parentLocalDatasourceId) &&
                Objects.equals(parentClientPageId, that.parentClientPageId) &&
                Objects.equals(pageName, that.pageName) &&
                Objects.equals(refreshClientDataSourceIds, that.refreshClientDataSourceIds) &&
                Objects.equals(refreshOnClose, that.refreshOnClose) &&
                Objects.equals(unsavedDataPromptOnClose, that.unsavedDataPromptOnClose) &&
                Objects.equals(datasources, that.datasources) &&
                Objects.equals(parentDatasourceIdsMap, that.parentDatasourceIdsMap) &&
                Objects.equals(clientPageId, that.clientPageId) &&
                Objects.equals(parentWidgetIdDatasourceMap, that.parentWidgetIdDatasourceMap) &&
                Objects.equals(parentTabIds, that.parentTabIds) &&
                Objects.equals(parentRoutes, that.parentRoutes) &&
                Objects.equals(toolbars, that.toolbars);
    }
}
