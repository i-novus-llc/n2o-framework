package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.control.list.CheckingStrategyEnum;
import net.n2oapp.framework.api.metadata.control.list.N2oInputSelectTree;
import net.n2oapp.framework.api.metadata.meta.control.InputSelectTree;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция компонента ввода с выбором в выпадающем списке в виде дерева
 */
@Component
public class InputSelectTreeCompiler extends ListControlCompiler<InputSelectTree, N2oInputSelectTree> {

    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.input_select_tree.src";
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
        control.setHasCheckboxes(castDefault(source.getCheckboxes(),
                () -> p.resolve(property("n2o.api.control.input_select_tree.checkboxes"), Boolean.class)));
        control.setMultiSelect(control.isHasCheckboxes());
        control.setThrottleDelay(castDefault(source.getThrottleDelay(),
                () -> p.resolve(property("n2o.api.control.input_select_tree.throttle_delay"), Integer.class)));
        control.setSearchMinLength(castDefault(source.getSearchMinLength(),
                () -> p.resolve(property("n2o.api.control.input_select_tree.search_min_length"), Integer.class)));
        control.setClosePopupOnSelect(!control.isHasCheckboxes());
        control.setAjax(castDefault(source.getAjax(),
                () -> p.resolve(property("n2o.api.control.input_select_tree.ajax"), Boolean.class)));
        control.setSize(castDefault(source.getSize(),
                () -> p.resolve(Placeholders.property("n2o.api.control.input_select_tree.size"), Integer.class)));
        control.setCheckingStrategy(castDefault(source.getCheckingStrategy(),
                () -> p.resolve(property("n2o.api.control.input_select_tree.checking_strategy"), CheckingStrategyEnum.class)));
        control.setMaxTagCount(source.getMaxTagCount());
        if (control.isHasCheckboxes())
            control.setMaxTagTextLength(castDefault(source.getMaxTagTextLength(),
                    () -> p.resolve(property("n2o.api.control.input_select_tree.max_tag_text_length"), Integer.class)));
        source.setQueryId(p.resolveJS(source.getQueryId()));
        source.setLabelFieldId(castDefault(p.resolveJS(source.getLabelFieldId()), "name"));
        source.setIconFieldId(p.resolveJS(source.getIconFieldId()));
        return compileListControl(control, source, context, p);
    }
}
