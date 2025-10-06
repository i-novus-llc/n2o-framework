package net.n2oapp.framework.config.metadata.compile.control.masked;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.masked.N2oMaskedInput;
import net.n2oapp.framework.api.metadata.domain.DomainEnum;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.control.masked.MaskedInput;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция поля {@code <masked-input>}
 */
@Component
public class MaskedInputCompiler extends MaskedFieldCompiler<MaskedInput, N2oMaskedInput> {

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
        source.setDomain(castDefault(source.getDomain(), DomainEnum.STRING.getName()));
        MaskedInput maskedInput = new MaskedInput();
        maskedInput.setMask(p.resolveJS(source.getMask()));
        maskedInput.setMeasure(source.getMeasure());
        maskedInput.setClassName(source.getCssClass());
        maskedInput.setClearOnBlur(castDefault(source.getClearOnBlur(),
                () -> p.resolve(property("n2o.api.control.masked_input.clear_on_blur"), Boolean.class)));
        maskedInput.setAutocomplete(castDefault(source.getAutocomplete(),
                () -> p.resolve(property("n2o.api.control.masked_input.autocomplete"), String.class)));
        return compileMaskedField(maskedInput, source, context, p);
    }
}
