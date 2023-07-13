package net.n2oapp.framework.config.metadata.validation.standard.application;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.application.N2oApplication;
import net.n2oapp.framework.api.metadata.application.N2oSidebar;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.config.metadata.compile.application.sidebar.SidebarPathsScope;
import net.n2oapp.framework.config.metadata.compile.datasource.DataSourcesScope;
import net.n2oapp.framework.config.metadata.compile.datasource.DatasourceIdsScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

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

        DataSourcesScope dataSourcesScope = new DataSourcesScope();
        p.safeStreamOf(source.getDatasources()).forEach(d -> dataSourcesScope.put(d.getId(), d));

        DatasourceIdsScope datasourceIdsScope = new DatasourceIdsScope(
                p.safeStreamOf(datasources).map(N2oAbstractDatasource::getId).collect(Collectors.toSet())
        );
        p.safeStreamOf(datasources).forEach(datasource -> p.validate(datasource, datasourceIdsScope));

        if (source.getHeader() != null) {
            if (source.getHeader().getExtraMenu() != null) {
                p.checkForExists(source.getHeader().getExtraMenu().getRefId(), N2oSimpleMenu.class,
                        String.format("<extra-menu> хедера приложения %s ссылается на несуществующий 'ref-id = %s'",
                                ValidationUtils.getIdOrEmptyString(source.getId()),
                                source.getHeader().getExtraMenu().getRefId()));
                p.validate(source.getHeader().getExtraMenu());
            }
            if (source.getHeader().getMenu() != null) {
                p.checkForExists(source.getHeader().getMenu().getRefId(), N2oSimpleMenu.class,
                        String.format("<menu> хедера приложения %s ссылается на несуществующий 'ref-id = %s'",
                                ValidationUtils.getIdOrEmptyString(source.getId()),
                                source.getHeader().getMenu().getRefId()));
                p.validate(source.getHeader().getExtraMenu());
            }
        }

        if (!ArrayUtils.isEmpty(source.getSidebars())) {
            p.safeStreamOf(sidebars).forEach(sidebar -> p.checkForExists(sidebar.getRefId(), N2oSidebar.class,
                    String.format("<sidebar> приложения %s ссылается на несуществующий 'ref-id = %s'",
                            ValidationUtils.getIdOrEmptyString(source.getId()),
                            sidebar.getRefId())));
            SidebarPathsScope sidebarsPaths = new SidebarPathsScope();
            p.safeStreamOf(sidebars).forEach(sidebar -> p.validate(sidebar, sidebarsPaths, dataSourcesScope, datasourceIdsScope));
        }
    }
}
