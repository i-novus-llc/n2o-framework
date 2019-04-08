package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.metadata.meta.Page;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Button;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class SubmitGeneratorTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllDataPack(), new N2oAllPagesPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/toolbar/testSubmitGeneratorObject.object.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/toolbar/testSubmitGeneratorButton.page.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/toolbar/testSubmitGeneratorPage.page.xml"));
    }

    @Test
    public void submitLabelFromButton() {
        read().compile().get(new PageContext("testSubmitGeneratorButton", "/p"));
        Page page = routeAndGet("/p/main/1/test1", Page.class);
        Button submit = page.getToolbar().getButton("submit");
        assertThat(submit, notNullValue());
        assertThat(submit.getLabel(), is("button"));

        page = routeAndGet("/p/main/1/test2", Page.class);
        submit = page.getToolbar().getButton("submit");
        assertThat(submit, notNullValue());
        assertThat(submit.getLabel(), is("operation"));
    }
}
