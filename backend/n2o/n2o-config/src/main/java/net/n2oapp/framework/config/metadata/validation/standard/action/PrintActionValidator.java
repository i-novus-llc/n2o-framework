package net.n2oapp.framework.config.metadata.validation.standard.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oPrintAction;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class PrintActionValidator implements SourceValidator<N2oPrintAction>, SourceClassAware {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oPrintAction.class;
    }

    @Override
    public void validate(N2oPrintAction source, SourceProcessor p) {
        if (StringUtils.isBlank(source.getUrl()))
            throw new N2oMetadataValidationException("В действии <print> не задан адрес документа для печати 'url'");
    }
}
