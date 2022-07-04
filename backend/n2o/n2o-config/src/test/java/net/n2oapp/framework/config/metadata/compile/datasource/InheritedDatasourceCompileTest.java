package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.datasource.InheritedDatasource;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

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
                compile("net/n2oapp/framework/config/metadata/compile/datasource/testInheritedDatasourceIOTest.page.xml")
                        .get(new PageContext("testInheritedDatasourceIOTest"));
        InheritedDatasource inh1 = (InheritedDatasource) page.getDatasources().get("testInheritedDatasourceIOTest_inh1");
        assertThat(inh1.getId(), is("testInheritedDatasourceIOTest_inh1"));
        assertThat(inh1.getProvider().getSourceDs(), is("ds1"));
        assertThat(inh1.getProvider().getSourceField(), is("name"));
        assertThat(inh1.getProvider().getType(), is("inherited"));
        assertThat(inh1.getProvider().getSourceModel(), is(ReduxModel.selected));

        InheritedDatasource inh2 = (InheritedDatasource) page.getDatasources().get("testInheritedDatasourceIOTest_inh2");
        assertThat(inh2.getId(), is("testInheritedDatasourceIOTest_inh2"));
        assertThat(inh2.getProvider().getType(), is("inherited"));
        assertThat(inh2.getProvider().getSourceDs(), is("ds2"));
        assertThat(inh2.getProvider().getSourceModel(), is(ReduxModel.datasource));
        assertThat(inh2.getSubmit().getType(), is("inherited"));
        assertThat(inh2.getSubmit().getAuto(), is(true));
        assertThat(inh2.getSubmit().getTargetField(), is("lastName"));
        assertThat(inh2.getSubmit().getTargetDs(), is("ds2"));
    }

}
