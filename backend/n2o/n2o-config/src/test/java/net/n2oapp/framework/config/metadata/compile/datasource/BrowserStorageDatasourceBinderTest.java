package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.datasource.BrowserStorageDatasource;
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
 * Тестирование связывания с данными источника, хранящего данные в браузере
 */
public class BrowserStorageDatasourceBinderTest extends SourceCompileTestBase {

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
        Page page = bind("net/n2oapp/framework/config/metadata/compile/datasource/testBindBrowserStorageDatasource.page.xml")
                .get(new PageContext("testBindBrowserStorageDatasource"), new DataSet());

        BrowserStorageDatasource ds1 = (BrowserStorageDatasource) page.getDatasources().get("testBindBrowserStorageDatasource_ds1");
        assertThat(ds1.getProvider().getKey(), is("User1.store"));
        assertThat(ds1.getSubmit().getKey(), is("User1.store"));

        BrowserStorageDatasource ds3 = (BrowserStorageDatasource) page.getDatasources().get("testBindBrowserStorageDatasource_ds3");
        assertThat(ds3.getProvider().getKey(), is("data.Username1.store"));
        assertThat(ds3.getSubmit().getKey(), is("data.Username1.store"));
    }
}
