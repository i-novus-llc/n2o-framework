package net.n2oapp.framework.config.metadata.validation.standard.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oOnFailAction;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.checkCloseInMultiAction;

@Component
public class OnFailActionValidator implements SourceValidator<N2oOnFailAction>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oOnFailAction.class;
    }

    @Override
    public void validate(N2oOnFailAction source, SourceProcessor p) {
        checkCloseInMultiAction(source.getActions());
    }
}
