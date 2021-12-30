package net.n2oapp.framework.config.metadata.compile.dynamic;

import net.n2oapp.framework.api.metadata.dataprovider.N2oSqlDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oTable;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
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
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Проверка получения динамического объекта, запроса, виджета и страницы
 */
public class DynamicMetadataCompileTest extends SourceCompileTestBase {
    @Override
    @Before
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
                        new JavaInfo("testDynamic", N2oPage.class),
                        new CompileInfo("net/n2oapp/framework/config/metadata/compile/dynamic/formForTestDynamic.widget.xml"),
                        new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testShowModal.object.xml"),
                        new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testOpenPageDynamicPage.query.xml"))
                .providers(new TestDynamicProvider())
                .loaders(new JavaSourceLoader(builder.getEnvironment().getDynamicMetadataProviderFactory()));
    }

    @Test
    public void testDynamicPage() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/dynamic/testDynamicObject.page.xml")
                .get(new PageContext("testDynamicObject", "/test/route"));
        Table table = (Table) page.getRegions().get("single").get(0).getContent().get(0);
        assertThat(table, instanceOf(Table.class));
        assertThat(table.getComponent().getCells().size(), is(1));
        assertThat(table.getComponent().getCells().get(0).getId(), is("id"));
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
        assertThat(dynamicPage.getWidget().getName(), is("Dummy"));
        assertThat((((Form) dynamicPage.getWidget()).getComponent().getFieldsets().get(0)
                .getRows().get(0).getCols().get(0).getFields().get(0)).getId(), is("id"));
        assertThat(((StandardField) ((Form) dynamicPage.getWidget()).getComponent().getFieldsets().get(0)
                .getRows().get(0).getCols().get(0).getFields().get(0)).getControl().getId(), is("id"));


        dynamicPage = (SimplePage) routeAndGet("/test/route/second/123/update", Page.class);
        assertThat(dynamicPage.getId(), is("test_route_second_123_update"));
        assertThat(dynamicPage.getWidget(), instanceOf(Form.class));
        assertThat(dynamicPage.getWidget().getName(), is("123"));
        assertThat((((Form) dynamicPage.getWidget()).getComponent().getFieldsets().get(0)
                .getRows().get(0).getCols().get(0).getFields().get(0)).getId(), is("id"));
        assertThat(((StandardField) ((Form) dynamicPage.getWidget()).getComponent().getFieldsets().get(0)
                .getRows().get(0).getCols().get(0).getFields().get(0)).getControl().getId(), is("id"));
    }
}
