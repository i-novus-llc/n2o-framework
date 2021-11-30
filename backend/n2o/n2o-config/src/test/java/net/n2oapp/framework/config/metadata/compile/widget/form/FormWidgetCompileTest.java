package net.n2oapp.framework.config.metadata.compile.widget.form;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.validation.ConditionValidation;
import net.n2oapp.framework.api.data.validation.MandatoryValidation;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.control.ValidationReference;
import net.n2oapp.framework.api.metadata.datasource.Datasource;
import net.n2oapp.framework.api.metadata.event.action.UploadType;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.saga.RefreshSaga;
import net.n2oapp.framework.api.metadata.meta.widget.RequestMethod;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.page.v2.SimplePageElementIOv2;
import net.n2oapp.framework.config.io.page.v2.StandardPageElementIOv2;
import net.n2oapp.framework.config.io.page.v3.SimplePageElementIOv3;
import net.n2oapp.framework.config.io.page.v3.StandardPageElementIOv3;
import net.n2oapp.framework.config.metadata.compile.context.ActionContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.compile.page.SimplePageCompiler;
import net.n2oapp.framework.config.metadata.compile.page.StandardPageCompiler;
import net.n2oapp.framework.config.metadata.compile.widget.N2oFormV5AdapterTransformer;
import net.n2oapp.framework.config.metadata.compile.widget.N2oWidgetV5AdapterTransformer;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
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
                .compilers(new SimplePageCompiler(), new StandardPageCompiler())
                .transformers(new N2oFormV5AdapterTransformer(), new N2oWidgetV5AdapterTransformer())
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
        assertThat(((MandatoryValidation) validations.get(0)).getEnablingExpression(), is("(testField2 == 'test') && (testField3 == 'test')"));
        assertThat(validations.get(0).getMoment(), is(N2oValidation.ServerMoment.beforeOperation));

        validations = form.getComponent().getValidation().get("testField4");
        assertThat(((MandatoryValidation) validations.get(0)).getEnablingExpression(), is("(function(){return typeof testField != 'undefined' && testField != null && testField == 2})()"));
        assertThat(validations.get(0).getMoment(), is(N2oValidation.ServerMoment.beforeOperation));

        validations = form.getComponent().getValidation().get("testInterval");
        assertThat(validations.size(), is(2));
        assertThat(((ConditionValidation) validations.get(0)).getExpression(), is("typeof testIntervalBegin == 'undefined'"));

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
        assertThat(context.getSuccessAlertWidgetId(), is("form"));
        assertThat(context.getFailAlertWidgetId(), is("form"));
        assertThat(context.getRefresh().getType(), is(RefreshSaga.Type.datasource));
      //fixme  assertThat(context.getRefresh().getOptions().getDatasourcesId(), is("form"));

        ClientDataProvider dataProvider =page.getDatasources().get("form").getSubmit();
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
        assertThat(link.getModel(), is(ReduxModel.FILTER));
        assertThat(link.getDatasource(), is("testFormSubmit_id2"));
        assertThat(link.getBindLink(), is("models.filter['testFormSubmit_id2']"));

        assertThat(dataProvider.getHeadersMapping().size(), is(1));
        link = dataProvider.getHeadersMapping().get("name3");
        assertThat(link.getValue(), is("`a`"));
        assertThat(link.getModel(), is(ReduxModel.RESOLVE));
        assertThat(link.getDatasource(), is("testFormSubmit_id3"));
        assertThat(link.getBindLink(), is("models.resolve['testFormSubmit_id3']"));

        assertThat(dataProvider.getFormMapping().size(), is(1));
        link = dataProvider.getFormMapping().get("name4");
        assertThat(link.getValue(), is("`b`"));
        assertThat(link.getModel(), is(ReduxModel.FILTER));
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
        assertThat(detailPage.getDatasources().get("testSubmitInModalIndex_open_main").getSubmit().getPathMapping().size(), is(1));
        assertThat(detailPage.getDatasources().get("testSubmitInModalIndex_open_main").getSubmit().getUrl(),
                is("n2o/data/testSubmitInModalIndex/:id/open/testSubmitInModalIndex_open_main"));
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
