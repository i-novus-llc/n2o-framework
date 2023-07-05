package net.n2oapp.framework.config.metadata.validation.standard.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oCopyAction;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

@Component
public class CopyActionValidator implements SourceValidator<N2oCopyAction>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oCopyAction.class;
    }

    @Override
    public void validate(N2oCopyAction source, SourceProcessor p) {
        if (source.getSourceDatasourceId() != null)
            ValidationUtils.checkDatasourceExistence(source.getSourceDatasourceId(), p,
                    String.format("Атрибут 'source-datasource' действия '<copy>' ссылается на несуществующий источник данных %s",
                            ValidationUtils.getIdOrEmptyString(source.getSourceDatasourceId())));

        if (source.getTargetDatasourceId() != null)
            ValidationUtils.checkDatasourceExistence(source.getTargetDatasourceId(), p,
                    String.format("Атрибут 'target-datasource' действия '<copy>' ссылается на несуществующий источник данных %s",
                            ValidationUtils.getIdOrEmptyString(source.getTargetDatasourceId())));
    }
}
