package net.n2oapp.framework.config.metadata.compile.context;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.event.action.SubmitActionType;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.CopyMode;
import net.n2oapp.framework.api.metadata.meta.Breadcrumb;
import net.n2oapp.framework.api.metadata.meta.page.Page;

import java.util.*;

@Getter
@Setter
public class PageContext extends BaseCompileContext<Page, N2oPage> {

    private List<Breadcrumb> breadcrumbs;

    /**
     * Задан ли бредкрамб на родительской странице
     */
    private Boolean breadcrumbFromParent;
    /**
     * Операция на кнопке отправки формы
     */
    private String submitOperationId;
    /**
     * Модель данных на кнопке отправки формы
     */
    private ReduxModel submitModel;
    /**
     * Заголовок кнопки отправки формы
     */
    private String submitLabel;
    /**
     * Тип действия кнопки отправки формы
     */
    private SubmitActionType submitActionType;
    /**
     * Модель, которая будет скопирована
     */
    private ReduxModel copyModel;
    /**
     * Идентификатор источника данных из которого будут копироваться данные
     */
    private String copyDatasourceId;
    /**
     * Идентификатор копируемого поля источника
     */
    private String copyFieldId;
    /**
     * Модель, в которую будут скопированы данные
     */
    private ReduxModel targetModel;
    /**
     * Идентификатор источника данных, в который будут скопированы данные
     */
    private String targetDatasourceId;
    /**
     * Идентификатор поля целевого виджета, в которое будут скопированы данные
     */
    private String targetFieldId;
    /**
     * Тип слияния при копировании данных
     */
    private CopyMode copyMode;
    /**
     * Маршрут родителя
     */
    private String parentRoute;
    /**
     * Родительский виджет (исходный), в котором находилось действие
     */
    private String parentWidgetId;
    /**
     * Родительский виджет (клиентский), в котором находилось действие
     */
    @Deprecated
    private String parentClientWidgetId;
    /**
     * Родительский глобальный источник данных, в котором находилось действие
     */
    private String parentClientDatasourceId;
    /**
     * Родительский локальный источник данных, в котором находилось действие
     */
    private String parentLocalDatasourceId;
    /**
     * Родительская страница (клиентский), в которой находилось действие
     */
    private String parentClientPageId;
    /**
     * Наименование страницы
     */
    private String pageName;
    /**
     * Закрыть окно после успешной отправки формы
     */
    private Boolean closeOnSuccessSubmit;
    /**
     * Показать сообщение об успешном выполнении действия
     */
    private Boolean submitMessageOnSuccess;
    /**
     * Показать сообщение о неудачном выполнении действия
     */
    private Boolean submitMessageOnFail;
    /**
     * Обновить данные виджета после успешной отправки формы
     */
    private Boolean refreshOnSuccessSubmit;
    /**
     * Идентификаторы источников данных, которые необходимо обновить после успешной отправки формы
     */
    private List<String> refreshClientDataSourceIds;
    /**
     * Обновить данные родительского виджета после закрытия страницы
     */
    private Boolean refreshOnClose;
    /**
     * Направить на URL адрес после успешной отправки формы
     */
    private String redirectUrlOnSuccessSubmit;
    /**
     * Сценарий перенаправления после успешной отправки формы
     */
    private Target redirectTargetOnSuccessSubmit;
    /**
     * Предупредить о несохраненных данных на форме при закрытии?
     */
    private Boolean unsavedDataPromptOnClose;
    /**
     * Список источников данных открываемой страницы
     */
    private List<N2oAbstractDatasource> datasources;
    /**
     * Множество источников данных родительской страницы
     */
    private Map<String, N2oAbstractDatasource> parentDatasources;

    /**
     * Клиентский идентификатор страницы
     */
    private String clientPageId;

    /**
     * Сооответствия идентификаторов виджета с источником данных в родительском виджете
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
}
