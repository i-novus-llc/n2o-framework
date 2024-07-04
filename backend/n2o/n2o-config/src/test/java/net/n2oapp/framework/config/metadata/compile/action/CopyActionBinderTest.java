package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.meta.action.copy.CopyAction;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oActionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class CopyActionBinderTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oActionsPack(), new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack());
    }

    @Test
    void bindItems() {
        Page page = bind("net/n2oapp/framework/config/metadata/compile/action/copy/testCopyActionBinder.page.xml")
                .get(new PageContext("testCopyActionBinder"), new DataSet("idx", 2));

        assertThat(((CopyAction)page.getToolbar().get("bottomRight").get(0).getButtons().get(0).getAction())
                .getPayload().getSource().getField(), is("groups[2]"));
        assertThat(((CopyAction)page.getToolbar().get("bottomRight").get(0).getButtons().get(0).getAction())
                .getPayload().getTarget().getField(), is("dictionary[2].id"));

    }
}
