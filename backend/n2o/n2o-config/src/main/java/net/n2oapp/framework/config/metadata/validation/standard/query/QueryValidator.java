package net.n2oapp.framework.config.metadata.validation.standard.query;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import org.springframework.stereotype.Component;


import java.util.HashSet;
import java.util.Set;

import static net.n2oapp.framework.config.metadata.validation.ValidationUtil.checkForExists;
import static net.n2oapp.framework.config.metadata.validation.ValidationUtil.checkIdsUnique;

/**
 * Валидатор выборки
 */
@Component
public class QueryValidator implements SourceValidator<N2oQuery>, SourceClassAware {

    @Override
    public void validate(N2oQuery n2oQuery) throws N2oMetadataValidationException {
        if (n2oQuery.getObjectId() != null) {
            checkForExists(n2oQuery.getObjectId(), N2oObject.class,
                    "Выборка '%s' ссылается не несуществующий объект \'" + n2oQuery.getObjectId() + "\'");
        }

        if (n2oQuery.getFields() != null) {
            checkIdsUnique(n2oQuery.getFields(), "Поле '%s' встречается более чем один раз в выборке \'" + n2oQuery.getId() + "\'!");
        }

        if (n2oQuery.getFields() != null) {
            Set<String> filterFields = new HashSet<>();
            for (N2oQuery.Field field : n2oQuery.getFields()) {
                if (!field.isSearchUnavailable() && field.getFilterList() != null) {
                    for (N2oQuery.Filter filter : field.getFilterList()) {
                        if (filter.getFilterField() != null && !filterFields.add(filter.getFilterField())) {
                            throw new N2oMetadataValidationException("filter-field " + filter.getFilterField() + " in query " + n2oQuery.getId() + " is repeated");
                        }
                    }
                }
            }
        }
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oQuery.class;
    }
}
