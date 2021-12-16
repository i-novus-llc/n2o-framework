package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

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
        assertThat(btn.getIcon(), nullValue());
        assertThat(btn.getColor(), is("danger"));
        assertThat(btn.getHintPosition(), is("right"));
        assertThat(btn.getClassName(), is("Button"));
        assertThat(btn.getStyle().size(), is(1));
        assertThat(btn.getStyle().get("color"), is("red"));
        assertThat(btn.getHint(), is("hint"));
        assertThat(((InvokeAction) btn.getAction()).getPayload().getDatasource(), is("testButton_table"));

        btn = page.getToolbar().getButton("btn2");
        assertThat(btn.getLabel(), nullValue());
        assertThat(btn.getIcon(), is("fa fa-pencil"));

        btn = page.getToolbar().getButton("btn3");
        assertThat(btn.getLabel(), is("load"));
        assertThat(btn.getIcon(), is("fa fa-download"));
    }
}
