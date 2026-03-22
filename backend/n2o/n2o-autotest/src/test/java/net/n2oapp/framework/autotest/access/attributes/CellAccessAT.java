package net.n2oapp.framework.autotest.access.attributes;

import net.n2oapp.framework.access.metadata.pack.AccessSchemaPack;
import net.n2oapp.framework.autotest.api.collection.Cells;
import net.n2oapp.framework.autotest.api.collection.TableHeaders;
import net.n2oapp.framework.autotest.api.component.cell.EditCell;
import net.n2oapp.framework.autotest.api.component.cell.ToolbarCell;
import net.n2oapp.framework.autotest.api.component.control.DateInput;
import net.n2oapp.framework.autotest.api.component.control.InputSelect;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.TextArea;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.table.TableSimpleHeader;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Автотест для доступа к ячейкам таблицы по sec атрибутам
 */
class CellAccessAT extends AutoTestBase {
    @BeforeAll
    static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oApplicationPack(), new N2oAllPagesPack(), new N2oAllDataPack(), new AccessSchemaPack());
        CompileInfo.setSourceTypes(builder.getEnvironment().getSourceTypeRegister());
        setResourcePath("net/n2oapp/framework/autotest/access/attributes/cell");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/access/attributes/cell/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/access/attributes/cell/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/access/attributes/cell/default.access.xml"));
    }

    @Test
    void testAdminAccess() {
        setUserInfo(loadAdminInfo());

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Доступ к ячейкам по sec атрибутам");

        TableWidget table = page.widget(TableWidget.class);
        table.shouldExists();
        TableHeaders headers = table.columns().headers();
        headers.shouldHaveSize(6);
        int i = 0;
        TableSimpleHeader header = headers.header(i++);
        header.shouldExists();
        header.shouldHaveTitle("id");
        headers.header(i++).shouldHaveTitle("Ячейка доступна с правом edit (toolbar)");
        headers.header(i++).shouldHaveTitle("Ячейка доступна с правом edit (input-text)");
        headers.header(i++).shouldHaveTitle("Ячейка доступна с правом edit (input-select)");
        headers.header(i++).shouldHaveTitle("Ячейка доступна с правом edit (date-time)");
        headers.header(i).shouldHaveTitle("Ячейка доступна с правом edit (text-area)");

        TableWidget.Rows rows = table.columns().rows();
        rows.shouldHaveSize(2);

        firstRowShouldBeEnabled(rows);
    }

    private static void firstRowShouldBeEnabled(TableWidget.Rows rows) {
        Cells row = rows.row(0);
        int i = 1;
        ToolbarCell toolbarCell = row.cell(i++, ToolbarCell.class);
        toolbarCell.shouldExists();
        toolbarCell.shouldBeEnabled();
        toolbarCell.toolbar().button("Кнопка").shouldExists();

        EditCell editCell = row.cell(i++, EditCell.class);
        editCell.shouldExists();
        editCell.shouldBeEnabled();
        editCell.control(InputText.class).shouldExists();
        editCell.control(InputText.class).shouldHaveValue("test1");

        editCell = row.cell(i++, EditCell.class);
        editCell.shouldExists();
        editCell.shouldBeEnabled();
        editCell.control(InputSelect.class).shouldExists();
        editCell.control(InputSelect.class).shouldHaveValue("Один");

        editCell = row.cell(i++, EditCell.class);
        editCell.shouldExists();
        editCell.shouldBeEnabled();
        editCell.control(DateInput.class).shouldExists();
        editCell.control(DateInput.class).shouldHaveValue("03.01.1990");

        editCell = row.cell(i, EditCell.class);
        editCell.shouldExists();
        editCell.shouldBeEnabled();
        editCell.control(TextArea.class).shouldExists();
        editCell.control(TextArea.class).shouldHaveValue("Текст");
    }

    @Test
    void testAnonymousAccess() {
        setUserInfo(null);

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Доступ к ячейкам по sec атрибутам");

        TableWidget table = page.widget(TableWidget.class);
        table.shouldExists();
        TableHeaders headers = table.columns().headers();
        headers.shouldHaveSize(6);
        TableSimpleHeader header = headers.header(0);
        header.shouldExists();

        TableWidget.Rows rows = table.columns().rows();
        rows.shouldHaveSize(2);
        firstRowShouldBeDisabled(rows);
        secondRowShouldBeDisabled(rows);
    }

    private static void firstRowShouldBeDisabled(TableWidget.Rows rows) {
        Cells row = rows.row(0);
        int i = 1;
        ToolbarCell toolbarCell = row.cell(i++, ToolbarCell.class);
        toolbarCell.shouldExists();
        toolbarCell.shouldBeDisabled();
        toolbarCell.toolbar().button("Кнопка").shouldExists();

        EditCell editCell = row.cell(i++, EditCell.class);
        editCell.shouldExists();
        editCell.shouldBeDisabled();
        editCell.control(InputText.class).shouldExists();
        editCell.control(InputText.class).shouldHaveValue("test1");

        editCell = row.cell(i++, EditCell.class);
        editCell.shouldExists();
        editCell.shouldBeDisabled();
        editCell.control(InputSelect.class).shouldExists();
        editCell.control(InputSelect.class).shouldHaveValue("Один");

        editCell = row.cell(i++, EditCell.class);
        editCell.shouldExists();
        editCell.shouldBeDisabled();
        editCell.control(DateInput.class).shouldExists();
        editCell.control(DateInput.class).shouldHaveValue("03.01.1990");

        editCell = row.cell(i, EditCell.class);
        editCell.shouldExists();
        editCell.shouldBeDisabled();
        editCell.control(TextArea.class).shouldExists();
        editCell.control(TextArea.class).shouldHaveValue("Текст");
    }

    private static void secondRowShouldBeDisabled(TableWidget.Rows rows) {
        Cells row = rows.row(1);
        int i = 1;

        ToolbarCell toolbarCell = row.cell(i++, ToolbarCell.class);
        toolbarCell.shouldExists();
        toolbarCell.shouldBeDisabled();

        EditCell editCell = row.cell(i++, EditCell.class);
        editCell.shouldExists();
        editCell.shouldBeDisabled();
        editCell.control(InputText.class).shouldExists();
        editCell.control(InputText.class).shouldBeEmpty();

        editCell = row.cell(i++, EditCell.class);
        editCell.shouldExists();
        editCell.shouldBeDisabled();
        editCell.control(InputSelect.class).shouldExists();
        editCell.control(InputSelect.class).shouldBeEmpty();

        editCell = row.cell(i++, EditCell.class);
        editCell.shouldExists();
        editCell.shouldBeDisabled();
        editCell.control(DateInput.class).shouldExists();
        editCell.control(DateInput.class).shouldBeEmpty();

        editCell = row.cell(i, EditCell.class);
        editCell.shouldExists();
        editCell.shouldBeDisabled();
        editCell.control(TextArea.class).shouldExists();
        editCell.control(TextArea.class).shouldBeEmpty();
    }

    private Map<String, Object> loadAdminInfo() {
        Map<String, Object> user = new HashMap<>();
        user.put("username", "Admin");
        user.put("roles", Collections.singletonList("admin"));
        user.put("permissions", Collections.singletonList("edit"));
        return user;
    }
}