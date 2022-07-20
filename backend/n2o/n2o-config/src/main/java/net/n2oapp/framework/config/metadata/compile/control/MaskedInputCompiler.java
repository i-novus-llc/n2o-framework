package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.plain.N2oMaskedInput;
import net.n2oapp.framework.api.metadata.domain.Domain;
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
    protected String getControlSrcProperty() {
        return "n2o.api.control.masked_input.src";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oMaskedInput.class;
    }

    @Override
    public StandardField<MaskedInput> compile(N2oMaskedInput source, CompileContext<?, ?> context, CompileProcessor p) {
        source.setDomain(p.cast(source.getDomain(), Domain.STRING.getName()));
        MaskedInput maskedInput = new MaskedInput();
        maskedInput.setMask(p.resolveJS(source.getMask()));
        maskedInput.setMeasure(source.getMeasure());
        maskedInput.setClassName(source.getCssClass());
        maskedInput.setClearOnBlur(p.cast(source.getClearOnBlur(), p.resolve(property("n2o.api.control.masked_input.clear_on_blur"), Boolean.class)));
        return compileStandardField(maskedInput, source, context, p);
    }
}
