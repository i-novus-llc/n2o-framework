package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.*;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.control.ValidationType;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Condition;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.PerformButton;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetObjectScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static net.n2oapp.framework.api.StringUtils.isLink;
import static net.n2oapp.framework.api.StringUtils.unwrapLink;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.*;

/**
 * Компиляция кнопки
 */
@Component
public class PerformButtonCompiler extends BaseButtonCompiler<N2oButton, PerformButton> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oButton.class;
    }

    @Override
    public PerformButton compile(N2oButton source, CompileContext<?, ?> context, CompileProcessor p) {
        PerformButton button = new PerformButton();
        IndexScope idx = p.getScope(IndexScope.class);
        initItem(button, source, idx, context, p);
        button.setId(p.cast(source.getId(), source.getActionId(), "menuItem" + idx.get()));
        source.setId(button.getId());
        button.setSrc(p.cast(source.getSrc(), p.resolve(property("n2o.api.action.button.src"), String.class)));
        button.setRounded(p.cast(source.getRounded(), false));
        if (source.getValidate() != null) {
            button.setValidate(source.getValidate().getValue());
            initValidate(source, button, context, p);
        }
        button.setProperties(p.mapAttributes(source));

        WidgetObjectScope widgetObjectScope = p.getScope(WidgetObjectScope.class);

        if (source.getWidgetId() == null && widgetObjectScope != null && widgetObjectScope.size() == 1)
            source.setWidgetId(widgetObjectScope.keySet().iterator().next());

        Action action = compileAction(source, button, context, p);
        button.setAction(action);

        initLinkAction(button);

        String objectId = null;
        if (action instanceof InvokeAction)
            objectId = ((InvokeAction) action).getObjectId();
        CompiledObject compiledObject = getCompiledObject(p, objectId);
        button.setConfirm(compileConfirm(source, action, context, p, compiledObject));

        if (source.getModel() == null)
            source.setModel(ReduxModel.RESOLVE);
        compileConditionsAndDependencies(source, button, context, p);

        return button;
    }

    private CompiledObject getCompiledObject(CompileProcessor p, String objectId) {
        if (objectId != null) {
            WidgetObjectScope widgetObjectScope = p.getScope(WidgetObjectScope.class);
            if (widgetObjectScope != null && widgetObjectScope.containsKey(objectId))
                return widgetObjectScope.getObject(objectId);
        }
        return p.getScope(CompiledObject.class);
    }

    private void initLinkAction(PerformButton button) {
        if (button.getAction() instanceof LinkAction) {
            LinkAction linkAction = ((LinkAction) button.getAction());
            button.setUrl(linkAction.getUrl());
            button.setTarget(linkAction.getTarget());
            if (linkAction.getPathMapping() != null)
                button.setPathMapping(new StrictMap<>(linkAction.getPathMapping()));
            if (linkAction.getQueryMapping() != null)
                button.setQueryMapping(new StrictMap<>(linkAction.getQueryMapping()));
        }
    }

    private void initValidate(N2oButton source, PerformButton button, CompileContext<?, ?> context, CompileProcessor p) {
        if (ValidateType.WIDGET.getId().equals(button.getValidate()))
            button.setValidateWidgetId(initWidgetId(source, context, p));
        else if (ValidateType.PAGE.getId().equals(button.getValidate()))
            button.setValidatePageId(p.getScope(PageScope.class).getPageId());
    }

    private Confirm compileConfirm(N2oButton source,
                                   Action action, CompileContext<?, ?> context,
                                   CompileProcessor p, CompiledObject object) {
        CompiledObject.Operation operation = getOperation(action, object);
        boolean operationConfirm = operation != null && operation.getConfirm() != null && operation.getConfirm();
        if (source.getConfirm() != null) {
            Object condition = p.resolveJS(source.getConfirm(), Boolean.class);
            if (condition instanceof Boolean) {
                if (!((Boolean) condition || operationConfirm))
                    return null;
                return initConfirm(source, p, context, operation, true);
            }
            if (condition instanceof String) {
                return initConfirm(source, p, context, operation, condition);
            }
        }
        if (operationConfirm)
            return initConfirm(source, p, context, operation, true);
        return null;
    }

    private Confirm initConfirm(N2oButton source, CompileProcessor p, CompileContext<?, ?> context, CompiledObject.Operation operation, Object condition) {
        Confirm confirm = new Confirm();
        confirm.setTitle(p.cast(source.getConfirmTitle(), operation != null ? operation.getFormSubmitLabel() : null, p.getMessage("n2o.confirm.title")));
        confirm.setMode(p.cast(source.getConfirmType(), ConfirmType.modal));
        confirm.setOkLabel(p.cast(source.getConfirmOkLabel(), p.getMessage("n2o.confirm.default.okLabel")));
        confirm.setCancelLabel(p.cast(source.getConfirmCancelLabel(), p.getMessage("n2o.confirm.default.cancelLabel")));
        confirm.setText(initExpression(
                p.cast(source.getConfirmText(), operation != null ? operation.getConfirmationText() : null, p.getMessage("n2o.confirm.text"))));
        confirm.setCondition(initConfirmCondition(condition));
        if (StringUtils.isJs(confirm.getText()) || StringUtils.isJs(confirm.getCondition())) {
            String widgetId = initWidgetId(source, context, p);
            ReduxModel reduxModel = source.getModel();
            confirm.setModelLink(new ModelLink(reduxModel == null ? ReduxModel.RESOLVE : reduxModel, widgetId).getBindLink());
        }
        return confirm;
    }

    private String initConfirmCondition(Object condition) {
        if (condition instanceof Boolean)
            return Placeholders.js(Boolean.toString(true));
        return initExpression((String) condition);
    }

    private String initExpression(String attr) {
        if (StringUtils.hasLink(attr)) {
            Set<String> links = StringUtils.collectLinks(attr);
            String text = js("'" + attr + "'");
            for (String link : links) {
                text = text.replace(ref(link), "' + this." + link + " + '");
            }
            return text;
        }
        return attr;
    }

    private CompiledObject.Operation getOperation(Action action, CompiledObject compiledObject) {
        CompiledObject.Operation operation = null;
        if (action != null) {
            if (action instanceof InvokeAction) {

                operation = compiledObject != null && compiledObject.getOperations() != null
                        && compiledObject.getOperations().containsKey(((InvokeAction) action).getOperationId()) ?
                        compiledObject.getOperations().get(((InvokeAction) action).getOperationId()) : null;
            }
            //todo если это invoke-action, то из action в объекте должны доставаться поля action.getName(), confirmationText
        }
        return operation;
    }

    private Action compileAction(N2oButton source, PerformButton button, CompileContext<?, ?> context, CompileProcessor p) {
        if (source.getAction() != null) {
            N2oAction butAction = source.getAction();
            String objectId = butAction.getObjectId() == null ? source.getWidgetId() : butAction.getObjectId();
            CompiledObject compiledObject = getCompiledObject(p, objectId);
            butAction.setId(p.cast(butAction.getId(), button.getId()));
            return p.compile(butAction, context, compiledObject, new ComponentScope(source));
        }
        if (source.getActionId() != null) {
            MetaActions metaActions = p.getScope(MetaActions.class);
            return metaActions.get(source.getActionId());
        }
        return null;
    }

    private String initWidgetId(N2oButton source, CompileContext<?, ?> context, CompileProcessor p) {
        PageScope pageScope = p.getScope(PageScope.class);
        if (source.getWidgetId() != null) {
            return pageScope == null ? source.getWidgetId() : pageScope.getGlobalWidgetId(source.getWidgetId());//todo обсудить
        }
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (widgetScope != null) {
            return widgetScope.getClientWidgetId();
        } else if (context instanceof PageContext && ((PageContext) context).getResultWidgetId() != null) {
            return pageScope.getGlobalWidgetId(((PageContext) context).getResultWidgetId());
        } else {
            throw new N2oException("Unknown widgetId for invoke action!");
        }
    }

    /**
     * Компиляция условий и зависимостей кнопки
     *
     * @param button  Клиентская модель кнопки
     * @param source  Исходная модель кнопки
     * @param context Контекст сборки метаданных
     * @param p       Процессор сборки метаданных
     */
    protected void compileConditionsAndDependencies(N2oButton source, PerformButton button,
                                                    CompileContext<?, ?> context, CompileProcessor p) {
        String widgetId = initWidgetId(source, context, p);
        List<Condition> enabledConditions = new ArrayList<>();

        if (source.getVisibilityConditions() != null)
            button.getConditions().put(ValidationType.visible,
                    compileConditions(source.getVisibilityConditions(), source.getModel(), widgetId));
        if (source.getEnablingConditions() != null)
            enabledConditions.addAll(compileConditions(source.getEnablingConditions(), source.getModel(), widgetId));

        ComponentScope componentScope = p.getScope(ComponentScope.class);

        Condition emptyModelCondition = enabledByEmptyModelCondition(source, widgetId, componentScope, p);
        if (emptyModelCondition != null)
            enabledConditions.add(emptyModelCondition);

        if (!enabledConditions.isEmpty()) {
            button.getConditions().put(ValidationType.enabled, enabledConditions);
        }

        if (source.getDependencies() != null)
            compileDependencies(source.getDependencies(), button, widgetId, source.getModel(), p);

        if (componentScope != null && componentScope.unwrap(N2oCell.class) != null) {
            button.setVisible(p.resolveJS(source.getVisible()));
            button.setEnabled(p.resolveJS(source.getEnabled()));
        } else {
            if (isLink(source.getVisible()))
                compileLinkCondition(button, widgetId, ValidationType.visible, source.getVisible(), source.getModel());
            else
                button.setVisible(p.resolveJS(source.getVisible(), Boolean.class));

            if (isLink(source.getEnabled()))
                compileLinkCondition(button, widgetId, ValidationType.enabled, source.getEnabled(), source.getModel());
            else
                button.setEnabled(p.resolveJS(source.getEnabled(), Boolean.class));
        }
    }

    /**
     * Получение условия доступности кнопки при пустой модели
     *
     * @param source         Исходная модель кнопки
     * @param widgetId       Идентификатор виджета, к которому относится кнопка
     * @param componentScope Родительский компонент
     * @param p              Процессор сборки метаданных
     * @return Условие доступности кнопки при пустой модели
     */
    private Condition enabledByEmptyModelCondition(N2oButton source, String widgetId, ComponentScope componentScope, CompileProcessor p) {
        DisableOnEmptyModelType disableOnEmptyModel = p.cast(source.getDisableOnEmptyModel(),
                p.resolve(property("n2o.api.button.disable_on_empty_model"), DisableOnEmptyModelType.class));
        if (DisableOnEmptyModelType.FALSE.equals(disableOnEmptyModel)) return null;

        boolean parentIsNotCell = componentScope == null || componentScope.unwrap(N2oCell.class) == null;
        boolean autoDisableCondition = DisableOnEmptyModelType.AUTO.equals(disableOnEmptyModel) &&
                (ReduxModel.RESOLVE.equals(source.getModel()) || ReduxModel.MULTI.equals(source.getModel())) &&
                parentIsNotCell;

        if (DisableOnEmptyModelType.TRUE.equals(disableOnEmptyModel) || autoDisableCondition) {
            Condition condition = new Condition();
            condition.setExpression("!_.isEmpty(this)");
            condition.setModelLink(new ModelLink(source.getModel(), widgetId).getBindLink());
            return condition;
        }
        return null;
    }

    private List<Condition> compileConditions(N2oButtonCondition[] conditions, ReduxModel model, String widgetId) {
        List<Condition> result = new ArrayList<>();
        for (N2oButtonCondition n2oCondition : conditions) {
            Condition condition = new Condition();
            condition.setExpression(n2oCondition.getExpression().trim());
            condition.setModelLink(new ModelLink(model, widgetId).getBindLink());
            result.add(condition);
        }
        return result;
    }

    private void compileLinkCondition(PerformButton button, String widgetId, ValidationType type,
                                      String linkCondition, ReduxModel model) {
        Condition condition = new Condition();
        condition.setExpression(unwrapLink(linkCondition));
        condition.setModelLink(new ModelLink(model, widgetId).getBindLink());
        if (!button.getConditions().containsKey(type))
            button.getConditions().put(type, new ArrayList<>());
        button.getConditions().get(type).add(condition);
    }

    private void compileDependencies(N2oButton.Dependency[] dependencies, PerformButton button, String widgetId,
                                     ReduxModel buttonModel, CompileProcessor p) {
        for (N2oButton.Dependency d : dependencies) {
            ValidationType validationType = null;
            if (d instanceof N2oButton.EnablingDependency)
                validationType = ValidationType.enabled;
            else if (d instanceof N2oButton.VisibilityDependency)
                validationType = ValidationType.visible;

            compileCondition(d, button, validationType, widgetId, buttonModel, p);
        }
    }

    private void compileCondition(N2oButton.Dependency dependency, PerformButton button, ValidationType validationType,
                                  String widgetId, ReduxModel buttonModel, CompileProcessor p) {
        String refWidgetId = null;
        if (dependency.getRefWidgetId() != null) {
            PageScope pageScope = p.getScope(PageScope.class);
            if (pageScope != null) {
                refWidgetId = pageScope.getGlobalWidgetId(dependency.getRefWidgetId());
            }
        }
        refWidgetId = p.cast(refWidgetId, widgetId);
        ReduxModel refModel = p.cast(dependency.getRefModel(), buttonModel, ReduxModel.RESOLVE);

        Condition condition = new Condition();
        condition.setExpression(ScriptProcessor.resolveFunction(dependency.getValue()));
        condition.setModelLink(new ModelLink(refModel, refWidgetId, null).getBindLink());
        if (dependency instanceof N2oButton.EnablingDependency)
            condition.setMessage(((N2oButton.EnablingDependency) dependency).getMessage());

        if (!button.getConditions().containsKey(validationType))
            button.getConditions().put(validationType, new ArrayList<>());
        button.getConditions().get(validationType).add(condition);
    }
}
