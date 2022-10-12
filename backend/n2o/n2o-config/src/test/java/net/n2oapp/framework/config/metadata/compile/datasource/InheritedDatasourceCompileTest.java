package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.datasource.AbstractDatasource;
import net.n2oapp.framework.api.metadata.datasource.InheritedDatasource;
import net.n2oapp.framework.api.metadata.meta.CopyDependency;
import net.n2oapp.framework.api.metadata.meta.Dependency;
import net.n2oapp.framework.api.metadata.meta.DependencyType;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
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
        assertThat(inh3.getSubmit().getType(), is("inherited"));
        assertThat(inh3.getSubmit().getAuto(), is(true));
        assertThat(inh3.getSubmit().getModel(), is(ReduxModel.filter));
        assertThat(inh3.getSubmit().getTargetDs(), is("testInheritedDatasource_ds1"));
        assertThat(inh3.getSubmit().getTargetModel(), is(ReduxModel.filter));
        assertThat(inh3.getSubmit().getTargetField(), is("name2"));

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
    }

    @Test
    public void testRefPageAttributes() {
        PageContext pageContext = new PageContext("testInheritedDatasourceRefPageAttributes", "/p");
        compile("net/n2oapp/framework/config/metadata/compile/datasource/testInheritedDatasourceRefPageAttributes.page.xml",
                "net/n2oapp/framework/config/metadata/compile/datasource/testInheritedDatasourceRefPageAttributesModal.page.xml")
                .get(pageContext);

        StandardPage page = (StandardPage) routeAndGet("/p/modal", Page.class);
        Map<String, AbstractDatasource> datasources = page.getDatasources();
        // source: parent, target: parent
        InheritedDatasource inhDs = ((InheritedDatasource) datasources.get("p_modal_inh"));
        assertThat(inhDs.getProvider().getSourceDs(), is("p_ds"));
        assertThat(inhDs.getSubmit().getTargetDs(), is("p_ds2"));
        // source: this, target: this
        inhDs = ((InheritedDatasource) datasources.get("p_modal_inh2"));
        assertThat(inhDs.getProvider().getSourceDs(), is("p_modal_ds"));
        assertThat(inhDs.getSubmit().getTargetDs(), is("p_modal_ds2"));
        // source: parent (app-ds), target: parent (app-ds)
        inhDs = ((InheritedDatasource) datasources.get("p_modal_inh3"));
        assertThat(inhDs.getProvider().getSourceDs(), is("appDs"));
        assertThat(inhDs.getSubmit().getTargetDs(), is("appDs"));
        // source: this (app-ds), target: this (app-ds)
        inhDs = ((InheritedDatasource) datasources.get("p_modal_inh4"));
        assertThat(inhDs.getProvider().getSourceDs(), is("appDs2"));
        assertThat(inhDs.getSubmit().getTargetDs(), is("appDs2"));
    }
}
