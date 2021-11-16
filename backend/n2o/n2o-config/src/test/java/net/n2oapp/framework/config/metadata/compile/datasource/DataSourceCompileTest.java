package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.datasource.Datasource;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesMode;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePlacement;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePosition;
import net.n2oapp.framework.api.metadata.meta.widget.RequestMethod;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.ActionContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static java.util.Collections.emptyMap;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;

public class DataSourceCompileTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oAllDataPack())
                .sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/datasource/testDatasourceCompile.query.xml"),
                        new CompileInfo("net/n2oapp/framework/config/metadata/compile/datasource/testDataSourceCompile.object.xml"));
    }

    @Test
    public void simple() {
        StandardPage page = (StandardPage)
                compile("net/n2oapp/framework/config/metadata/compile/datasource/testDSSimple.page.xml")
                        .get(new PageContext("testDSSimple"));

        Datasource ds = page.getDatasources().get("ds1");
        assertThat(ds, notNullValue());
        assertThat(ds.getDefaultValuesMode(), is(DefaultValuesMode.defaults));
        assertThat(ds.getProvider(), nullValue());
    }

    @Test
    public void query() {
        StandardPage page = (StandardPage)
                compile("net/n2oapp/framework/config/metadata/compile/datasource/testDSQuery.page.xml")
                        .get(new PageContext("testDSQuery"));

        Datasource ds = page.getDatasources().get("ds1");
        assertThat(ds, notNullValue());
        assertThat(ds.getDefaultValuesMode(), is(DefaultValuesMode.query));
        assertThat(ds.getProvider(), notNullValue());
        assertThat(ds.getProvider().getUrl(), is("n2o/data/ds1"));
    }

    @Test
    public void queryFilters() {
        PageContext context = new PageContext("testDSQueryFilters", "p/w/a");
        context.setParentRoute("p/w");
        StandardPage page = (StandardPage)
                compile("net/n2oapp/framework/config/metadata/compile/datasource/testDSQueryFilters.page.xml")
                        .get(context);

        Datasource ds = page.getDatasources().get("ds1");
        assertThat(ds.getProvider().getUrl(), is("n2o/data/p/w/a/ds1"));
        assertThat(ds.getProvider().getQueryMapping(), hasEntry("id", new ModelLink(1)));

        ds = page.getDatasources().get("ds2");
        assertThat(ds.getProvider().getUrl(), is("n2o/data/p/w/a/ds2"));
        assertThat(ds.getProvider().getQueryMapping(), hasEntry("id", new ModelLink(ReduxModel.RESOLVE, "p_w_a_ds3", "id")));
    }

    @Test
    public void fetch() {
        StandardPage page = (StandardPage)
                compile("net/n2oapp/framework/config/metadata/compile/datasource/testDSFetch.page.xml")
                        .get(new PageContext("testDSFetch", "p/w/a"));

        Datasource ds = page.getDatasources().get("detail");
        assertThat(ds.getDependencies().size(), is(1));
        assertThat(ds.getDependencies().get(0).getOn(), is("models.resolve[\"p_w_a_master\"]"));
    }

    @Test
    public void submit() {
        StandardPage page = (StandardPage)
                compile("net/n2oapp/framework/config/metadata/compile/datasource/testDSSubmit.page.xml")
                        .get(new PageContext("testDSSubmit", "p/w/a"));

        //        simple
        Datasource ds = page.getDatasources().get("ds1");
        assertThat(ds.getSubmit(), Matchers.notNullValue());
        assertThat(ds.getSubmit().getUrl(), is("n2o/data/p/w/a/ds1"));
        assertThat(ds.getSubmit().getSubmitForm(), is(true));
        assertThat(ds.getSubmit().getMethod(), is(RequestMethod.POST));
        ActionContext opCtx = ((ActionContext)route("p/w/a/ds1", CompiledObject.class));
        assertThat(opCtx.getOperationId(), is("update"));
        assertThat(opCtx.isMessageOnSuccess(), is(false));
        assertThat(opCtx.isMessageOnFail(), is(true));
        assertThat(opCtx.getMessagePosition(), is(MessagePosition.fixed));
        assertThat(opCtx.getMessagePlacement(), is(MessagePlacement.top));

        //        with form-param
        ds = page.getDatasources().get("ds2");
        assertThat(ds.getSubmit(), Matchers.notNullValue());
        assertThat(ds.getSubmit().getSubmitForm(), is(false));
        assertThat(ds.getSubmit().getFormMapping(), hasEntry("id", new ModelLink(ReduxModel.RESOLVE, "p_w_a_ds2", "id")));

        //        with messages
        ds = page.getDatasources().get("ds3");
        assertThat(ds.getSubmit(), Matchers.notNullValue());
        opCtx = ((ActionContext)route("p/w/a/ds3", CompiledObject.class));
        assertThat(opCtx.isMessageOnSuccess(), is(true));
        assertThat(opCtx.isMessageOnFail(), is(true));
        assertThat(opCtx.getMessagePosition(), is(MessagePosition.fixed));
        assertThat(opCtx.getMessagePlacement(), is(MessagePlacement.bottom));

        //        with path-param
        ds = page.getDatasources().get("ds4");
        assertThat(ds.getSubmit(), Matchers.notNullValue());
        assertThat(ds.getSubmit().getUrl(), is("n2o/data/p/w/a/:_id/update"));
        assertThat(ds.getSubmit().getPathMapping(), hasEntry("_id", new ModelLink(ReduxModel.RESOLVE, "p_w_a_ds4", "id")));
        opCtx = ((ActionContext)route("p/w/a/123/update", CompiledObject.class));
        assertThat(opCtx, Matchers.notNullValue());
        assertThat(opCtx.getParams("p/w/a/123/update", emptyMap()), hasEntry("_id", 123));
    }
}
