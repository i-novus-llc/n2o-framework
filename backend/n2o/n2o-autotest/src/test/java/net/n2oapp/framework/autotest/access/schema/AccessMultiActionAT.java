package net.n2oapp.framework.autotest.access.schema;

import net.n2oapp.framework.access.metadata.pack.AccessSchemaPack;
import net.n2oapp.framework.autotest.api.component.button.DropdownButton;
import net.n2oapp.framework.autotest.api.component.cell.ToolbarCell;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AccessMultiActionAT extends AutoTestBase {

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
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack(), new AccessSchemaPack());
        CompileInfo.setSourceTypes(builder.getEnvironment().getSourceTypeRegister());
        setJsonPath("net/n2oapp/framework/autotest/access/schema/action/multi");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/access/schema/action/multi/default.access.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/access/schema/action/multi/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/access/schema/action/multi/test1.object.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/access/schema/action/multi/test2.object.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/access/schema/action/multi/test3.object.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/access/schema/action/multi/test4.object.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/access/schema/action/multi/test.query.xml"));
    }

    @Test
    void testHaveNotEnoughAccess() {
        Map<String, Object> user = new HashMap<>();
        user.put("username", "Admin");
        user.put("permissions", Arrays.asList("update1"));
        setUserInfo(user);

        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class);
        table.shouldExists();
        table.toolbar().topLeft().shouldBeEmpty();
        table.columns().rows().row(0).cell(1).shouldBeEmpty();
    }

    @Test
    void testPartOfAccess() {
        Map<String, Object> user = new HashMap<>();
        user.put("username", "Admin");
        user.put("permissions", Arrays.asList("update1", "update2"));
        setUserInfo(user);

        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class);
        table.shouldExists();
        table.toolbar().topLeft().button("Удалить").shouldExists();
        DropdownButton subMenu = table.columns().rows().row(0).cell(1, ToolbarCell.class)
                .toolbar().dropdown();
        subMenu.click();
        subMenu.shouldHaveItems(1);
        subMenu.menuItem(0).shouldExists();
        subMenu.menuItem(0).shouldHaveLabel("update1 И update2");
    }

    @Test
    void testFullAccess() {
        Map<String, Object> user = new HashMap<>();
        user.put("username", "Admin");
        user.put("permissions", Arrays.asList("update1", "update2", "update3", "update4"));
        setUserInfo(user);

        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class);
        table.shouldExists();
        table.toolbar().topLeft().button("Удалить").shouldExists();
        DropdownButton subMenu = table.columns().rows().row(0).cell(1, ToolbarCell.class)
                .toolbar().dropdown();
        subMenu.click();
        subMenu.shouldHaveItems(2);
        subMenu.menuItem(0).shouldExists();
        subMenu.menuItem(0).shouldHaveLabel("update1 И update2");
        subMenu.menuItem(1).shouldExists();
        subMenu.menuItem(1).shouldHaveLabel("update3 И update4");
    }

}
