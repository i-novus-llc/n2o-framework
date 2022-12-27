package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.meta.action.custom.CustomAction;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Тестирование компиляции настраиваемого действия
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
        builder.packs(new N2oAllPagesPack());
    }

    @Test
    public void simple() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/testCustomAction.page.xml")
                .get(new PageContext("testCustomAction"));

        CustomAction action = (CustomAction) page.getToolbar().getButton("b1").getAction();

        assertThat(action.getType(), is("n2o/CUSTOM"));
        Map<String, Object> attributes = action.getPayload().getAttributes();
        assertThat(attributes.size(), is(4));
        assertThat(attributes.get("formId"), is("`formGuid`"));
        assertThat(attributes.get("docId"), is("`s3Guid`"));
        assertThat(((Map) attributes.get("redirect")).get("url"), is("/print"));
        assertThat(((Map) attributes.get("redirect")).get("target"), is("application"));
        assertThat(((Map) attributes.get("update")).get("datasource"), is("ds"));
        assertThat(((Map) attributes.get("update")).get("model"), is("resolve"));
        assertThat(((Map) attributes.get("update")).get("fieldId"), is("pdfId"));
        assertThat(((Map) attributes.get("update")).get("value"), is("`value`"));

        assertThat(action.getMeta().getSuccess().getRefresh().getDatasources().size(), is(2));
        assertThat(action.getMeta().getSuccess().getRefresh().getDatasources().get(0), is("testCustomAction_ds1"));
        assertThat(action.getMeta().getSuccess().getRefresh().getDatasources().get(1), is("testCustomAction_ds2"));
        assertThat(action.getMeta().getSuccess().getRedirect().getPath(), is("/main"));
        assertThat(action.getMeta().getSuccess().getRedirect().getTarget(), is(Target.application));
        assertThat(action.getMeta().getFail(), notNullValue());
    }
}
