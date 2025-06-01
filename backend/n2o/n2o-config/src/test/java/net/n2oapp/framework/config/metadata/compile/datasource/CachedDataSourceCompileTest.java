package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.datasource.BrowserStorageTypeEnum;
import net.n2oapp.framework.api.metadata.datasource.CachedDatasource;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.CopyDependency;
import net.n2oapp.framework.api.metadata.meta.Dependency;
import net.n2oapp.framework.api.metadata.meta.DependencyTypeEnum;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePlacementEnum;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePositionEnum;
import net.n2oapp.framework.api.metadata.meta.widget.RequestMethodEnum;
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
    public void setUp() {
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
        assertThat(datasource.getProvider().getStorage(), is(BrowserStorageTypeEnum.SESSION_STORAGE));
        assertThat(datasource.getProvider().getCacheExpires(), is("1d"));
        assertThat(datasource.getProvider().getUrl(), is("n2o/data/testCachedDatasource/test"));

        assertThat(datasource.getSubmit(), Matchers.notNullValue());
        assertThat(datasource.getSubmit().getUrl(), is("n2o/data/testCachedDatasource/sub"));
        assertThat(datasource.getSubmit().getSubmitForm(), is(false));
        assertThat(datasource.getSubmit().getMethod(), is(RequestMethodEnum.POST));
        ActionContext opCtx = ((ActionContext) route("/testCachedDatasource/sub", CompiledObject.class));
        assertThat(opCtx.getOperationId(), is("update"));
        assertThat(opCtx.isMessageOnSuccess(), is(true));
        assertThat(opCtx.isMessageOnFail(), is(false));
        assertThat(opCtx.getMessagePosition(), is(MessagePositionEnum.FIXED));
        assertThat(opCtx.getMessagePlacement(), is(MessagePlacementEnum.TOP));
        ModelLink link = new ModelLink(ReduxModelEnum.RESOLVE, "testCachedDatasource_ds1");
        link.setValue("`id`");
        assertThat(datasource.getSubmit().getFormMapping(), hasEntry("id", link));
        assertThat(datasource.getFetchOnInit(), is(false));
        assertThat(datasource.getDependencies().size(), is(2));
        Dependency dependency = datasource.getDependencies().get(0);
        assertThat(dependency.getOn(), is("models.filter['testCachedDatasource_master']"));
        assertThat(dependency.getType(), is(DependencyTypeEnum.FETCH));

        dependency = datasource.getDependencies().get(1);
        assertThat(dependency.getType(), is(DependencyTypeEnum.COPY));
        assertThat(dependency.getOn(), is("models.filter['testCachedDatasource_master'].source"));
        assertThat(((CopyDependency) dependency).getModel(), is(ReduxModelEnum.DATASOURCE));
        assertThat(((CopyDependency) dependency).getField(), is("target"));
        assertThat(((CopyDependency) dependency).getSubmit(), is(true));
        assertThat(((CopyDependency) dependency).getApplyOnInit(), is(true));

        datasource = (CachedDatasource) page.getDatasources().get("testCachedDatasource_ds2");
        assertThat(datasource.getFetchOnInit(), is(true));

        datasource = (CachedDatasource) page.getDatasources().get("testCachedDatasource_ds3");
        assertThat(datasource.getFetchOnInit(), is(false));
    }
}
