package net.n2oapp.framework.autotest.condition;

import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.RadioGroup;
import net.n2oapp.framework.autotest.api.component.field.Field;
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
 * Автотест dependencies-visibility
 */
public class VisibilityAT extends AutoTestBase {

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
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/condition/visibility/index.page.xml"));
    }

    @Test
    public void testVisibility() {
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("VisibilityTestPage");

        Fields fields1 = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class).fields();
        fields1.shouldHaveSize(4);
        RadioGroup fs1 = fields1.field("Field Selector 1").control(RadioGroup.class);
        fs1.shouldBeChecked("Show left field");

        fields1.field("Field L11").control(InputText.class).shouldExists();
        fields1.field("Field L21").control(InputText.class).shouldExists();
        fields1.field("Field R11").control(InputText.class).shouldNotExists();
        fields1.field("Field R21").control(InputText.class).shouldExists();

        Fields fields2 = page.regions().region(1, SimpleRegion.class).content().widget(FormWidget.class).fields();
        fields2.shouldHaveSize(3);
        RadioGroup fs2 = fields2.field("Field Selector 2").control(RadioGroup.class);
        fs2.shouldBeEmpty();

        fields2.field("Field L12").control(InputText.class).shouldNotExists();
        fields2.field("Field L22").control(InputText.class).shouldExists();
        fields2.field("Field R12").control(InputText.class).shouldNotExists();
        fields2.field("Field R22").control(InputText.class).shouldExists();

        fs1.check("Show right field");
        fs1.shouldBeChecked("Show right field");
        fields1.field("Field L11").control(InputText.class).shouldNotExists();
        fields1.field("Field L21").control(InputText.class).shouldNotExists();
        fields1.field("Field R11").control(InputText.class).shouldExists();
        fields1.field("Field R21").control(InputText.class).shouldExists();

        fs1.check("Show left field");
        fs1.shouldBeChecked("Show left field");
        fields1.field("Field L11").control(InputText.class).shouldExists();
        fields1.field("Field L21").control(InputText.class).shouldExists();
        fields1.field("Field R11").control(InputText.class).shouldNotExists();
        fields1.field("Field R21").control(InputText.class).shouldNotExists();

        fs2.check("Show left field");
        fs2.shouldBeChecked("Show left field");
        fields2.field("Field L12").control(InputText.class).shouldExists();
        fields2.field("Field L22").control(InputText.class).shouldExists();
        fields2.field("Field R12").control(InputText.class).shouldNotExists();
        fields2.field("Field R22").control(InputText.class).shouldNotExists();

        fs2.check("Show right field");
        fs2.shouldBeChecked("Show right field");
        fields2.field("Field L12").control(InputText.class).shouldNotExists();
        fields2.field("Field L22").control(InputText.class).shouldNotExists();
        fields2.field("Field R12").control(InputText.class).shouldExists();
        fields2.field("Field R22").control(InputText.class).shouldExists();
    }

    @Test
    public void testDependenciesAndVisibility() {
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("VisibilityTestPage");

        Fields fields = page.regions().region(2, SimpleRegion.class).content().widget(FormWidget.class).fields();
        fields.shouldHaveSize(2);

        InputText firstInput = fields.field("FirstInput").control(InputText.class);
        firstInput.shouldExists();
        firstInput.shouldBeEnabled();
        firstInput.val("2");
        firstInput.shouldHaveValue("2");
        InputText secondInput = fields.field("SecondInput").control(InputText.class);
        secondInput.shouldExists();
        secondInput.shouldBeEnabled();
        secondInput.val("2");
        secondInput.shouldHaveValue("2");

        InputText type1Input = fields.field("Type1").control(InputText.class);
        type1Input.shouldExists();
        InputText type2Input = fields.field("Type2").control(InputText.class);
        type2Input.shouldExists();

        //при смене значения на secondInput на 1, в соответствии с условием visibility type2Input должен быть скрыт
        secondInput.val("1");
        secondInput.shouldHaveValue("1");
        firstInput.shouldHaveValue("1");
        type1Input.shouldExists();
        type2Input.shouldNotExists();
    }

    @Test
    public void testVisibilityAndDependencyVisibility() {
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("VisibilityTestPage");

        Fields fields = page.regions().region(3, SimpleRegion.class).content().widget(FormWidget.class).fields();
        Field field = fields.field("Should be visible");
        field.shouldExists();
        field.shouldBeVisible();

        field = fields.field("Should be hidden");
        field.shouldNotExists();
        field.shouldBeHidden();
    }
}
