package net.n2oapp.framework.config.metadata.validation.standard.button;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oGroup;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

/**
 * Валидатор исходной модели группы кнопок тулбара.
 */
@Component
public class GroupValidator implements SourceValidator<N2oGroup>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oGroup.class;
    }

    @Override
    public void validate(N2oGroup source, SourceProcessor p) {
        if (source.getItems() == null && source.getGenerate() == null)
            throw new N2oMetadataValidationException(
                    "В группе кнопок не заданы элементы или атрибут 'generate'");

        if (source.getGenerate() != null
                && source.getGenerate().length == 1
                && StringUtils.isEmpty(source.getGenerate()[0]))
            throw new N2oMetadataValidationException(
                    "Атрибут 'generate' группы кнопок не может содержать пустую строку");

        if (!ArrayUtils.isEmpty(source.getItems()))
            p.safeStreamOf(source.getItems()).forEach(p::validate);
    }
}