package net.n2oapp.framework.config.metadata.validation.standard.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oAnchor;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.action.control.TargetEnum;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

@Component
public class AnchorActionValidator implements SourceValidator<N2oAnchor>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oAnchor.class;
    }

    @Override
    public void validate(N2oAnchor source, SourceProcessor p) {
        if (source.getHref() == null)
            ValidationUtils.checkDatasourceExistence(source.getDatasourceId(), p,
                    "Для действия <a> не задан `href`");


        if (source.getTarget() != null && source.getTarget().equals(TargetEnum.application)
                && source.getHref() != null && source.getHref().startsWith("http")) {
            throw new N2oMetadataValidationException(
                    "Для действия <a> при абсолютном пути (http\\https) не может быть задан target=\"application\"");
        }
    }
}
