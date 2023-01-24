package net.n2oapp.framework.test;

import net.n2oapp.framework.api.data.validation.ConditionValidation;
import net.n2oapp.framework.api.data.validation.MandatoryValidation;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.exception.SeverityType;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;
import net.n2oapp.framework.api.metadata.meta.Filter;
import net.n2oapp.framework.api.metadata.meta.control.InputText;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.register.route.RouteInfo;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.ActionContext;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TestPack implements MetadataPack<N2oApplicationBuilder> {
    @Override
    public void build(N2oApplicationBuilder b) {
        b.routes(new RouteInfo("/test/sql/v4", getTestQueryContext("testSqlQuery4", "/test/sql/v4")),
                new RouteInfo("/test/rest/v4", getTestQueryContext("testRestQuery4", "/test/rest/v4")),
                new RouteInfo("/test/java/v4", getTestQueryContext("testJavaQuery4", "/test/java/v4")),
                new RouteInfo("/test/java/spring/v4", new QueryContext("testSpringQuery4", "/test/java/spring/v4")),
                new RouteInfo("/test/java/static/v4", new QueryContext("testStaticQuery4", "/test/java/static/v4")),
                new RouteInfo("/test/invoke/action", getActionContext()),
                new RouteInfo("/test/master/:master_id/detail", getMasterDetailQueryContext()),
                new RouteInfo("/test/select", new QueryContext("testSqlQuery4", "/test/select")),
                new RouteInfo("/test/sql/validation", getQueryContext()),
                new RouteInfo("/testDialog", getTestDialogActionContext()),
                new RouteInfo("/testInsertMongo", getTestInsertMongodbContext("create", "/testInsertMongo")),
                new RouteInfo("/testUpdateMongo", getTestInsertMongodbContext("update", "/testUpdateMongo")),
                new RouteInfo("/testDeleteMongo", getTestInsertMongodbContext("delete", "/testDeleteMongo")),
                new RouteInfo("/test/mongodb", getTestMongoQueryContext("testMongodbQuery4", "/test/mongodb")),
                new RouteInfo("/test/mongodbCount", getTestMongoQueryContext("testMongodbQuery4", "/test/mongodbCount")),
                new RouteInfo("/test/subModels", getQueryContextWithSubModel()),
                new RouteInfo("/test/testAdditionalInfo", new QueryContext("testAdditionalInfo", "/test/testAdditionalInfo")),
                // graphql
                new RouteInfo("/test/graphql/query/variables", getTestGraphqlQueryContext("testGraphqlVariables", "/test/graphql/query/variables")),
                new RouteInfo("/test/graphql/query/headersForwarding", getTestGraphqlQueryContext("testGraphqlHeaderForwarding", "/test/graphql/query/headersForwarding")),
                new RouteInfo("/test/graphql/mutationVariables", getTestInsertGraphqlContext("testVariables", "/test/graphql/mutationVariables")),
                new RouteInfo("/test/graphql/mutationPlaceholders", getTestInsertGraphqlContext("testPlaceholders", "/test/graphql/mutationPlaceholders")),
                new RouteInfo("/test/graphql/select", getTestGraphqlQueryContext("testGraphqlSelect", "/test/graphql/select")),
                new RouteInfo("/test/graphql/filters", getTestGraphqlQueryContext("testGraphqlFilters", "/test/graphql/filters")),
                new RouteInfo("/test/graphql/pagination", getTestGraphqlQueryContext("testGraphqlPagination", "/test/graphql/pagination")),
                new RouteInfo("/test/graphql/sorting", getTestGraphqlQueryContext("testGraphqlSorting", "/test/graphql/sorting")),
                new RouteInfo("/test/graphql/hierarchicalSelect", getTestGraphqlQueryContext("testGraphqlHierarchicalSelect", "/test/graphql/hierarchicalSelect")),
                new RouteInfo("/test/graphql/enums", getTestGraphqlQueryContext("testGraphqlEnums", "/test/graphql/enums")));
    }

    private QueryContext getTestQueryContext(String testQuery, String s) {
        QueryContext queryContext = new QueryContext(testQuery, s);
        ArrayList<Filter> filters = new ArrayList<>();
        createFilter(filters, "id");
        queryContext.setFilters(filters);
        HashMap<String, String> sortingMap = new HashMap<>();
        sortingMap.put("sorting.value", "value");
        queryContext.setSortingMap(sortingMap);
        return queryContext;
    }

    private QueryContext getTestMongoQueryContext(String testQuery, String s) {
        QueryContext queryContext = new QueryContext(testQuery, s);
        ArrayList<Filter> filters = new ArrayList<>();
        createFilter(filters, "id");
        createFilter(filters, "name");
        createFilter(filters, "gender_id");
        createFilter(filters, "nameLike");
        createFilter(filters, "nameStart");
        createFilter(filters, "notName");
        createFilter(filters, "birthdayMore");
        createFilter(filters, "userAgeNotIn");
        createFilter(filters, "idIn");
        createFilter(filters, "userNameIn");
        createFilter(filters, "userAgeIn");
        createFilter(filters, "birthdayLess");
        queryContext.setFilters(filters);
        return queryContext;
    }

    private QueryContext getTestGraphqlQueryContext(String testQuery, String s) {
        QueryContext queryContext = new QueryContext(testQuery, s);

        ArrayList<Filter> filters = new ArrayList<>();
        createFilter(filters, "id");
        createFilter(filters, "personName");
        createFilter(filters, "age");
        createFilter(filters, "salary");
        createFilter(filters, "address.name");
        queryContext.setFilters(filters);

        HashMap<String, String> sortingMap = new HashMap<>();
        sortingMap.put("sorting.name", "name");
        sortingMap.put("sorting.age", "age");
        sortingMap.put("sorting.id", "id");
        queryContext.setSortingMap(sortingMap);

        return queryContext;
    }

    private ActionContext getTestInsertGraphqlContext(String operationId, String route) {
        ActionContext actionContext = new ActionContext("testGraphqlMutation", operationId, route);
        actionContext.setMessagesForm("testForm");
        return actionContext;
    }

    private void createFilter(ArrayList<Filter> filters, String id) {
        Filter filter1 = new Filter();
        filter1.setFilterId(id);
        filter1.setParam(id);
        filters.add(filter1);
    }

    private QueryContext getQueryContext() {
        QueryContext context = new QueryContext("testSqlQuery4", "/test/sql/validation");
        StandardField field = new StandardField();
        InputText control = new InputText();
        control.setId("id");
        field.setControl(control);
        MandatoryValidation mandatory = new MandatoryValidation("id_validation", "id is required", field.getControl().getId());
        mandatory.setMoment(N2oValidation.ServerMoment.beforeQuery);
        context.setValidations(Arrays.asList(mandatory));
        context.setMessagesForm("testTable.filter");
        return context;
    }

    private QueryContext getQueryContextWithSubModel() {
        QueryContext context = new QueryContext("testModel", "/test/subModels");
        SubModelQuery subModel = new SubModelQuery("subModel", "testSubModel", "id", "name", false, null);
        context.setSubModelQueries(Collections.singletonList(subModel));
        context.setQuerySize(1);
        return context;
    }

    private ActionContext getActionContext() {
        ActionContext actionContext = new ActionContext("testActionContext", "create", "/test/invoke/action");
        actionContext.setValidations(createValidations());
        actionContext.setMessagesForm("testForm");
        return actionContext;
    }

    private ActionContext getTestDialogActionContext() {
        ActionContext actionContext = new ActionContext("testDialog", "create", "/testDialog");
        actionContext.setMessagesForm("testForm");
        actionContext.setParentSourceDatasourceId("testForm");
        actionContext.setParentClientWidgetId("testDialog_main");
        actionContext.setParentPageId("testDialog");
        return actionContext;
    }

    private ActionContext getTestInsertMongodbContext(String operationId, String route) {
        ActionContext actionContext = new ActionContext("testMongodbCRUD", operationId, route);
        actionContext.setMessagesForm("testForm");
        return actionContext;
    }

    private QueryContext getMasterDetailQueryContext() {
        QueryContext queryContext = new QueryContext("testMasterDetail", "/test/master/:master_id/detail");
        Filter preFilter = new Filter();
        preFilter.setRoutable(false);
        preFilter.setFilterId("individualId");
        preFilter.setParam("master_id");
        queryContext.setFilters(Collections.singletonList(preFilter));
        return queryContext;
    }

    private List<Validation> createValidations() {
        List<Validation> validations = new ArrayList<>();
        StandardField field = new StandardField();
        InputText control = new InputText();
        control.setId("id");
        field.setControl(control);
        MandatoryValidation mandatory = new MandatoryValidation("required_id", "Id is null", field.getControl().getId());
        mandatory.setMoment(N2oValidation.ServerMoment.beforeOperation);
        validations.add(mandatory);
        ConditionValidation conditionValidation = new ConditionValidation();
        conditionValidation.setId("testName");
        conditionValidation.setSeverity(SeverityType.danger);
        conditionValidation.setExpression("name === 'testName'");
        conditionValidation.setFieldId("name");
        conditionValidation.setMessage("Name should be testName");
        validations.add(conditionValidation);
        return validations;
    }

}
