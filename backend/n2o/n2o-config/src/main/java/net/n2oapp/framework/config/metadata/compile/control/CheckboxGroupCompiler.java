package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.multi.N2oCheckboxGroup;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.control.CheckboxGroup;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.widget.ModelsScope;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

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
    public StandardField<CheckboxGroup> compile(N2oCheckboxGroup source, CompileContext<?,?> context, CompileProcessor p) {
        CheckboxGroup checkboxGroup = new CheckboxGroup();
        checkboxGroup.setInline(p.cast(source.getInline(), p.resolve(property("n2o.api.control.alt.inline"), Boolean.class)));
        checkboxGroup.setType(p.cast(source.getType() == N2oCheckboxGroup.CheckboxGroupType.n2o ? N2oCheckboxGroup.CheckboxGroupType.defaultType : source.getType(), new DomainProcessor().deserializeEnum(p.resolve(property("n2o.api.control.alt.type"), N2oCheckboxGroup.CheckboxGroupType.class), N2oCheckboxGroup.CheckboxGroupType.class)));
        StandardField<CheckboxGroup> result = compileListControl(checkboxGroup, source, context, p);
        return compileFetchDependencies(result, source, p);
    }

    @Override
    protected ModelLink compileLinkOnSet(StandardField<CheckboxGroup> control, N2oCheckboxGroup source, ModelsScope modelsScope) {
        ModelLink onSet = new ModelLink(modelsScope.getModel(), modelsScope.getWidgetId());
        onSet.setParam(source.getParam());
        onSet.setSubModelQuery(createSubModel(source, control.getControl().getData()));
        onSet.setValue(ScriptProcessor.resolveExpression(String.format("{%s*.id}", source.getId())));
        return onSet;
    }

    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.checkbox_group.src";
    }
}
