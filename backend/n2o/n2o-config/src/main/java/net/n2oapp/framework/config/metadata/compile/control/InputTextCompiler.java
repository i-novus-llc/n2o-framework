package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.plain.N2oInputText;
import net.n2oapp.framework.api.metadata.domain.Domain;
import net.n2oapp.framework.api.metadata.meta.control.InputText;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция поля для ввода текста
 */
@Component
public class InputTextCompiler extends StandardFieldCompiler<InputText, N2oInputText> {

    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.input.number.src";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oInputText.class;
    }

    @Override
    public StandardField<InputText> compile(N2oInputText source, CompileContext<?, ?> context, CompileProcessor p) {
        InputText inputText = new InputText();
        inputText.setMeasure(source.getMeasure());
        compileDomain(inputText, source, p);
        if (source.getStep() != null && source.getStep().replace(",", "").replace(".", "").replace("0", "").isEmpty()) {
            inputText.setShowButtons(false);
        }
        return compileStandardField(inputText, source, context, p);
    }

    /**
     * Компиляция домена поля <input-text>
     *
     * @param inputText клиентская модель поля ввода
     * @param source    исходная модель модель поля ввода
     */

    private void compileDomain(InputText inputText, N2oInputText source, CompileProcessor p) {
        Domain domain = Domain.getByName(source.getDomain());
        if (domain == null) domain = Domain.STRING;
        switch (domain) {
            case INTEGER:
                inputText.setMin(p.cast(p.resolveJS(source.getMin(), Integer.class), Integer.MIN_VALUE));
                inputText.setMax(p.cast(p.resolveJS(source.getMax(), Integer.class), Integer.MAX_VALUE));
                inputText.setStep(castDefault(source.getStep(), "1"));
                return;
            case LONG:
                inputText.setMin(p.cast(p.resolveJS(source.getMin(), Long.class), "-99999999999999"));
                inputText.setMax(p.cast(p.resolveJS(source.getMax(), Long.class), "99999999999999"));
                inputText.setStep(castDefault(source.getStep(), "1"));
                return;
            case SHORT:
                inputText.setMin(p.cast(p.resolveJS(source.getMin(), Short.class), Short.MIN_VALUE));
                inputText.setMax(p.cast(p.resolveJS(source.getMax(), Short.class), Short.MAX_VALUE));
                inputText.setStep(castDefault(source.getStep(), "1"));
                return;
            case NUMERIC:
                inputText.setMin(p.cast(p.resolveJS(source.getMin(), BigDecimal.class), "-999999999"));
                inputText.setMax(p.cast(p.resolveJS(source.getMax(), BigDecimal.class), "999999999"));
                inputText.setStep(castDefault(source.getStep(), "0.01"));
                inputText.setPrecision(p.cast(source.getPrecision(), p.resolve(property("n2o.api.control.input_text.precision"), Integer.class)));
                return;
            case STRING:
                inputText.setLength(source.getLength());
                inputText.setSrc(p.cast(source.getSrc(), p.resolve(property("n2o.api.control.input_text.src"), String.class)));
                return;
            default:
        }
    }
}

