package net.n2oapp.framework.config.metadata.compile.field;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.control.v2.plain.InputTextIOv2;
import net.n2oapp.framework.config.metadata.compile.control.InputTextCompiler;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;

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
        builder.propertySources("application-test.properties");
    }

//    @Test FIXME
//    public void testFieldToolbar() {
//        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/field/testFieldToolbarCompile.page.xml")
//                .get(new PageContext("testFieldToolbarCompile"));
//        Group[] toolbar = ((FormWidgetComponent) ((Widget) page.getRegions().get("single").get(0).getContent().get(0))
//                .getComponent()).getFieldsets().get(0).getRows()
//                .get(0).getCols().get(0).getFields().get(0).getToolbar();
//
//        assertThat(toolbar.length, is(2));
//        List<AbstractButton> buttons = toolbar[0].getButtons();
//        assertThat(buttons.size(), is(1));
//        assertThat(buttons.get(0), instanceOf(PerformButton.class));
//        assertThat(((InvokeAction) buttons.get(0).getAction()).getObjectId(), is("utBlank"));
//        assertThat(((InvokeAction) buttons.get(0).getAction()).getOperationId(), is("create"));
//        assertThat(((InvokeAction) buttons.get(0).getAction()).getType(), is("n2o/actionImpl/START_INVOKE"));
//
//        buttons = toolbar[1].getButtons();
//        assertThat(buttons.size(), is(1));
//        assertThat(buttons.get(0), instanceOf(Submenu.class));
//        Submenu submenu = (Submenu) buttons.get(0);
//        assertThat(submenu.getSubMenu().get(0).getUrl(), is("/testFieldToolbarCompile/n2o.i-novus.ru"));
//    }
}
