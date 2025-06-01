package net.n2oapp.framework.config.metadata.compile.widget;


import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.global.view.widget.FormModeEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.Models;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetParamScope;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.ValidationScope;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.metadata.compile.action.ActionCompileStaticProcessor.initMetaActions;

/**
 * Компиляция виджета форма
 */
@Component
public class FormCompiler extends BaseWidgetCompiler<Form, N2oForm> {
    @Override
    public Class<N2oForm> getSourceClass() {
        return N2oForm.class;
    }

    @Override
    protected String getPropertyWidgetSrc() {
        return "n2o.api.widget.form.src";
    }

    @Override
    public Form compile(N2oForm source, CompileContext<?, ?> context, CompileProcessor p) {
        Form form = new Form();
        compileBaseWidget(form, source, context, p);
        N2oAbstractDatasource datasource = getDatasourceById(source.getDatasourceId(), p);
        CompiledQuery query = getQuery(datasource, p);
        CompiledObject object = getObject(source, datasource, p);
        WidgetScope widgetScope = new WidgetScope(source.getId(), source.getDatasourceId(), ReduxModelEnum.RESOLVE, p);
        MetaActions widgetActions = initMetaActions(source, p);
        Models models = p.getScope(Models.class);
        SubModelsScope subModelsScope = castDefault(p.getScope(SubModelsScope.class), SubModelsScope::new);
        CopiedFieldScope copiedFieldScope = castDefault(p.getScope(CopiedFieldScope.class), CopiedFieldScope::new);
        WidgetParamScope paramScope = new WidgetParamScope();
        ValidationScope validationScope = p.getScope(ValidationScope.class) == null ? new ValidationScope() : p.getScope(ValidationScope.class);
        form.getComponent().setPrompt(initPrompt(source, p));
        form.getComponent().setFieldsets(initFieldSets(source.getItems(), context, p,
                widgetScope, query, object, widgetActions,
                new ModelsScope(ReduxModelEnum.RESOLVE, widgetScope.getClientDatasourceId(), models),
                subModelsScope,
                new MomentScope(N2oValidation.ServerMomentEnum.BEFORE_OPERATION),
                copiedFieldScope,
                paramScope,
                new ComponentScope(source),
                validationScope));
        addParamRoutes(paramScope, context, p);
        compileToolbarAndAction(form, source, context, p, widgetScope, widgetActions, object, validationScope);
        form.getComponent().setModelPrefix(FormModeEnum.TWO_MODELS.equals(source.getMode()) ? "edit" : "resolve");
        return form;
    }

    private Boolean initPrompt(N2oForm source, CompileProcessor p) {
        return castDefault(source.getUnsavedDataPrompt(),
                () -> p.resolve(property("n2o.api.widget.form.unsaved_data_prompt"), Boolean.class));
    }

    @Override
    protected N2oAbstractDatasource initDatasource(Form compiled, N2oForm source, CompileProcessor p) {
        N2oAbstractDatasource datasource = super.initDatasource(compiled, source, p);
        if (datasource.getSize() == null)
            datasource.setSize(p.resolve(property("n2o.api.widget.form.size"), Integer.class));
        return datasource;
    }
}
