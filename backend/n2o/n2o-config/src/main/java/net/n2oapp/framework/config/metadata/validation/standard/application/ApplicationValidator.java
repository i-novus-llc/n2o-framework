package net.n2oapp.framework.config.metadata.validation.standard.application;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.application.N2oApplication;
import net.n2oapp.framework.api.metadata.application.N2oSidebar;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.application.sidebar.SidebarPathsScope;
import net.n2oapp.framework.config.metadata.compile.datasource.ValidatorDataSourcesScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import net.n2oapp.framework.config.metadata.validation.standard.ValidatorDatasourceIdsScope;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static net.n2oapp.framework.api.StringUtils.hasLink;

@Component
public class ApplicationValidator implements SourceValidator<N2oApplication>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oApplication.class;
    }

    @Override
    public void validate(N2oApplication source, SourceProcessor p) {
        N2oAbstractDatasource[] datasources = source.getDatasources();
        N2oSidebar[] sidebars = source.getSidebars();

        p.checkIdsUnique(datasources, String.format("Источник данных '%s' встречается более чем один раз в метаданной приложения %s",
                "%s", ValidationUtils.getIdOrEmptyString(source.getId())));

        ValidatorDataSourcesScope dataSourcesScope = new ValidatorDataSourcesScope();
        p.safeStreamOf(source.getDatasources()).forEach(d -> dataSourcesScope.put(d.getId(), d));

        ValidatorDatasourceIdsScope datasourceIdsScope = new ValidatorDatasourceIdsScope(
                p.safeStreamOf(datasources).map(N2oAbstractDatasource::getId).collect(Collectors.toSet())
        );
        p.safeStreamOf(datasources).forEach(datasource -> p.validate(datasource, datasourceIdsScope));

        if (source.getHeader() != null) {
            if (hasLink(source.getHeader().getTitle()) && source.getHeader().getDatasourceId() == null)
                throw new N2oMetadataValidationException("Заголовок компонента <header> имеет плейсхолдер, но при этом не указан источник данных");

            if (source.getHeader().getDatasourceId() != null && !dataSourcesScope.containsKey(source.getHeader().getDatasourceId()))
                throw new N2oMetadataValidationException(String.format("<header> ссылается на несуществующий источник данных %s",
                        ValidationUtils.getIdOrEmptyString(source.getHeader().getDatasourceId())));

            if (source.getHeader().getExtraMenu() != null)
                p.validate(source.getHeader().getExtraMenu(), datasourceIdsScope);
            if (source.getHeader().getMenu() != null)
                p.validate(source.getHeader().getMenu(), datasourceIdsScope);

        }

        if (!ArrayUtils.isEmpty(source.getSidebars())) {
            SidebarPathsScope sidebarsPaths = new SidebarPathsScope();
            p.safeStreamOf(sidebars).forEach(sidebar -> p.validate(sidebar, sidebarsPaths, dataSourcesScope, datasourceIdsScope));
        }
    }
}
