package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.datasource.InheritedDatasource;
import net.n2oapp.framework.api.metadata.meta.CopyDependency;
import net.n2oapp.framework.api.metadata.meta.Dependency;
import net.n2oapp.framework.api.metadata.meta.DependencyType;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование компиляции источника данных, получающего данные из другого источника данных
 */
public class InheritedDatasourceCompileTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oAllDataPack());
    }

    @Test
    public void testInheritedDatasourceFromPage() {
        StandardPage page = (StandardPage)
                compile("net/n2oapp/framework/config/metadata/compile/datasource/testInheritedDatasource.page.xml")
                        .get(new PageContext("testInheritedDatasource"));
        InheritedDatasource inh1 = (InheritedDatasource) page.getDatasources().get("testInheritedDatasource_inh1");
        assertThat(inh1.getId(), is("testInheritedDatasource_inh1"));
        assertThat(inh1.getProvider().getSourceDs(), is("testInheritedDatasource_ds1"));
        assertThat(inh1.getProvider().getSourceField(), is("name"));
        assertThat(inh1.getProvider().getType(), is("inherited"));
        assertThat(inh1.getProvider().getSourceModel(), is(ReduxModel.selected));

        InheritedDatasource inh2 = (InheritedDatasource) page.getDatasources().get("testInheritedDatasource_inh2");
        assertThat(inh2.getId(), is("testInheritedDatasource_inh2"));
        assertThat(inh2.getProvider().getType(), is("inherited"));
        assertThat(inh2.getProvider().getSourceDs(), is("testInheritedDatasource_ds2"));
        assertThat(inh2.getProvider().getSourceModel(), is(ReduxModel.datasource));
        assertThat(inh2.getProvider().getSourceField(), is("name"));
        assertThat(inh2.getSubmit().getType(), is("inherited"));
        assertThat(inh2.getSubmit().getAuto(), is(false));
        assertThat(inh2.getSubmit().getModel(), is(ReduxModel.resolve));
        assertThat(inh2.getSubmit().getTargetDs(), is("testInheritedDatasource_ds2"));
        assertThat(inh2.getSubmit().getTargetModel(), is(ReduxModel.datasource));
        assertThat(inh2.getSubmit().getTargetField(), is("name"));

        InheritedDatasource inh3 = (InheritedDatasource) page.getDatasources().get("testInheritedDatasource_inh3");
        assertThat(inh3.getId(), is("testInheritedDatasource_inh3"));
        assertThat(inh3.getPaging().getSize(), is(13));
        assertThat(inh3.getProvider().getType(), is("inherited"));
        assertThat(inh3.getProvider().getSourceDs(), is("testInheritedDatasource_ds2"));
        assertThat(inh3.getProvider().getSourceModel(), is(ReduxModel.datasource));
        assertThat(inh3.getProvider().getSourceField(), is("name"));
        assertThat(inh3.getProvider().getFetchValueExpression(), is("(function(){var result = source\n" +
                "                return result})()"));
        assertThat(inh3.getSubmit().getType(), is("inherited"));
        assertThat(inh3.getSubmit().getAuto(), is(true));
        assertThat(inh3.getSubmit().getModel(), is(ReduxModel.filter));
        assertThat(inh3.getSubmit().getTargetDs(), is("testInheritedDatasource_ds1"));
        assertThat(inh3.getSubmit().getTargetModel(), is(ReduxModel.filter));
        assertThat(inh3.getSubmit().getTargetField(), is("name2"));
        assertThat(inh3.getSubmit().getSubmitValueExpression(), is("(function(){var result = target\n" +
                "                    return result})()"));

        assertThat(inh3.getDependencies().size(), is(2));
        Dependency dependency = inh3.getDependencies().get(0);
        assertThat(dependency.getOn(), is("models.resolve['testInheritedDatasource_ds']"));
        assertThat(dependency.getType(), is(DependencyType.fetch));
        dependency = inh3.getDependencies().get(1);
        assertThat(dependency.getType(), is(DependencyType.copy));
        assertThat(dependency.getOn(), is("models.filter['testInheritedDatasource_ds'].source"));
        assertThat(((CopyDependency) dependency).getModel(), is(ReduxModel.filter));
        assertThat(((CopyDependency) dependency).getSubmit(), is(true));
        assertThat(((CopyDependency) dependency).getApplyOnInit(), is(true));

        assertThat(inh3.getProvider().getFilters().size(), is(2));
        List<InheritedDatasource.Filter> filters = inh3.getProvider().getFilters();
        assertThat(filters.get(0).getType(), is(FilterType.eq));
        assertThat(filters.get(0).getFieldId(), is("id"));
        assertThat(filters.get(0).getModelLink().getParam(), is("id"));
        assertThat(filters.get(0).getRequired(), is(false));
        assertThat(filters.get(1).getType(), is(FilterType.eq));
        assertThat(filters.get(1).getFieldId(), is("name"));
        assertThat(filters.get(1).getLink(), is("models.filter['testInheritedDatasource_inh1']"));
        assertThat(filters.get(1).getValue(), is("`name`"));
        assertThat(filters.get(1).getRequired(), is(true));

        InheritedDatasource inh4 = (InheritedDatasource) page.getDatasources().get("testInheritedDatasource_inh4");
        assertThat(inh4.getSubmit().getType(), is("inherited"));
        assertThat(inh4.getSubmit().getAuto(), is(true));
        assertThat(inh4.getSubmit().getModel(), is(ReduxModel.resolve));
        assertThat(inh4.getSubmit().getTargetDs(), is("testInheritedDatasource_ds1"));
        assertThat(inh4.getSubmit().getTargetModel(), is(ReduxModel.datasource));
        assertThat(inh4.getSubmit().getTargetField(), nullValue());
        assertThat(inh4.getSubmit().getSubmitValueExpression(), is("(function(){var result = target\n" +
                "                    return result})()"));
    }
}
