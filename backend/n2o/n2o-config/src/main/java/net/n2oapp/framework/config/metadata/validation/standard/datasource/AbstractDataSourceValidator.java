package net.n2oapp.framework.config.metadata.validation.standard.datasource;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;

public abstract class AbstractDataSourceValidator<S extends N2oAbstractDatasource> implements SourceValidator<S>, SourceClassAware {

    @Override
    public void validate(S source, SourceProcessor p) {
        PageScope pageScope = p.getScope(PageScope.class);
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (source.getId() == null && widgetScope == null)
            throw new N2oMetadataValidationException(String.format("В одном из источников данных страницы %s не задан 'id'",
                    ValidationUtils.getIdOrEmptyString(pageScope.getPageId())));
    }
}
