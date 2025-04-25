package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.action.confirm.ConfirmAction;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.action.modal.show_modal.ShowModal;
import net.n2oapp.framework.api.metadata.meta.action.multi.MultiAction;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.ModalPageContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class ToolbarCrudCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllDataPack(), new N2oAllPagesPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.object.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.widget.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.page.xml"));
    }

    @Test
    void testGeneratedCrudButtons() {
        ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> pipeline = compile(
                "net/n2oapp/framework/config/metadata/compile/widgets/testToolbarCrudCompile.page.xml");
        SimplePage page = (SimplePage) pipeline.get(new PageContext("testToolbarCrudCompile"));
        Form form = (Form) page.getWidget();
        assertThat(((ModalPageContext) builder.route("/testToolbarCrudCompile/w1/create", Page.class, null))
                .getPageName(), is("Пустой объект для unit тестов - Создание"));
        assertThat(((ModalPageContext) builder.route("/testToolbarCrudCompile/w1/1/update", Page.class, null))
                .getPageName(), is("Пустой объект для unit тестов - Изменение"));

        assertThat(form.getToolbar().size(), is(2));
        assertThat(form.getToolbar().get("topLeft").get(0).getButtons().size(), is(1));
        assertThat(form.getToolbar().get("topLeft").get(1).getButtons().size(), is(3));
        assertThat(form.getToolbar().get("bottomLeft").get(0).getButtons().size(), is(1));
        List<String> buttonsId = form.getToolbar().get("topLeft").get(1)
                .getButtons().stream().map(AbstractButton::getId).toList();
        List<String> buttonsLabel = form.getToolbar().get("topLeft").get(1)
                .getButtons().stream().map(AbstractButton::getLabel).toList();
        List<Action> buttonsAction = form.getToolbar().get("topLeft").get(1)
                .getButtons().stream().map(AbstractButton::getAction).toList();


        assertThat(buttonsId.contains("create"), is(true));
        assertThat(buttonsId.contains("update"), is(true));
        assertThat(buttonsId.contains("delete"), is(true));

        assertThat(buttonsLabel.contains("Создать"), is(true));
        assertThat(buttonsLabel.contains("Изменить"), is(true));
        assertThat(buttonsLabel.contains("Удалить"), is(true));

        assertThat(((ShowModal) buttonsAction.get(0)).getOperationId(), is("create"));
        assertThat(((ShowModal) buttonsAction.get(1)).getOperationId(), is("update"));

        MultiAction multiAction = (MultiAction) buttonsAction.get(2);
        assertThat(multiAction.getPayload().getActions().size(), is(2));
        assertThat(multiAction.getPayload().getActions().get(0).getClass(), is(ConfirmAction.class));
        assertThat(multiAction.getPayload().getActions().get(1).getClass(), is(InvokeAction.class));
        ConfirmAction confirmAction = (ConfirmAction) multiAction.getPayload().getActions().get(0);
        assertThat(confirmAction.getPayload().getText(), is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.text")));
        assertThat(confirmAction.getPayload().getTitle(), is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.title")));
        assertThat(confirmAction.getPayload().getOk().getLabel(), is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.ok_label")));
        assertThat(confirmAction.getPayload().getCancel().getLabel(), is(builder.getEnvironment().getMessageSource().getMessage("n2o.api.action.confirm.cancel_label")));
        assertThat(confirmAction.getPayload().getCloseButton(), is(false));
        assertThat(((InvokeAction)multiAction.getPayload().getActions().get(1)).getOperationId(), is("delete"));

    }
}
