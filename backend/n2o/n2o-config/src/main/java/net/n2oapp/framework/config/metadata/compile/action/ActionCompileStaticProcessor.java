package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.aware.ActionBarAware;
import net.n2oapp.framework.api.metadata.aware.ActionsAware;
import net.n2oapp.framework.api.metadata.aware.ToolbarsAware;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.event.action.ifelse.N2oConditionBranch;
import net.n2oapp.framework.api.metadata.event.action.ifelse.N2oElseBranchAction;
import net.n2oapp.framework.api.metadata.event.action.ifelse.N2oElseIfBranchAction;
import net.n2oapp.framework.api.metadata.event.action.ifelse.N2oIfBranchAction;
import net.n2oapp.framework.api.metadata.global.view.ActionBar;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.*;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.action.multi.MultiAction;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.action.condition.ConditionBranchesScope;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.compile.toolbar.ToolbarPlaceScope;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;
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
        if (source.getActions() != null) {
            for (ActionBar actionBar : source.getActions()) {
                metaActions.addAction(actionBar.getId(), actionBar);
            }
        }
        return metaActions;
    }

    /**
     * Передача свойств метадействия в кнопки тулбара
     *
     * @param source      Компонент, содержащая метадействия и тулбар
     * @param metaActions Карта собранных действий
     */
    public static <T extends ActionBarAware & ToolbarsAware> void actionsToToolbar(T source, MetaActions metaActions) {
        if (source.getActions() == null || source.getToolbars() == null)
            return;
        for (N2oToolbar toolbar : source.getToolbars()) {
            if (toolbar.getItems() == null) continue;
            ToolbarItem[] toolbarItems = toolbar.getItems();
            copyActionForToolbarItem(metaActions, toolbarItems);
        }
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
                                          Object... scopes) {
        if (source.getActions() != null) {
            for (ActionBar a : source.getActions()) {
                a.setModel(p.cast(a.getModel(), ReduxModel.resolve));
                if (isNotEmpty(a.getN2oActions())) {
                    initMultiActionIds(a.getN2oActions(), "act_multi", p);
                    Arrays.stream(a.getN2oActions())
                            .forEach(n2oAction ->
                                    p.compile(n2oAction, context, new ComponentScope(a), scopes));
                }
            }
        }
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
            toolbar.putAll(p.compile(n2oToolbar, context, new IndexScope(), toolbarPlaceScope, scopes));
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

        initMultiActionIds(actions, "multi", p);
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
        N2oAction[] n2oActions = source.getActions();
        if (n2oActions == null)
            return null;

        List<Action> actions = Arrays.stream(n2oActions)
                .filter(ActionCompileStaticProcessor::isNotFailConditions)
                .map(n2oAction -> (Action) p.compile(n2oAction, context,
                        initActionObject(n2oAction, dsObject, p),
                        initFailConditionBranchesScope(n2oAction, n2oActions),
                        new ComponentScope(source, p.getScope(ComponentScope.class)), scopes))
                .collect(Collectors.toList());

        if (actions.size() == 0)
            return null;
        if (actions.size() > 1) {
           return new MultiAction(actions, p);
        }
        return actions.get(0);
    }

    private static ConditionBranchesScope initFailConditionBranchesScope(N2oAction n2oAction, N2oAction[] n2oActions) {
        if (!(n2oAction instanceof N2oIfBranchAction))
            return null;
        List<N2oConditionBranch> failBranches = new ArrayList<>();
        for (N2oAction act: n2oActions) {
            if (act instanceof N2oIfBranchAction && !act.equals(n2oAction))
                break;
            if (act instanceof N2oElseIfBranchAction || act instanceof N2oElseBranchAction)
                failBranches.add((N2oConditionBranch) act);
        }

        return new ConditionBranchesScope(failBranches);
    }

    private static CompiledObject initActionObject(N2oAction n2oAction, CompiledObject dsObject, CompileProcessor p) {
        if (dsObject != null)
            return dsObject;
        if (n2oAction.getObjectId() != null) {
            return p.getCompiled(new ObjectContext(n2oAction.getObjectId()));
        }
        return p.getScope(CompiledObject.class);
    }

    private static void initMultiActionIds(N2oAction[] actions, String prefix, CompileProcessor p) {
        if (ArrayUtils.getLength(actions) > 1) {
            IndexScope indexScope = new IndexScope();
            Arrays.stream(actions).filter(ActionCompileStaticProcessor::isNotFailConditions)
                    .forEach(action -> action.setId(p.cast(action.getId(), prefix + indexScope.get())));
        }
    }

    private static void copyActionForToolbarItem(Map<String, ActionBar> actionMap, ToolbarItem[] toolbarItems) {
        for (ToolbarItem item : toolbarItems) {
            if (item instanceof N2oButton) {
                copyAction((N2oButton) item, actionMap);
            } else if (item instanceof N2oSubmenu) {
                for (N2oButton subItem : ((N2oSubmenu) item).getMenuItems()) {
                    copyAction(subItem, actionMap);
                }
            } else if (item instanceof N2oGroup) {
                copyActionForToolbarItem(actionMap, ((N2oGroup) item).getItems());
            }
        }
    }

    private static void copyAction(N2oButton item, Map<String, ActionBar> actionMap) {
        if (isEmpty(item.getActions()) && item.getActionId() != null) {
            ActionBar actionsBar = actionMap.get(item.getActionId());
            if (actionsBar == null) {
                throw new N2oException(String.format("Toolbar has reference to nonexistent action by actionId %s!", item.getActionId()));
            }
            if (item.getModel() == null)
                item.setModel(actionsBar.getModel());
            if (item.getDatasourceId() == null)
                item.setDatasourceId(actionsBar.getDatasourceId());
            if (item.getLabel() == null)
                item.setLabel(actionsBar.getLabel());
            if (item.getIcon() == null)
                item.setIcon(actionsBar.getIcon());
        }
    }

    private static boolean isNotFailConditions(N2oAction n2oAction) {
        return !(n2oAction instanceof N2oElseIfBranchAction) && !(n2oAction instanceof N2oElseBranchAction);
    }
}
