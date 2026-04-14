package net.n2oapp.framework.config.metadata.validation.standard.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.control.plain.N2oIntervalField;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import org.springframework.stereotype.Component;


@Component
public class IntervalFieldValidator implements SourceValidator<N2oIntervalField>, SourceClassAware {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oIntervalField.class;
    }

    @Override
    public void validate(N2oIntervalField source, SourceProcessor p) {
        if (source.getBegin() == null || source.getEnd() == null)
            throw new N2oMetadataValidationException("Не заданы элементы <begin> или <end> в <interval-field>");
        p.validate(source.getBegin());
        p.validate(source.getEnd());
    }
}
