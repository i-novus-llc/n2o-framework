package net.n2oapp.framework.config.metadata.validation.standard.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.control.N2oListField;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import org.springframework.stereotype.Component;

/**
 * Валидация списковых компонентов
 */
@Component
public class ListFieldQueryValidator implements SourceValidator<N2oListField>, SourceClassAware {

    @Override
    public void validate(N2oListField field, SourceProcessor p) {
        String queryId = field.getQueryId();
        p.checkForExists(queryId, N2oQuery.class, String.format("Field %s contains a non-existent query {0}", field.getId()));
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oListField.class;
    }
}


