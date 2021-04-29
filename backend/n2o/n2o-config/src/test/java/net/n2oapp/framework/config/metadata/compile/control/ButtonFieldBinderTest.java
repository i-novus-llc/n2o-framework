package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.action.modal.show_modal.ShowModal;
import net.n2oapp.framework.api.metadata.meta.control.ButtonField;
import net.n2oapp.framework.api.metadata.meta.control.CustomField;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.toolbar.ToolbarCell;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
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
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack(), new N2oCellsPack(),
                new N2oFieldSetsPack(), new N2oAllDataPack(), new N2oControlsV2IOPack(), new N2oControlsPack());
    }

    @Test
    public void testField() {
        ReadCompileBindTerminalPipeline pipeline = bind("net/n2oapp/framework/config/metadata/compile/control/testButtonFieldCompile.widget.xml");
        Form form = (Form) pipeline.get(new WidgetContext("testButtonFieldCompile"), new DataSet().add("param2", "2"));
        ButtonField field = (ButtonField) form.getComponent().getFieldsets().get(0).getRows().get(1).getCols().get(0).getFields().get(0);
        assertThat(field.getUrl(), Matchers.is("/test2/:param1/2?param3=:param3"));
        assertThat(field.getPathMapping().size(), Matchers.is(2));
        field = (ButtonField) form.getComponent().getFieldsets().get(0).getRows().get(4).getCols().get(0).getFields().get(0);
        assertThat(((ShowModal)field.getAction()).getPayload().getPageUrl(), Matchers.is("/test2/:param1/1?param3=:param3"));
        assertThat(((ShowModal)field.getAction()).getPayload().getPathMapping().size(), Matchers.is(1));
    }

    /**
     * Проверка резолва ссылок в button field в полях формы
     */
    @Test
    public void buttonField() {
        ReadCompileBindTerminalPipeline pipeline = bind("net/n2oapp/framework/config/metadata/compile/control/testButtonFieldBinderShowModal.page.xml",
                "net/n2oapp/framework/config/metadata/compile/control/testButtonFieldBinder.object.xml");
        PageContext context = new PageContext("testButtonFieldBinderShowModal", "/p/w/:nm/form");
        StandardPage page = (StandardPage) pipeline.get(context, new DataSet().add("nm", "1"));
        Form form = (Form) page.getRegions().get("single").get(0).getContent().get(0);
        CustomField field = (CustomField) form.getComponent().getFieldsets().get(0).getRows().get(1).getCols().get(0).getFields().get(0);
        assertThat(((InvokeAction)((ButtonField)field.getControl()).getAction()).getPayload().getDataProvider().getUrl(),
                is("n2o/data/p/w/1/form/greeting"));
        ButtonField field1 = (ButtonField) form.getComponent().getFieldsets().get(0).getRows().get(2).getCols().get(0).getFields().get(0);
        assertThat(((InvokeAction)field1.getAction()).getPayload().getDataProvider().getUrl(), is("n2o/data/p/w/1/form/greeting"));
        InvokeAction invokeAction = (InvokeAction) form.getComponent().getFieldsets().get(0).getRows().get(3).getCols().get(0)
                .getFields().get(0).getToolbar()[0].getButtons().get(0).getAction();
        assertThat(invokeAction.getPayload().getDataProvider().getUrl(), is("n2o/data/p/w/1/form/greeting"));
        Table table = (Table) page.getRegions().get("single").get(0).getContent().get(1);
        assertThat(((InvokeAction)((ToolbarCell)table.getComponent().getCells().get(0)).getToolbar().get(0).getButtons().get(0).getAction())
                        .getPayload().getDataProvider().getUrl(), is("n2o/data/p/w/1/form/w2/greeting"));
    }
}
