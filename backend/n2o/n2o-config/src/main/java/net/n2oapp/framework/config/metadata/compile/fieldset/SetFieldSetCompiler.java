package net.n2oapp.framework.config.metadata.compile.fieldset;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oSetFieldSet;
import net.n2oapp.framework.api.metadata.meta.fieldset.SetFieldSet;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция простого филдсета
 */
@Component
public class SetFieldSetCompiler extends AbstractFieldSetCompiler<SetFieldSet, N2oSetFieldSet> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSetFieldSet.class;
    }

    @Override
    public SetFieldSet compile(N2oSetFieldSet source, CompileContext<?, ?> context, CompileProcessor p) {
        SetFieldSet fieldSet = new SetFieldSet();
        fieldSet.setSrc(p.cast(source.getSrc(), p.resolve(property("n2o.api.fieldset.set.src"), String.class)));
        compileFieldSet(fieldSet, source, context, p);
        return fieldSet;
    }
}
