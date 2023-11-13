package net.n2oapp.framework.config.metadata.validation.standard.regions;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.region.N2oTabsRegion;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.datasource.DatasourceIdsScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import java.util.Arrays;

import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.getIdOrEmptyString;

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

        if (source.getDatasourceId() != null)
            ValidationUtils.checkDatasourceExistence(source.getDatasourceId(), p.getScope(DatasourceIdsScope.class),
                    String.format("Регион <tabs> ссылается на несуществующий источник данных '%s'", source.getDatasourceId()));

        Arrays.stream(source.getTabs()).forEach(tab -> this.validateTab(tab, p));
    }

    private void validateTab(N2oTabsRegion.Tab source, SourceProcessor p) {
        if (source.getDatasource() != null)
            ValidationUtils.checkDatasourceExistence(source.getDatasource(), p.getScope(DatasourceIdsScope.class),
                    String.format(
                            "Вкладка %s ссылается на несуществующий источник данных %s",
                            getIdOrEmptyString(source.getName()),
                            getIdOrEmptyString(source.getDatasource())
                    )
            );
        if (source.getContent() != null)
            Arrays.stream(source.getContent()).forEach(p::validate);
    }
}
