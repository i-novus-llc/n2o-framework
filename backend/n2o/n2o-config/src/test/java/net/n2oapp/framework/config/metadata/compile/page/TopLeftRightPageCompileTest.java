package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.framework.api.metadata.meta.page.TopLeftRightPage;
import net.n2oapp.framework.api.metadata.meta.region.*;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Тестирование компиляции страницы с тремя регионами
 */
public class TopLeftRightPageCompileTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oAllDataPack());
    }

    @Test
    public void topLeftRightPage() {
        TopLeftRightPage page = (TopLeftRightPage) compile("net/n2oapp/framework/config/metadata/compile/page/testTopLeftRightPage.page.xml")
                .get(new PageContext("testTopLeftRightPage"));

        assertThat(page.getNeedScrollButton(), is(true));
        assertThat(page.getPlaces().getTop().getWidth(), is("100%"));
        assertThat(page.getPlaces().getTop().getFixed(), is(true));
        assertThat(page.getPlaces().getTop().getOffset(), is(100));
        assertThat(page.getPlaces().getLeft().getWidth(), is("70%"));
        assertThat(page.getPlaces().getLeft().getFixed(), is(false));
        assertThat(page.getPlaces().getLeft().getOffset(), nullValue());
        assertThat(page.getPlaces().getRight().getWidth(), is("30%"));
        assertThat(page.getPlaces().getRight().getFixed(), is(true));
        assertThat(page.getPlaces().getRight().getOffset(), is(50));

        assertThat(page.getRegions().size(), is(3));
        List<Region> top = page.getRegions().get("top");
        List<Region> left = page.getRegions().get("left");
        List<Region> right = page.getRegions().get("right");

        assertThat(top.size(), is(3));
        assertThat(top.get(0), instanceOf(CustomRegion.class));
        assertThat(top.get(0).getContent().size(), is(2));
        assertThat(top.get(0).getContent().get(0), instanceOf(Form.class));
        assertThat(top.get(0).getContent().get(1), instanceOf(Table.class));
        assertThat(top.get(1), instanceOf(PanelRegion.class));
        assertThat(top.get(2), instanceOf(TabsRegion.class));

        assertThat(left.size(), is(2));
        assertThat(left.get(0), instanceOf(CustomRegion.class));
        assertThat(left.get(0).getContent().size(), is(2));
        assertThat(left.get(0).getContent().get(0), instanceOf(Form.class));
        assertThat(left.get(0).getContent().get(1), instanceOf(Table.class));
        assertThat(left.get(1), instanceOf(LineRegion.class));

        assertThat(right.size(), is(4));
        assertThat(right.get(0), instanceOf(PanelRegion.class));
        assertThat(right.get(1), instanceOf(TabsRegion.class));
        assertThat(right.get(2), instanceOf(CustomRegion.class));
        assertThat(right.get(3), instanceOf(CustomRegion.class));
        assertThat(right.get(3).getContent().size(), is(2));
        assertThat(right.get(3).getContent().get(0), instanceOf(Form.class));
        assertThat(right.get(3).getContent().get(1), instanceOf(Table.class));
    }
}
