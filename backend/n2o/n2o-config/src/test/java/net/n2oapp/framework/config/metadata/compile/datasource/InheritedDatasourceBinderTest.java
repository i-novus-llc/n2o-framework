package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.datasource.BrowserStorageDatasource;
import net.n2oapp.framework.api.metadata.datasource.InheritedDatasource;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тестирование связывания с данными источника, получающего данные из другого источника данных
 */
public class InheritedDatasourceBinderTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.properties("userId=User1");
        builder.getEnvironment().getContextProcessor().set("username", "Username1");
        builder.packs(new N2oAllPagesPack(), new N2oAllDataPack());
    }

    @Test
    public void testBind() {
        PageContext context = new PageContext("testBindInheritedDatasource", "/:id/open");
        Page page = bind("net/n2oapp/framework/config/metadata/compile/datasource/testBindInheritedDatasource.page.xml")
                .get(context, new DataSet().add("id", 222));

        InheritedDatasource ds1 = (InheritedDatasource) page.getDatasources().get("id_open_ds1");
        assertThat(ds1.getProvider().getFilters().get(0).getValue(), is(222));
        assertThat(ds1.getProvider().getFilters().get(1).getModelLink().normalizeLink(), is("models.resolve['id_open_ds0'].name"));
    }
}
