package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.meta.action.submit.SubmitAction;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oActionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тестирование компиляции действия сохранения источника данных
 */
class SubmitActionCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack());
    }

    @Test
    void testSubmit() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/testSubmitAction.page.xml")
                .get(new PageContext("testSubmitAction"));

        List<AbstractButton> buttons = ((Form) page.getRegions().get("single").get(0).getContent().get(0))
                .getToolbar().get("topLeft").get(0).getButtons();

        assertThat(buttons.size(), is(2));
        SubmitAction submitAction = (SubmitAction) buttons.get(0).getAction();
        assertThat(submitAction.getPayload().getDatasource(), is("testSubmitAction_ds1"));
        assertThat(submitAction.getType(), is("n2o/actionImpl/SUBMIT"));

        submitAction = (SubmitAction) buttons.get(1).getAction();
        assertThat(submitAction.getPayload().getDatasource(), is("testSubmitAction_ds2"));
    }
}
