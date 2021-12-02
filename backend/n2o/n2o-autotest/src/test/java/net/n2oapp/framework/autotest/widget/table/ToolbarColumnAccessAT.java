package net.n2oapp.framework.autotest.widget.table;

import net.n2oapp.framework.access.metadata.compile.SecurityExtensionAttributeMapper;
import net.n2oapp.framework.autotest.api.collection.TableHeaders;
import net.n2oapp.framework.autotest.api.component.cell.TextCell;
import net.n2oapp.framework.autotest.api.component.cell.ToolbarCell;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
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
 * Автотест для доступа к заголовкам и тулбарам в ячейках таблицы по sec атрибутам
 */
public class ToolbarColumnAccessAT extends AutoTestBase {

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oApplicationPack(), new N2oAllPagesPack(), new N2oAllDataPack());
        builder.extensions(new SecurityExtensionAttributeMapper());;
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/toolbar/access/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/toolbar/access/test.query.xml"));
    }

    @Test
    public void testAdminAccess() {
        setUserInfo(loadAdminInfo());

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Доступ к колонкам по sec атрибутам");

        TableWidget table = page.widget(TableWidget.class);
        table.shouldExists();
        TableHeaders headers = table.columns().headers();
        headers.shouldHaveSize(3);

        headers.header(0).shouldHaveTitle("Имя");
        headers.header(1).shouldHaveTitle("Доступно с ролью admin");
        headers.header(2).shouldHaveTitle("Доступно с правом edit");

        TableWidget.Rows rows = table.columns().rows();
        rows.shouldHaveSize(1);
        rows.row(0).cell(0, TextCell.class).textShouldHave("test1");

        ToolbarCell toolbarCell = rows.row(0).cell(1, ToolbarCell.class);
        toolbarCell.toolbar().shouldHaveSize(1);
        toolbarCell.toolbar().button("Удалить").shouldHaveIcon("fa-trash");

        toolbarCell = rows.row(0).cell(2, ToolbarCell.class);
        toolbarCell.toolbar().shouldHaveSize(1);
        toolbarCell.toolbar().button("Редактировать").shouldHaveIcon("fa-edit");
    }

    @Test
    public void testAnonymousAccess() {
        setUserInfo(null);

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Доступ к колонкам по sec атрибутам");

        TableWidget table = page.widget(TableWidget.class);
        table.shouldExists();
        TableHeaders headers = table.columns().headers();
        headers.shouldHaveSize(3);

        headers.header(0).shouldHaveTitle("Имя");
        headers.header(1).shouldNotHaveTitle();
        headers.header(2).shouldNotHaveTitle();

        TableWidget.Rows rows = table.columns().rows();
        rows.shouldHaveSize(1);
        rows.row(0).cell(0, TextCell.class).textShouldHave("test1");

        rows.row(0).cell(1, ToolbarCell.class).toolbar().shouldBeEmpty();
        rows.row(0).cell(2, ToolbarCell.class).toolbar().shouldBeEmpty();
    }

    private Map<String, Object> loadAdminInfo() {
        Map<String, Object> user = new HashMap<>();
        user.put("username", "Admin");
        user.put("roles", Collections.singletonList("admin"));
        user.put("permissions", Collections.singletonList("edit"));
        return user;
    }
}
