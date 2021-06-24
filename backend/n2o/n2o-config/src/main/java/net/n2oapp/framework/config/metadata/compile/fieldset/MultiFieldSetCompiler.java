package net.n2oapp.framework.config.metadata.compile.fieldset;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oMultiFieldSet;
import net.n2oapp.framework.api.metadata.meta.fieldset.MultiFieldSet;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция филдсета с динамическим числом полей <multi-set/>
 */
@Component
public class MultiFieldSetCompiler extends AbstractFieldSetCompiler<MultiFieldSet, N2oMultiFieldSet> {

    @Override
    public MultiFieldSet compile(N2oMultiFieldSet source, CompileContext<?, ?> context, CompileProcessor p) {
        MultiFieldSet fieldSet = new MultiFieldSet();
        compileFieldSet(fieldSet, source, context, p);
        fieldSet.setChildrenLabel(p.resolveJS(source.getChildrenLabel()));
        fieldSet.setFirstChildrenLabel(p.resolveJS(source.getFirstChildrenLabel()));
        fieldSet.setSrc(p.cast(source.getSrc(),
                p.resolve(property("n2o.api.fieldset.multi_set.src"), String.class)));
        fieldSet.setName(source.getId());
        fieldSet.setAddButtonLabel(source.getAddButtonLabel());
        fieldSet.setRemoveAllButtonLabel(source.getRemoveAllButtonLabel());
        fieldSet.setCanRemoveFirstItem(p.cast(source.getCanRemoveFirst(),
                p.resolve(property("n2o.api.fieldset.multi-set.can_remove_first_item"), Boolean.class)));
        fieldSet.setNeedAddButton(p.cast(source.getCanAdd(),
                p.resolve(property("n2o.api.fieldset.multi-set.can_add"), Boolean.class)));
        fieldSet.setNeedRemoveButton(p.cast(source.getCanRemove(),
                p.resolve(property("n2o.api.fieldset.multi-set.can_remove"), Boolean.class)));
        fieldSet.setNeedCopyButton(p.cast(source.getCanCopy(),
                p.resolve(property("n2o.api.fieldset.multi-set.can_copy"), Boolean.class)));
        fieldSet.setNeedRemoveAllButton(p.cast(source.getCanRemoveAll(),
                p.resolve(property("n2o.api.fieldset.multi-set.can_remove_all"), Boolean.class)));
        return fieldSet;
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oMultiFieldSet.class;
    }
}
