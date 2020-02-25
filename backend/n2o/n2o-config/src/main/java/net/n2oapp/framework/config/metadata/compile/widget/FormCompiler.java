package net.n2oapp.framework.config.metadata.compile.widget;


import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.UploadType;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.global.view.widget.FormMode;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.Models;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetParamScope;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.metadata.compile.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        form.getComponent().setPrompt(source.getPrompt());
        CompiledQuery query = getQuery(source, p);
        CompiledObject object = getObject(source, p);
        compileWidget(form, source, context, p, object);
        WidgetScope widgetScope = new WidgetScope();
        widgetScope.setWidgetId(source.getId());
        widgetScope.setQueryId(source.getQueryId());
        widgetScope.setClientWidgetId(form.getId());
        MetaActions widgetActions = new MetaActions();
        ParentRouteScope widgetRoute = initWidgetRouteScope(form, context, p);
        PageRoutesScope pageRoutesScope = p.getScope(PageRoutesScope.class);
        if (pageRoutesScope != null) {
            pageRoutesScope.put(form.getId(), widgetRoute);
        }
        Models models = p.getScope(Models.class);
        UploadScope uploadScope = new UploadScope();
        uploadScope.setUpload(form.getUpload());
        SubModelsScope subModelsScope = new SubModelsScope();
        CopiedFieldScope copiedFieldScope = new CopiedFieldScope();
        WidgetParamScope paramScope = form.getUpload().equals(UploadType.defaults) ? new WidgetParamScope(form.getId()) : null;
        form.getComponent().setFieldsets(initFieldSets(source.getItems(), context, p, widgetScope, query, object,
                new ModelsScope(ReduxModel.RESOLVE, form.getId(), models), null, subModelsScope, uploadScope,
                new MomentScope(N2oValidation.ServerMoment.beforeOperation), copiedFieldScope,
                paramScope, new ComponentScope(source)));
        ValidationList validationList = p.getScope(ValidationList.class) == null ? new ValidationList(new HashMap<>()) : p.getScope(ValidationList.class);
        ValidationScope validationScope = new ValidationScope(form.getId(), ReduxModel.RESOLVE, validationList);
        compileValidation(form, source, validationScope);
        compileDataProviderAndRoutes(form, source, context, p, validationList, widgetRoute, subModelsScope, copiedFieldScope, object);
        addParamRoutes(paramScope, p);
        compileToolbarAndAction(form, source, context, p, widgetScope, widgetRoute, widgetActions, object, validationList);
        if (source.getMode() != null && source.getMode().equals(FormMode.TWO_MODELS)) {
            form.getComponent().setModelPrefix("edit");
        } else {
            form.getComponent().setModelPrefix("resolve");
        }
        return form;
    }

    private void compileValidation(Form form, N2oForm source, ValidationScope validationScope) {
        if (source.getItems() == null)
            return;
        Map<String, List<Validation>> clientValidations = new HashMap<>();
        form.getComponent().getFieldsets().forEach(fs -> collectValidation(fs, clientValidations, validationScope));
        form.getComponent().setValidation(clientValidations);
    }

    private void addParamRoutes(WidgetParamScope paramScope, CompileProcessor p) {
        if (paramScope != null && !paramScope.getQueryMapping().isEmpty()) {
            PageRoutes routes = p.getScope(PageRoutes.class);
            if (routes == null)
                return;
            paramScope.getQueryMapping().forEach((k, v) -> routes.addQueryMapping(k, v.getOnGet(), v.getOnSet()));
        }
    }

}
