package net.n2oapp.framework.config.metadata.compile.widget.form;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.validation.MandatoryValidation;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.datasource.StandardDatasource;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesModeEnum;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.close.CloseAction;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.RequestMethodEnum;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.page.v3.SimplePageElementIOv3;
import net.n2oapp.framework.config.io.page.v3.StandardPageElementIOv3;
import net.n2oapp.framework.config.io.page.v4.SimplePageElementIOv4;
import net.n2oapp.framework.config.io.page.v4.StandardPageElementIOv4;
import net.n2oapp.framework.config.metadata.compile.context.ActionContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.compile.datasource.StandardDatasourceCompiler;
import net.n2oapp.framework.config.metadata.compile.page.SimplePageCompiler;
import net.n2oapp.framework.config.metadata.compile.page.StandardPageCompiler;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тест сборки формы
 */
class FormWidgetCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(
          new N2oAllDataPack(),
          new N2oFieldSetsPack(),
          new N2oControlsPack(),
          new N2oCellsPack(),
          new N2oActionsPack(),
          new N2oWidgetsPack(),
          new N2oRegionsPack())
                .ios(new SimplePageElementIOv4(), new SimplePageElementIOv3(), new StandardPageElementIOv3(), new SimplePageElementIOv3(), new StandardPageElementIOv3(), new StandardPageElementIOv4())
                .compilers(new SimplePageCompiler(), new StandardPageCompiler(), new StandardDatasourceCompiler())
                .sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/widgets/testTable5Compile.query.xml"),
                        new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.object.xml"));
    }

    @Test
    void uploadDefaults() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testFormCompile.page.xml")
                .get(new PageContext("testFormCompile"));
        assertThat(page.getWidget().getId(), is("testFormCompile_w1"));
        Form form = (Form) page.getWidget();
        assertThat(form.getComponent().getPrompt(), is(true));
        StandardDatasource datasource = (StandardDatasource) page.getDatasources().get("testFormCompile_w1");
        assertThat(datasource.getDefaultValuesMode(), is(DefaultValuesModeEnum.DEFAULTS));
        assertThat(datasource.getProvider(), nullValue());
    }

    @Test
    void uploadQuery() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testFormCompile2.page.xml")
                .get(new PageContext("testFormCompile2"));
        assertThat(page.getWidget().getId(), is("testFormCompile2_w1"));
        assertThat(page.getWidget().getDatasource(), notNullValue());
        StandardDatasource datasource = (StandardDatasource) page.getDatasources().get(page.getWidget().getDatasource());
        assertThat(datasource.getDefaultValuesMode(), is(DefaultValuesModeEnum.QUERY));
        assertThat(datasource.getPaging().getSize(), is(1));
        QueryContext queryContext = (QueryContext) route("/testFormCompile2/w1", CompiledQuery.class);
        assertThat(queryContext, notNullValue());
    }

    @Test
    void testFormClientValidations() {
        PageContext pageContext = new PageContext("testFormValidations");
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testFormValidations.page.xml",
                "net/n2oapp/framework/config/metadata/compile/widgets/testFormValidations.object.xml")
                .get(pageContext);


        List<Validation> validations = page.getDatasources().get("testFormValidations_w1").getValidations().get("testField");

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
        assertThat(validations.get(8).getMoment(), is(N2oValidation.ServerMomentEnum.BEFORE_OPERATION));
        assertThat(validations.get(8).getSide().contains("client"), is(true));

        assertThat(validations.get(9).getId(), is("Condition2"));
        assertThat(validations.get(9).getSide().contains("client"), is(true));

        assertThat(validations.get(10).getId(), is("Condition3"));
        assertThat(validations.get(10).getSide(), is(nullValue()));
    }

    @Test
    void testFormPreFilterValidations() {
        compile("net/n2oapp/framework/config/metadata/compile/widgets/testFormPreFilterValidation.page.xml",
                "net/n2oapp/framework/config/metadata/compile/widgets/testFormPreFilterValidation.query.xml",
                "net/n2oapp/framework/config/metadata/compile/widgets/testFormValidations.object.xml")
                .get(new PageContext("testFormPreFilterValidation"));
        QueryContext queryContext = (QueryContext) route("/testFormPreFilterValidation/form", CompiledQuery.class);
        List<Validation> validations = queryContext.getValidations();
        assertThat(validations.size(), is(1));
        MandatoryValidation validation = (MandatoryValidation) validations.get(0);
        assertThat(validation.getId(), is("testField"));
        assertThat(validation.getMessage(), is("Поле обязательно для заполнения"));
    }

    @Test
    void testFormStyles() {
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
    void testSubmit() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testFormSubmit.page.xml",
                "net/n2oapp/framework/config/metadata/compile/widgets/testFormValidations.object.xml")
                .get(new PageContext("testFormSubmit"));
        Form form = (Form) page.getWidget();

        ActionContext context = (ActionContext) route("/testFormSubmit/a/b/c", CompiledObject.class);
        assertThat(context, notNullValue());
        assertThat(context.getOperationId(), is("test"));
        assertThat(context.isMessageOnFail(), is(true));
        assertThat(context.isMessageOnSuccess(), is(true));
        assertThat(context.getMessagesForm(), is("form"));
        assertThat(context.getRefresh().getDatasources(), hasItem("testFormSubmit_form"));

        ClientDataProvider dataProvider = ((StandardDatasource) page.getDatasources().get(form.getDatasource())).getSubmit();
        assertThat(dataProvider.getMethod(), is(RequestMethodEnum.POST));
        assertThat(dataProvider.getSubmitForm(), is(true));
        assertThat(dataProvider.getUrl(), is("n2o/data/testFormSubmit/a/b/c"));

        assertThat(dataProvider.getPathMapping().size(), is(2));
        ModelLink link = dataProvider.getPathMapping().get("name1");
        assertThat(link.getValue(), is("value1"));
        assertThat(link.getModel(), nullValue());
        assertThat(link.getDatasource(), nullValue());
        assertThat(link.getLink(), nullValue());
        link = dataProvider.getPathMapping().get("name2");
        assertThat(link.getValue(), nullValue());
        assertThat(link.getModel(), is(ReduxModelEnum.FILTER));
        assertThat(link.getDatasource(), is("testFormSubmit_form"));
        assertThat(link.getLink(), is("models.filter['testFormSubmit_form']"));

        assertThat(dataProvider.getHeadersMapping().size(), is(1));
        link = dataProvider.getHeadersMapping().get("name3");
        assertThat(link.getValue(), is("`a`"));
        assertThat(link.getModel(), is(ReduxModelEnum.RESOLVE));
        assertThat(link.getDatasource(), is("testFormSubmit_form"));
        assertThat(link.getLink(), is("models.resolve['testFormSubmit_form']"));

        assertThat(dataProvider.getFormMapping().size(), is(1));
        link = dataProvider.getFormMapping().get("name4");
        assertThat(link.getValue(), is("`b`"));
        assertThat(link.getModel(), is(ReduxModelEnum.FILTER));
        assertThat(link.getDatasource(), is("testFormSubmit_form"));
        assertThat(link.getLink(), is("models.filter['testFormSubmit_form']"));
    }

    @Test
    void testSubmitInModal() {
        compile("net/n2oapp/framework/config/metadata/compile/widgets/testSubmitInModalIndex.page.xml",
                "net/n2oapp/framework/config/metadata/compile/widgets/testSubmitInModal.page.xml"
        ).get(new PageContext("testSubmitInModalIndex"));

        PageContext detailContext = (PageContext) route("/testSubmitInModalIndex/:id/open", Page.class);
        DataSet data = new DataSet();
        data.put("id", 1);
        SimplePage detailPage = (SimplePage) read().compile().bind().get(detailContext, data);
        Form form = (Form) detailPage.getWidget();
        assertThat(((StandardDatasource) detailPage.getDatasources().get(form.getDatasource())).getSubmit().getPathMapping().size(), is(1));
        assertThat(((StandardDatasource) detailPage.getDatasources().get(form.getDatasource())).getSubmit().getUrl(), is("n2o/data/testSubmitInModalIndex/:id/open/w1"));
        AbstractButton closeBtn = detailPage.getWidget().getToolbar().get("bottomRight").get(0).getButtons().get(0);
        assertThat(closeBtn, notNullValue());
        assertThat(closeBtn.getAction(), instanceOf(CloseAction.class));
        assertThat(closeBtn.getValidate(), nullValue());
    }

    @Test
    void testFormAsFilter() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testFormAsFilter.page.xml",
                "net/n2oapp/framework/config/metadata/compile/widgets/testFormAsFilter.query.xml")
                .get(new PageContext("testFormAsFilter"));
        assertThat(page.getRoutes().getQueryMapping().get("period").getOnSet().getLink(), is("models.resolve['testFormAsFilter_filters'].period"));
        assertThat(page.getRoutes().getQueryMapping().get("period").getOnSet().getValue(), is("`id`"));
    }

    @Test
    void testInlineDatasourceInSimplePage() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testFormInlineDatasourceInSimplePage.page.xml")
                .get(new PageContext("testFormInlineDatasourceInSimplePage"));

        assertThat(page.getDatasources().size(), is(1));
        assertThat(page.getDatasources().get("testFormInlineDatasourceInSimplePage_ds").getPaging().getSize(), is(1));
    }

    @Test
    void testInlineDatasourceInPage() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testFormInlineDatasourceInPage.page.xml")
         .get(new PageContext("testFormInlineDatasourceInPage"));

        assertThat(page.getDatasources().size(), is(2));
        Form formWithoutId = (Form) page.getRegions().get("single").get(0).getContent().get(0);
        assertThat(formWithoutId.getDatasource(), is("testFormInlineDatasourceInPage_ds"));
        Form formWithId = (Form) page.getRegions().get("single").get(0).getContent().get(1);
        assertThat(formWithId.getDatasource(), is("testFormInlineDatasourceInPage_ds_w"));
    }

    @Test
    void testFetchOnInitAndOnVisibility() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testFetchOnInitOnVisibility.page.xml")
                .get(new PageContext("testFetchOnInitOnVisibility"));

        assertThat(page.getWidget().getFetchOnInit(), is(false));
        assertThat(page.getWidget().getFetchOnVisibility(), is(false));
    }
}
