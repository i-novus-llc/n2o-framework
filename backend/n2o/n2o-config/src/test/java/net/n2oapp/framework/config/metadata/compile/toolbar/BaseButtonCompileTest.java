package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.Confirm;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ConfirmType;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * Тестирование компиляции базовой кнопки
 */
public class BaseButtonCompileTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oWidgetsPack(), new N2oActionsPack(), new N2oAllDataPack(), new N2oPagesPack(), new N2oRegionsPack())
                .sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.object.xml"));
    }

    @Test
    public void testButton() {
        Page page = compile("net/n2oapp/framework/config/metadata/compile/toolbar/testButton.page.xml")
                .get(new PageContext("testButton"));

        AbstractButton btn = page.getToolbar().getButton("btn1");
        assertThat(btn.getLabel(), is("delete"));
        assertThat(btn.getIcon(), is("fa fa-trash"));
        assertThat(btn.getColor(), is("danger"));
        assertThat(btn.getHintPosition(), is("right"));
        assertThat(btn.getClassName(), is("Button"));
        assertThat(btn.getStyle().size(), is(1));
        assertThat(btn.getStyle().get("color"), is("red"));
        assertThat(btn.getHint(), is("hint"));
        assertThat(((InvokeAction) btn.getAction()).getPayload().getDatasource(), is("testButton_table"));

        btn = page.getToolbar().getButton("btn2");
        assertThat(btn.getLabel(), is("edit"));
        assertThat(btn.getIcon(), is("fa fa-pencil"));

        btn = page.getToolbar().getButton("btn3");
        assertThat(btn.getLabel(), is("load"));
        assertThat(btn.getIcon(), is("fa fa-download"));

        btn = page.getToolbar().getButton("btn4");
        assertThat(btn.getHint(), is("`description`"));
    }

    @Test
    public void testConfirm() {
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/toolbar/testButtonConfirm.object.xml"));
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/toolbar/testButtonConfirm.page.xml")
                .get(new PageContext("testButtonConfirm"));

        Confirm confirm = page.getWidget().getToolbar().getButton("btn1").getConfirm();
        assertThat(confirm, nullValue());

        confirm = page.getWidget().getToolbar().getButton("btn2").getConfirm();
        assertThat(confirm.getCondition(), is("`true`"));
        assertThat(confirm.getText(), is("`'' + this.id === '1' ? 'id is 1' : 'id is 2' + ''`"));
        assertThat(confirm.getTitle(), is("Подтвердить действие"));
        assertThat(confirm.getOk().getLabel(), is("Ок"));
        assertThat(confirm.getOk().getColor(), is("success"));
        assertThat(confirm.getCancel().getLabel(), is("Отмена"));
        assertThat(confirm.getCancel().getColor(), is("danger"));
        assertThat(confirm.getReverseButtons(), is(false));
        assertThat(confirm.getCloseButton(), is(false));
        assertThat(confirm.getMode(), is(ConfirmType.POPOVER));
        assertThat(confirm.getModelLink(), is("models.resolve['testButtonConfirm_main']"));

        confirm = page.getWidget().getToolbar().getButton("btn3").getConfirm();
        assertThat(confirm.getCondition(), is("`id === '1'|| id === '2'`"));
        assertThat(confirm.getText(), is("Нажмите \"Да\", если Вы уверены в совершаемом действии. Или \"Нет\", если ещё хотите обдумать совершаемое действие."));
        assertThat(confirm.getTitle(), is("Предупреждение"));
        assertThat(confirm.getOk().getLabel(), is("Да"));
        assertThat(confirm.getOk().getColor(), is("primary"));
        assertThat(confirm.getCancel().getLabel(), is("Нет"));
        assertThat(confirm.getCancel().getColor(), is("secondary"));
        assertThat(confirm.getReverseButtons(), is(false));
        assertThat(confirm.getCloseButton(), is(false));
        assertThat(confirm.getMode(), is(ConfirmType.MODAL));
        assertThat(confirm.getModelLink(), is("models.resolve['testButtonConfirm_main']"));

        confirm = page.getWidget().getToolbar().getButton("btn4").getConfirm();
        assertThat(confirm.getCondition(), is("`true`"));
        assertThat(confirm.getText(), is("Текст подтверждения из операции объекта"));
        assertThat(confirm.getTitle(), is("Предупреждение"));
        assertThat(confirm.getOk().getLabel(), is("Да"));
        assertThat(confirm.getOk().getColor(), is("primary"));
        assertThat(confirm.getCancel().getLabel(), is("Нет"));
        assertThat(confirm.getCancel().getColor(), is("secondary"));
        assertThat(confirm.getReverseButtons(), is(false));
        assertThat(confirm.getCloseButton(), is(false));
        assertThat(confirm.getMode(), is(ConfirmType.MODAL));
        assertThat(confirm.getModelLink(), is("models.resolve['testButtonConfirm_main']"));
    }
}
