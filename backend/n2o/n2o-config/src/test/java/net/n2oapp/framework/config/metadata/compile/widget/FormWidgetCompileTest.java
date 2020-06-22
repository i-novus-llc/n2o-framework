package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.event.action.UploadType;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.widget.form.FormElementIOV4;
import net.n2oapp.framework.config.io.widget.table.cell.ToolbarCellElementIOv2;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.compile.toolbar.ToolbarCompiler;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тест сборки формы
 */
public class FormWidgetCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllDataPack(), new N2oFieldSetsPack(), new N2oControlsPack(), new N2oCellsPack(), new N2oActionsPack())
                .ios(new FormElementIOV4(), new ToolbarCellElementIOv2())
                .compilers(new FormCompiler(), new ToolbarCompiler())
                .sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/widgets/testTable4Compile.query.xml"),
                        new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.object.xml"));
    }

    @Test
    public void uploadDefaults() {
        Form form = (Form) compile("net/n2oapp/framework/config/metadata/compile/widgets/testFormCompile.widget.xml")
                .get(new WidgetContext("testFormCompile"));
        assertThat(form.getId(), is("$testFormCompile"));
        assertThat(form.getUpload(), is(UploadType.defaults));
        assertThat(form.getDataProvider(), nullValue());
        assertThat(form.getComponent().getFetchOnInit(), is(false));
        assertThat(form.getComponent().getPrompt(), is(true));
    }

    @Test
    public void uploadQuery() {
        Form form = (Form) compile("net/n2oapp/framework/config/metadata/compile/widgets/testFormCompile2.widget.xml")
                .get(new WidgetContext("testFormCompile2"));
        assertThat(form.getId(), is("$testFormCompile2"));
        assertThat(form.getUpload(), is(UploadType.query));
        assertThat(form.getDataProvider(), notNullValue());
        assertThat(form.getComponent().getFetchOnInit(), is(true));
        QueryContext queryContext = (QueryContext) route("/testFormCompile2", CompiledQuery.class);
        assertThat(queryContext.getFailAlertWidgetId(), is("$testFormCompile2"));
        assertThat(queryContext.getSuccessAlertWidgetId(), is("$testFormCompile2"));
    }

    @Test
    public void testFormStyles() {
        Form form = (Form) compile("net/n2oapp/framework/config/metadata/compile/widgets/testFormRowColCompile.widget.xml")
                .get(new WidgetContext("testFormRowColCompile"));
        assertThat(form.getStyle().get("width"), is("300px"));
        assertThat(form.getStyle().get("marginLeft"), is("10px"));

        FieldSet fieldSet = form.getComponent().getFieldsets().get(0);
        assertThat(fieldSet.getStyle().get("width"), is("300px"));
        assertThat(fieldSet.getStyle().get("marginLeft"), is("10px"));
        assertThat(fieldSet.getRows().get(0).getStyle().get("width"), is("300px"));
        assertThat(fieldSet.getRows().get(0).getStyle().get("marginLeft"), is("10px"));
        assertThat(fieldSet.getRows().get(0).getCols().get(0).getStyle().get("width"), is("300px"));
        assertThat(fieldSet.getRows().get(0).getCols().get(0).getStyle().get("marginLeft"), is("10px"));
    }
}
