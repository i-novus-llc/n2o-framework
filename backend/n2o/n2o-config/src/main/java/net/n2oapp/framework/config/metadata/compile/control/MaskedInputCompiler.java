package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.plain.N2oMaskedInput;
import net.n2oapp.framework.api.metadata.meta.control.MaskedInput;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция поля с маской для ввода текста
 */
@Component
public class MaskedInputCompiler extends StandardFieldCompiler<MaskedInput, N2oMaskedInput> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oMaskedInput.class;
    }

    @Override
    public StandardField<MaskedInput> compile(N2oMaskedInput source, CompileContext<?,?> context, CompileProcessor p) {
        MaskedInput maskedInput = new MaskedInput();
        maskedInput.setPlaceholder(p.resolveJS(source.getPlaceholder()));
        maskedInput.setMask(p.resolveJS(source.getMask()));
        maskedInput.setClassName(source.getCssClass());
        maskedInput.setControlSrc(p.cast(source.getSrc(), p.resolve(property("n2o.api.control.maskedinput.src"), String.class)));
        return compileStandardField(maskedInput, source, context, p);
    }
}
