package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.control.N2oStandardField;
import net.n2oapp.framework.api.metadata.control.Submit;
import net.n2oapp.framework.api.metadata.dataprovider.N2oClientDataProvider;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.control.Control;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.saga.RefreshSaga;
import net.n2oapp.framework.api.metadata.meta.widget.RequestMethod;
import net.n2oapp.framework.config.metadata.compile.dataprovider.ClientDataProviderUtil;
import net.n2oapp.framework.config.metadata.compile.fieldset.FieldSetScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.util.StylesResolver;


/**
 * Абстрактная реализация компиляции поля ввода
 */
public abstract class StandardFieldCompiler<D extends Control, S extends N2oStandardField> extends FieldCompiler<StandardField<D>, S> {

    protected StandardField<D> compileStandardField(D control, S source, CompileContext<?, ?> context, CompileProcessor p) {
        StandardField<D> field = new StandardField<>();
        if (control.getSrc() == null)
            control.setSrc(source.getSrc());
        source.setSrc(null);
        compileField(field, source, context, p);
        field.setControl(control);
        field.setClassName(null);//для StandardField className должен попасть в control, а не field
        initValidations(source, field, context, p);
        compileFilters(source, p);
        compileCopied(source, p);
        compileControl(control, source, p, field);
        control.setProperties(field.getProperties());
        field.setProperties(null);//для StandardField properties должны попасть в control, а не field
        field.setDataProvider(initDataProvider(source, context, p));
        initRefAttributes(source, context, p);
        return field;
    }

    protected void compileControl(D control, S source, CompileProcessor p, StandardField<D> field) {
        control.setSrc(p.cast(control.getSrc(), p.resolve(Placeholders.property(getControlSrcProperty()), String.class)));
        if (control.getSrc() == null)
            throw new N2oException("control src is required");
        control.setId(source.getId());
        control.setClassName(p.resolveJS(source.getCssClass()));
        control.setStyle(StylesResolver.resolveStyles(source.getStyle()));
        compileDefaultValues(field, source, p);
    }

    @Override
    protected String initLabel(S source, CompileProcessor p) {
        if (source.getNoLabel() == null || !source.getNoLabel()) {
            String label = super.initLabel(source, p);

            FieldSetScope scope = p.getScope(FieldSetScope.class);
            if (label == null && scope != null) {
                label = scope.get(source.getId());
            }
            if (label == null)
                label = source.getId();
            return label;
        } else
            return null;
    }

    /**
     * Настройка React компонента ввода по умолчанию
     */
    protected abstract String getControlSrcProperty();

    private ClientDataProvider initDataProvider(S source, CompileContext<?, ?> context, CompileProcessor p) {
        if (source.getSubmit() == null)
            return null;
        N2oClientDataProvider dataProvider = new N2oClientDataProvider();
        Submit submit = source.getSubmit();
        dataProvider.setMethod(RequestMethod.POST);
        dataProvider.setUrl(submit.getRoute());
        dataProvider.setSubmitForm(false);
        dataProvider.setTargetModel(ReduxModel.RESOLVE);
        dataProvider.setPathParams(submit.getPathParams());
        dataProvider.setHeaderParams(submit.getHeaderParams());
        dataProvider.setFormParams(submit.getFormParams());

        CompiledObject compiledObject = p.getScope(CompiledObject.class);
        if (compiledObject == null)
            throw new N2oException("For compilation submit for field [{0}] is necessary object!").addData(source.getId());

        N2oClientDataProvider.ActionContextData actionContextData = new N2oClientDataProvider.ActionContextData();
        actionContextData.setObjectId(compiledObject.getId());
        actionContextData.setOperationId(submit.getOperationId());
        actionContextData.setRoute(submit.getRoute());
        actionContextData.setMessageOnSuccess(p.cast(submit.getMessageOnSuccess(), false));
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (widgetScope != null) {
            actionContextData.setSuccessAlertWidgetId(widgetScope.getWidgetId());
            actionContextData.setFailAlertWidgetId(widgetScope.getWidgetId());
        }
        actionContextData.setMessageOnFail(p.cast(submit.getMessageOnFail(), false));
        actionContextData.setOperation(compiledObject.getOperations().get(submit.getOperationId()));
        if (Boolean.TRUE.equals(submit.getRefreshOnSuccess())) {
            actionContextData.setRefresh(new RefreshSaga());
            actionContextData.getRefresh().setType(RefreshSaga.Type.widget);
            actionContextData.getRefresh().getOptions().setWidgetId(widgetScope != null ? widgetScope.getWidgetId() : source.getId());
        }
        dataProvider.setActionContextData(actionContextData);
        return ClientDataProviderUtil.compile(dataProvider, context, p);
    }
}
