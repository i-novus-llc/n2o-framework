package net.n2oapp.framework.config.metadata.validation.standard.datasource;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.application.N2oStompDatasource;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Валидатор STOMP-источника данных
 */
@Component
public class StompDatasourceValidator extends AbstractDatasourceValidator<N2oStompDatasource> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oStompDatasource.class;
    }

    @Override
    public void validate(N2oStompDatasource source, SourceProcessor p) {
        super.validate(source, p);

        if (StringUtils.isBlank(source.getDestination())) {
            throw new N2oMetadataValidationException(
                    "В источнике данных \"<stomp-datasource>\" не задан обязательный атрибут 'destination'");
        }
    }
}