package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.meta.action.set_value.SetValueAction;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class SetValueBinderTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.getEnvironment().getContextProcessor().set("test", "Test");
        builder.packs(new N2oActionsPack(), new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack());
    }

    @Test
    void bindItems() {
        StandardPage page =(StandardPage) bind("net/n2oapp/framework/config/metadata/compile/action/testSetValueActionBinder.page.xml")
                .get(new PageContext("testSetValueActionBinder"), new DataSet());

        assertThat(((SetValueAction) ((Form) page.getRegions().get("single").get(0).getContent().get(0)).getToolbar().get("topLeft").get(0).getButtons().get(0).getAction())
                .getPayload().getSourceMapper(), is("(function(){return 'Test';}).call(this)"));

    }
}
