package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.meta.Filter;
import net.n2oapp.framework.api.metadata.meta.Page;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class IntervalFieldFilterCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oControlsV2IOPack(), new N2oCellsPack(), new N2oAllDataPack());
        builder.compilers(new DateIntervalCompiler());
    }

    @Test
    public void testFieldFilter() {
        Page page = compile("net/n2oapp/framework/config/metadata/compile/control/testIntervalFieldFilter.page.xml",
                "net/n2oapp/framework/config/metadata/compile/control/testIntervalFieldFilter.query.xml")
                .get(new PageContext("testIntervalFieldFilter"));

        List<Filter> filters = page.getWidgets().get("testIntervalFieldFilter_main").getFilters();
        assertThat(filters.size(), is(6));
        // стандартное определение фильтров поля
        assertThat(filters.get(0).getFilterId(), is("date1.end"));
        assertThat(filters.get(0).getLink().getValue(), is("`date1.end`"));
        assertThat(filters.get(1).getFilterId(), is("date1.begin"));
        assertThat(filters.get(1).getLink().getValue(), is("`date1.begin`"));
        // определение фильтра поля с помощью begin-filter-id
        assertThat(filters.get(2).getFilterId(), is("start2"));
        assertThat(filters.get(2).getLink().getValue(), is("`date2.begin`"));
        // определение фильтра поля с помощью end-filter-id
        assertThat(filters.get(3).getFilterId(), is("end3"));
        assertThat(filters.get(3).getLink().getValue(), is("`date3.end`"));
        // определение фильтров поля с помощью begin-filter-id и end-filter-id
        assertThat(filters.get(4).getFilterId(), is("start4"));
        assertThat(filters.get(4).getLink().getValue(), is("`date4.begin`"));
        assertThat(filters.get(5).getFilterId(), is("end4"));
        assertThat(filters.get(5).getLink().getValue(), is("`date4.end`"));
    }
}
