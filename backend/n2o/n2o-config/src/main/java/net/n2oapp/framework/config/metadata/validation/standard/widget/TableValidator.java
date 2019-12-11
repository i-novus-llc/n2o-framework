package net.n2oapp.framework.config.metadata.validation.standard.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oTable;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validate.ValidateProcessor;
import net.n2oapp.framework.config.metadata.validation.standard.IdValidationUtils;
import org.springframework.core.env.PropertyResolver;
import org.springframework.stereotype.Component;

@Component
public class TableValidator implements SourceValidator<N2oTable>, SourceClassAware {

    private final IdValidationUtils idValidationUtils;

    public TableValidator(PropertyResolver propertyResolver) {
        idValidationUtils = new IdValidationUtils(propertyResolver);
    }

    @Override
    public void validate(N2oTable source, ValidateProcessor p) {
        idValidationUtils.checkIds(source.getFilters());
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oTable.class;
    }
}
