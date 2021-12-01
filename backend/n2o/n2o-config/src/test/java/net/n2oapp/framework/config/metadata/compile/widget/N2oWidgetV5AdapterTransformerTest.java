package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oTable;
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
        PageScope pageScope = new PageScope();
        HashMap<String, String> widgetIdSourceDatasourceMap = new HashMap<>();
        widgetIdSourceDatasourceMap.put("f1", "ds1");
        pageScope.setWidgetIdSourceDatasourceMap(widgetIdSourceDatasourceMap);
        when(p.getScope(PageScope.class)).thenReturn(pageScope);
        N2oTable table = read("net/n2oapp/framework/config/metadata/transformer/testWidgetTransformer.widget.xml")
                .merge().transform().get("testWidgetTransformer", N2oTable.class, p);
        assertThat(table.getDatasource().getQueryId(), is("test"));
        assertThat(table.getDatasource().getObjectId(), is("test"));
        assertThat(table.getDatasource().getDefaultValuesMode(), is(query));
        assertThat(table.getDatasource().getFilters().length, is(3));
        assertThat(table.getDatasource().getFilters()[2].getDatasource(), is("ds1"));
        assertThat(table.getDatasource().getFilters()[2].getFieldId(), is("id"));
        assertThat(table.getDatasource().getFilters()[2].getParam(), is("ds1_master"));
        assertThat(table.getDatasource().getFilters()[2].getModel(), is(ReduxModel.RESOLVE));
        assertThat(table.getDatasource().getRoute(), is("/test"));
        assertThat(table.getDatasource().getSize(), is(10));
        assertThat(table.getDatasource().getDependencies().length, is(1));
        assertThat(((N2oDatasource.FetchDependency)table.getDatasource().getDependencies()[0]).getModel(), is(ReduxModel.RESOLVE));
        assertThat(((N2oDatasource.FetchDependency)table.getDatasource().getDependencies()[0]).getOn(), is("ds1"));
        assertThat(table.getDependencies().length, is(1));
        assertThat(table.getDependencies()[0].getDatasource(), is("ds1"));
        assertThat(table.getDependencies()[0].getValue(), is("test==1"));
    }

    @Test
    public void testFormV5adapterTransformer() {
        N2oCompileProcessor p = mock(N2oCompileProcessor.class);
        PageScope pageScope = new PageScope();
        HashMap<String, String> widgetIdSourceDatasourceMap = new HashMap<>();
        widgetIdSourceDatasourceMap.put("f1", "ds1");
        pageScope.setWidgetIdSourceDatasourceMap(widgetIdSourceDatasourceMap);
        when(p.getScope(PageScope.class)).thenReturn(pageScope);
        N2oForm form = read("net/n2oapp/framework/config/metadata/transformer/testFormTransformer.widget.xml")
                .merge().transform().get("testFormTransformer", N2oForm.class, p);
        assertThat(form.getDatasource().getQueryId(), is("test"));
        assertThat(form.getDatasource().getSubmit().getOperationId(), is("save"));
        assertThat(form.getDatasource().getSubmit().getRoute(), is("/test"));
        assertThat(form.getDatasource().getSubmit().getRefreshDatasources().length, is(1));
        assertThat(form.getDatasource().getSubmit().getRefreshDatasources()[0], is("ds1"));
        assertThat(form.getDatasource().getSubmit().getMessageWidgetId(), is("testFormTransformer"));
    }
}
