package net.n2oapp.framework.config.metadata.validation.standard.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.control.N2oActionField;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.checkOnFailAction;

@Component
public class ActionFieldValidator implements SourceValidator<N2oActionField>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oActionField.class;
    }

    @Override
    public void validate(N2oActionField source, SourceProcessor p) {
        if (source.getActions() != null) {
            checkOnFailAction(source.getActions());
        }
    }
}