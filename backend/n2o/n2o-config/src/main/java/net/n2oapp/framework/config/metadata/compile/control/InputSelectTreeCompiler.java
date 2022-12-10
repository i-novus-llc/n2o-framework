package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.control.list.N2oInputSelectTree;
import net.n2oapp.framework.api.metadata.meta.control.InputSelectTree;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;

/**
 * Компиляция компонента ввода с выбором в выпадающем списке в виде дерева
 */
@Component
public class InputSelectTreeCompiler extends ListControlCompiler<InputSelectTree, N2oInputSelectTree> {

    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.input.select.tree.src";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oInputSelectTree.class;
    }

    @Override
    public StandardField<InputSelectTree> compile(N2oInputSelectTree source, CompileContext<?, ?> context, CompileProcessor p) {
        InputSelectTree control = new InputSelectTree();
        control.setParentFieldId(p.resolveJS(source.getParentFieldId()));
        control.setHasChildrenFieldId(p.resolveJS(source.getHasChildrenFieldId()));
        control.setHasCheckboxes(p.cast(source.getCheckboxes(), false));
        control.setMultiSelect(control.isHasCheckboxes());
        control.setClosePopupOnSelect(!control.isHasCheckboxes());
        control.setAjax(p.cast(source.getAjax(), false));
        control.setSize(p.cast(source.getSize(), p.resolve(Placeholders.property("n2o.api.control.input.select.tree.size"), Integer.class)));
        control.setCheckingStrategy(source.getCheckingStrategy());
        control.setMaxTagCount(source.getMaxTagCount());
        if (control.isHasCheckboxes())
            control.setMaxTagTextLength(p.cast(source.getMaxTagTextLength(), p.resolve(Placeholders.property("n2o.api.control.input.select.tree.max_tag_text_length"), Integer.class)));
        source.setQueryId(p.resolveJS(source.getQueryId()));
        source.setLabelFieldId(p.cast(p.resolveJS(source.getLabelFieldId()), "name"));
        source.setIconFieldId(p.resolveJS(source.getIconFieldId()));
        return compileListControl(control, source, context, p);
    }
}
