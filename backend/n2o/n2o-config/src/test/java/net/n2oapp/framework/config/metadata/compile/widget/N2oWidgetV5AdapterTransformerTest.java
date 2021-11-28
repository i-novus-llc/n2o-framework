package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oTable;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesMode.query;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class N2oWidgetV5AdapterTransformerTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllDataPack(), new N2oAllPagesPack());
    }

    @Test
    public void testWidgetV5adapterTransformer() {
        N2oTable table = read("net/n2oapp/framework/config/metadata/transformer/testWidgetTransformer.widget.xml")
                .merge().transform().get("testWidgetTransformer", N2oTable.class);
        assertThat(table.getDatasource().getQueryId(), is("test"));
        assertThat(table.getDatasource().getObjectId(), is("test"));
        assertThat(table.getDatasource().getDefaultValuesMode(), is(query));
        assertThat(table.getDatasource().getFilters().length, is(2));
        assertThat(table.getDatasource().getRoute(), is("/test"));
        assertThat(table.getDatasource().getSize(), is(10));
    }

    @Test
    public void testFormV5adapterTransformer() {
        N2oForm form = read("net/n2oapp/framework/config/metadata/transformer/testFormTransformer.widget.xml")
                .merge().transform().get("testFormTransformer", N2oForm.class);
        assertThat(form.getDatasource().getQueryId(), is("test"));
        assertThat(form.getDatasource().getSubmit().getOperationId(), is("save"));
        assertThat(form.getDatasource().getSubmit().getRoute(), is("/test"));
    }
}
