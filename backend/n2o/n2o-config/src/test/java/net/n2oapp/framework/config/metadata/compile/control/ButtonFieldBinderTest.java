package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.action.modal.show_modal.ShowModal;
import net.n2oapp.framework.api.metadata.meta.cell.ToolbarCell;
import net.n2oapp.framework.api.metadata.meta.control.ButtonField;
import net.n2oapp.framework.api.metadata.meta.control.CustomField;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование компиляции ButtonField компонента
 */
class ButtonFieldBinderTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.getEnvironment().getContextProcessor().set("ctxParam", "testValue");
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack(), new N2oCellsPack(),
                new N2oFieldSetsPack(), new N2oAllDataPack(), new N2oControlsV2IOPack(), new N2oControlsPack());
    }

    @Test
    void testField() {
        ReadCompileBindTerminalPipeline pipeline = bind("net/n2oapp/framework/config/metadata/compile/control/testButtonFieldCompile.page.xml");
        StandardPage page = (StandardPage) pipeline.get(new PageContext("testButtonFieldCompile"), new DataSet());
        Form form = (Form) page.getRegions().get("single").getFirst().getContent().getFirst();
        ButtonField field = (ButtonField) form.getComponent().getFieldsets().getFirst().getRows().get(1).getCols().getFirst().getFields().getFirst();
        assertThat(((LinkAction) field.getAction()).getUrl(), is("/test2/:param1/:param2?param3=:param3"));
        assertThat(((LinkAction) field.getAction()).getPathMapping().size(), is(2));
        field = (ButtonField) form.getComponent().getFieldsets().getFirst().getRows().get(4).getCols().getFirst().getFields().getFirst();
        assertThat(((ShowModal) field.getAction()).getPayload().getPageUrl(), is("/testButtonFieldCompile/test2/:param1/1?param3=:param3"));
        assertThat(((ShowModal) field.getAction()).getPayload().getPathMapping().size(), is(1));
    }

    /**
     * Проверка резолва ссылок в button field в полях формы
     */
    @Test
    void buttonField() {
        ReadCompileBindTerminalPipeline pipeline = bind("net/n2oapp/framework/config/metadata/compile/control/testButtonFieldBinderShowModal.page.xml",
                "net/n2oapp/framework/config/metadata/compile/control/testButtonFieldBinder.object.xml");
        PageContext context = new PageContext("testButtonFieldBinderShowModal", "/p/w/:nm/form");
        StandardPage page = (StandardPage) pipeline.get(context, new DataSet().add("nm", "1"));
        Form form = (Form) page.getRegions().get("single").getFirst().getContent().getFirst();
        CustomField field = (CustomField) form.getComponent().getFieldsets().getFirst().getRows().get(1).getCols().getFirst().getFields().getFirst();
        assertThat(((InvokeAction) ((ButtonField) field.getControl()).getAction()).getPayload().getDataProvider().getUrl(),
                is("n2o/data/p/w/1/form/greeting"));
        ButtonField field1 = (ButtonField) form.getComponent().getFieldsets().getFirst().getRows().get(2).getCols().getFirst().getFields().getFirst();
        assertThat(((InvokeAction) field1.getAction()).getPayload().getDataProvider().getUrl(), is("n2o/data/p/w/1/form/greeting"));
        InvokeAction invokeAction = (InvokeAction) form.getComponent().getFieldsets().getFirst().getRows().get(3).getCols().getFirst()
                .getFields().getFirst().getToolbar()[0].getButtons().getFirst().getAction();
        assertThat(invokeAction.getPayload().getDataProvider().getUrl(), is("n2o/data/p/w/1/form/greeting"));
        Table table = (Table) page.getRegions().get("single").getFirst().getContent().get(1);
        assertThat(((InvokeAction) ((ToolbarCell) table.getComponent().getBody().getCells().getFirst()).getToolbar().getFirst().getButtons().getFirst().getAction())
                .getPayload().getDataProvider().getUrl(), is("n2o/data/p/w/1/form/greeting"));
    }

    /**
     * Проверка резолва контекстных переменных в выражениях зависимостей ButtonField
     */
    @Test
    void dependencyExpressionResolve() {
        ReadCompileBindTerminalPipeline pipeline = bind(
                "net/n2oapp/framework/config/metadata/compile/control/testButtonFieldConditionBinder.page.xml");
        SimplePage page = (SimplePage) pipeline.get(new PageContext("testButtonFieldConditionBinder"), new DataSet());
        Form form = (Form) page.getWidget();
        ButtonField field = (ButtonField) form.getComponent().getFieldsets().getFirst()
                .getRows().getFirst().getCols().getFirst().getFields().getFirst();
        assertThat(field.getDependencies().getFirst().getExpression(), is("status == 'testValue'"));
    }
}
