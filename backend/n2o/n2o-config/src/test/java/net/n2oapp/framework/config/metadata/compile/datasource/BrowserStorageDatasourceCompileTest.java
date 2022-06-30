package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.datasource.BrowserStorageDatasource;
import net.n2oapp.framework.api.metadata.datasource.BrowserStorageType;
import net.n2oapp.framework.api.metadata.meta.CopyDependency;
import net.n2oapp.framework.api.metadata.meta.DependencyConditionType;
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
                compile("net/n2oapp/framework/config/metadata/compile/datasource/testBrowserStorageDatasourceIOTest.page.xml")
                        .get(new PageContext("testBrowserStorageDatasourceIOTest"));

        BrowserStorageDatasource datasource = (BrowserStorageDatasource) page.getDatasources().get("testBrowserStorageDatasourceIOTest_test_id");
        assertThat(datasource.getSize(),is(13));

        assertThat(datasource.getDependencies().size(),is(2) );
        assertThat(datasource.getDependencies().get(0).getOn(),is("models.resolve['testBrowserStorageDatasourceIOTest_123']") );
        CopyDependency copyDependency = (CopyDependency) datasource.getDependencies().get(1);
        assertThat(copyDependency.getOn(), is("models.resolve['form_ds'].field_1"));
        assertThat(copyDependency.getField(), is("form"));
        assertThat(copyDependency.getType(), is(DependencyConditionType.copy));
        assertThat(copyDependency.getModel(), is(ReduxModel.resolve));

        assertThat(datasource.getSubmit().getStorage(),is(BrowserStorageType.localStorage));
        assertThat(datasource.getSubmit().getType(),is("browser"));
        assertThat(datasource.getSubmit().getAuto(),is(false));
        assertThat(datasource.getSubmit().getKey(),is("submit_test_key"));

        assertThat(datasource.getProvider().getStorage(),is(BrowserStorageType.localStorage));
        assertThat(datasource.getProvider().getType(),is("browser"));
        assertThat(datasource.getProvider().getKey(),is("test_key"));

        // default
        datasource = (BrowserStorageDatasource) page.getDatasources().get("testBrowserStorageDatasourceIOTest_test_id2");
        assertThat(datasource.getSize(),is(10));
        assertThat(datasource.getSubmit().getStorage(),is(BrowserStorageType.sessionStorage));
        assertThat(datasource.getSubmit().getType(),is("browser"));
        assertThat(datasource.getSubmit().getAuto(),is(true));
        assertThat(datasource.getSubmit().getKey(),is("test_id2"));

        assertThat(datasource.getProvider().getStorage(),is(BrowserStorageType.sessionStorage));
        assertThat(datasource.getProvider().getType(),is("browser"));
        assertThat(datasource.getProvider().getKey(),is("test_id2"));
    }
}
