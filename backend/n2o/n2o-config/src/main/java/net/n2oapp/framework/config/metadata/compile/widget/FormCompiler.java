package net.n2oapp.framework.config.metadata.compile.widget;


import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.control.Submit;
import net.n2oapp.framework.api.metadata.dataprovider.N2oClientDataProvider;
import net.n2oapp.framework.api.metadata.event.action.UploadType;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.global.view.widget.FormMode;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.Models;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.saga.RefreshSaga;
import net.n2oapp.framework.api.metadata.meta.widget.RequestMethod;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetParamScope;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.metadata.compile.*;
import net.n2oapp.framework.config.metadata.compile.dataprovider.ClientDataProviderUtil;
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
        form.getComponent().setPrompt(p.cast(source.getPrompt(),
                p.resolve(Placeholders.property("n2o.api.widget.form.unsaved_data_prompt"), Boolean.class)));
        CompiledQuery query = getQuery(source, p);
        CompiledObject object = getObject(source, p);
        compileWidget(form, source, context, p, object);
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (widgetScope == null)
            widgetScope = new WidgetScope();
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
        WidgetParamScope paramScope = form.getUpload().equals(UploadType.defaults) ? new WidgetParamScope() : null;
        form.getComponent().setFieldsets(initFieldSets(source.getItems(), context, p, widgetScope, query, object, widgetActions,
                new ModelsScope(ReduxModel.RESOLVE, form.getId(), models), null, subModelsScope, uploadScope,
                new MomentScope(N2oValidation.ServerMoment.beforeOperation), copiedFieldScope, widgetRoute,
                paramScope, new ComponentScope(source)));
        ValidationList validationList = p.getScope(ValidationList.class) == null ? new ValidationList(new HashMap<>()) : p.getScope(ValidationList.class);
        ValidationScope validationScope = new ValidationScope(form.getId(), ReduxModel.RESOLVE, validationList);
        compileValidation(form, source, validationScope);
        compileDataProviderAndRoutes(form, source, context, p, validationList, widgetRoute, subModelsScope, copiedFieldScope, object);
        addParamRoutes(paramScope, context, p);
        compileToolbarAndAction(form, source, context, p, widgetScope, widgetRoute, widgetActions, object, validationList);
        form.getComponent().setModelPrefix(FormMode.TWO_MODELS.equals(source.getMode()) ? "edit" : "resolve");
        form.setFormDataProvider(initDataProvider(source, object, context, p));
        return form;
    }

    private ClientDataProvider initDataProvider(N2oForm source, CompiledObject compiledObject,
                                                CompileContext<?, ?> context, CompileProcessor p) {
        if (source.getSubmit() == null)
            return null;
        N2oClientDataProvider dataProvider = new N2oClientDataProvider();
        Submit submit = source.getSubmit();
        dataProvider.setMethod(RequestMethod.POST);
        dataProvider.setUrl(submit.getRoute());
        dataProvider.setSubmitForm(true);
        dataProvider.setTargetModel(ReduxModel.RESOLVE);
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (widgetScope != null)
            dataProvider.setTargetWidgetId(widgetScope.getClientWidgetId());
        dataProvider.setPathParams(submit.getPathParams());
        dataProvider.setHeaderParams(submit.getHeaderParams());
        dataProvider.setFormParams(submit.getFormParams());

        if (compiledObject == null)
            throw new N2oException("For compilation submit for form widget [{0}] is necessary object!").addData(source.getId());

        N2oClientDataProvider.ActionContextData actionContextData = new N2oClientDataProvider.ActionContextData();
        actionContextData.setObjectId(compiledObject.getId());
        actionContextData.setOperationId(submit.getOperationId());
        actionContextData.setRoute(submit.getRoute());
        actionContextData.setMessageOnSuccess(p.cast(submit.getMessageOnSuccess(), false));
        actionContextData.setSuccessAlertWidgetId(source.getId());
        actionContextData.setFailAlertWidgetId(source.getId());
        actionContextData.setMessageOnFail(p.cast(submit.getMessageOnFail(), false));
        actionContextData.setOperation(compiledObject.getOperations().get(submit.getOperationId()));
        if (Boolean.TRUE.equals(submit.getRefreshOnSuccess())) {
            actionContextData.setRefresh(new RefreshSaga());
            actionContextData.getRefresh().setType(RefreshSaga.Type.widget);
            actionContextData.getRefresh().getOptions().setWidgetId(source.getId());
        }
        dataProvider.setActionContextData(actionContextData);

        return ClientDataProviderUtil.compile(dataProvider, context, p);
    }

    private void compileValidation(Form form, N2oForm source, ValidationScope validationScope) {
        if (source.getItems() == null)
            return;
        Map<String, List<Validation>> clientValidations = new HashMap<>();
        form.getComponent().getFieldsets().forEach(fs -> collectValidation(fs, clientValidations, validationScope));
        form.getComponent().setValidation(clientValidations);
    }

    private void addParamRoutes(WidgetParamScope paramScope, CompileContext<?, ?> context, CompileProcessor p) {
        if (paramScope != null && !paramScope.getQueryMapping().isEmpty()) {
            PageRoutes routes = p.getScope(PageRoutes.class);
            if (routes == null)
                return;
            paramScope.getQueryMapping().forEach((k, v) -> {
                if (context.getPathRouteMapping() == null || !context.getPathRouteMapping().containsKey(k)) {
                    routes.addQueryMapping(k, v.getOnGet(), v.getOnSet());
                }
            });
        }
    }

}
