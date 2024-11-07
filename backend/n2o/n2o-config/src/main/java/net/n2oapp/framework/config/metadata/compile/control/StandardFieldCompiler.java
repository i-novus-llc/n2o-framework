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
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import net.n2oapp.framework.config.metadata.compile.dataprovider.ClientDataProviderUtil;
import net.n2oapp.framework.config.metadata.compile.fieldset.FieldSetScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.util.N2oClientDataProviderUtil;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;


/**
 * Абстрактная реализация компиляции поля ввода
 */
public abstract class StandardFieldCompiler<D extends Control, S extends N2oStandardField> extends FieldCompiler<StandardField<D>, S> {

    protected StandardField<D> compileStandardField(D control, S source, CompileContext<?, ?> context, CompileProcessor p) {
        initDefaults(source, context, p);
        if (isNull(control.getSrc()))
            control.setSrc(source.getSrc());
        source.setSrc(null);
        source.setCopied(castDefault(source.getCopied(), true));
        StandardField<D> field = new StandardField<>();
        compileField(field, source, context, p);
        field.setControl(control);
        field.setClassName(null); //для StandardField className должен попасть в control, а не field
        initValidations(source, field, context, p);
        compileFilters(source, p);
        compileCopied(source, p);
        compileControl(control, source, p, field, context);
        control.setProperties(field.getProperties());
        field.setProperties(null); //для StandardField properties должны попасть в control, а не field
        field.setDataProvider(initDataProvider(source, context, p));

        return field;
    }

    protected void compileControl(D control, S source, CompileProcessor p, StandardField<D> field, CompileContext<?, ?> context) {
        String src = castDefault(
                control.getSrc(),
                () -> p.resolve(Placeholders.property(getControlSrcProperty()), String.class)
        );
        if (isNull(src))
            throw new N2oException("Требуется указать атрибут 'src'");
        control.setSrc(src);
        control.setId(source.getId());
        control.setPlaceholder(p.resolveJS(source.getPlaceholder()));
        control.setClassName(p.resolveJS(source.getCssClass()));
        compileDefaultValues(field, source, context, p);
    }

    @Override
    protected String initLabel(S source, CompileProcessor p) {
        if (!"true".equals(source.getNoLabel())) {
            String label = p.resolveJS(source.getLabel());

            FieldSetScope scope = p.getScope(FieldSetScope.class);
            if (isNull(label) && nonNull(scope)) {
                label = scope.get(source.getId());
            }
            if (isNull(label))
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
        if (isNull(source.getSubmit()))
            return null;
        N2oClientDataProvider dataProvider = N2oClientDataProviderUtil.initFromSubmit(
                source.getSubmit(),
                source.getId(),
                p.getScope(CompiledObject.class),
                p
        );
        dataProvider.setSubmitForm(false);
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (nonNull(widgetScope)) {
            dataProvider.getActionContextData().setParentWidgetId(widgetScope.getClientWidgetId());
            dataProvider.getActionContextData().setMessagesForm(widgetScope.getClientWidgetId());
        }
        ClientDataProvider clientDataProvider = ClientDataProviderUtil.compile(dataProvider, context, p);
        ParentRouteScope parentRouteScope = p.getScope(ParentRouteScope.class);
        if (nonNull(parentRouteScope))
            clientDataProvider.getPathMapping().putAll(parentRouteScope.getPathMapping());

        return clientDataProvider;
    }
}
