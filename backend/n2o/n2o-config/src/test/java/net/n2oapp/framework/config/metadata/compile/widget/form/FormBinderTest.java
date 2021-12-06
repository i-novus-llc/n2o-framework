package net.n2oapp.framework.config.metadata.compile.widget.form;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.meta.control.Field;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.api.metadata.meta.widget.table.TableWidgetComponent;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тест {@link net.n2oapp.framework.config.metadata.compile.widget.FormBinder}
 */
public class FormBinderTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(
                new N2oAllDataPack(),
                new N2oPagesPack(),
                new N2oWidgetsPack(),
                new N2oRegionsPack(),
                new N2oActionsPack(),
                new N2oFieldSetsPack(),
                new N2oControlsPack()
        );
    }

    /**
     * Проверка резолва ссылок в autoSubmit
     */
    @Test
    public void autoSubmit() {
        ReadCompileBindTerminalPipeline pipeline = bind("net/n2oapp/framework/config/metadata/compile/page/testFormBinderAutoSubmit.page.xml",
                "net/n2oapp/framework/config/metadata/compile/object/utAction.object.xml");
        PageContext context = new PageContext("testFormBinderAutoSubmit", "/p/w/:param0/form");
        Form form = (Form) ((SimplePage) pipeline.get(context, new DataSet().add("param0", "1"))).getWidget();
     //   assertThat(form.getFormDataProvider().getUrl(), containsString("/p/w/1/form")); fixme
    }

    /**
     * Проверка резолва ссылок в submit в полях формы
     */
    @Test
    public void fieldSubmit() {
        ReadCompileBindTerminalPipeline pipeline = bind("net/n2oapp/framework/config/metadata/compile/page/testFormBinderAutoSubmit.page.xml",
                "net/n2oapp/framework/config/metadata/compile/object/utAction.object.xml");
        PageContext context = new PageContext("testFormBinderAutoSubmit", "/p/w/:param0/form");
        Form form = (Form) ((SimplePage) pipeline.get(context, new DataSet().add("param0", "1"))).getWidget();
        StandardField<?> field = (StandardField<?>) form.getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0);
        assertThat(field.getDataProvider().getUrl(), containsString("/p/w/1/form"));
    }
}
