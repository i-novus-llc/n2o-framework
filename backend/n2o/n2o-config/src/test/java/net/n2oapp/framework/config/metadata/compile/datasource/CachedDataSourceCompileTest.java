package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.datasource.BrowserStorageType;
import net.n2oapp.framework.api.metadata.datasource.CachedDatasource;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePlacement;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePosition;
import net.n2oapp.framework.api.metadata.meta.widget.RequestMethod;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.control.v3.plain.InputTextIOv3;
import net.n2oapp.framework.config.metadata.compile.context.ActionContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;

/**
 * Тестирование компиляции кэширующего источника данных
 */
class CachedDataSourceCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oAllDataPack()).ios(new InputTextIOv3())
                .sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/datasource/testDatasourceCompile.query.xml"),
                        new CompileInfo("net/n2oapp/framework/config/metadata/compile/datasource/testDataSourceCompile.object.xml"));
    }

    @Test
    void simple() {
        StandardPage page = (StandardPage)
                compile("net/n2oapp/framework/config/metadata/compile/datasource/testCachedDatasource.page.xml")
                        .get(new PageContext("testCachedDatasource"));

        CachedDatasource datasource = (CachedDatasource) page.getDatasources().get("testCachedDatasource_ds1");
        assertThat(datasource.getProvider().getSize(), is(12));
        assertThat(datasource.getProvider().getType(), is("cached"));
        assertThat(datasource.getProvider().getKey(), is("test_key"));
        assertThat(datasource.getProvider().getStorage(), is(BrowserStorageType.sessionStorage));
        assertThat(datasource.getProvider().getCacheExpires(), is("1d"));
        assertThat(datasource.getProvider().getUrl(), is("n2o/data/testCachedDatasource/test"));

        assertThat(datasource.getSubmit(), Matchers.notNullValue());
        assertThat(datasource.getSubmit().getUrl(), is("n2o/data/testCachedDatasource/sub"));
        assertThat(datasource.getSubmit().getSubmitForm(), is(false));
        assertThat(datasource.getSubmit().getMethod(), is(RequestMethod.POST));
        ActionContext opCtx = ((ActionContext) route("/testCachedDatasource/sub", CompiledObject.class));
        assertThat(opCtx.getOperationId(), is("update"));
        assertThat(opCtx.isMessageOnSuccess(), is(true));
        assertThat(opCtx.isMessageOnFail(), is(false));
        assertThat(opCtx.getMessagePosition(), is(MessagePosition.fixed));
        assertThat(opCtx.getMessagePlacement(), is(MessagePlacement.top));
        ModelLink link = new ModelLink(ReduxModel.resolve, "testCachedDatasource_ds1");
        link.setValue("`id`");
        assertThat(datasource.getSubmit().getFormMapping(), hasEntry("id", link));
        assertThat(datasource.getFetchOnInit(), is(false));

        datasource = (CachedDatasource) page.getDatasources().get("testCachedDatasource_ds2");
        assertThat(datasource.getFetchOnInit(), is(true));

        datasource = (CachedDatasource) page.getDatasources().get("testCachedDatasource_ds3");
        assertThat(datasource.getFetchOnInit(), is(false));
    }
}
