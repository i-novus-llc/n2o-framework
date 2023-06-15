package net.n2oapp.framework.config.metadata.validation.standard.regions;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.region.N2oTabsRegion;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.datasource.DatasourceIdsScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

@Component
public class TabsValidator implements SourceValidator<N2oTabsRegion>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oTabsRegion.class;
    }

    @Override
    public void validate(N2oTabsRegion source, SourceProcessor p) {
        if (source.getTabs() == null)
            throw new N2oMetadataValidationException("В регионе <tabs> отсутствуют вкладки <tab>");

        ValidationUtils.checkDatasourceExistence(source.getDatasourceId(), p.getScope(DatasourceIdsScope.class),
                String.format("Регион <tabs> ссылается на несуществующий источник данных '%s'", source.getDatasourceId()));

    }
}
