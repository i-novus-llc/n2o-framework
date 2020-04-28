package net.n2oapp.framework.autotest.action;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.OutputText;
import net.n2oapp.framework.autotest.api.component.control.Select;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oHeaderPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SetValueAT extends AutoTestBase {

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
        builder.packs(new N2oAllPagesPack(), new N2oHeaderPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/set_value/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));
    }

    @Test
    public void testSetValue() {
        SimplePage page = open(SimplePage.class);
        page.breadcrumb().titleShouldHaveText("set-value-tests");
        page.shouldExists();

        Fields fields = page.single().widget(FormWidget.class).fields();
        fields.field("calcResult").control(OutputText.class).shouldBeEmpty();
        fields.field("calc 55+66+77").control(StandardButton.class).click();
        fields.field("calcResult").control(OutputText.class).shouldHaveValue("198");

        fields.field("clockResult").control(OutputText.class).shouldBeEmpty();
        fields.field("getTime").control(StandardButton.class).click();
        fields.field("clockResult").control(OutputText.class).element().shouldBe(Condition.matchText("^([0-1]\\d|2[0-3])(:[0-5]\\d){2}$"));

        fields.field("url").control(OutputText.class).shouldBeEmpty();
        fields.field("social").control(Select.class).select(0);
        fields.field("copyUrl").control(StandardButton.class).click();
        fields.field("siteUrl").control(OutputText.class).shouldHaveValue("https://fb.com");
        fields.field("social").control(Select.class).select(2);
        fields.field("copyUrl").control(StandardButton.class).click();
        fields.field("siteUrl").control(OutputText.class).shouldHaveValue("https://youtube.com");
    }
}
