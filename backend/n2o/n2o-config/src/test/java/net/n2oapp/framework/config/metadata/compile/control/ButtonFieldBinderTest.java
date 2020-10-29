package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.meta.action.show_modal.ShowModal;
import net.n2oapp.framework.api.metadata.meta.control.ButtonField;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование компиляции ButtonField компонента
 */
public class ButtonFieldBinderTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack(),
                new N2oFieldSetsPack(), new N2oAllDataPack(), new N2oControlsV2IOPack(), new N2oControlsPack());
    }

    @Test
    public void testField() {
        ReadCompileBindTerminalPipeline pipeline = bind("net/n2oapp/framework/config/metadata/compile/control/testButtonFieldCompile.widget.xml");
        Form form = (Form) pipeline.get(new WidgetContext("testButtonFieldCompile"), new DataSet().add("param2", "2"));
        ButtonField field = (ButtonField) form.getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0);
        assertThat(field.getUrl(), Matchers.is("/test2/:param1/2?param3=:param3"));
        assertThat(field.getPathMapping().size(), Matchers.is(2));
        field = (ButtonField) form.getComponent().getFieldsets().get(0).getRows().get(3).getCols().get(0).getFields().get(0);
        assertThat(((ShowModal)field.getAction()).getPayload().getPageUrl(), Matchers.is("/test2/:param1/1?param3=:param3"));
        assertThat(((ShowModal)field.getAction()).getPayload().getPathMapping().size(), Matchers.is(1));
    }
}
