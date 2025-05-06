package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware;
import net.n2oapp.framework.api.metadata.compile.ButtonGeneratorFactory;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.DisableOnEmptyModelTypeEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.control.ValidationTypeEnum;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Condition;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.PerformButton;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.compile.datasource.DataSourcesScope;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.*;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.metadata.compile.action.ActionCompileStaticProcessor.*;
import static net.n2oapp.framework.config.metadata.compile.toolbar.ButtonCompileUtil.*;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;

/**
 * Компиляция кнопки
 */
@Component
public class PerformButtonCompiler extends BaseButtonCompiler<N2oButton, PerformButton> implements MetadataEnvironmentAware {

    protected ButtonGeneratorFactory buttonGeneratorFactory;

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oButton.class;
    }

    @Override
    public void setEnvironment(MetadataEnvironment environment) {
        buttonGeneratorFactory = environment.getButtonGeneratorFactory();
    }

    @Override
    public PerformButton compile(N2oButton source, CompileContext<?, ?> context, CompileProcessor p) {
        if (!ArrayUtils.isEmpty(source.getGenerate())) {
            N2oToolbar toolbar = p.getScope(N2oToolbar.class);
            toolbar.setGeneratedForSubMenu(source.isGeneratedForSubMenu());

            return (PerformButton) generateButtons(source, toolbar, buttonGeneratorFactory, context, p).get(0);
        }

        PerformButton button = new PerformButton();
        initDefaults(source, p);
        compileBase(button, source, p);
        button.setSrc(source.getSrc());
        button.setRounded(source.getRounded());
        button.setValidate(compileValidate(source,
                p,
                source.getDatasourceId()));
        CompiledObject compiledObject = initObject(p, source);
        Action action = compileAction(source, context, p, compiledObject);
        button.setAction(action);
        compileDependencies(source, button, p);

        return button;
    }

    @Override
    protected void initDefaults(N2oButton source, CompileProcessor p) {
        source.setId(castDefault(source.getId(), source.getActionId()));
        super.initDefaults(source, p);

        source.setDatasourceId(initDatasource(source, p));
        source.setSrc(castDefault(source.getSrc(),
                () -> p.resolve(property("n2o.api.button.src"), String.class)));
        source.setRounded(castDefault(source.getRounded(),
                () -> p.resolve(property("n2o.api.button.rounded"), Boolean.class)));
        source.setActions(initActions(source, p));
        boolean validate = initValidate(source, source.getDatasourceId());
        source.setValidate(validate);
        source.setValidateDatasourceIds(initValidateDatasources(source, validate, source.getDatasourceId()));
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

            if (datasource instanceof N2oStandardDatasource standardDatasource) {
                if (standardDatasource.getObjectId() != null) {
                    return p.getCompiled(new ObjectContext(standardDatasource.getObjectId()));
                } else if (standardDatasource.getQueryId() != null) {
                    return p.getCompiled(new QueryContext(standardDatasource.getQueryId())).getObject();
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
            button.getConditions().put(ValidationTypeEnum.enabled, enabledConditions);
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
        DisableOnEmptyModelTypeEnum disableOnEmptyModel = castDefault(
                source.getDisableOnEmptyModel(),
                () -> p.resolve(property("n2o.api.button.disable_on_empty_model"), DisableOnEmptyModelTypeEnum.class)
        );
        if (DisableOnEmptyModelTypeEnum.FALSE.equals(disableOnEmptyModel)) return null;

        boolean parentIsNotCell = componentScope == null || componentScope.unwrap(N2oCell.class) == null;
        boolean autoDisableCondition = DisableOnEmptyModelTypeEnum.AUTO.equals(disableOnEmptyModel) &&
                (ReduxModelEnum.resolve.equals(source.getModel()) || ReduxModelEnum.multi.equals(source.getModel())) &&
                parentIsNotCell;

        if (DisableOnEmptyModelTypeEnum.TRUE.equals(disableOnEmptyModel) || autoDisableCondition) {
            Condition condition = new Condition();
            condition.setExpression("!$.isEmptyModel(this)");
            condition.setModelLink(new ModelLink(source.getModel(), clientDatasource).getLink());

            return condition;
        }

        return null;
    }

    private void compileDependencies(N2oButton.Dependency[] dependencies, PerformButton button, String clientDatasource,
                                     ReduxModelEnum buttonModel, CompileProcessor p) {
        for (N2oButton.Dependency d : dependencies) {
            ValidationTypeEnum validationType = null;
            if (d instanceof N2oButton.EnablingDependency)
                validationType = ValidationTypeEnum.enabled;
            else if (d instanceof N2oButton.VisibilityDependency)
                validationType = ValidationTypeEnum.visible;

            compileDependencyCondition(d, button, validationType, clientDatasource, buttonModel, p);
        }
    }

    private void compileDependencyCondition(N2oButton.Dependency dependency, PerformButton button, ValidationTypeEnum validationType,
                                            String buttonDatasource, ReduxModelEnum buttonModel, CompileProcessor p) {
        ReduxModelEnum refModel = castDefault(dependency.getModel(), buttonModel, ReduxModelEnum.resolve);
        Condition condition = new Condition();
        condition.setExpression(ScriptProcessor.resolveFunction(dependency.getValue()));
        String datasource = (dependency.getDatasource() != null) ?
                getClientDatasourceId(dependency.getDatasource(), p) :
                buttonDatasource;
        condition.setModelLink(new ModelLink(refModel, datasource, null).getLink());
        if (dependency instanceof N2oButton.EnablingDependency enablingDependency)
            condition.setMessage(enablingDependency.getMessage());

        if (!button.getConditions().containsKey(validationType))
            button.getConditions().put(validationType, new ArrayList<>());
        button.getConditions().get(validationType).add(condition);
    }
}
