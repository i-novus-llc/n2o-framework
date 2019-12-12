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

public class StandardFieldFilterCompileTest extends SourceCompileTestBase {
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
        builder.compilers(new InputTextCompiler());
    }

    @Test
    public void testStandardFieldFilter() {
        Page page = compile("net/n2oapp/framework/config/metadata/compile/control/testStandardFieldFilter.page.xml",
                "net/n2oapp/framework/config/metadata/compile/control/testStandardFieldFilter.query.xml")
                .get(new PageContext("testStandardFieldFilter"));

        List<Filter> filters = page.getWidgets().get("testStandardFieldFilter_main").getFilters();
        assertThat(filters.size(), is(2));
        // стандартное определение фильтра поля
        assertThat(filters.get(0).getFilterId(), is("minPrice"));
        assertThat(filters.get(0).getLink().getValue(), is("`minPrice`"));
        // определение фильтра поля с помощью filter-id
        assertThat(filters.get(1).getFilterId(), is("maxP"));
        assertThat(filters.get(1).getLink().getValue(), is("`maxPrice`"));
    }
}
