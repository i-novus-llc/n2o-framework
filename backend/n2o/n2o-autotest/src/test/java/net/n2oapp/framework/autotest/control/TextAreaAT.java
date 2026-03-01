package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.component.control.TextArea;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотесты поля ввода многострочного текста
 */
class TextAreaAT extends AutoTestBase {

    private SimplePage simplePage;

    @BeforeAll
    static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();

        simplePage = open(SimplePage.class);
        simplePage.shouldExists();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oControlsPack(), new N2oControlsV2IOPack());
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/textarea/index.page.xml"));
    }

    @Test
    void textAreaTest() {
        TextArea textArea = simplePage.widget(FormWidget.class).fields().field("TextArea").control(TextArea.class);
        textArea.shouldBeEnabled();
        textArea.shouldHaveValue("");
        textArea.shouldHavePlaceholder("test");
        textArea.setValue("1\n2\n3\n4\n5\n6\n7");
        textArea.shouldHaveValue("1\n2\n3\n4\n5\n6\n7");
        textArea.setValue("1 2 3 4 5 6 7 8 9 0 1 2 3 4 5");
        textArea.shouldHaveValue("1 2 3 4 5 6 7 8 9 0 ");
    }
}