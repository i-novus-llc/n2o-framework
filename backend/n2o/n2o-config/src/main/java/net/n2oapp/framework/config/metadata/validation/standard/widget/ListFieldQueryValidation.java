package net.n2oapp.framework.config.metadata.validation.standard.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.control.N2oListField;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.validation.ValidationUtil;


public class ListFieldQueryValidation implements SourceValidator<N2oListField>, SourceClassAware {

    @Override
    public void validate(N2oListField field) throws N2oMetadataValidationException {
        String queryId = field.getQueryId();
        if (queryId == null || ValidationUtil.hasLinks(queryId))
            return;

        ValidationUtil.checkForExists(queryId, N2oQuery.class,
                "Не найдена выборка '${queryId}', указанная в компоненте '${field.id}'");
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oListField.class;
    }
}


