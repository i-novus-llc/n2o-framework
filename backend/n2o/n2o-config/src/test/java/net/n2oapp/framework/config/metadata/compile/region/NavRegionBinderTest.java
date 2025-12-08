package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.api.metadata.meta.menu.*;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.region.CompiledRegionItem;
import net.n2oapp.framework.api.metadata.meta.region.NavRegion;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тестирование связывания региона {@code <nav>} с данными
 */
class NavRegionBinderTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack());
    }

    @Test
    void test() {
        ReadCompileBindTerminalPipeline pipeline =
                bind("net/n2oapp/framework/config/metadata/compile/region/testNavRegionBinder.page.xml");
        PageContext context = new PageContext("testNavRegionBinder", "/user/:parentId");
        NavRegion region = (NavRegion) ((StandardPage) pipeline.get(context, new DataSet("parentId", 2)))
                .getRegions().get("single").getFirst();

        List<CompiledRegionItem> items = region.getContent();
        assertThat(((LinkAction) ((ButtonMenuItem) items.getFirst()).getAction()).getUrl(), is("/user/2/route1"));
        assertThat(((LinkMenuItem) items.get(1)).getUrl(), is("/user/2/route2"));
        assertThat(((LinkAction) ((MenuItem) items.get(2)).getAction()).getUrl(), is("/user/2/route3"));

        ArrayList<BaseMenuItem> subItems = ((DropdownMenuItem) items.get(3)).getContent();
        assertThat(((LinkAction) ((ButtonMenuItem) subItems.getFirst()).getAction()).getUrl(), is("/user/2/route4"));
        subItems = ((GroupMenuItem) subItems.get(1)).getContent();
        assertThat(((LinkMenuItem) subItems.getFirst()).getUrl(), is("/user/2/route5"));
        assertThat(((LinkAction) ((MenuItem) subItems.get(1)).getAction()).getUrl(), is("/user/2/route6"));
    }
}
