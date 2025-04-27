package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oTable;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oAbstractColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oSimpleColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oToolbarCell;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ToolbarItem;
import net.n2oapp.framework.api.register.DynamicMetadataProvider;

import java.util.List;

public class TestToolbarDynamicProvider implements DynamicMetadataProvider {

    public static final String TEST_DYNAMIC = "testDynamic";

    @Override
    public String getCode() {
        return TEST_DYNAMIC;
    }

    @Override
    public List<? extends SourceMetadata> read(String context) {
        N2oTable table = new N2oTable();
        N2oSimpleColumn idColumn = new N2oSimpleColumn();
        idColumn.setId("id");
        idColumn.setTextFieldId("id");
        table.setColumns(new N2oAbstractColumn[]{idColumn});
        table.setId("testDynamic?Dummy");

        N2oToolbarCell toolbar = new N2oToolbarCell();
        N2oButton deleteButton = new N2oButton();
        N2oButton addButton = new N2oButton();
        deleteButton.setIcon("fa fa-pencil");
        deleteButton.setColor("danger");
        addButton.setIcon("fa fa-pencil");
        addButton.setColor("primary");
        toolbar.setItems(new ToolbarItem[]{deleteButton, addButton});
        N2oSimpleColumn n2oSimpleColumn = new N2oSimpleColumn();
        n2oSimpleColumn.setCell(toolbar);
        table.setColumns(new N2oSimpleColumn[]{n2oSimpleColumn});
        return List.of(table);
    }

}
