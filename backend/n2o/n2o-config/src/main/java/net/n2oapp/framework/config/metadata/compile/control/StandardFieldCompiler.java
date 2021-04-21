package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.control.N2oStandardField;
import net.n2oapp.framework.api.metadata.dataprovider.N2oClientDataProvider;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.control.Control;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.config.metadata.compile.dataprovider.ClientDataProviderUtil;
import net.n2oapp.framework.config.metadata.compile.fieldset.FieldSetScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.util.N2oClientDataProviderUtil;
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
        compileControl(control, source, p, field, context);
        control.setProperties(field.getProperties());
        field.setProperties(null);//для StandardField properties должны попасть в control, а не field
        field.setDataProvider(initDataProvider(source, context, p));
        return field;
    }

    protected void compileControl(D control, S source, CompileProcessor p, StandardField<D> field, CompileContext<?, ?> context) {
        control.setSrc(p.cast(control.getSrc(), p.resolve(Placeholders.property(getControlSrcProperty()), String.class)));
        if (control.getSrc() == null)
            throw new N2oException("control src is required");
        control.setId(source.getId());
        control.setClassName(p.resolveJS(source.getCssClass()));
        control.setStyle(StylesResolver.resolveStyles(source.getStyle()));
        compileDefaultValues(field, source, context, p);
    }

    @Override
    protected String initLabel(S source, CompileProcessor p) {
        if (!Boolean.TRUE.equals(source.getNoLabel())) {
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
        N2oClientDataProvider dataProvider = N2oClientDataProviderUtil.initFromSubmit(source.getSubmit(), source.getId(), p.getScope(CompiledObject.class), p);

        dataProvider.setSubmitForm(false);
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (widgetScope != null) {
            dataProvider.getActionContextData().setSuccessAlertWidgetId(widgetScope.getWidgetId());
            dataProvider.getActionContextData().setFailAlertWidgetId(widgetScope.getWidgetId());
        }
        return ClientDataProviderUtil.compile(dataProvider, context, p);
    }
}
