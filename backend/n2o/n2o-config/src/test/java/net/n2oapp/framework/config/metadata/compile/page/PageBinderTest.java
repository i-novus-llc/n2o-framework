package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.meta.Page;
import net.n2oapp.framework.api.metadata.meta.control.DefaultValues;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

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
}
