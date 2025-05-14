package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.data.validation.ConditionValidation;
import net.n2oapp.framework.api.data.validation.MandatoryValidation;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.datasource.StandardDatasource;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesModeEnum;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.CopyDependency;
import net.n2oapp.framework.api.metadata.meta.Dependency;
import net.n2oapp.framework.api.metadata.meta.DependencyTypeEnum;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePlacementEnum;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePositionEnum;
import net.n2oapp.framework.api.metadata.meta.widget.RequestMethodEnum;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.control.v3.plain.InputTextIOv3;
import net.n2oapp.framework.config.metadata.compile.context.ActionContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyMap;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;

/**
 * Тестирование компиляции стандартного источника данных
 */
class StandardDataSourceCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oAllDataPack()).ios( new InputTextIOv3())
                .sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/datasource/testDatasourceCompile.query.xml"),
                        new CompileInfo("net/n2oapp/framework/config/metadata/compile/datasource/testDataSourceCompile.object.xml"));
    }

    @Test
    void simple() {
        StandardPage page = (StandardPage)
                compile("net/n2oapp/framework/config/metadata/compile/datasource/testDSStandardPage.page.xml")
                        .get(new PageContext("testDSStandardPage"));

        StandardDatasource ds = (StandardDatasource) page.getDatasources().get("testDSStandardPage_ds1");
        assertThat(ds, notNullValue());
        assertThat(ds.getDefaultValuesMode(), is(DefaultValuesModeEnum.defaults));
        assertThat(ds.getProvider(), nullValue());
        assertThat(ds.getFetchOnInit(), is(false));

        ds = (StandardDatasource) page.getDatasources().get("testDSStandardPage_ds2");
        assertThat(ds.getFetchOnInit(), is(true));

        ds = (StandardDatasource) page.getDatasources().get("testDSStandardPage_ds3");
        assertThat(ds.getFetchOnInit(), is(false));

        SimplePage simplePage = (SimplePage)
                compile("net/n2oapp/framework/config/metadata/compile/datasource/testDSSimplePage.page.xml")
                        .get(new PageContext("testDSSimplePage"));

        ds = (StandardDatasource) simplePage.getDatasources().get("testDSSimplePage_w1");
        assertThat(ds, notNullValue());
        assertThat(ds.getDefaultValuesMode(), is(DefaultValuesModeEnum.defaults));
        assertThat(ds.getProvider(), nullValue());
    }

    @Test
    void query() {
        StandardPage page = (StandardPage)
                compile("net/n2oapp/framework/config/metadata/compile/datasource/testDSQuery.page.xml")
                        .get(new PageContext("testDSQuery", "/"));

        StandardDatasource ds = (StandardDatasource) page.getDatasources().get("_ds1");
        assertThat(ds, notNullValue());
        assertThat(ds.getDefaultValuesMode(), is(DefaultValuesModeEnum.query));
        assertThat(ds.getProvider(), notNullValue());
        assertThat(ds.getProvider().getUrl(), is("n2o/data/_ds1"));
        QueryContext queryCtx = ((QueryContext)route("/_ds1", CompiledQuery.class));
        assertThat(queryCtx, notNullValue());
    }

    @Test
    void queryFilters() {
        PageContext context = new PageContext("testDSQueryFilters", "/p/w/a");
        context.setParentRoute("p/w");
        StandardPage page = (StandardPage)
                compile("net/n2oapp/framework/config/metadata/compile/datasource/testDSQueryFilters.page.xml")
                        .get(context);

        StandardDatasource ds = (StandardDatasource) page.getDatasources().get("p_w_a_ds1");
        assertThat(ds.getProvider().getUrl(), is("n2o/data/p/w/a/ds1"));
        assertThat(ds.getProvider().getQueryMapping(), hasEntry("ds1_id", new ModelLink(1)));
        CompiledQuery query = routeAndGet("/p/w/a/ds1", CompiledQuery.class);
        assertThat(query.getParamToFilterIdMap(), hasEntry("ds1_id", "id"));

        ds = (StandardDatasource) page.getDatasources().get("p_w_a_ds2");
        assertThat(ds.getProvider().getUrl(), is("n2o/data/p/w/a/ds2"));
        ModelLink link = new ModelLink(ReduxModelEnum.resolve, "p_w_a_ds3");
        link.setValue("`id`");
        assertThat(ds.getProvider().getQueryMapping(), hasEntry("ds2_id", link));
        assertThat(ds.getProvider().getQueryMapping().get("ds2_id").isRequired(), is(true));
        query = routeAndGet("/p/w/a/ds2", CompiledQuery.class);
        assertThat(query.getParamToFilterIdMap(), hasEntry("ds2_id", "id"));
    }

    @Test
    void dependencies() {
        StandardPage page = (StandardPage)
                compile("net/n2oapp/framework/config/metadata/compile/datasource/testDatasourceDependencies.page.xml")
                        .get(new PageContext("testDatasourceDependencies", "/p/w/a"));

        StandardDatasource ds = (StandardDatasource) page.getDatasources().get("p_w_a_detail");
        assertThat(ds.getDependencies().size(), is(2));
        Dependency dependency = ds.getDependencies().get(0);
        assertThat(dependency.getOn(), is("models.filter['p_w_a_master']"));
        assertThat(dependency.getType(), is(DependencyTypeEnum.fetch));

        dependency = ds.getDependencies().get(1);
        assertThat(dependency.getType(), is(DependencyTypeEnum.copy));
        assertThat(dependency.getOn(), is("models.filter['p_w_a_detail'].source"));
        assertThat(((CopyDependency) dependency).getModel(), is(ReduxModelEnum.datasource));
        assertThat(((CopyDependency) dependency).getField(), is("target"));
        assertThat(((CopyDependency) dependency).getSubmit(), is(true));
        assertThat(((CopyDependency) dependency).getApplyOnInit(), is(true));
    }

    @Test
    void submit() {
        StandardPage page = (StandardPage)
                compile("net/n2oapp/framework/config/metadata/compile/datasource/testDSSubmit.page.xml")
                        .get(new PageContext("testDSSubmit", "/p/w/a"));

        //        simple
        StandardDatasource ds = (StandardDatasource) page.getDatasources().get("p_w_a_ds1");
        assertThat(ds.getSubmit(), Matchers.notNullValue());
        assertThat(ds.getSubmit().getUrl(), is("n2o/data/p/w/a/ds1"));
        assertThat(ds.getSubmit().getSubmitForm(), is(true));
        assertThat(ds.getSubmit().getMethod(), is(RequestMethodEnum.POST));
        ActionContext opCtx = ((ActionContext)route("/p/w/a/ds1", CompiledObject.class));
        assertThat(opCtx.getOperationId(), is("update"));
        assertThat(opCtx.isMessageOnSuccess(), is(true));
        assertThat(opCtx.isMessageOnFail(), is(true));
        assertThat(opCtx.getMessagePosition(), is(MessagePositionEnum.fixed));
        assertThat(opCtx.getMessagePlacement(), is(MessagePlacementEnum.top));

        //        with form-param
        ds = (StandardDatasource) page.getDatasources().get("p_w_a_ds2");
        assertThat(ds.getSubmit(), Matchers.notNullValue());
        assertThat(ds.getSubmit().getSubmitForm(), is(false));
        ModelLink link = new ModelLink(ReduxModelEnum.resolve, "p_w_a_ds2");
        link.setValue("`id`");
        assertThat(ds.getSubmit().getFormMapping(), hasEntry("id", link));

        //        with messages
        ds = (StandardDatasource) page.getDatasources().get("p_w_a_ds3");
        assertThat(ds.getSubmit(), Matchers.notNullValue());
        opCtx = ((ActionContext)route("/p/w/a/ds3", CompiledObject.class));
        assertThat(opCtx.isMessageOnSuccess(), is(true));
        assertThat(opCtx.isMessageOnFail(), is(true));
        assertThat(opCtx.getMessagePosition(), is(MessagePositionEnum.fixed));
        assertThat(opCtx.getMessagePlacement(), is(MessagePlacementEnum.bottom));

        //        with path-param
        ds = (StandardDatasource) page.getDatasources().get("p_w_a_ds4");
        assertThat(ds.getSubmit(), Matchers.notNullValue());
        assertThat(ds.getSubmit().getUrl(), is("n2o/data/p/w/a/:_id/update"));
        link = new ModelLink(ReduxModelEnum.resolve, "p_w_a_ds4");
        link.setValue("`id`");
        assertThat(ds.getSubmit().getPathMapping(), hasEntry("_id", link));
        opCtx = ((ActionContext)route("/p/w/a/123/update", CompiledObject.class));
        assertThat(opCtx, Matchers.notNullValue());
        assertThat(opCtx.getParams("/p/w/a/123/update", emptyMap()), hasEntry("_id", "123"));
    }

    @Test
    void validation() {
        StandardPage page = (StandardPage)
                compile("net/n2oapp/framework/config/metadata/compile/datasource/testDSValidation.page.xml")
                        .get(new PageContext("testDSValidation", "/p/w/a"));

        StandardDatasource ds = (StandardDatasource) page.getDatasources().get("p_w_a_ds1");

        // validations
        assertThat(ds.getValidations().get("id"), notNullValue());
        assertThat(ds.getValidations().get("id").size(), is(1));
        assertThat(ds.getValidations().get("id").get(0), instanceOf(MandatoryValidation.class));

        assertThat(ds.getValidations().get("name"), notNullValue());
        assertThat(ds.getValidations().get("name").size(), is(1));
        assertThat(ds.getValidations().get("name").get(0), instanceOf(ConditionValidation.class));
        assertThat(((ConditionValidation) ds.getValidations().get("name").get(0)).getExpression(), is("name.length>1"));

        // filter validations
        assertThat(ds.getFilterValidations().get("id2"), notNullValue());
        assertThat(ds.getFilterValidations().get("id2").size(), is(1));
        assertThat(ds.getFilterValidations().get("id2").get(0), instanceOf(MandatoryValidation.class));

        assertThat(ds.getFilterValidations().get("name2"), notNullValue());
        assertThat(ds.getFilterValidations().get("name2").size(), is(1));
        assertThat(ds.getFilterValidations().get("name2").get(0), instanceOf(ConditionValidation.class));
        assertThat(((ConditionValidation) ds.getFilterValidations().get("name2").get(0)).getExpression(), is("name2.length>1"));
    }

    @Test
    void validationInMultiset() {
        StandardPage page = (StandardPage)
                compile("net/n2oapp/framework/config/metadata/compile/datasource/testDSValidationInMultiset.page.xml")
                        .get(new PageContext("testDSValidationInMultiset", "/p/w/a"));

        StandardDatasource ds = (StandardDatasource) page.getDatasources().get("p_w_a_ds1");

        //multi-set validations
        assertThat(ds.getValidations().get("members[index].id"), notNullValue());
        assertThat(ds.getValidations().get("members[index].id").size(), is(1));
        assertThat(ds.getValidations().get("members[index].id").get(0), instanceOf(MandatoryValidation.class));

        assertThat(ds.getValidations().get("members[index].name"), notNullValue());
        assertThat(ds.getValidations().get("members[index].name").size(), is(1));
        assertThat(ds.getValidations().get("members[index].name").get(0), instanceOf(ConditionValidation.class));
        assertThat(((ConditionValidation) ds.getValidations().get("members[index].name").get(0)).getExpression(), is("name.length>3"));

        //multi-set filter validations
        assertThat(ds.getFilterValidations().get("members2[index].id2"), notNullValue());
        assertThat(ds.getFilterValidations().get("members2[index].id2").size(), is(1));
        assertThat(ds.getFilterValidations().get("members2[index].id2").get(0), instanceOf(MandatoryValidation.class));

        assertThat(ds.getFilterValidations().get("members2[index].name2"), notNullValue());
        assertThat(ds.getFilterValidations().get("members2[index].name2").size(), is(1));
        assertThat(ds.getFilterValidations().get("members2[index].name2").get(0), instanceOf(ConditionValidation.class));
        assertThat(((ConditionValidation) ds.getFilterValidations().get("members2[index].name2").get(0)).getExpression(), is("name2.length>3"));
    }
}
