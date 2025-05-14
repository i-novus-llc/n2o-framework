package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.meta.action.clear.ClearAction;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.action.ClearActionElementIOV1;
import net.n2oapp.framework.config.metadata.compile.context.ModalPageContext;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class ClearActionCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack());
        builder.ios(new ClearActionElementIOV1());
        builder.compilers(new ClearActionCompiler());
    }

    @Test
    void clearActionTest() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/action/testClearAction.page.xml")
                .get(new ModalPageContext("testClearAction", "/p/w/a"));
        ClearAction testAction = (ClearAction) page.getWidget().getToolbar().getButton("test").getAction();
        assertThat(testAction.getType(), is("n2o/models/CLEAR"));
        assertThat(testAction.getPayload().getKey(), is("p_w_a_w1"));
        assertThat(testAction.getPayload().getPrefixes()[0], is("edit"));
        assertThat(testAction.getPayload().getPrefixes()[1], is("resolve"));
        assertThat(testAction.getMeta().getModalsToClose(), is(1));
    }

}
