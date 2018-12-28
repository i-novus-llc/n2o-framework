package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.meta.action.CustomAction;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.action.CloseActionElementIOV1;
import net.n2oapp.framework.config.io.action.CustomActionIOv1;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тест компиляции custom action
 */
public class CustomActionCompileTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oControlsPack());
        builder.ios(new CustomActionIOv1());
        builder.compilers(new CustomActionCompiler());
    }

    @Test
    public void testCompileActions() {
        Table table = (Table) compile("net/n2oapp/framework/config/metadata/compile/action/testCustomAction.widget.xml")
                .get(new WidgetContext("testCustomAction"));
        assertThat(((CustomAction)table.getActions().get("test")).getSrc(),is("mySrc"));
        assertThat(((CustomAction)table.getActions().get("menuItem0")).getSrc(),is("mySrc"));
    }
}