package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.compile.enums.ColorEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ConfirmTypeEnum;
import net.n2oapp.framework.api.metadata.meta.action.confirm.ConfirmAction;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class ConfirmActionCompilerTest extends SourceCompileTestBase {

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
    void test() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/testConfirmAction.page.xml")
                .get(new PageContext("testConfirmAction"));
        ConfirmAction testAction = (ConfirmAction) page.getToolbar().getButton("b1").getAction();
        assertThat(testAction.getType(), is("n2o/api/action/confirm"));
        assertThat(testAction.getPayload().getTitle(), is("testTitle"));
        assertThat(testAction.getPayload().getText(), is("testText"));
        assertThat(testAction.getPayload().getClassName(), is("test"));
        assertThat(testAction.getPayload().getStyle().get("pageBreakBefore"), CoreMatchers.is("avoid"));
        assertThat(testAction.getPayload().getStyle().get("paddingTop"), CoreMatchers.is("0"));
        assertThat(testAction.getPayload().getMode(), is(ConfirmTypeEnum.MODAL));
        assertThat(testAction.getPayload().getModel(), is(ReduxModelEnum.RESOLVE));
        assertThat(testAction.getPayload().getDatasource(), is("testConfirmAction_ds"));
        assertThat(testAction.getPayload().getCloseButton(), is(false));
        assertThat(testAction.getPayload().getReverseButtons(), is(false));
        assertThat(testAction.getPayload().getOk().getLabel(), is("test"));
        assertThat(testAction.getPayload().getOk().getColor(), is("secondary"));
        assertThat(testAction.getPayload().getOk().getClassName(), is("test__class"));
        assertThat(testAction.getPayload().getOk().getStyle().get("pageBreakBefore"), CoreMatchers.is("avoid"));
        assertThat(testAction.getPayload().getOk().getStyle().get("paddingTop"), CoreMatchers.is("0"));
        assertThat(testAction.getPayload().getCancel().getLabel(), is("test2"));
        assertThat(testAction.getPayload().getCancel().getColor(), is("primary"));
        assertThat(testAction.getPayload().getCancel().getClassName(), is("test__class"));
        assertThat(testAction.getPayload().getCancel().getStyle().get("pageBreakBefore"), CoreMatchers.is("avoid"));
        assertThat(testAction.getPayload().getCancel().getStyle().get("paddingTop"), CoreMatchers.is("0"));

        testAction2(page);

        testAction3(page);
    }

    private void testAction2(StandardPage page) {
        ConfirmAction testAction;
        testAction = (ConfirmAction) page.getToolbar().getButton("b2").getAction();
        assertThat(testAction.getPayload().getCloseButton(), is(true));
        assertThat(testAction.getPayload().getMode(), is(ConfirmTypeEnum.POPOVER));
        assertThat(testAction.getPayload().getReverseButtons(), is(true));
    }

    private void testAction3(StandardPage page) {
        ConfirmAction testAction;
        testAction = (ConfirmAction) page.getToolbar().getButton("b3").getAction();
        assertThat(testAction.getType(), is("n2o/api/action/confirm"));
        assertThat(testAction.getPayload().getTitle(), is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.title")));
        assertThat(testAction.getPayload().getText(), is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.text")));
        assertThat(testAction.getPayload().getMode(), is(ConfirmTypeEnum.MODAL));
        assertThat(testAction.getPayload().getCloseButton(), is(false));
        assertThat(testAction.getPayload().getReverseButtons(), is(false));
        assertThat(testAction.getPayload().getOk().getLabel(), is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.ok_label")));
        assertThat(testAction.getPayload().getOk().getColor(), is(ColorEnum.PRIMARY.getId()));
        assertThat(testAction.getPayload().getCancel().getLabel(), is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.cancel_label")));
        assertThat(testAction.getPayload().getCancel().getColor(), is(ColorEnum.SECONDARY.getId()));
    }
}
