package net.n2oapp.framework.config.metadata.compile.widget.list;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.ListWidget;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ListWidgetBinderTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(
                new N2oAllDataPack(),
                new N2oPagesPack(),
                new N2oWidgetsPack(),
                new N2oRegionsPack(),
                new N2oActionsPack(),
                new N2oCellsPack()
        );
    }

    @Test
    public void rowClickResolve() {
        ReadCompileBindTerminalPipeline pipeline = bind("net/n2oapp/framework/config/metadata/compile/widgets/testListWidgetRowClick.page.xml",
                "net/n2oapp/framework/config/metadata/compile/query/testEmptyBody.query.xml");
        PageContext context = new PageContext("testListWidgetRowClick", "/p/w/:param/row");
        ListWidget listWidget = (ListWidget) ((StandardPage) pipeline.get(context, new DataSet().add("param", "1")))
                .getRegions().get("right").get(0).getContent().get(8);
        assertThat(listWidget.getRowClick().getUrl(), is("https://www.google.com/"));
        assertThat(listWidget.getRowClick().getTarget(), is(Target.self));
    }
}
