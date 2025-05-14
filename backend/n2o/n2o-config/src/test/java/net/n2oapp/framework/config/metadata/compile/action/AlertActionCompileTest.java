package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.meta.action.alert.AlertAction;
import net.n2oapp.framework.api.metadata.meta.action.alert.AlertActionPayload;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePlacementEnum;
import net.n2oapp.framework.api.ui.ResponseMessage;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Тестирование компиляции {@link AlertActionCompiler}
 */
class AlertActionCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oAllDataPack());

    }

    @Test
    void testAlertAction() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/testAlert.page.xml")
                .get(new PageContext("testAlert"));

        assertThat(((AlertAction) page.getToolbar().getButton("defaultAlert").getAction()).getType(), is("n2o/api/alerts/add"));
        assertThat(((AlertActionPayload) ((AlertAction) page.getToolbar().getButton("defaultAlert").getAction()).getPayload()).getKey(), is(MessagePlacementEnum.top));
        ResponseMessage message = ((AlertActionPayload) ((AlertAction) page.getToolbar().getButton("defaultAlert").getAction()).getPayload()).getAlerts().get(0);
        assertThat(message.getId(), notNullValue());
        assertThat(message.getTitle(), is("title"));
        assertThat(message.getText(), is("text"));
        assertThat(message.getTimeout(), is(3000));
        assertThat(message.getPlacement(), is(MessagePlacementEnum.top));
        assertThat(message.getCloseButton(), is(true));

        assertThat(((AlertAction) page.getToolbar().getButton("alert").getAction()).getType(), is("n2o/api/alerts/add"));
        assertThat(((AlertActionPayload) ((AlertAction) page.getToolbar().getButton("alert").getAction()).getPayload()).getKey(), is(MessagePlacementEnum.bottomRight));
        message = ((AlertActionPayload) ((AlertAction) page.getToolbar().getButton("alert").getAction()).getPayload()).getAlerts().get(0);
        assertThat(message.getId(), notNullValue());
        assertThat(message.getTitle(), is("`'Title '+message`"));
        assertThat(message.getText(), is("`'Text '+message`"));
        assertThat(message.getPlacement(), is(MessagePlacementEnum.bottomRight));
        assertThat(message.getCloseButton(), is(false));
        assertThat(message.getClassName(), is("css-on-action"));
        assertThat(message.getStyle().get("width"), is("90%"));
        assertThat(message.getHref(), is("http://example.org"));
        assertThat(message.getSeverity(), is("info"));
        assertThat(message.getTimeout(), is(5000));
        assertThat(message.getTime(), is(LocalDateTime.parse("2022-02-02T12:15:23")));
        assertThat(message.getModelLink(), is("models.filter['testAlert_ds1']"));
    }
}
