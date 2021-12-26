package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.meta.control.Alert;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование компиляции {@link AlertCompiler}
 */
public class AlertCompileTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack(),
                new N2oFieldSetsPack(), new N2oAllDataPack(), new N2oControlsPack());
    }

    @Test
    public void testAlertField(){
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/control/testAlertFieldCompile.page.xml")
                .get(new PageContext("testAlertFieldCompile"));
        Form form = (Form) page.getWidget();
        Alert field = (Alert)form.getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0);

        assertThat(field.getSrc(), is("AlertField"));
        assertThat(field.getText(), is("`'Text '+message`"));
        assertThat(field.getHeader(), is("`'Header '+message`"));
        assertThat(field.getFooter(), is("`'Footer '+message`"));

    }

}
