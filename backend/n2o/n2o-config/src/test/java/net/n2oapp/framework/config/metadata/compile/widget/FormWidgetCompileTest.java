package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.data.validation.MandatoryValidation;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.event.action.UploadType;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
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

import java.util.List;

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
        assertThat(form.getId(), is("testFormCompile"));
        assertThat(form.getUpload(), is(UploadType.defaults));
        assertThat(form.getDataProvider(), nullValue());
        assertThat(form.getComponent().getFetchOnInit(), is(false));
    }

    @Test
    public void uploadQuery() {
        Form form = (Form) compile("net/n2oapp/framework/config/metadata/compile/widgets/testFormCompile2.widget.xml")
                .get(new WidgetContext("testFormCompile2"));
        assertThat(form.getId(), is("testFormCompile2"));
        assertThat(form.getUpload(), is(UploadType.query));
        assertThat(form.getDataProvider(), notNullValue());
        assertThat(form.getComponent().getFetchOnInit(), is(true));
        QueryContext queryContext = (QueryContext) route("/testFormCompile2").getContext(CompiledQuery.class);
        assertThat(queryContext.getFailAlertWidgetId(), is("testFormCompile2"));
        assertThat(queryContext.getSuccessAlertWidgetId(), is("testFormCompile2"));
    }

    @Test
    public void testFormClientValidations() {
        Form form = (Form) compile("net/n2oapp/framework/config/metadata/compile/widgets/testFormValidations.widget.xml",
                "net/n2oapp/framework/config/metadata/compile/widgets/testFormValidations.object.xml")
                .get(new WidgetContext("testFormValidations"));
        List<Validation> validations = form.getComponent().getValidation().get("testField");

        assertThat(validations.size(), is(11));
        assertThat(validations.get(0).getId(), is("Mandatory1"));
        assertThat(validations.get(0).getSide().contains("client"), is(true));
        assertThat(validations.get(1).getId(), is("Mandatory2"));
        assertThat(validations.get(1).getSide().contains("client"), is(true));
        assertThat(validations.get(2).getId(), is("Mandatory3"));
        assertThat(validations.get(2).getSide().contains("client"), is(true));
        assertThat(validations.get(3).getId(), is("Mandatory4"));
        assertThat(validations.get(3).getSide().contains("client"), is(true));
        assertThat(validations.get(4).getId(), is("test1"));
        assertThat(validations.get(4).getSide().contains("client"), is(true));
        assertThat(validations.get(5).getId(), is("test2"));
        assertThat(validations.get(5).getSide().contains("client"), is(true));
        assertThat(validations.get(6).getId(), is("test4"));
        assertThat(validations.get(6).getSide().contains("client"), is(true));
        assertThat(validations.get(7).getId(), is("test5"));
        assertThat(validations.get(7).getSide().contains("client"), is(true));
        assertThat(validations.get(8).getId(), is("Condition1"));
        assertThat(validations.get(8).getMoment(), is(N2oValidation.ServerMoment.beforeOperation));
        assertThat(validations.get(8).getSide().contains("client"), is(true));
        assertThat(validations.get(9).getId(), is("Condition2"));
        assertThat(validations.get(9).getSide().contains("client"), is(true));
        assertThat(validations.get(10).getId(), is("Condition3"));
        assertThat(validations.get(10).getSide(), is(nullValue()));

        validations = form.getComponent().getValidation().get("testField3");
        assertThat(((MandatoryValidation)validations.get(0)).getExpression(), is("(testField2 == 'test') && (testField3 == 'test')"));
        assertThat(validations.get(0).getMoment(), is(N2oValidation.ServerMoment.beforeOperation));


    }
}
