package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
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
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        initDefaults(source, context, p);
        PerformButton button = new PerformButton();
        IndexScope idx = p.getScope(IndexScope.class);
        compileBase(button, source, idx, context, p);
        button.setSrc(source.getSrc());
        button.setRounded(source.getRounded());
        button.setValidate(compileValidate(source, p));
        Action action = compileAction(source, context, p);
        button.setAction(action);
        compileLink(button);
        button.setConfirm(compileConfirm(source, action, p));
        compileDependencies(source, button, context, p);
        return button;
    }

    private CompiledObject.Operation getOperation(N2oButton source, CompileProcessor p, Action action) {
        CompiledObject.Operation operation = null;
        if (action != null) {
            if (action instanceof InvokeAction) {
                CompiledObject compiledObject = getCompiledObject(p, ((InvokeAction) action).getObjectId(), source.getWidgetId());

                operation = compiledObject != null && compiledObject.getOperations() != null
                        && compiledObject.getOperations().containsKey(((InvokeAction) action).getOperationId()) ?
                        compiledObject.getOperations().get(((InvokeAction) action).getOperationId()) : null;
            }
            //todo если это invoke-action, то из action в объекте должны доставаться поля action.getName(), confirmationText
        }
        return operation;
    }

    protected void initDefaults(N2oButton source, CompileContext<?, ?> context, CompileProcessor p) {
        source.setId(p.cast(source.getId(), source.getActionId()));
        super.initDefaults(source, context, p);
        source.setSrc(p.cast(source.getSrc(), p.resolve(property("n2o.api.action.button.src"), String.class)));
        source.setRounded(p.cast(source.getRounded(), false));
        boolean validate = p.cast(source.getValidate(), true);
        source.setValidate(validate);
        String datasource = initDatasource(source, p);
        source.setDatasource(datasource);
        source.setModel(p.cast(source.getModel(), ReduxModel.RESOLVE));
        source.setValidateDatasources(initValidateDatasources(source, p, validate, datasource));
        source.setAction(initAction(source, p));

        source.setConfirmType(p.cast(source.getConfirmType(), ConfirmType.modal));
        source.setConfirmOkLabel(p.cast(source.getConfirmOkLabel(), p.getMessage("n2o.confirm.default.okLabel")));
        source.setConfirmCancelLabel(p.cast(source.getConfirmCancelLabel(), p.getMessage("n2o.confirm.default.cancelLabel")));
    }

    private N2oAction initAction(N2oButton source, CompileProcessor p) {
        if (source.getAction() != null)
            return source.getAction();
        MetaActions metaActions = p.getScope(MetaActions.class);
        return metaActions.get(source.getActionId()) == null ? null : metaActions.get(source.getActionId()).getAction();
    }

    private List<String> compileValidate(N2oButton source, CompileProcessor p) {
        if (!source.getValidate())
            return null;
        PageScope pageScope = p.getScope(PageScope.class);
        return Stream.of(source.getValidateDatasources())
                .map(ds -> pageScope != null ? pageScope.getGlobalDatasourceId(ds) : ds)
                .collect(Collectors.toList());
    }

    private String[] initValidateDatasources(N2oButton source, CompileProcessor p, boolean validate, String datasource) {
        if (validate) {
            if (source.getValidateDatasources() != null)
                return source.getValidateDatasources();
            if (source.getDatasource() != null)
                return new String[]{datasource};
        }
        return null;
    }

    private CompiledObject getCompiledObject(CompileProcessor p, String objectId, String widgetId) {
        if (objectId != null) {
            return p.getCompiled(new ObjectContext(objectId));
        }
        return p.getScope(CompiledObject.class);
    }

    private void compileLink(PerformButton button) {
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

    private Confirm compileConfirm(N2oButton source,
                                   Action action,
                                   CompileProcessor p) {
        CompiledObject.Operation operation = getOperation(source, p, action);
        if ((source.getConfirm() == null || !source.getConfirm()) &&
                (source.getConfirm() != null || operation == null || operation.getConfirm() == null || !operation.getConfirm())) {
            return null;
        }
        Confirm confirm = new Confirm();
        confirm.setMode(source.getConfirmType());
        confirm.setText(p.cast(source.getConfirmText(), (operation != null ? operation.getConfirmationText() : null), p.getMessage("n2o.confirm.text")));
        confirm.setTitle(p.cast(source.getConfirmTitle(), (operation != null ? operation.getFormSubmitLabel() : null), p.getMessage("n2o.confirm.title")));
        confirm.setOkLabel(source.getConfirmOkLabel());
        confirm.setCancelLabel(source.getConfirmCancelLabel());
        if (StringUtils.hasLink(confirm.getText())) {
            Set<String> links = StringUtils.collectLinks(confirm.getText());
            String text = js("'" + confirm.getText() + "'");
            for (String link : links) {
                text = text.replace(ref(link), "' + this." + link + " + '");
            }
            confirm.setText(text);
        }
        if (StringUtils.isJs(confirm.getText())) {
            String clientDatasource = p.getScope(PageScope.class).getGlobalDatasourceId(source.getDatasource());
            ReduxModel reduxModel = source.getModel();
            confirm.setModelLink(new ModelLink(reduxModel == null ? ReduxModel.RESOLVE : reduxModel, clientDatasource).getBindLink());
        }
        return confirm;
    }

    private Action compileAction(N2oButton source, CompileContext<?, ?> context, CompileProcessor p) {
        N2oAction butAction = source.getAction();
        if (source.getAction() == null && source.getActionId() != null) {
            MetaActions metaActions = p.getScope(MetaActions.class);
            butAction = metaActions.get(source.getActionId()) == null ? null : metaActions.get(source.getActionId()).getAction();
        }
        if (butAction == null) return null;
        CompiledObject compiledObject = getCompiledObject(p, butAction.getObjectId(), source.getWidgetId());
        butAction.setId(p.cast(butAction.getId(), source.getId()));
        return p.compile(butAction, context, compiledObject, new ComponentScope(source));
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

    private String initDatasource(N2oButton source, CompileProcessor p) {
        if (source.getDatasource() != null)
            return source.getDatasource();
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (widgetScope != null)
            return widgetScope.getDatasourceId();
        return null;
    }

    /**
     * Компиляция условий и зависимостей кнопки
     *
     * @param button  Клиентская модель кнопки
     * @param source  Исходная модель кнопки
     * @param context Контекст сборки метаданных
     * @param p       Процессор сборки метаданных
     */
    protected void compileDependencies(N2oButton source, PerformButton button,
                                       CompileContext<?, ?> context, CompileProcessor p) {
        String clientDatasource = p.getScope(PageScope.class).getGlobalDatasourceId(source.getDatasource());
        List<Condition> enabledConditions = new ArrayList<>();

        if (source.getVisibilityConditions() != null)
            button.getConditions().put(ValidationType.visible,
                    compileConditions(source.getVisibilityConditions(), source.getModel(), clientDatasource));
        if (source.getEnablingConditions() != null)
            enabledConditions.addAll(compileConditions(source.getEnablingConditions(), source.getModel(), clientDatasource));

        ComponentScope componentScope = p.getScope(ComponentScope.class);

        Condition emptyModelCondition = enabledByEmptyModelCondition(source, clientDatasource, componentScope, p);
        if (emptyModelCondition != null)
            enabledConditions.add(emptyModelCondition);

        if (!enabledConditions.isEmpty()) {
            button.getConditions().put(ValidationType.enabled, enabledConditions);
        }

        if (source.getDependencies() != null)
            compileDependencies(source.getDependencies(), button, clientDatasource, source.getModel(), p);

        if (componentScope != null && componentScope.unwrap(N2oCell.class) != null) {
            button.setVisible(p.resolveJS(source.getVisible(), Boolean.class));
            button.setEnabled(p.resolveJS(source.getEnabled(), Boolean.class));
        } else {
            if (isLink(source.getVisible()))
                compileLinkCondition(button, clientDatasource, ValidationType.visible, source.getVisible(), source.getModel());
            else
                button.setVisible(p.resolveJS(source.getVisible(), Boolean.class));

            if (isLink(source.getEnabled()))
                compileLinkCondition(button, clientDatasource, ValidationType.enabled, source.getEnabled(), source.getModel());
            else
                button.setEnabled(p.resolveJS(source.getEnabled(), Boolean.class));
        }
    }

    /**
     * Получение условия доступности кнопки при пустой модели
     *
     * @param source         Исходная модель кнопки
     * @param clientDatasource     Идентификатор источника данных, к которому относится кнопка
     * @param componentScope Родительский компонент
     * @param p              Процессор сборки метаданных
     * @return Условие доступности кнопки при пустой модели
     */
    private Condition enabledByEmptyModelCondition(N2oButton source, String clientDatasource, ComponentScope componentScope, CompileProcessor p) {
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
            condition.setModelLink(new ModelLink(source.getModel(), clientDatasource).getBindLink());
            return condition;
        }
        return null;
    }

    private List<Condition> compileConditions(N2oButtonCondition[] conditions, ReduxModel model, String clientDatasource) {
        List<Condition> result = new ArrayList<>();
        for (N2oButtonCondition n2oCondition : conditions) {
            Condition condition = new Condition();
            condition.setExpression(n2oCondition.getExpression().trim());
            condition.setModelLink(new ModelLink(model, clientDatasource).getBindLink());
            result.add(condition);
        }
        return result;
    }

    private void compileLinkCondition(PerformButton button, String datasource, ValidationType type,
                                      String linkCondition, ReduxModel model) {
        Condition condition = new Condition();
        condition.setExpression(unwrapLink(linkCondition));
        condition.setModelLink(new ModelLink(model, datasource).getBindLink());
        if (!button.getConditions().containsKey(type))
            button.getConditions().put(type, new ArrayList<>());
        button.getConditions().get(type).add(condition);
    }

    private void compileDependencies(N2oButton.Dependency[] dependencies, PerformButton button, String clientDatasource,
                                     ReduxModel buttonModel, CompileProcessor p) {
        for (N2oButton.Dependency d : dependencies) {
            ValidationType validationType = null;
            if (d instanceof N2oButton.EnablingDependency)
                validationType = ValidationType.enabled;
            else if (d instanceof N2oButton.VisibilityDependency)
                validationType = ValidationType.visible;

            compileCondition(d, button, validationType, clientDatasource, buttonModel, p);
        }
    }

    private void compileCondition(N2oButton.Dependency dependency, PerformButton button, ValidationType validationType,
                                  String clientDatasource, ReduxModel buttonModel, CompileProcessor p) {
        ReduxModel refModel = p.cast(dependency.getModel(), buttonModel, ReduxModel.RESOLVE);
        Condition condition = new Condition();
        condition.setExpression(ScriptProcessor.resolveFunction(dependency.getValue()));
        condition.setModelLink(new ModelLink(refModel, clientDatasource, null).getBindLink());
        if (dependency instanceof N2oButton.EnablingDependency)
            condition.setMessage(((N2oButton.EnablingDependency) dependency).getMessage());

        if (!button.getConditions().containsKey(validationType))
            button.getConditions().put(validationType, new ArrayList<>());
        button.getConditions().get(validationType).add(condition);
    }
}
