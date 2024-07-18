package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.action.N2oAction;
import net.n2oapp.framework.api.metadata.action.N2oMultiAction;
import net.n2oapp.framework.api.metadata.action.ifelse.N2oConditionBranch;
import net.n2oapp.framework.api.metadata.action.ifelse.N2oElseBranchAction;
import net.n2oapp.framework.api.metadata.action.ifelse.N2oElseIfBranchAction;
import net.n2oapp.framework.api.metadata.action.ifelse.N2oIfBranchAction;
import net.n2oapp.framework.api.metadata.aware.ActionBarAware;
import net.n2oapp.framework.api.metadata.aware.ActionsAware;
import net.n2oapp.framework.api.metadata.aware.ToolbarsAware;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.ActionBar;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.PageIndexScope;
import net.n2oapp.framework.config.metadata.compile.action.condition.ConditionBranchesScope;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.compile.toolbar.ToolbarPlaceScope;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
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
        N2oAction[] n2oActions = source.getActions();
        if (n2oActions == null)
            return null;

        return compileAction(source.getActions(), dsObject, context, p,
                new ComponentScope(source, p.getScope(ComponentScope.class)), scopes);
    }

    public static ConditionBranchesScope initFailConditionBranchesScope(N2oAction n2oAction, N2oAction[] n2oActions) {
        if (!(n2oAction instanceof N2oIfBranchAction))
            return null;
        List<N2oConditionBranch> failBranches = new ArrayList<>();
        for (N2oAction act : n2oActions) {
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

    private static void initMultiActionIds(N2oAction[] actions, String prefix, CompileProcessor p, PageIndexScope pageIndexScope) {
        if (ArrayUtils.getLength(actions) > 1) {
            PageIndexScope indexScope = castDefault(pageIndexScope, () -> p.getScope(PageIndexScope.class));
            Arrays.stream(actions).filter(ActionCompileStaticProcessor::isNotFailConditions)
                    .forEach(action -> action.setId(castDefault(action.getId(), prefix + indexScope.get())));
        }
    }

    public static boolean isNotFailConditions(N2oAction n2oAction) {
        return !(n2oAction instanceof N2oElseIfBranchAction) && !(n2oAction instanceof N2oElseBranchAction);
    }
}
