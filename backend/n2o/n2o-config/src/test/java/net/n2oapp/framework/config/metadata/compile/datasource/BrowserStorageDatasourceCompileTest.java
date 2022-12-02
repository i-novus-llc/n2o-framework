package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.datasource.BrowserStorageDatasource;
import net.n2oapp.framework.api.metadata.datasource.BrowserStorageType;
import net.n2oapp.framework.api.metadata.meta.CopyDependency;
import net.n2oapp.framework.api.metadata.meta.Dependency;
import net.n2oapp.framework.api.metadata.meta.DependencyType;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.control.v3.plain.InputTextIOv3;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class BrowserStorageDatasourceCompileTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oAllDataPack()).ios(new InputTextIOv3());
    }

    @Test
    public void simple() {
        StandardPage page = (StandardPage)
                compile("net/n2oapp/framework/config/metadata/compile/datasource/testBrowserStorageDatasource.page.xml")
                        .get(new PageContext("testBrowserStorageDatasource"));

        BrowserStorageDatasource datasource = (BrowserStorageDatasource) page.getDatasources().get("testBrowserStorageDatasource_ds1");
        assertThat(datasource.getPaging().getSize(), is(13));

        assertThat(datasource.getSubmit().getStorage(), is(BrowserStorageType.localStorage));
        assertThat(datasource.getSubmit().getType(), is("browser"));
        assertThat(datasource.getSubmit().getAuto(), is(false));
        assertThat(datasource.getSubmit().getModel(), is(ReduxModel.filter));
        assertThat(datasource.getSubmit().getKey(), is("submit_test_key"));

        assertThat(datasource.getProvider().getStorage(), is(BrowserStorageType.localStorage));
        assertThat(datasource.getProvider().getType(), is("browser"));
        assertThat(datasource.getProvider().getKey(), is("test_key"));

        assertThat(datasource.getDependencies().size(), is(2));
        Dependency dependency = datasource.getDependencies().get(0);
        assertThat(dependency.getOn(), is("models.resolve['testBrowserStorageDatasource_ds']"));
        assertThat(dependency.getType(), is(DependencyType.fetch));

        dependency = datasource.getDependencies().get(1);
        assertThat(dependency.getType(), is(DependencyType.copy));
        assertThat(dependency.getOn(), is("models.filter['testBrowserStorageDatasource_ds'].source"));
        assertThat(((CopyDependency) dependency).getModel(), is(ReduxModel.filter));
        assertThat(((CopyDependency) dependency).getSubmit(), is(true));
        assertThat(((CopyDependency) dependency).getApplyOnInit(), is(true));


        // default
        datasource = (BrowserStorageDatasource) page.getDatasources().get("testBrowserStorageDatasource_ds2");
        assertThat(datasource.getPaging().getSize(), is(10));
        assertThat(datasource.getSubmit().getStorage(), is(BrowserStorageType.sessionStorage));
        assertThat(datasource.getSubmit().getType(), is("browser"));
        assertThat(datasource.getSubmit().getAuto(), is(true));
        assertThat(datasource.getSubmit().getModel(), is(ReduxModel.resolve));
        assertThat(datasource.getSubmit().getKey(), is("test_key"));

        assertThat(datasource.getProvider().getStorage(), is(BrowserStorageType.sessionStorage));
        assertThat(datasource.getProvider().getType(), is("browser"));
        assertThat(datasource.getProvider().getKey(), is("test_key"));


        // default without submit
        datasource = (BrowserStorageDatasource) page.getDatasources().get("testBrowserStorageDatasource_ds3");
        assertThat(datasource.getSubmit(), nullValue());
        assertThat(datasource.getProvider().getKey(), is("ds3"));
    }
}
