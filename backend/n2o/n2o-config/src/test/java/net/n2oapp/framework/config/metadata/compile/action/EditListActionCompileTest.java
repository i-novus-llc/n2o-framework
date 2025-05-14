package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.meta.action.editlist.EditListAction;
import net.n2oapp.framework.api.metadata.meta.action.editlist.ListOperationTypeEnum;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oActionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование компиляции действия редактирования записи списка
 */
class EditListActionCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack());
    }

    @Test
    void testEditListAction() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/edit_list/testEditListAction.page.xml")
                .get(new PageContext("testEditListAction"));

        EditListAction action = (EditListAction) page.getToolbar().getButton("b1").getAction();
        assertThat(action.getType(), is("n2o/api/models/edit_list"));
        assertThat(action.getPayload().getOperation(), is(ListOperationTypeEnum.create));
        assertThat(action.getPayload().getPrimaryKey(), is("name"));
        assertThat(action.getPayload().getList().getDatasource(), is("testEditListAction_ds1"));
        assertThat(action.getPayload().getList().getModel(), is(ReduxModelEnum.multi));
        assertThat(action.getPayload().getList().getField(), is("groups[{idx}]"));
        assertThat(action.getPayload().getItem().getDatasource(), is("testEditListAction_ds2"));
        assertThat(action.getPayload().getItem().getModel(), is(ReduxModelEnum.filter));
        assertThat(action.getPayload().getItem().getField(), is("test2"));

        action = (EditListAction) page.getToolbar().getButton("b2").getAction();
        assertThat(action.getType(), is("n2o/api/models/edit_list"));
        assertThat(action.getPayload().getOperation(), is(ListOperationTypeEnum.delete));
        assertThat(action.getPayload().getPrimaryKey(), is("id"));
        assertThat(action.getPayload().getList().getDatasource(), is("testEditListAction_ds2"));
        assertThat(action.getPayload().getList().getModel(), is(ReduxModelEnum.resolve));
        assertThat(action.getPayload().getList().getField(), nullValue());
        assertThat(action.getPayload().getItem().getDatasource(), is("testEditListAction_ds2"));
        assertThat(action.getPayload().getItem().getModel(), is(ReduxModelEnum.resolve));
        assertThat(action.getPayload().getItem().getField(), nullValue());
    }

    @Test
    void testNullItemDatasource() {
        N2oException e = assertThrows(
                N2oException.class,
                () -> compile("net/n2oapp/framework/config/metadata/compile/action/edit_list/testEditListNullItemDatasource.page.xml")
                        .get(new PageContext("testEditListNullItemDatasource"))
        );
        assertThat(e.getMessage(), is("Источник данных 'item-datasource' не определен для действия \"<edit-list>\""));
    }
}