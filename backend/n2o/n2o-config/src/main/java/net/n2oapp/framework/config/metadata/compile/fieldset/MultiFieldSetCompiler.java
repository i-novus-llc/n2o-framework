package net.n2oapp.framework.config.metadata.compile.fieldset;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oMultiFieldSet;
import net.n2oapp.framework.api.metadata.meta.fieldset.MultiFieldSet;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция филдсета с динамическим числом полей <multi-set/>
 */
@Component
public class MultiFieldSetCompiler extends AbstractFieldSetCompiler<MultiFieldSet, N2oMultiFieldSet> {

    @Override
    public MultiFieldSet compile(N2oMultiFieldSet source, CompileContext<?, ?> context, CompileProcessor p) {
        MultiFieldSet fieldSet = new MultiFieldSet();
        MultiFieldSetScope multiFieldSetScope = new MultiFieldSetScope(source.getId());
        compileFieldSet(fieldSet, source, context, p, multiFieldSetScope);
        fieldSet.setChildrenLabel(p.resolveJS(source.getChildrenLabel()));
        fieldSet.setFirstChildrenLabel(p.resolveJS(source.getFirstChildrenLabel()));
        fieldSet.setSrc(castDefault(source.getSrc(),
                () -> p.resolve(property("n2o.api.fieldset.multi_set.src"), String.class)));
        fieldSet.setName(source.getId());
        fieldSet.setAddButtonLabel(source.getAddButtonLabel());
        fieldSet.setRemoveAllButtonLabel(source.getRemoveAllButtonLabel());
        fieldSet.setCanRemoveFirstItem(castDefault(source.getCanRemoveFirst(),
                () -> p.resolve(property("n2o.api.fieldset.multi_set.can_remove_first_item"), Boolean.class)));
        fieldSet.setNeedAddButton(castDefault(source.getCanAdd(),
                () -> p.resolve(property("n2o.api.fieldset.multi_set.can_add"), Boolean.class)));
        fieldSet.setNeedRemoveButton(castDefault(source.getCanRemove(),
                () -> p.resolve(property("n2o.api.fieldset.multi_set.can_remove"), Boolean.class)));
        fieldSet.setNeedCopyButton(castDefault(source.getCanCopy(),
                () -> p.resolve(property("n2o.api.fieldset.multi_set.can_copy"), Boolean.class)));
        fieldSet.setNeedRemoveAllButton(castDefault(source.getCanRemoveAll(),
                () -> p.resolve(property("n2o.api.fieldset.multi_set.can_remove_all"), Boolean.class)));
        fieldSet.setPrimaryKey(castDefault(source.getPrimaryKey(), "id"));
        fieldSet.setGeneratePrimaryKey(castDefault(source.getGeneratePrimaryKey(),
                () -> p.resolve(property("n2o.api.fieldset.multi_set.generate_primary_key"), Boolean.class)));

        return fieldSet;
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oMultiFieldSet.class;
    }
}
