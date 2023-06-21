package net.n2oapp.framework.config.metadata.validation.standard.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oClearAction;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

@Component
public class ClearActionValidator implements SourceValidator<N2oClearAction>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oClearAction.class;
    }

    @Override
    public void validate(N2oClearAction source, SourceProcessor p) {
        if (source.getDatasourceId() == null)
            ValidationUtils.checkDatasourceExistence(source.getDatasourceId(), p,
                    String.format("Действие <clear> ссылается на несуществующий источник данных '%s'", source.getDatasourceId()));
    }
}
