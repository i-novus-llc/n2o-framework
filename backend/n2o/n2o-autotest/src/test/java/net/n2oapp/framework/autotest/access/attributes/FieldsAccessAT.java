package net.n2oapp.framework.autotest.access.attributes;

import net.n2oapp.framework.access.metadata.pack.AccessSchemaPack;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
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

class FieldsAccessAT extends AutoTestBase {

    @BeforeAll
    static void beforeClass() {
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
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/access/attributes/fields/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/access/attributes/fields/default.access.xml"));
    }

    @Test
    void testAdminAccess() {
        Map<String, Object> user = new HashMap<>();
        user.put("username", "Admin");
        user.put("roles", Collections.singleton("admin"));
        user.put("permissions", Collections.singleton("edit"));
        setUserInfo(user);

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.header().shouldHaveBrandName("N2O");
        page.breadcrumb().crumb(0).shouldHaveLabel("Доступ к полям по sec атрибутам");

        Fields fields = page.widget(FormWidget.class).fields();
        fields.shouldHaveSize(3);
        fields.field("Доступно всем").shouldExists();
        fields.field("Только с ролью admin").shouldExists();
        fields.field("Только с правом edit").shouldExists();
        fields.field("Только анонимам").shouldNotExists();
    }

    @Test
    void testAnonymousAccess() {
        setUserInfo(null);

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.header().shouldHaveBrandName("N2O");
        page.breadcrumb().crumb(0).shouldHaveLabel("Доступ к полям по sec атрибутам");

        Fields fields = page.widget(FormWidget.class).fields();
        fields.shouldHaveSize(2);
        fields.field("Доступно всем").shouldExists();
        fields.field("Только с ролью admin").shouldNotExists();
        fields.field("Только с правом edit").shouldNotExists();
        fields.field("Только анонимам").shouldExists();
    }

}
