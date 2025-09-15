package net.n2oapp.framework.config.metadata.compile.control.masked;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.masked.N2oPhoneField;
import net.n2oapp.framework.api.metadata.meta.control.masked.PhoneField;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Компиляция поля {@code <phone>}
 */
@Component
public class PhoneFieldCompiler extends MaskedFieldCompiler<PhoneField, N2oPhoneField> {

    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.phone.src";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oPhoneField.class;
    }

    @Override
    public StandardField<PhoneField> compile(N2oPhoneField source, CompileContext<?, ?> context, CompileProcessor p) {
        PhoneField field = new PhoneField();
        field.setCountries(List.of(source.getCountry()));
        return compileMaskedField(field, source, context, p);
    }
}
