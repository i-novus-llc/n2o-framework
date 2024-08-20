package net.n2oapp.framework.config.metadata.validation.standard.datasource;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oCachedDatasource;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.checkQueryExists;
import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.getIdOrEmptyString;

/**
 * Валидатор кэширующего источника данных
 */
@Component
public class CachedDatasourceValidator extends AbstractDatasourceValidator<N2oCachedDatasource> {

    private static final Pattern CACHES_EXPIRES_PATTERN = Pattern.compile("^(\\d+d\\s*)?(\\d+h\\s*)?(\\d+m)?$");

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oCachedDatasource.class;
    }

    @Override
    public void validate(N2oCachedDatasource datasource, SourceProcessor p) {
        super.validate(datasource, p);
        ValidationUtils.checkForExistsObject(datasource.getObjectId(),
                String.format("Источник данных %s", getIdOrEmptyString(datasource.getId())), p);
        N2oQuery query = checkQueryExists(datasource.getQueryId(),
                String.format("Источник данных %s", getIdOrEmptyString(datasource.getId())), p);
        checkSubmit(datasource.getId(), datasource.getSubmit(), p);
        checkPrefilters(datasource.getId(), datasource.getFilters(), query, p);

        if (datasource.getCacheExpires() != null) {
            if (!CACHES_EXPIRES_PATTERN.matcher(datasource.getCacheExpires()).matches()) {
                throw new N2oMetadataValidationException(
                        String.format("Периодичность проверки кэша в источнике данных %s указана в неверном формате",
                                getIdOrEmptyString(datasource.getId())));
            }
        }
    }
}
