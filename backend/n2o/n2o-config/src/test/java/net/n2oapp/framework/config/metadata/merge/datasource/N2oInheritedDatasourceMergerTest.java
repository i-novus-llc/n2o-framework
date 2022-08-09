package net.n2oapp.framework.config.metadata.merge.datasource;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.datasource.InheritedDatasource;
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
 * Тестирование слияния двух источников данных, получающих данные из другого источника данных
 */
public class N2oInheritedDatasourceMergerTest extends SourceMergerTestBase {
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
    public void mergeInheritedDatasource() {
        PageContext pageContext = new PageContext("testInheritedDatasourceMerger", "/");
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/merge/datasource/inherited/testInheritedDatasourceMerger.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/merge/datasource/inherited/testInheritedDatasourceMergerModal.page.xml"));
        builder.read().compile().get(pageContext);
        Page modal = builder.read().compile().get(builder.route("/modal", Page.class, null));

        InheritedDatasource datasource = (InheritedDatasource) modal.getDatasources().get("modal_ids");
        assertThat(datasource, notNullValue());
        assertThat(datasource.getSize(), is(15));

        assertThat(datasource.getProvider().getType(), is("inherited"));
        assertThat(datasource.getProvider().getSourceDs(), is("modal_ids"));
        assertThat(datasource.getProvider().getSourceModel(), is(ReduxModel.filter));
        assertThat(datasource.getProvider().getSourceField(), is("field1"));

        assertThat(datasource.getSubmit().getAuto(), is(false));
        assertThat(datasource.getSubmit().getModel(), is(ReduxModel.edit));
        assertThat(datasource.getSubmit().getTargetDs(), is("modal_ids"));
        assertThat(datasource.getSubmit().getTargetModel(), is(ReduxModel.filter));
        assertThat(datasource.getSubmit().getTargetField(), is("field2"));

        assertThat(datasource.getDependencies().size(), is(1));
        assertThat(datasource.getDependencies().get(0).getOn(), is("models.filter['modal_ids']"));
    }
}

