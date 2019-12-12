package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.meta.Filter;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.Page;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertTrue;
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
    public void testIntervalFieldFilter() {
        Page page = compile("net/n2oapp/framework/config/metadata/compile/control/testIntervalFieldFilter.page.xml",
                "net/n2oapp/framework/config/metadata/compile/control/testIntervalFieldFilter.query.xml")
                .get(new PageContext("testIntervalFieldFilter"));

        Map<String, ModelLink> filtersLink = (Map<String, ModelLink>) page.getWidgets().get("testIntervalFieldFilter_main").getFilters()
                .stream().collect(Collectors.toMap(Filter::getFilterId, Filter::getLink));

        assertThat(filtersLink.size(), is(6));
        // стандартное определение фильтров поля
        assertTrue(filtersLink.containsKey("date1.begin"));
        assertThat(filtersLink.get("date1.begin").getValue(), is("`date1.begin`"));
        assertTrue(filtersLink.containsKey("date1.end"));
        assertThat(filtersLink.get("date1.end").getValue(), is("`date1.end`"));
        // определение фильтра поля с помощью begin-filter-id
        assertTrue(filtersLink.containsKey("start2"));
        assertThat(filtersLink.get("start2").getValue(), is("`date2.begin`"));
        // определение фильтра поля с помощью end-filter-id
        assertTrue(filtersLink.containsKey("end3"));
        assertThat(filtersLink.get("end3").getValue(), is("`date3.end`"));
        // определение фильтров поля с помощью begin-filter-id и end-filter-id
        assertTrue(filtersLink.containsKey("start4"));
        assertThat(filtersLink.get("start4").getValue(), is("`date4.begin`"));
        assertTrue(filtersLink.containsKey("end4"));
        assertThat(filtersLink.get("end4").getValue(), is("`date4.end`"));
    }
}
