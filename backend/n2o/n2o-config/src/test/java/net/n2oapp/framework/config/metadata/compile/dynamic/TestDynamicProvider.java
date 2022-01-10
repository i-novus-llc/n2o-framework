package net.n2oapp.framework.config.metadata.compile.dynamic;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.dataprovider.N2oSqlDataProvider;
import net.n2oapp.framework.api.metadata.event.action.N2oShowModal;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.view.page.N2oSimplePage;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oTable;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.AbstractColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oSimpleColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ToolbarItem;
import net.n2oapp.framework.api.register.DynamicMetadataProvider;

import java.util.Arrays;
import java.util.List;

public class TestDynamicProvider implements DynamicMetadataProvider {

    public static final String TEST_DYNAMIC = "testDynamic";

    @Override
    public String getCode() {
        return TEST_DYNAMIC;
    }

    @Override
    public List<? extends SourceMetadata> read(String context) {
        N2oSimplePage page = new N2oSimplePage();
        N2oForm form = new N2oForm();
        form.setName(context);
        form.setRefId("formForTestDynamic");
        form.setQueryId("testDynamic?Dummy");
        form.adapterV4();
        page.setWidget(form);


        N2oObject n2oObject = new N2oObject();
        n2oObject.setName(TEST_DYNAMIC + "Object");

        N2oQuery query = new N2oQuery();
        query.setId(TEST_DYNAMIC + "?" +context);
        N2oQuery.Selection selection = new N2oQuery.Selection(N2oQuery.Selection.Type.list);
        N2oSqlDataProvider invocation = new N2oSqlDataProvider();
        invocation.setQuery("test select");

        selection.setInvocation(invocation);
        query.setLists(new N2oQuery.Selection[]{selection});
        query.setFields(new N2oQuery.Field[]{new N2oQuery.Field("id")});
        query.setObjectId("testDynamic?Dummy");

        N2oTable table = new N2oTable();
        table.setQueryId("testDynamic?Dummy");
        N2oSimpleColumn idColumn = new N2oSimpleColumn();
        idColumn.setId("id");
        idColumn.setTextFieldId("id");
        table.setColumns(new AbstractColumn[]{idColumn});
        table.setId("testDynamic?Dummy");
        N2oToolbar toolbar = new N2oToolbar();
        toolbar.setPlace("topLeft");

        N2oButton create = new N2oButton();
        create.setId("create");
        N2oShowModal showModal = new N2oShowModal();
        showModal.setPageId("testDynamic?Dummy");
        create.setModel(ReduxModel.filter);
        create.setAction(showModal);

        N2oButton update = new N2oButton();
        update.setId("update");
        N2oShowModal updShowModal = new N2oShowModal();
        updShowModal.setPageId("testDynamic?Dummy");
        update.setModel(ReduxModel.resolve);
        update.setAction(updShowModal);



        toolbar.setItems(new ToolbarItem[]{create, update});
        table.setToolbars(new N2oToolbar[]{toolbar});
        table.adapterV4();

        return Arrays.asList(n2oObject, query, table, page);
    }


}
