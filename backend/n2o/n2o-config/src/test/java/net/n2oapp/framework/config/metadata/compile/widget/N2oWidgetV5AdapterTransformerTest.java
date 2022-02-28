package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oTable;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.N2oCompileProcessor;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesMode.query;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        N2oCompileProcessor p = mock(N2oCompileProcessor.class);
        N2oTable table = read("net/n2oapp/framework/config/metadata/transformer/testWidgetTransformer.widget.xml")
                .merge().transform().get("testWidgetTransformer", N2oTable.class);
        assertThat(table.getDatasource().getQueryId(), is("test"));
        assertThat(table.getDatasource().getObjectId(), is("test"));
        assertThat(table.getDatasource().getDefaultValuesMode(), is(query));
        assertThat(table.getDatasource().getFilters().length, is(3));
        assertThat(table.getDatasource().getFilters()[2].getDatasource(), is("f1"));
        assertThat(table.getDatasource().getFilters()[2].getFieldId(), is("id"));
        assertThat(table.getDatasource().getFilters()[2].getModel(), is(ReduxModel.resolve));
        assertThat(table.getDatasource().getRoute(), is("/test"));
        assertThat(table.getDatasource().getSize(), is(10));
        assertThat(table.getDatasource().getDependencies().length, is(1));
        assertThat(((N2oDatasource.FetchDependency)table.getDatasource().getDependencies()[0]).getModel(), is(ReduxModel.resolve));
        assertThat(((N2oDatasource.FetchDependency)table.getDatasource().getDependencies()[0]).getOn(), is("f1"));
        assertThat(table.getDependencies().length, is(1));
        assertThat(table.getDependencies()[0].getDatasource(), is("f1"));
        assertThat(table.getDependencies()[0].getValue(), is("test==1"));
    }

    @Test
    public void testFormV5adapterTransformer() {
        N2oForm form = read("net/n2oapp/framework/config/metadata/transformer/testFormTransformer.widget.xml")
                .merge().transform().get("testFormTransformer", N2oForm.class);
        assertThat(form.getDatasource().getQueryId(), is("test"));
        assertThat(form.getDatasource().getSubmit().getOperationId(), is("save"));
        assertThat(form.getDatasource().getSubmit().getRoute(), is("/test"));
        assertThat(form.getDatasource().getSubmit().getRefreshDatasources().length, is(1));
        assertThat(form.getDatasource().getSubmit().getRefreshDatasources()[0], is("f1"));
    }

    @Test
    public void testTableV5adapterTransformer() {
        N2oTable table = read("net/n2oapp/framework/config/metadata/transformer/testTableTransformer.widget.xml")
                .merge().transform().get("testTableTransformer", N2oTable.class);
        assertThat(table.getDatasource().getQueryId(), is("test"));
        assertThat(table.getDatasource().getObjectId(), is("test"));
        assertThat(table.getFiltersDatasource().getQueryId(), is("test"));
    }
}
