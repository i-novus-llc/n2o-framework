package net.n2oapp.framework.config.metadata.merge;

import net.n2oapp.framework.api.metadata.datasource.Datasource;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesMode;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceMergerTestBase;
import org.junit.Before;
import org.junit.Test;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование слияния источников данных
 */
public class N2oDatasourceMergeTest extends SourceMergerTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oQueriesPack(), new N2oObjectsPack());
    }

    /**
     * Тестирование слияния источника данных, переданного из родительской страницы, с текущими источниками
     */
    @Test
    public void mergePageContextDatasource() {
        PageContext pageContext = new PageContext("testDatasourceMerger", "/");
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/merge/testDatasourceMerger.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/merge/testDatasourceMergerModal1.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/merge/testDatasourceMergerModal2.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/merge/testDatasourceMerger.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/merge/testDatasourceMerger.object.xml"));
        Page page = builder.read().compile().get(pageContext);
        Page modal1 = builder.read().compile().get(builder.route("/modal1", Page.class, null));
        Datasource mainDs = modal1.getDatasources().get("modal1_main");
        assertThat(mainDs, notNullValue());
        assertThat(mainDs.getDefaultValuesMode(), is(DefaultValuesMode.query));
        assertThat(mainDs.getProvider(), notNullValue());
        assertThat(mainDs.getProvider().getQueryMapping().get("main_id_eq"), notNullValue());

        Page modal2 = builder.read().compile().get(builder.route("/modal2", Page.class, null));
        assertThat(modal2.getDatasources().get("modal2_ds1"), notNullValue());
        Datasource ds2 = modal2.getDatasources().get("modal2_ds2");
        assertThat(ds2, notNullValue());
        assertThat(ds2.getDefaultValuesMode(), is(DefaultValuesMode.query));
        assertThat(ds2.getProvider(), notNullValue());
        assertThat(ds2.getProvider().getQueryMapping().get("ds2_id_eq"), notNullValue());
        assertThat(ds2.getDependencies().size(), is(1));
    }
}
