package net.n2oapp.framework.config.metadata.compile.widget.form;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.validation.ConditionValidation;
import net.n2oapp.framework.api.data.validation.MandatoryValidation;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.control.ValidationReference;
import net.n2oapp.framework.api.metadata.datasource.Datasource;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesMode;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.close.CloseAction;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.RequestMethod;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.page.v2.SimplePageElementIOv2;
import net.n2oapp.framework.config.io.page.v2.StandardPageElementIOv2;
import net.n2oapp.framework.config.io.page.v3.SimplePageElementIOv3;
import net.n2oapp.framework.config.io.page.v3.StandardPageElementIOv3;
import net.n2oapp.framework.config.metadata.compile.context.ActionContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.compile.datasource.DatasourceCompiler;
import net.n2oapp.framework.config.metadata.compile.page.SimplePageCompiler;
import net.n2oapp.framework.config.metadata.compile.page.StandardPageCompiler;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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
        builder.packs(new N2oAllDataPack(), new N2oFieldSetsPack(), new N2oControlsPack(), new N2oCellsPack(), new N2oActionsPack(),
                new N2oWidgetsPack(), new N2oRegionsPack())
                .ios(new SimplePageElementIOv3(), new StandardPageElementIOv3(), new SimplePageElementIOv2(), new StandardPageElementIOv2())
                .compilers(new SimplePageCompiler(), new StandardPageCompiler(), new DatasourceCompiler())
                .sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/widgets/testTable4Compile.query.xml"),
                        new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.object.xml"));
    }

    @Test
    public void uploadDefaults() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testFormCompile.page.xml")
                .get(new PageContext("testFormCompile"));
        assertThat(page.getWidget().getId(), is("testFormCompile_main"));
        Form form = (Form) page.getWidget();
        assertThat(form.getComponent().getPrompt(), is(true));
        Datasource datasource = page.getDatasources().get("testFormCompile_main");
        assertThat(datasource.getDefaultValuesMode(), is(DefaultValuesMode.defaults));
        assertThat(datasource.getProvider(), nullValue());
    }

    @Test
    public void uploadQuery() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testFormCompile2.page.xml")
                .get(new PageContext("testFormCompile2"));
        assertThat(page.getWidget().getId(), is("testFormCompile2_main"));
        assertThat(page.getWidget().getDatasource(), notNullValue());
        Datasource datasource = page.getDatasources().get(page.getWidget().getDatasource());
        assertThat(datasource.getDefaultValuesMode(), is(DefaultValuesMode.query));
        assertThat(datasource.getSize(), is(1));
        QueryContext queryContext = (QueryContext) route("/testFormCompile2/main", CompiledQuery.class);
        assertThat(queryContext, notNullValue());
    }

    @Test
    public void testFormClientValidations() {
        PageContext pageContext = new PageContext("testFormValidations");
        pageContext.setSubmitOperationId("test");
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testFormValidations.page.xml",
                "net/n2oapp/framework/config/metadata/compile/widgets/testFormValidations.object.xml")
                .get(pageContext);


        List<Validation> validations = page.getDatasources().get("testFormValidations_main").getValidations().get("testField");

//        assertThat(validations.size(), is(11));
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

        validations = page.getDatasources().get(page.getWidget().getId()).getValidations().get("testField3");
        assertThat(((MandatoryValidation) validations.get(0)).getEnablingExpression(), is("(testField2 == 'test') && (testField3 == 'test')"));
        assertThat(validations.get(0).getMoment(), is(N2oValidation.ServerMoment.beforeOperation));

        validations = page.getDatasources().get(page.getWidget().getId()).getValidations().get("testField4");
        assertThat(((MandatoryValidation) validations.get(0)).getEnablingExpression(), is("(function(){return typeof testField != 'undefined' && testField != null && testField == 2})()"));
        assertThat(validations.get(0).getMoment(), is(N2oValidation.ServerMoment.beforeOperation));

        validations = page.getDatasources().get(page.getWidget().getId()).getValidations().get("testInterval.begin");
        assertThat(validations.size(), is(1));
        assertThat(((ConditionValidation) validations.get(0)).getExpression(), is("typeof testIntervalBegin == 'undefined'"));
        validations = page.getDatasources().get(page.getWidget().getId()).getValidations().get("testInterval.end");
        assertThat(validations.size(), is(1));
        assertThat(((ConditionValidation) validations.get(0)).getExpression(), is("typeof testIntervalEnd == 'undefined'"));
    }

    @Test
    public void testFormPreFilterValidations() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testFormPreFilterValidation.page.xml",
                "net/n2oapp/framework/config/metadata/compile/widgets/testFormPreFilterValidation.query.xml",
                "net/n2oapp/framework/config/metadata/compile/widgets/testFormValidations.object.xml")
                .get(new PageContext("testFormPreFilterValidation"));
        QueryContext queryContext = (QueryContext) route("/testFormPreFilterValidation/form", CompiledQuery.class);
        List<Validation> validations = queryContext.getValidations();
        assertThat(validations.size(), is(1));
        MandatoryValidation validation = (MandatoryValidation) validations.get(0);
        assertThat(validation.getId(), is("testField"));
        assertThat(validation.getTarget(), is(ValidationReference.Target.field));
        assertThat(validation.getMessage(), is("Поле обязательно для заполнения"));
    }

    @Test
    public void testFormStyles() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testFormRowColCompile.page.xml")
                .get(new PageContext("testFormRowColCompile"));
        Form form = (Form) page.getWidget();
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

    @Test
    public void testSubmit() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testFormSubmit.page.xml",
                "net/n2oapp/framework/config/metadata/compile/widgets/testFormValidations.object.xml")
                .get(new PageContext("testFormSubmit"));
        Form form = (Form) page.getWidget();

        ActionContext context = (ActionContext) route("/testFormSubmit/a/b/c", CompiledObject.class);
        assertThat(context, notNullValue());
        assertThat(context.getOperationId(), is("test"));
        assertThat(context.isMessageOnFail(), is(true));
        assertThat(context.isMessageOnSuccess(), is(false));
        assertThat(context.getMessagesForm(), is("form"));
        assertThat(context.getRefresh().getDatasources(), hasItem("form"));

        ClientDataProvider dataProvider = page.getDatasources().get(form.getDatasource()).getSubmit();
        assertThat(dataProvider.getMethod(), is(RequestMethod.POST));
        assertThat(dataProvider.getSubmitForm(), is(true));
        assertThat(dataProvider.getUrl(), is("n2o/data/testFormSubmit/a/b/c"));

        assertThat(dataProvider.getPathMapping().size(), is(2));
        ModelLink link = dataProvider.getPathMapping().get("name1");
        assertThat(link.getValue(), is("value1"));
        assertThat(link.getModel(), nullValue());
        assertThat(link.getDatasource(), nullValue());
        assertThat(link.getBindLink(), nullValue());
        link = dataProvider.getPathMapping().get("name2");
        assertThat(link.getValue(), nullValue());
        assertThat(link.getModel(), is(ReduxModel.filter));
        assertThat(link.getDatasource(), is("testFormSubmit_form"));
        assertThat(link.getBindLink(), is("models.filter['testFormSubmit_form']"));

        assertThat(dataProvider.getHeadersMapping().size(), is(1));
        link = dataProvider.getHeadersMapping().get("name3");
        assertThat(link.getValue(), is("`a`"));
        assertThat(link.getModel(), is(ReduxModel.resolve));
        assertThat(link.getDatasource(), is("testFormSubmit_form"));
        assertThat(link.getBindLink(), is("models.resolve['testFormSubmit_form']"));

        assertThat(dataProvider.getFormMapping().size(), is(1));
        link = dataProvider.getFormMapping().get("name4");
        assertThat(link.getValue(), is("`b`"));
        assertThat(link.getModel(), is(ReduxModel.filter));
        assertThat(link.getDatasource(), is("testFormSubmit_form"));
        assertThat(link.getBindLink(), is("models.filter['testFormSubmit_form']"));
    }

    @Test
    public void testSubmitInModal() {
        compile("net/n2oapp/framework/config/metadata/compile/widgets/testSubmitInModalIndex.page.xml",
                "net/n2oapp/framework/config/metadata/compile/widgets/testSubmitInModal.page.xml"
        ).get(new PageContext("testSubmitInModalIndex"));

        PageContext detailContext = (PageContext) route("/testSubmitInModalIndex/:id/open", Page.class);
        DataSet data = new DataSet();
        data.put("id", 1);
        SimplePage detailPage = (SimplePage) read().compile().bind().get(detailContext, data);
        Form form = (Form) detailPage.getWidget();
        assertThat(detailPage.getDatasources().get(form.getDatasource()).getSubmit().getPathMapping().size(), is(1));
        assertThat(detailPage.getDatasources().get(form.getDatasource()).getSubmit().getUrl(), is("n2o/data/testSubmitInModalIndex/:id/open/main"));
        AbstractButton closeBtn = detailPage.getWidget().getToolbar().get("bottomRight").get(0).getButtons().get(0);
        assertThat(closeBtn, notNullValue());
        assertThat(closeBtn.getConfirm(), nullValue());
        assertThat(closeBtn.getAction(), instanceOf(CloseAction.class));
        assertThat(closeBtn.getValidate(), nullValue());
    }

    @Test
    public void testFormAsFilter () {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testFormAsFilter.page.xml",
                "net/n2oapp/framework/config/metadata/compile/widgets/testFormAsFilter.query.xml")
                .get(new PageContext("testFormAsFilter"));
        assertThat(page.getRoutes().getQueryMapping().get("period").getOnSet().getBindLink(), is("models.resolve['testFormAsFilter_filters'].period"));
        assertThat(page.getRoutes().getQueryMapping().get("period").getOnSet().getValue(), is("`id`"));
    }
}
