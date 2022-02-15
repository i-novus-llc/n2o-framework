package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.api.metadata.meta.region.TabsRegion;
import net.n2oapp.framework.api.metadata.meta.region.scrollspy.GroupScrollspyElement;
import net.n2oapp.framework.api.metadata.meta.region.scrollspy.ScrollspyElement;
import net.n2oapp.framework.api.metadata.meta.region.scrollspy.ScrollspyRegion;
import net.n2oapp.framework.api.metadata.meta.region.scrollspy.SingleScrollspyElement;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Тестирование компиляции региона с отслеживанием прокрутки
 */
public class ScrollspyRegionCompileTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack());
    }

    @Test
    public void testScrollspyRegion() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/region/testScrollspyRegion.page.xml")
                .get(new PageContext("testScrollspyRegion"));

        assertThat(page.getRegions().size(), is(1));
        List<Region> regions = page.getRegions().get("single");
        assertThat(regions.size(), is(2));
        ScrollspyRegion region = ((ScrollspyRegion) regions.get(0));
        assertThat(region.getId(), is("r1"));
        assertThat(region.getTitle(), is("regionTitle"));
        assertThat(region.getSrc(), is("ScrollSpy"));
        assertThat(region.getClassName(), is("regionClass"));
        assertThat(region.getStyle().get("width"), is("500px"));
        assertThat(region.getActive(), is("mi2"));
        assertThat(region.getPlacement(), is("right"));
        assertThat(region.getHeadlines(), is(true));

        region =  ((ScrollspyRegion) regions.get(1));
        assertThat(region.getId(), is("scrollspy_1"));
        assertThat(region.getTitle(), nullValue());
        assertThat(region.getSrc(), is("ScrollSpyRegion"));
        assertThat(region.getClassName(), nullValue());
        assertThat(region.getStyle(), nullValue());
        assertThat(region.getActive(), nullValue());
        assertThat(region.getPlacement(), is("left"));
        assertThat(region.getHeadlines(), is(false));
    }

    @Test
    public void testElements() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/region/testScrollspyRegionElements.page.xml")
                .get(new PageContext("testScrollspyRegionElements"));

        assertThat(page.getRegions().size(), is(1));
        List<Region> regions = page.getRegions().get("single");
        assertThat(regions.size(), is(1));

        ScrollspyRegion region = ((ScrollspyRegion) regions.get(0));
        assertThat(region.getId(), is("scrollspy_0"));
        assertThat(region.getTitle(), nullValue());
        assertThat(region.getSrc(), is("ScrollSpyRegion"));
        assertThat(region.getClassName(), nullValue());
        assertThat(region.getStyle(), nullValue());
        assertThat(region.getActive(), is("mi1"));
        assertThat(region.getPlacement(), is("left"));
        assertThat(region.getHeadlines(), is(false));

        //single element with tabs
        ScrollspyElement element = region.getMenu().get(0);
        assertThat(element.getId(), is("mi1"));
        assertThat(element.getTitle(), is("First item title in the list"));
        assertThat(((SingleScrollspyElement) element).getContent().size(), is(2));
        assertThat(((TabsRegion) ((SingleScrollspyElement) element).getContent().get(0)).getId(), is("testScrollspyRegionElements_tabs_2"));
        assertThat(((TabsRegion) ((SingleScrollspyElement) element).getContent().get(0)).getItems().size(), is(2));
        assertThat(((TabsRegion) ((SingleScrollspyElement) element).getContent().get(0)).getItems().get(0).getId(), is("tab1"));
        assertThat(((TabsRegion) ((SingleScrollspyElement) element).getContent().get(0)).getItems().get(1).getId(), is("tab2"));
        assertThat(((SingleScrollspyElement) element).getContent().get(1), instanceOf(Form.class));
        assertThat(((Form) ((SingleScrollspyElement) element).getContent().get(1)).getName(), is("form1"));

        //single element with from and table
        element = region.getMenu().get(1);
        assertThat(element.getId(), is("element_scrollspy_5"));
        assertThat(element.getTitle(), is("Second item title in the list"));
        assertThat(((SingleScrollspyElement) element).getContent().size(), is(2));
        assertThat(((Table) ((SingleScrollspyElement) element).getContent().get(0)).getName(), is("table1"));
        assertThat(((Form) ((SingleScrollspyElement) element).getContent().get(1)).getName(), is("form2"));

        //group element
        element = region.getMenu().get(2);
        assertThat(element.getId(), is("element_scrollspy_6"));
        assertThat(element.getTitle(), is("Third item title in the list"));

        //single element in group
        assertThat(((GroupScrollspyElement) element).getMenu().size(), is(3));
        assertThat((((GroupScrollspyElement) element).getMenu().get(0)).getId(), is("mi2"));
        assertThat((((GroupScrollspyElement) element).getMenu().get(0)).getTitle(), is("Title1"));
        assertThat(((SingleScrollspyElement) ((GroupScrollspyElement) element).getMenu().get(0)).getContent().size(), is(2));
        assertThat(((Table) ((SingleScrollspyElement) ((GroupScrollspyElement) element).getMenu().get(0)).getContent().get(0)).getName(), is("table2"));
        assertThat(((Form) ((SingleScrollspyElement) ((GroupScrollspyElement) element).getMenu().get(0)).getContent().get(1)).getName(), is("form3"));

        //single element in group
        assertThat((((GroupScrollspyElement) element).getMenu().get(1)).getId(), is("element_scrollspy_8"));
        assertThat((((GroupScrollspyElement) element).getMenu().get(1)).getTitle(), is("Title2"));
        assertThat(((SingleScrollspyElement) ((GroupScrollspyElement) element).getMenu().get(1)).getContent().size(), is(1));
        assertThat(((Form) ((SingleScrollspyElement) ((GroupScrollspyElement) element).getMenu().get(1)).getContent().get(0)).getName(), is("form4"));

        //group element in group
        assertThat((((GroupScrollspyElement) element).getMenu().get(2)).getId(), is("element_scrollspy_9"));
        assertThat((((GroupScrollspyElement) element).getMenu().get(2)).getTitle(), is("Title3"));
        assertThat(((GroupScrollspyElement) ((GroupScrollspyElement) element).getMenu().get(2)).getMenu().size(), is(1));

        //single element in group element in group
        assertThat((((GroupScrollspyElement) ((GroupScrollspyElement) element).getMenu().get(2)).getMenu().get(0)).getId(), is("mi3"));
        assertThat((((GroupScrollspyElement) ((GroupScrollspyElement) element).getMenu().get(2)).getMenu().get(0)).getTitle(), is("Title4"));
        assertThat(((SingleScrollspyElement) ((GroupScrollspyElement) ((GroupScrollspyElement) element).getMenu().get(2)).getMenu().get(0)).getContent().size(), is(1));
        assertThat(((Form) ((SingleScrollspyElement) ((GroupScrollspyElement) ((GroupScrollspyElement) element).getMenu().get(2)).getMenu().get(0)).getContent().get(0)).getName(), is("form5"));
    }
}
