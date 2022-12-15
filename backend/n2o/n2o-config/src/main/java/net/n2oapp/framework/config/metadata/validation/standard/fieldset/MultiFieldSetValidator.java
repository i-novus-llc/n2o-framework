package net.n2oapp.framework.config.metadata.validation.standard.fieldset;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oMultiFieldSet;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.FieldsScope;
import org.springframework.stereotype.Component;

/**
 * Валидатор филдсета с динамическим числом полей
 */
@Component
public class MultiFieldSetValidator implements SourceValidator<N2oMultiFieldSet>, SourceClassAware {

    @Override
    public void validate(N2oMultiFieldSet source, SourceProcessor p) {
        validateItems(source, p);
    }

    /**
     * Валидация внутренних элементов
     *
     * @param source Филдсет
     * @param p      Процессор исходных метаданных
     */
    private void validateItems(N2oMultiFieldSet source, SourceProcessor p) {
        FieldsScope scope = new FieldsScope();
        p.safeStreamOf(source.getItems()).forEach(i -> p.validate(i, scope));
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oMultiFieldSet.class;
    }
}
