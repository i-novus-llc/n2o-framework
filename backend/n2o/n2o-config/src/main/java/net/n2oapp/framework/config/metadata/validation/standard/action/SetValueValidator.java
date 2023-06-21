package net.n2oapp.framework.config.metadata.validation.standard.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oSetValueAction;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;

public class SetValueValidator implements SourceValidator<N2oSetValueAction>, SourceClassAware {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSetValueAction.class;
    }

    @Override
    public void validate(N2oSetValueAction source, SourceProcessor p) {
        if (source.getSourceDatasourceId() != null)
            ValidationUtils.checkDatasourceExistence(source.getSourceDatasourceId(), p,
                    String.format("В действии <set-value> указан несуществующий источник данных 'source-datasource = %s'", source.getSourceDatasourceId()));

        if (source.getTargetDatasourceId() != null)
            ValidationUtils.checkDatasourceExistence(source.getTargetDatasourceId(), p,
                    String.format("В действии <set-value> указан несуществующий источник данных 'target-datasource = %s'", source.getTargetDatasourceId()));
    }
}
