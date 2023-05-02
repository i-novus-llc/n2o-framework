package net.n2oapp.framework.autotest.condition.button.visibility;

import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.fieldset.SimpleFieldSet;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
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

/**
 * Автотест проверяющий условие видимости у кнопки
 */
public class ButtonVisibilityAT extends AutoTestBase {

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
        builder.packs(
                new N2oApplicationPack(),
                new N2oAllPagesPack(),
                new N2oAllDataPack()
        );
        setJsonPath("net/n2oapp/framework/autotest/condition/button/visibility");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/condition/button/visibility/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/condition/button/visibility/test.query.xml")
        );
    }

    @Test
    public void visibilityDependencyTest() {
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        FormWidget form = page.regions()
                .region(0, SimpleRegion.class)
                .content()
                .widget(1, FormWidget.class);
        form.shouldExists();

        Fields fields = form.fieldsets()
                .fieldset(0, SimpleFieldSet.class)
                .fields();
        InputText inputOne = fields.field("operatorId")
                .control(InputText.class);
        InputText inputTwo = fields.field("assigneeId")
                .control(InputText.class);
        StandardButton button = page.toolbar()
                .topLeft()
                .button("Кнопка");

        button.shouldBeVisible();

        inputOne.clear();
        button.shouldBeHidden();

        inputTwo.clear();
        button.shouldBeVisible();
    }
}
