package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.meta.action.alert.AlertAction;
import net.n2oapp.framework.api.metadata.meta.action.alert.AlertActionPayload;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePlacement;
import net.n2oapp.framework.api.ui.ResponseMessage;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Тестирование компиляции {@link AlertActionCompiler}
 */
public class AlertActionCompileTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oAllDataPack());

    }

    @Test
    public void testAlertAction() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/testAlert.page.xml")
                .get(new PageContext("testAlert"));

        assertThat(((AlertAction) page.getToolbar().getButton("defaultAlert").getAction()).getType(), is("n2o/alerts/ADD_MULTI"));
        assertThat(((AlertActionPayload) ((AlertAction) page.getToolbar().getButton("defaultAlert").getAction()).getPayload()).getKey(), is(MessagePlacement.top));
        ResponseMessage message = ((AlertActionPayload) ((AlertAction) page.getToolbar().getButton("defaultAlert").getAction()).getPayload()).getAlerts().get(0);
        assertThat(message.getId(), notNullValue());
        assertThat(message.getTitle(), is("title"));
        assertThat(message.getText(), is("text"));
        assertThat(message.getTimeout(), is(3000));
        assertThat(message.getPlacement(), is(MessagePlacement.top));
        assertThat(message.getCloseButton(), is(true));

        assertThat(((AlertAction) page.getToolbar().getButton("alert").getAction()).getType(), is("n2o/alerts/ADD_MULTI"));
        assertThat(((AlertActionPayload) ((AlertAction) page.getToolbar().getButton("alert").getAction()).getPayload()).getKey(), is(MessagePlacement.bottomRight));
        message = ((AlertActionPayload) ((AlertAction) page.getToolbar().getButton("alert").getAction()).getPayload()).getAlerts().get(0);
        assertThat(message.getId(), notNullValue());
        assertThat(message.getTitle(), is("`'Title '+message`"));
        assertThat(message.getText(), is("`'Text '+message`"));
        assertThat(message.getPlacement(), is(MessagePlacement.bottomRight));
        assertThat(message.getCloseButton(), is(false));
        assertThat(message.getClassName(), is("css-on-action"));
        assertThat(message.getStyle().get("width"), is("90%"));
        assertThat(message.getHref(), is("http://example.org"));
        assertThat(message.getColor(), is("info"));
        assertThat(message.getTimeout(), is(5000));
        assertThat(message.getTime(), is(LocalDateTime.parse("2022-02-02T12:15:23")));
    }
}
