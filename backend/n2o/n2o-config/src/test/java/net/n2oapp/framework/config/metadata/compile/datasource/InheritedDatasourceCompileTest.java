package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.datasource.InheritedDatasource;
import net.n2oapp.framework.api.metadata.meta.CopyDependency;
import net.n2oapp.framework.api.metadata.meta.Dependency;
import net.n2oapp.framework.api.metadata.meta.DependencyTypeEnum;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Тестирование компиляции источника данных, получающего данные из другого источника данных
 */
class InheritedDatasourceCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oAllDataPack());
    }

    @Test
    void testInheritedDatasourceFromPage() {
        StandardPage page = (StandardPage)
                compile("net/n2oapp/framework/config/metadata/compile/datasource/testInheritedDatasource.page.xml")
                        .get(new PageContext("testInheritedDatasource"));
        InheritedDatasource inh1 = (InheritedDatasource) page.getDatasources().get("testInheritedDatasource_inh1");
        assertThat(inh1.getId(), is("testInheritedDatasource_inh1"));
        assertThat(inh1.getProvider(), allOf(
                hasProperty("sourceDs", is("testInheritedDatasource_ds1")),
                hasProperty("sourceField", is("name")),
                hasProperty("type", is("inherited")),
                hasProperty("sourceModel", is(ReduxModelEnum.SELECTED))
        ));

        InheritedDatasource inh2 = (InheritedDatasource) page.getDatasources().get("testInheritedDatasource_inh2");
        assertThat(inh2.getId(), is("testInheritedDatasource_inh2"));
        assertThat(inh2.getProvider(), allOf(
                hasProperty("type", is("inherited")),
                hasProperty("sourceDs", is("testInheritedDatasource_ds2")),
                hasProperty("sourceModel", is(ReduxModelEnum.DATASOURCE)),
                hasProperty("sourceField", is("name"))
        ));
        assertThat(inh2.getSubmit(), allOf(
                hasProperty("type", is("inherited")),
                hasProperty("auto", is(false)),
                hasProperty("model", is(ReduxModelEnum.RESOLVE)),
                hasProperty("targetDs", is("testInheritedDatasource_ds2")),
                hasProperty("targetModel", is(ReduxModelEnum.DATASOURCE)),
                hasProperty("targetField", is("name"))
        ));

        InheritedDatasource inh3 = (InheritedDatasource) page.getDatasources().get("testInheritedDatasource_inh3");
        assertThat(inh3.getId(), is("testInheritedDatasource_inh3"));
        assertThat(inh3.getPaging().getSize(), is(13));
        assertThat(inh3.getProvider(), allOf(
                hasProperty("type", is("inherited")),
                hasProperty("sourceDs", is("testInheritedDatasource_ds2")),
                hasProperty("sourceModel", is(ReduxModelEnum.DATASOURCE)),
                hasProperty("sourceField", is("name")),
                hasProperty("fetchValueExpression", is("(function(){var result = source\nreturn result}).call(this)"))
        ));
        assertThat(inh3.getSubmit(), allOf(
                hasProperty("type", is("inherited")),
                hasProperty("auto", is(true)),
                hasProperty("model", is(ReduxModelEnum.FILTER)),
                hasProperty("targetDs", is("testInheritedDatasource_ds1")),
                hasProperty("targetModel", is(ReduxModelEnum.FILTER)),
                hasProperty("targetField", is("name2")),
                hasProperty("submitValueExpression", is("(function(){var result = target\nreturn result}).call(this)"))
        ));
        assertThat(inh3.getDependencies().size(), is(2));
        Dependency dependency = inh3.getDependencies().get(0);
        assertThat(dependency.getOn(), is("models.resolve['testInheritedDatasource_ds']"));
        assertThat(dependency.getType(), is(DependencyTypeEnum.FETCH));
        dependency = inh3.getDependencies().get(1);
        assertThat(dependency, allOf(
                instanceOf(CopyDependency.class),
                hasProperty("type", is(DependencyTypeEnum.COPY)),
                hasProperty("on", is("models.filter['testInheritedDatasource_ds'].source")),
                hasProperty("model", is(ReduxModelEnum.FILTER)),
                hasProperty("submit", is(true)),
                hasProperty("applyOnInit", is(true))
        ));

        assertThat(inh3.getProvider().getFilters().size(), is(2));
        List<InheritedDatasource.Filter> filters = inh3.getProvider().getFilters();
        assertThat(filters.get(0), allOf(
                hasProperty("type", is(FilterTypeEnum.EQ)),
                hasProperty("fieldId", is("id")),
                hasProperty("modelLink", hasProperty("param", is("id"))),
                hasProperty("required", is(false))
        ));
        assertThat(filters.get(1), allOf(
                hasProperty("type", is(FilterTypeEnum.EQ)),
                hasProperty("fieldId", is("name")),
                hasProperty("link", is("models.filter['testInheritedDatasource_inh1']")),
                hasProperty("value", is("`name`")),
                hasProperty("required", is(true))
        ));

        InheritedDatasource inh4 = (InheritedDatasource) page.getDatasources().get("testInheritedDatasource_inh4");
        assertThat(inh4.getSubmit(), allOf(
                hasProperty("type", is("inherited")),
                hasProperty("auto", is(true)),
                hasProperty("model", is(ReduxModelEnum.RESOLVE)),
                hasProperty("targetDs", is("testInheritedDatasource_ds1")),
                hasProperty("targetModel", is(ReduxModelEnum.DATASOURCE)),
                hasProperty("targetField", nullValue()),
                hasProperty("submitValueExpression", is("(function(){var result = target\nreturn result}).call(this)"))
        ));
    }
}
