package net.n2oapp.framework.config.metadata.compile.field;

import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.metadata.meta.widget.form.FormWidgetComponent;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.PerformButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Submenu;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.control.plain.InputTextIOv2;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.control.InputTextCompiler;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class N2oFieldCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack(),
                new N2oObjectsPack(), new N2oFieldSetsPack());
        builder.ios(new InputTextIOv2());
        builder.compilers(new InputTextCompiler());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.page.xml"));
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.object.xml"));
    }

    @Test
    public void testFieldToolbar() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/field/testFieldToolbarCompile.page.xml")
                .get(new PageContext("testFieldToolbarCompile"));
        List<AbstractButton> toolbar = ((FormWidgetComponent) ((Widget) page.getRegions().get("single").get(0).getContent().get(0))
                .getComponent()).getFieldsets().get(0).getRows()
                .get(0).getCols().get(0).getFields().get(0).getToolbar()[0].getButtons();
        assertThat(toolbar.size(), is(2));
        assertThat(toolbar.get(0), instanceOf(PerformButton.class));
        assertThat(toolbar.get(1), instanceOf(Submenu.class));
        assertThat(((InvokeAction) toolbar.get(0).getAction()).getObjectId(), is("utBlank"));
        assertThat(((InvokeAction) toolbar.get(0).getAction()).getOperationId(), is("create"));
        assertThat(((InvokeAction) toolbar.get(0).getAction()).getType(), is("n2o/actionImpl/START_INVOKE"));

        Submenu submenu = (Submenu) toolbar.get(1);
        assertThat(((PerformButton) submenu.getSubMenu().get(0)).getUrl(), is("/testFieldToolbarCompile/testForm/n2o.i-novus.ru"));
    }
}
