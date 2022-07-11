package net.n2oapp.framework.config.metadata.merge.datasource;

import net.n2oapp.framework.api.metadata.application.StompDatasource;
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
 * Тестирование слияния двух STOMP источников данных
 */
public class N2oStompDatasourceMergerTest extends SourceMergerTestBase {
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
    public void mergeStompDatasource() {
        PageContext pageContext = new PageContext("testStompDatasourceMerger", "/");
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/merge/datasource/stomp/testStompDatasourceMerger.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/merge/datasource/stomp/testStompDatasourceMergerModal.page.xml"));
        builder.read().compile().get(pageContext);
        Page modal = builder.read().compile().get(builder.route("/modal", Page.class, null));

        StompDatasource datasource = (StompDatasource) modal.getDatasources().get("modal_sd");
        assertThat(datasource, notNullValue());
        assertThat(datasource.getValues().size(), is(1));
        assertThat(datasource.getValues().get(0).get("test"), is(123));

        assertThat(datasource.getProvider().getType(), is("stomp"));
        assertThat(datasource.getProvider().getDestination(), is("/dst"));
    }
}
