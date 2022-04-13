package net.n2oapp.framework.sandbox.autotest.cases;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.ElementsCollection;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.fieldset.SimpleFieldSet;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestApplication;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Nullable;
import java.util.stream.IntStream;

@SpringBootTest(properties = "server.servlet.context-path=/",
        classes = AutoTestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FieldsWidthAndMarginAT extends AutoTestBase {
    private static final int WIDTH = 162; // эталонная ширина полей под заданный размер окна
    private static final int PRECISION_SHIFT = 10; // допустимая погрешность ширины полей
    private static final int MARGIN_BETWEEN_FIELDS = 62; // отступ между полями

    private SimplePage page;

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
        Configuration.browserSize = "1366x768";
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        page = open(SimplePage.class);
        page.shouldExists();
        page.header().brandNameShouldBe("N2O");
        page.breadcrumb().titleShouldHaveText("Поля");
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/default/default.application.xml"),
                new CompileInfo("autotest/cases/fields_autosize/index.page.xml"));
    }

    @Test
    public void testFieldAutoResize() {
        Fields fields = page.widget(FormWidget.class).fieldsets().fieldset(0, SimpleFieldSet.class).fields();
        fields.shouldHaveSize(14);
        fields.elements().forEach(element -> element.shouldBe(new FieldWidthCondition()));
    }

    @Test
    public void testMarginBetweenFields() {
        Fields fields = page.widget(FormWidget.class).fieldsets().fieldset(1, SimpleFieldSet.class).fields();
        fields.shouldHaveSize(5);
        ElementsCollection elements = fields.elements();
        IntStream.range(0, elements.size() - 1)
                .forEach(i -> elements.get(i).shouldBe(new MarginBetweenFieldsCondition(elements.get(i + 1))));
    }


    static class FieldWidthCondition extends Condition {
        public FieldWidthCondition() {
            super("isFieldWidthSame");
        }

        @Override
        public boolean apply(Driver driver, WebElement webElement) {
            return Math.abs(webElement.getRect().getWidth() - WIDTH) < PRECISION_SHIFT;
        }

        @Nullable
        @Override
        public String actualValue(Driver driver, WebElement element) {
            return element.getRect().getWidth() + "px";
        }

        @Override
        public String toString() {
            return String.format("with width in (%s - %s)px",
                    WIDTH - PRECISION_SHIFT, WIDTH + PRECISION_SHIFT);
        }
    }

    static class MarginBetweenFieldsCondition extends Condition {
        private final WebElement nextField;

        public MarginBetweenFieldsCondition(WebElement nextField) {
            super("isMarginBetweenFieldsSame");
            this.nextField = nextField;
        }

        @Override
        public boolean apply(Driver driver, WebElement element) {
            return nextField.getRect().getY() - element.getRect().getY() == MARGIN_BETWEEN_FIELDS;
        }

        @Nullable
        @Override
        public String actualValue(Driver driver, WebElement element) {
            return nextField.getRect().getY() - element.getRect().getY() + "px";
        }

        @Override
        public String toString() {
            return String.format("with margin: %spx", MARGIN_BETWEEN_FIELDS);
        }
    }
}
