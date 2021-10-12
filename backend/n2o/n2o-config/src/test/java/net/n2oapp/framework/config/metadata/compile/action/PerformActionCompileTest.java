package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.meta.action.Perform;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.action.PerformElementIOv1;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.N2oControlsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Тест компиляции custom action
 */
public class PerformActionCompileTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oControlsPack());
        builder.ios(new PerformElementIOv1());
        builder.compilers(new PerformCompiler());
    }

    @Test
    public void testCompileActions() {
        Form table = (Form) compile("net/n2oapp/framework/config/metadata/compile/action/testPerformAction.widget.xml")
                .get(new WidgetContext("testPerformAction"));
        assertThat(table.getActions().get("test"), notNullValue());
        assertThat(table.getActions().get("menuItem0"), notNullValue());
        assertThat(table.getActions().get("test") instanceof Perform, is(true));
        assertThat(table.getActions().get("menuItem0") instanceof Perform, is(true));
    }
}