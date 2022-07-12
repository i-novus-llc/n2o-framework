package net.n2oapp.framework.config.metadata.merge.datasource;

import net.n2oapp.framework.api.metadata.datasource.StandardDatasource;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesMode;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oObjectsPack;
import net.n2oapp.framework.config.metadata.pack.N2oQueriesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceMergerTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование слияния стандартных источников данных
 */
public class N2oStandardDatasourceMergerTest extends SourceMergerTestBase {
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

    @Test
    public void mergeStandardDatasource() {
        PageContext pageContext = new PageContext("testStandardDatasourceMerger", "/");
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/merge/datasource/standard/testStandardDatasourceMerger.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/merge/datasource/standard/testStandardDatasourceMergerModal1.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/merge/datasource/standard/testStandardDatasourceMergerModal2.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/merge/datasource/standard/testStandardDatasourceMerger.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/merge/datasource/standard/testStandardDatasourceMerger.object.xml"));
        builder.read().compile().get(pageContext);
        Page modal1 = builder.read().compile().get(builder.route("/modal1", Page.class, null));
        StandardDatasource mainDs = (StandardDatasource) modal1.getDatasources().get("modal1_main");
        assertThat(mainDs, notNullValue());
        assertThat(mainDs.getDefaultValuesMode(), is(DefaultValuesMode.query));
        assertThat(mainDs.getProvider(), notNullValue());
        assertThat(mainDs.getProvider().getQueryMapping().get("main_id_eq"), notNullValue());

        Page modal2 = builder.read().compile().get(builder.route("/modal2", Page.class, null));
        assertThat(modal2.getDatasources().get("modal2_ds1"), notNullValue());
        StandardDatasource ds2 = (StandardDatasource) modal2.getDatasources().get("modal2_ds2");
        assertThat(ds2, notNullValue());
        assertThat(ds2.getDefaultValuesMode(), is(DefaultValuesMode.query));
        assertThat(ds2.getProvider(), notNullValue());
        assertThat(ds2.getProvider().getQueryMapping().get("ds2_id_eq"), notNullValue());
        assertThat(ds2.getDependencies().size(), is(1));
    }
}
