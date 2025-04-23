package net.n2oapp.framework.config.metadata.compile.dynamic;

import net.n2oapp.framework.api.metadata.dataprovider.N2oSqlDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QuerySimpleField;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oTable;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.control.Select;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.register.JavaInfo;
import net.n2oapp.framework.config.register.dynamic.JavaSourceLoader;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Проверка получения динамического объекта, запроса, виджета и страницы
 */
class DynamicMetadataCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllDataPack(), new N2oAllPagesPack())
                .sources(new JavaInfo("testDynamic", N2oObject.class),
                        new JavaInfo("testDynamic", N2oQuery.class),
                        new JavaInfo("testDynamic", N2oTable.class),
                        new JavaInfo("testDynamic", N2oPage.class))
                .providers(new TestDynamicProvider())
                .loaders(new JavaSourceLoader(builder.getEnvironment().getDynamicMetadataProviderFactory()));
    }

    @Test
    void testDynamicPage() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/dynamic/testDynamicObject.page.xml",
                "net/n2oapp/framework/config/metadata/compile/dynamic/formForTestDynamic.widget.xml",
                "net/n2oapp/framework/config/metadata/compile/action/testShowModal.object.xml",
                "net/n2oapp/framework/config/metadata/compile/action/testOpenPageDynamicPage.query.xml")
                .get(new PageContext("testDynamicObject", "/test/route"));

        Table table = (Table) page.getRegions().get("single").get(0).getContent().get(0);
        assertThat(table, instanceOf(Table.class));
        assertThat(table.getComponent().getBody().getCells().size(), is(1));
        assertThat(table.getComponent().getBody().getCells().get(0).getId(), is("id"));
        CompiledQuery query = routeAndGet("/test/route/main", CompiledQuery.class);
        assertThat(query.getId(), is("testDynamic?Dummy"));
        assertThat(((N2oSqlDataProvider) query.getLists()[0].getInvocation()).getQuery(), is("test select"));
        // динамическая страница в контекстно-независимой кнопке
        SimplePage dynamicCreatePage = (SimplePage) routeAndGet("/test/route/create", Page.class);
        assertThat(dynamicCreatePage.getId(), is("test_route_create"));
        assertThat(dynamicCreatePage.getWidget(), instanceOf(Form.class));
        assertThat((((Form) dynamicCreatePage.getWidget()).getComponent().getFieldsets().get(0)
                .getRows().get(0).getCols().get(0).getFields().get(0)).getId(), is("id"));
        assertThat(((StandardField) ((Form) dynamicCreatePage.getWidget()).getComponent().getFieldsets().get(0)
                .getRows().get(0).getCols().get(0).getFields().get(0)).getControl().getId(), is("id"));
        // динамическая страница в контекстной кнопке
        SimplePage dynamicPage = (SimplePage) routeAndGet("/test/route/123/update", Page.class);
        assertThat(dynamicPage.getId(), is("test_route_update"));
        assertThat(dynamicPage.getWidget(), instanceOf(Form.class));
        assertThat((((Form) dynamicPage.getWidget()).getComponent().getFieldsets().get(0)
                .getRows().get(0).getCols().get(0).getFields().get(0)).getId(), is("id"));
        assertThat(((StandardField) ((Form) dynamicPage.getWidget()).getComponent().getFieldsets().get(0)
                .getRows().get(0).getCols().get(0).getFields().get(0)).getControl().getId(), is("id"));


        dynamicPage = (SimplePage) routeAndGet("/test/route/second/123/update", Page.class);
        assertThat(dynamicPage.getId(), is("test_route_second_123_update"));
        assertThat(dynamicPage.getWidget(), instanceOf(Form.class));
        assertThat((((Form) dynamicPage.getWidget()).getComponent().getFieldsets().get(0)
                .getRows().get(0).getCols().get(0).getFields().get(0)).getId(), is("id"));
        assertThat(((StandardField) ((Form) dynamicPage.getWidget()).getComponent().getFieldsets().get(0)
                .getRows().get(0).getCols().get(0).getFields().get(0)).getControl().getId(), is("id"));
    }

    @Test
    void testDynamicQuery() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/dynamic/testDynamicQuery.page.xml",
                "net/n2oapp/framework/config/metadata/compile/dynamic/testDynamicQuery.query.xml")
                .get(new PageContext("testDynamicQuery"));

        List<FieldSet.Row> rows = ((Form) page.getWidget()).getComponent().getFieldsets().get(0).getRows();
        Select select1 = (Select) ((StandardField) rows.get(0).getCols().get(0).getFields().get(0)).getControl();
        assertThat(select1.getDataProvider().getUrl(), is("n2o/data/testDynamicQuery_version_1"));
        CompiledQuery query = routeAndGet("/testDynamicQuery_version_1", CompiledQuery.class);
        assertThat(((QuerySimpleField) query.getFieldsMap().get("versionId")).getDefaultValue(), is("1"));
        assertThat(((QuerySimpleField) query.getFieldsMap().get("name")).getDefaultValue(), is("name"));

        Select select2 = (Select) ((StandardField) rows.get(1).getCols().get(0).getFields().get(0)).getControl();
        assertThat(select2.getDataProvider().getUrl(), is("n2o/data/testDynamicQuery_version_2_name_test"));
        query = routeAndGet("/testDynamicQuery_version_2_name_test", CompiledQuery.class);
        assertThat(((QuerySimpleField) query.getFieldsMap().get("versionId")).getDefaultValue(), is("2"));
        assertThat(((QuerySimpleField) query.getFieldsMap().get("name")).getDefaultValue(), is("test"));
    }
}
