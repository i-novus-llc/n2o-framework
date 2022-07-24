package net.n2oapp.framework.config.metadata.compile.widget;


import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.global.view.widget.FormMode;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.Models;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetParamScope;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.ValidationList;
import net.n2oapp.framework.config.metadata.compile.ValidationScope;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

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
        N2oAbstractDatasource datasource = initDatasource(form, source, p);
        CompiledQuery query = getQuery(datasource, p);
        CompiledObject object = getObject(source, datasource, p);
        WidgetScope widgetScope = new WidgetScope(source.getId(), source.getDatasourceId(), ReduxModel.resolve, p);
        MetaActions widgetActions = initMetaActions(source, p);
        Models models = p.getScope(Models.class);
        SubModelsScope subModelsScope = p.cast(p.getScope(SubModelsScope.class), new SubModelsScope());
        CopiedFieldScope copiedFieldScope = p.cast(p.getScope(CopiedFieldScope.class), new CopiedFieldScope());
        WidgetParamScope paramScope = new WidgetParamScope();
        ValidationList validationList = p.getScope(ValidationList.class) == null ? new ValidationList() : p.getScope(ValidationList.class);
        ValidationScope validationScope = new ValidationScope(datasource, ReduxModel.resolve, validationList);
        form.getComponent().setPrompt(initPrompt(source, p));
        form.getComponent().setFieldsets(initFieldSets(source.getItems(), context, p,
                widgetScope, query, object, widgetActions,
                new ModelsScope(ReduxModel.resolve, widgetScope.getClientDatasourceId(), models),
                subModelsScope,
                new MomentScope(N2oValidation.ServerMoment.beforeOperation),
                copiedFieldScope,
                paramScope,
                new ComponentScope(source),
                validationScope));
        addParamRoutes(paramScope, context, p);
        compileToolbarAndAction(form, source, context, p, widgetScope, widgetActions, object, validationList);
        form.getComponent().setModelPrefix(FormMode.TWO_MODELS.equals(source.getMode()) ? "edit" : "resolve");
        return form;
    }

    private Boolean initPrompt(N2oForm source, CompileProcessor p) {
        return p.cast(source.getPrompt(),
                p.resolve(property("n2o.api.widget.form.unsaved_data_prompt"), Boolean.class));
    }

    @Override
    protected N2oAbstractDatasource initDatasource(Form compiled, N2oForm source, CompileProcessor p) {
        N2oAbstractDatasource datasource = super.initDatasource(compiled, source, p);
        if (datasource.getSize() == null)
            datasource.setSize(p.resolve(property("n2o.api.widget.form.size"), Integer.class));
        return datasource;
    }
}
