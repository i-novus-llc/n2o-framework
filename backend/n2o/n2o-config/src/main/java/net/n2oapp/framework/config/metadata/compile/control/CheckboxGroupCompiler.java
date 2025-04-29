package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.multi.N2oCheckboxGroup;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.control.CheckboxGroup;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция группы чекбоксов
 */
@Component
public class CheckboxGroupCompiler extends ListControlCompiler<CheckboxGroup, N2oCheckboxGroup> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oCheckboxGroup.class;
    }

    @Override
    public StandardField<CheckboxGroup> compile(N2oCheckboxGroup source, CompileContext<?, ?> context, CompileProcessor p) {
        CheckboxGroup checkboxGroup = new CheckboxGroup();
        checkboxGroup.setInline(castDefault(source.getInline(),
                () -> p.resolve(property("n2o.api.control.checkbox_group.inline"), Boolean.class)));
        checkboxGroup.setType(castDefault(source.getType(),
                () -> p.resolve(property("n2o.api.control.checkbox_group.type"), N2oCheckboxGroup.CheckboxGroupTypeEnum.class)));
        StandardField<CheckboxGroup> result = compileListControl(checkboxGroup, source, context, p);
        return compileFetchDependencies(result, source, p);
    }

    @Override
    protected ModelLink compileLinkOnSet(StandardField<CheckboxGroup> control, N2oCheckboxGroup source,
                                         WidgetScope widgetScope, CompileProcessor p) {
        ModelLink onSet = new ModelLink(widgetScope.getModel(), widgetScope.getClientDatasourceId());
        onSet.setParam(source.getParam());
        onSet.setSubModelQuery(createSubModel(source, control.getControl().getData(), p));
        onSet.setValue(ScriptProcessor.resolveExpression(String.format("{%s*.id}", source.getId())));
        return onSet;
    }

    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.checkbox_group.src";
    }
}
