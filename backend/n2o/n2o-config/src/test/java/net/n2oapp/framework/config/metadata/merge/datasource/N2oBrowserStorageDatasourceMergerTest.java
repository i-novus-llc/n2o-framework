package net.n2oapp.framework.config.metadata.merge.datasource;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.datasource.BrowserStorageDatasource;
import net.n2oapp.framework.api.metadata.datasource.BrowserStorageType;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceMergerTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тестирование слияния двух источников, хранящих данные в браузере
 */
public class N2oBrowserStorageDatasourceMergerTest extends SourceMergerTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack());
    }

    @Test
    public void mergeBrowserStorageDatasource() {
        PageContext pageContext = new PageContext("testBrowserStorageDatasourceMerger", "/");
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/merge/datasource/browser/testBrowserStorageDatasourceMerger.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/merge/datasource/browser/testBrowserStorageDatasourceMergerModal.page.xml"));
        builder.read().compile().get(pageContext);
        Page modal = builder.read().compile().get(builder.route("/modal", Page.class, null));

        BrowserStorageDatasource datasource = (BrowserStorageDatasource) modal.getDatasources().get("modal_bs");
        assertThat(datasource, notNullValue());
        assertThat(datasource.getSize(), is(15));

        assertThat(datasource.getProvider().getKey(), is("key1"));
        assertThat(datasource.getProvider().getType(), is("browser"));
        assertThat(datasource.getProvider().getStorage(), is(BrowserStorageType.localStorage));

        assertThat(datasource.getSubmit().getKey(), is("key2"));
        assertThat(datasource.getSubmit().getAuto(), is(false));
        assertThat(datasource.getSubmit().getModel(), is(ReduxModel.filter));
        assertThat(datasource.getSubmit().getStorage(), is(BrowserStorageType.localStorage));

        assertThat(datasource.getDependencies().size(), is(1));
        assertThat(datasource.getDependencies().get(0).getOn(), is("models.filter['modal_bs']"));
    }
}
