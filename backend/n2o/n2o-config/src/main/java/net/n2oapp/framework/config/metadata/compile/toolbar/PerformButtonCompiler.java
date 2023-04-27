package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.DisableOnEmptyModelType;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.control.ValidationType;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Condition;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.PerformButton;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.compile.datasource.DataSourcesScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.*;
import static net.n2oapp.framework.config.metadata.compile.action.ActionCompileStaticProcessor.*;
import static net.n2oapp.framework.config.metadata.compile.toolbar.ButtonCompileUtil.compileValidate;
import static net.n2oapp.framework.config.metadata.compile.toolbar.ButtonCompileUtil.initDatasource;
import static net.n2oapp.framework.config.metadata.compile.toolbar.ButtonCompileUtil.initValidate;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;

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
        initDefaults(source, context, p);
        compileBase(button, source, context, p);
        button.setSrc(source.getSrc());
        button.setRounded(source.getRounded());
        button.setValidate(compileValidate(source,
                p,
                initDatasource(source, p)));
        CompiledObject compiledObject = initObject(p, source);
        Action action = compileAction(source, context, p, compiledObject);
        button.setAction(action);
        button.setConfirm(ButtonCompileUtil.compileConfirm(source, p, getOperation(action, compiledObject)));
        compileDependencies(source, button, p);
        return button;
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

    protected void initDefaults(N2oButton source, CompileContext<?, ?> context, CompileProcessor p) {
        source.setId(p.cast(source.getId(), source.getActionId()));
        super.initDefaults(source, context, p);

        source.setDatasourceId(initDatasource(source, p));
        source.setSrc(p.cast(source.getSrc(), p.resolve(property("n2o.api.action.button.src"), String.class)));
        source.setRounded(p.cast(source.getRounded(), false));
        boolean validate = initValidate(source, p, source.getDatasourceId());
        source.setValidate(validate);
        source.setValidateDatasourceIds(initValidateDatasources(source, validate, source.getDatasourceId()));
        source.setActions(initActions(source, p));
    }

    private String[] initValidateDatasources(N2oButton source, boolean validate, String datasource) {
        if (validate) {
            if (source.getValidateDatasourceIds() != null)
                return source.getValidateDatasourceIds();
            if (source.getDatasourceId() != null)
                return new String[]{datasource};
        }
        return null;
    }

    private CompiledObject initObject(CompileProcessor p, N2oButton button) {
        if (button.getDatasourceId() != null && p.getScope(DataSourcesScope.class) != null) {
            N2oAbstractDatasource datasource = p.getScope(DataSourcesScope.class).get(button.getDatasourceId());
            if (datasource instanceof N2oStandardDatasource) {
                N2oStandardDatasource standardDatasource = (N2oStandardDatasource) datasource;
                if (standardDatasource.getObjectId() != null) {
                    return p.getCompiled(new ObjectContext(standardDatasource.getObjectId()));
                } else if (standardDatasource.getQueryId() != null) {
                    CompiledQuery query = p.getCompiled(new QueryContext(standardDatasource.getQueryId()));
                    return query.getObject();
                }
            }
        }
        return p.getScope(CompiledObject.class);
    }

    /**
     * Компиляция условий и зависимостей кнопки
     *
     * @param button Клиентская модель кнопки
     * @param source Исходная модель кнопки
     * @param p      Процессор сборки метаданных
     */
    protected void compileDependencies(N2oButton source, PerformButton button,
                                       CompileProcessor p) {
        String clientDatasource = getClientDatasourceId(source.getDatasourceId(), p);

        ComponentScope componentScope = p.getScope(ComponentScope.class);

        List<Condition> enabledConditions = new ArrayList<>();
        if (source.getDatasourceId() != null) {
            Condition emptyModelCondition = enabledByEmptyModelCondition(source, clientDatasource, componentScope, p);
            if (emptyModelCondition != null)
                enabledConditions.add(emptyModelCondition);
        }

        if (!enabledConditions.isEmpty()) {
            button.getConditions().put(ValidationType.enabled, enabledConditions);
        }

        if (source.getDependencies() != null)
            compileDependencies(source.getDependencies(), button, clientDatasource, source.getModel(), p);

        compileCondition(source, button, p, componentScope);
    }

    /**
     * Получение условия доступности кнопки при пустой модели
     *
     * @param source           Исходная модель кнопки
     * @param clientDatasource Идентификатор источника данных, к которому относится кнопка
     * @param componentScope   Родительский компонент
     * @param p                Процессор сборки метаданных
     * @return Условие доступности кнопки при пустой модели
     */
    private Condition enabledByEmptyModelCondition(N2oButton source, String clientDatasource, ComponentScope componentScope, CompileProcessor p) {
        DisableOnEmptyModelType disableOnEmptyModel = p.cast(source.getDisableOnEmptyModel(),
                p.resolve(property("n2o.api.button.disable_on_empty_model"), DisableOnEmptyModelType.class));
        if (DisableOnEmptyModelType.FALSE.equals(disableOnEmptyModel)) return null;

        boolean parentIsNotCell = componentScope == null || componentScope.unwrap(N2oCell.class) == null;
        boolean autoDisableCondition = DisableOnEmptyModelType.AUTO.equals(disableOnEmptyModel) &&
                (ReduxModel.resolve.equals(source.getModel()) || ReduxModel.multi.equals(source.getModel())) &&
                parentIsNotCell;

        if (DisableOnEmptyModelType.TRUE.equals(disableOnEmptyModel) || autoDisableCondition) {
            Condition condition = new Condition();
            condition.setExpression("!$.isEmptyModel(this)");
            condition.setModelLink(new ModelLink(source.getModel(), clientDatasource).getBindLink());
            return condition;
        }
        return null;
    }

    private void compileDependencies(N2oButton.Dependency[] dependencies, PerformButton button, String clientDatasource,
                                     ReduxModel buttonModel, CompileProcessor p) {
        for (N2oButton.Dependency d : dependencies) {
            ValidationType validationType = null;
            if (d instanceof N2oButton.EnablingDependency)
                validationType = ValidationType.enabled;
            else if (d instanceof N2oButton.VisibilityDependency)
                validationType = ValidationType.visible;

            compileDependencyCondition(d, button, validationType, clientDatasource, buttonModel, p);
        }
    }

    private void compileDependencyCondition(N2oButton.Dependency dependency, PerformButton button, ValidationType validationType,
                                            String buttonDatasource, ReduxModel buttonModel, CompileProcessor p) {
        ReduxModel refModel = p.cast(dependency.getModel(), buttonModel, ReduxModel.resolve);
        Condition condition = new Condition();
        condition.setExpression(ScriptProcessor.resolveFunction(dependency.getValue()));
        String datasource = (dependency.getDatasource() != null) ?
                getClientDatasourceId(dependency.getDatasource(), p) :
                buttonDatasource;
        condition.setModelLink(new ModelLink(refModel, datasource, null).getBindLink());
        if (dependency instanceof N2oButton.EnablingDependency)
            condition.setMessage(((N2oButton.EnablingDependency) dependency).getMessage());

        if (!button.getConditions().containsKey(validationType))
            button.getConditions().put(validationType, new ArrayList<>());
        button.getConditions().get(validationType).add(condition);
    }
}
