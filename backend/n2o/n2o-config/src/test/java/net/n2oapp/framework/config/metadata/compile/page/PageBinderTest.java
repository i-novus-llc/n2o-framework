package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.meta.Breadcrumb;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.Page;
import net.n2oapp.framework.api.metadata.meta.control.DefaultValues;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class PageBinderTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.getEnvironment().getContextProcessor().set("test", "Test");
        builder.packs(new N2oAllDataPack(), new N2oFieldSetsPack(), new N2oControlsPack(), new N2oPagesPack(),
                new N2oWidgetsPack(), new N2oRegionsPack());
    }

    @Test
    public void fieldsResolve() {
        Page page = bind("net/n2oapp/framework/config/metadata/compile/page/testPageBinders.page.xml")
                .get(new PageContext("testPageBinders"), new DataSet());
        assertThat(page.getModels().get("resolve['testPageBinders_main'].name").getValue(), is("Test"));
        assertThat(page.getModels().get("resolve['testPageBinders_main'].gender").getBindLink(), nullValue());
        assertThat(((DefaultValues)page.getModels().get("resolve['testPageBinders_main'].gender").getValue()).getValues().get("id"), is("#{test}"));
        assertThat(page.getModels().get("resolve['testPageBinders_main'].birthday").getBindLink(), nullValue());
        assertThat(((DefaultValues)page.getModels().get("resolve['testPageBinders_main'].birthday").getValue()).getValues().get("begin"), is("01.11.2018"));
        assertThat(((DefaultValues)page.getModels().get("resolve['testPageBinders_main'].birthday").getValue()).getValues().get("end"), is("11.11.2018"));
    }

    @Test
    public void pageNameResolve() {
        PageContext context = new PageContext("testPageBinders", "/page/:name_param/view");
        context.setParentModelLink(new ModelLink(ReduxModel.RESOLVE, "page_master"));
        context.setParentWidgetId("page_master");
        context.setParentRoute("/page");
        ModelLink modelLink = new ModelLink(ReduxModel.RESOLVE, "page_master");
        modelLink.setValue("`name`");
        context.setPathRouteMapping(Collections.singletonMap("name_param", modelLink));
        Page page = bind("net/n2oapp/framework/config/metadata/compile/page/testPageBinders.page.xml")
                .get(context, new DataSet().add("name_param", "Joe"));
        assertThat(page.getPageProperty().getTitle(), is("Hello, Joe"));
    }

    @Test
    public void pageBreadcrumbResolve() {
        PageContext context = new PageContext("testPageBinders", "/page/:name_param/view");
        context.setParentModelLink(new ModelLink(ReduxModel.RESOLVE, "page_master"));
        context.setParentWidgetId("page_master");
        context.setParentRoute("/page");
        ModelLink modelLink = new ModelLink(ReduxModel.RESOLVE, "page_master");
        modelLink.setValue("`name`");
        context.setPathRouteMapping(Collections.singletonMap("name_param", modelLink));
        context.setBreadcrumbs(Collections.singletonList(new Breadcrumb("prev", "/page")));
        Page page = bind("net/n2oapp/framework/config/metadata/compile/page/testPageBinders.page.xml")
                .get(context, new DataSet().add("name_param", "Joe"));
        assertThat(page.getBreadcrumb().get(1).getLabel(), is("Hello, Joe"));
    }
}
