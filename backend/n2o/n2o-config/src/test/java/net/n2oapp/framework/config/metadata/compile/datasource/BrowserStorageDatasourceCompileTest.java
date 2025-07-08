package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.datasource.BrowserStorageDatasource;
import net.n2oapp.framework.api.metadata.datasource.BrowserStorageTypeEnum;
import net.n2oapp.framework.api.metadata.meta.CopyDependency;
import net.n2oapp.framework.api.metadata.meta.Dependency;
import net.n2oapp.framework.api.metadata.meta.DependencyTypeEnum;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.control.v3.plain.InputTextIOv3;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;

class BrowserStorageDatasourceCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oAllDataPack()).ios(new InputTextIOv3());
    }

    @Test
    void simple() {
        StandardPage page = (StandardPage)
                compile("net/n2oapp/framework/config/metadata/compile/datasource/testBrowserStorageDatasource.page.xml")
                        .get(new PageContext("testBrowserStorageDatasource"));

        BrowserStorageDatasource datasource = (BrowserStorageDatasource) page.getDatasources().get("testBrowserStorageDatasource_ds1");
        assertThat(datasource.getPaging().getSize(), is(13));
        assertThat(datasource.getFetchOnInit(), is(false));

        assertThat(datasource.getSubmit(), allOf(
                hasProperty("storage", is(BrowserStorageTypeEnum.LOCAL_STORAGE)),
                hasProperty("type", is("browser")),
                hasProperty("auto", is(false)),
                hasProperty("model", is(ReduxModelEnum.FILTER)),
                hasProperty("key", is("submit_test_key"))
        ));

        assertThat(datasource.getProvider(), allOf(
                hasProperty("storage", is(BrowserStorageTypeEnum.LOCAL_STORAGE)),
                hasProperty("type", is("browser")),
                hasProperty("key", is("test_key"))
        ));

        assertThat(datasource.getDependencies().size(), is(2));
        Dependency dependency = datasource.getDependencies().get(0);
        assertThat(dependency.getOn(), is("models.resolve['testBrowserStorageDatasource_ds']"));
        assertThat(dependency.getType(), is(DependencyTypeEnum.FETCH));

        dependency = datasource.getDependencies().get(1);
        assertThat(dependency.getType(), is(DependencyTypeEnum.COPY));
        assertThat(dependency.getOn(), is("models.filter['testBrowserStorageDatasource_ds'].source"));
        assertThat(((CopyDependency) dependency).getModel(), is(ReduxModelEnum.FILTER));
        assertThat(((CopyDependency) dependency).getSubmit(), is(true));
        assertThat(((CopyDependency) dependency).getApplyOnInit(), is(true));


        // default
        datasource = (BrowserStorageDatasource) page.getDatasources().get("testBrowserStorageDatasource_ds2");
        assertThat(datasource.getPaging().getSize(), is(10));
        assertThat(datasource.getSubmit().getStorage(), is(BrowserStorageTypeEnum.SESSION_STORAGE));
        assertThat(datasource.getSubmit().getType(), is("browser"));
        assertThat(datasource.getSubmit().getAuto(), is(true));
        assertThat(datasource.getSubmit().getModel(), is(ReduxModelEnum.RESOLVE));
        assertThat(datasource.getSubmit().getKey(), is("test_key"));

        assertThat(datasource.getProvider().getStorage(), is(BrowserStorageTypeEnum.SESSION_STORAGE));
        assertThat(datasource.getProvider().getType(), is("browser"));
        assertThat(datasource.getProvider().getKey(), is("test_key"));
        assertThat(datasource.getFetchOnInit(), is(false));


        // default without submit
        datasource = (BrowserStorageDatasource) page.getDatasources().get("testBrowserStorageDatasource_ds3");
        assertThat(datasource.getSubmit(), nullValue());
        assertThat(datasource.getProvider().getKey(), is("ds3"));
        assertThat(datasource.getFetchOnInit(), is(true));
    }
}
