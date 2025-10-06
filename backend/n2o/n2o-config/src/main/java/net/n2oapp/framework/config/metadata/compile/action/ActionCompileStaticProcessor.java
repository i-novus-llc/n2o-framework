package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.action.N2oAction;
import net.n2oapp.framework.api.metadata.action.N2oMultiAction;
import net.n2oapp.framework.api.metadata.action.ifelse.N2oConditionBranch;
import net.n2oapp.framework.api.metadata.action.ifelse.N2oElseBranchAction;
import net.n2oapp.framework.api.metadata.action.ifelse.N2oElseIfBranchAction;
import net.n2oapp.framework.api.metadata.action.ifelse.N2oIfBranchAction;
import net.n2oapp.framework.api.metadata.aware.*;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.view.ActionBar;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.PageIndexScope;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import net.n2oapp.framework.config.metadata.compile.action.condition.ConditionBranchesScope;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.toolbar.ToolbarPlaceScope;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.register.route.RouteUtil;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import java.util.*;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.metadata.compile.dataprovider.ClientDataProviderUtil.getWidgetIdByComponentScope;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

/**
 * Утилитный класс для компиляции компонентов, использующих действия
 */
public class ActionCompileStaticProcessor {

    private ActionCompileStaticProcessor() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Инициализация метадействий компонента
     *
     * @param source Компонент, содержащая метадействия
     * @param p      Процессор сборки метаданных
     * @return Карта собранных метадействий
     */
    public static MetaActions initMetaActions(ActionBarAware source, CompileProcessor p) {
        MetaActions metaActions = p.getScope(MetaActions.class);
        if (metaActions == null)
            metaActions = new MetaActions();
        if (source.getActions() != null)
            for (ActionBar actionBar : source.getActions())
                metaActions.addAction(actionBar.getId(), actionBar);
        return metaActions;
    }

    /**
     * Компиляция метадействий
     *
     * @param source  Компонент, содержащая метадействия
     * @param context Контекст сборки
     * @param p       Процессор сборки метаданных
     * @param scopes  Метаданные, влияющие на сборку. Должны быть разных классов
     */
    public static void compileMetaActions(ActionBarAware source, CompileContext<?, ?> context, CompileProcessor p,
                                          PageIndexScope pageIndexScope, Object... scopes) {
        if (source.getActions() != null) {
            for (ActionBar a : source.getActions()) {
                initMultiActionIds(a.getN2oActions(), "act_multi", p, pageIndexScope);
                // TODO - don't compile, only init id if necessary
                compileAction(a.getN2oActions(), null, context, p, new ComponentScope(a), pageIndexScope, scopes);
            }
        }
    }

    /**
     * Компиляция действия компонента.
     * Если на входе больше одной модели действия (массив размера 2 и более),
     * то клиентская модель будет мультидействием или действием по условию (if-else)
     *
     * @param n2oActions Список исходных моделей действий
     * @param dsObject   Скомпилированный объект из источника данных, если равен null, то будет вычислен из object-id действия
     * @param context    Контекст сборки
     * @param p          Процессор сборки метаданных
     * @param scopes     Метаданные, влияющие на сборку. Должны быть разных классов
     * @return Скомпилированная клиентская модель действия
     */
    public static Action compileAction(N2oAction[] n2oActions, CompiledObject dsObject,
                                       CompileContext<?, ?> context, CompileProcessor p, Object... scopes) {
        if (isNotEmpty(n2oActions)) {
            List<N2oAction> actions = Arrays.stream(n2oActions)
                    .filter(ActionCompileStaticProcessor::isNotFailConditions).toList();
            if (actions.size() > 1) {
                N2oMultiAction n2oMultiAction = new N2oMultiAction();
                n2oMultiAction.setN2oActions(n2oActions);
                return p.compile(n2oMultiAction, context, initActionObject(actions.get(0), dsObject, p), scopes);
            } else {
                return p.compile(actions.get(0), context,
                        initActionObject(actions.get(0), dsObject, p),
                        initFailConditionBranchesScope(actions.get(0), n2oActions), scopes);
            }
        }
        return null;
    }

    /**
     * Компиляция тулбара
     *
     * @param source               Компонент, содержащий тулбар
     * @param defaultPlaceProperty Настройка расположения тулбара по умолчанию
     * @param context              Контекст сборки
     * @param p                    Процессор сборки метаданных
     * @param scopes               Метаданные, влияющие на сборку. Должны быть разных классов
     * @return Скомпилированная клиентская модель тулбара
     */
    public static Toolbar compileToolbar(ToolbarsAware source, String defaultPlaceProperty, CompileContext<?, ?> context,
                                         CompileProcessor p, Object... scopes) {
        if (source.getToolbars() == null)
            return null;
        Toolbar toolbar = new Toolbar();
        ToolbarPlaceScope toolbarPlaceScope = new ToolbarPlaceScope(p.resolve(property(defaultPlaceProperty), String.class));
        for (N2oToolbar n2oToolbar : source.getToolbars()) {
            toolbar.putAll(p.compile(n2oToolbar, context, toolbarPlaceScope, scopes));
        }
        return toolbar;
    }

    /**
     * Инициализация исходных действий компонента
     *
     * @param source Исходная модель компонента с действиями
     * @param p      Процессор сборки метаданных
     * @return Массив исходных действий или null
     */
    public static N2oAction[] initActions(ActionsAware source, CompileProcessor p) {
        N2oAction[] actions = null;
        if (isNotEmpty(source.getActions()))
            actions = source.getActions();
        else if (source.getActionId() != null) {
            MetaActions metaActions = p.getScope(MetaActions.class);
            actions = (metaActions.get(source.getActionId()) == null ?
                    null : metaActions.get(source.getActionId()).getN2oActions());
        }

        initMultiActionIds(actions, "multi", p, null);
        return actions;
    }

    /**
     * Компиляция действия компонента
     *
     * @param source   Исходная модель компонента с действиями
     * @param context  Контекст сборки
     * @param p        Процессор сборки метаданных
     * @param dsObject Скомпилированный объект из источника данных, если равен null, то будет вычислен из object-id действия
     * @param scopes   Метаданные, влияющие на сборку. Должны быть разных классов
     * @return Скомпилированная клиентская модель действия
     */
    public static Action compileAction(ActionsAware source, CompileContext<?, ?> context, CompileProcessor p,
                                       @Nullable CompiledObject dsObject, Object... scopes) {
        return compileAction(source.getActions(), dsObject, context, p,
                new ComponentScope(source, p.getScope(ComponentScope.class)), scopes);
    }

    /**
     * Проверка, что действие не является `else` или `else-if`
     *
     * @param n2oAction Действие
     * @return true - если действие не является `else` или `else-if`, иначе - false
     */
    public static boolean isNotFailConditions(N2oAction n2oAction) {
        return !(n2oAction instanceof N2oElseIfBranchAction) && !(n2oAction instanceof N2oElseBranchAction);
    }

    /**
     * Наполнение действия `if` информацией о его `else` и `else-if` ветках
     *
     * @param n2oAction  Действие
     * @param n2oActions Список действий цепочки
     * @return Скоуп действия `if`, заполненный информацией о его `else` и `else-if` ветках
     */
    public static ConditionBranchesScope initFailConditionBranchesScope(N2oAction n2oAction, N2oAction[] n2oActions) {
        if (!(n2oAction instanceof N2oIfBranchAction))
            return null;
        List<N2oConditionBranch> failBranches = new ArrayList<>();

        int i = 0;
        while (!n2oAction.equals(n2oActions[i]))
            i++;
        i++;

        while (i < n2oActions.length && !isNotFailConditions(n2oActions[i])) {
            failBranches.add((N2oConditionBranch) n2oActions[i]);
            i++;
        }

        return new ConditionBranchesScope(failBranches);
    }

    /**
     * Инициализация маппингов для query и path параметров
     *
     * @param pathParams   Исходные модели параметров пути
     * @param queryParams  Исходные модели параметров запроса
     * @param pathMapping  Map, в которую будет произведена сборка собранных параметров пути
     * @param queryMapping Map, в которую будет произведена сборка собранных параметров запроса
     * @param p            Процессор сборки метаданных
     */
    public static void initMappings(N2oParam[] pathParams, N2oParam[] queryParams,
                                    Map<String, ModelLink> pathMapping, Map<String, ModelLink> queryMapping,
                                    CompileProcessor p) {
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        ReduxModelEnum defaultModel = getModelFromComponentScope(p);
        if (widgetScope != null) {
            String defaultClientWidgetId = getDefaultClientWidgetId(p);
            if (pathParams != null)
                for (N2oParam pathParam : pathParams)
                    pathMapping.put(pathParam.getName(), initParamModelLink(pathParam, defaultClientWidgetId, defaultModel, p));
            if (queryParams != null)
                for (N2oParam queryParam : queryParams)
                    queryMapping.put(queryParam.getName(), initParamModelLink(queryParam, defaultClientWidgetId, defaultModel, p));
        } else {
            if (pathParams != null)
                for (N2oParam pathParam : pathParams)
                    pathMapping.put(pathParam.getName(), initParamModelLink(pathParam, null, defaultModel, p));
            if (queryParams != null)
                for (N2oParam queryParam : queryParams)
                    queryMapping.put(queryParam.getName(), initParamModelLink(queryParam, null, defaultModel, p));
        }
    }

    /**
     * Сборка родительских маппингов путей
     *
     * @param routeScope Информация о родительском маршруте
     * @param url        Маршрут
     * @return Map родительских маппингов путей
     */
    public static Map<String, ModelLink> initParentRoutePathMappings(ParentRouteScope routeScope, String url) {
        Map<String, ModelLink> parentRoutePathMappings = new HashMap<>();
        if (routeScope != null && routeScope.getPathMapping() != null) {
            List<String> pathParams = RouteUtil.getParams(url);
            routeScope.getPathMapping().forEach((k, v) -> {
                if (pathParams.contains(k)) {
                    parentRoutePathMappings.put(k, v);
                }
            });
        }
        return parentRoutePathMappings;
    }

    /**
     * Инициализация модели ссылки параметра
     *
     * @param param                 Исходная модель параметра
     * @param defaultClientWidgetId Идентификатор виджета по умолчанию
     * @param defaultModel          Модель по умолчанию
     * @param p                     Процессор сборки метаданных
     * @return Модель ссылки параметра
     */
    public static ModelLink initParamModelLink(N2oParam param, String defaultClientWidgetId, ReduxModelEnum defaultModel, CompileProcessor p) {
        PageScope pageScope = p.getScope(PageScope.class);
        String widgetId = castDefault(getClientDatasourceId(param.getRefWidgetId(), p), defaultClientWidgetId);

        String clientDatasourceId;
        if (pageScope == null) {
            clientDatasourceId = param.getDatasourceId() != null ? param.getDatasourceId() : widgetId;
            if (clientDatasourceId == null)
                clientDatasourceId = getLocalDatasourceId(p);
        } else {
            clientDatasourceId = getClientDatasourceId(
                    param.getDatasourceId() != null ? param.getDatasourceId() : getLocalDatasourceId(p), p);
        }

        ModelLink link = new ModelLink(castDefault(param.getModel(), defaultModel), clientDatasourceId);
        link.setValue(p.resolveJS(param.getValue()));
        return link;
    }

    /**
     * Получение идентификатора клиентского виджета по умолчанию
     *
     * @param p Процессор сборки метаданных
     * @return Идентификатор клиентского виджета по умолчанию
     */
    public static String getDefaultClientWidgetId(CompileProcessor p) {
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        String widgetIdByComponentScope = getClientDatasourceId(getWidgetIdByComponentScope(p), p);
        return widgetIdByComponentScope != null ? widgetIdByComponentScope : widgetScope.getClientWidgetId();
    }

    public static ReduxModelEnum getModelFromComponentScope(CompileProcessor p) {
        ComponentScope componentScope = p.getScope(ComponentScope.class);
        if (componentScope != null) {
            ModelAware modelAware = componentScope.unwrap(ModelAware.class);
            if (modelAware != null && modelAware.getModel() != null) {
                return modelAware.getModel();
            }
        }
        return ReduxModelEnum.RESOLVE;
    }

    /**
     * Получение идентификатора локального источника данных действия (из компонента или из его родителей)
     *
     * @param p Процессор сборки метаданных
     * @return Идентификатор локального источника данных действия
     */
    public static String getLocalDatasourceId(CompileProcessor p) {
        String datasourceId = ComponentScope.getFirstNotNull(p.getScope(ComponentScope.class),
                DatasourceIdAware.class, DatasourceIdAware::getDatasourceId);
        if (datasourceId != null)
            return datasourceId;
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (widgetScope != null)
            return widgetScope.getDatasourceId();
        return null;
    }

    /**
     * Получение модели действия (из компонента или из его родителей)
     *
     * @param p Процессор сборки метаданных
     * @return Модель действия из ComponentScope или модель resolve, если модель из скоупа равна null
     */
    public static ReduxModelEnum getLocalModel(CompileProcessor p) {
        ComponentScope componentScope = p.getScope(ComponentScope.class);
        return castDefault(ComponentScope.getFirstNotNull(componentScope, ModelAware.class, ModelAware::getModel), ReduxModelEnum.RESOLVE);
    }

    private static CompiledObject initActionObject(N2oAction n2oAction, CompiledObject dsObject, CompileProcessor p) {
        if (dsObject != null)
            return dsObject;
        if (n2oAction.getObjectId() != null) {
            return p.getCompiled(new ObjectContext(n2oAction.getObjectId()));
        }
        return p.getScope(CompiledObject.class);
    }

    private static void initMultiActionIds(N2oAction[] actions, String prefix, CompileProcessor p, PageIndexScope pageIndexScope) {
        if (ArrayUtils.getLength(actions) > 1) {
            PageIndexScope indexScope = castDefault(pageIndexScope, () -> p.getScope(PageIndexScope.class));
            Arrays.stream(actions).filter(ActionCompileStaticProcessor::isNotFailConditions)
                    .forEach(action -> action.setId(castDefault(action.getId(), prefix + indexScope.get())));
        }
    }
}
