package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class SubmitGeneratorTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
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
    void submitLabelFromButton() {
        read().compile().get(new PageContext("testSubmitGeneratorButton", "/p"));
        SimplePage page = (SimplePage) routeAndGet("/p/test1", Page.class);
        AbstractButton submit = page.getToolbar().getButton("submit");
        assertThat(submit, notNullValue());
        assertThat(submit.getLabel(), is("button"));

        page = (SimplePage) routeAndGet("/p/test2", Page.class);
        submit = page.getToolbar().getButton("submit");
        assertThat(submit, notNullValue());
        assertThat(submit.getLabel(), is("operation"));

        page = (SimplePage) routeAndGet("/p/test3", Page.class);
        submit = page.getToolbar().getButton("submit");
        assertThat(submit, notNullValue());
        assertThat(submit.getLabel(), is("Сохранить"));
    }
}
