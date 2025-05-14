package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.component.control.CodeEditor;
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
 * Автотест для редактора кода
 */
class CodeEditorAT extends AutoTestBase {

    private static final String BIG_LINE = "biglinebiglinebiglinebiglinebiglinebiglinebiglinebiglinebiglinebiglinebigline" +
            "biglinebiglinebiglinebiglinebiglinebiglinebiglinebiglinebiglinebiglinebiglinebiglinebigline";

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
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
    }

    @Test
    void testCodeEditor() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/code_editor/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        CodeEditor textEditor = page.widget(FormWidget.class).fields().field("CodeEditor")
                .control(CodeEditor.class);
        textEditor.shouldExists();

        textEditor.shouldHaveValue("var str = 'Hello, World!'");
        textEditor.setValue("test1\ntest2\ntest3");
        textEditor.shouldHaveValue("test1\ntest2\ntest3");
        textEditor.setValue(BIG_LINE);
        textEditor.shouldHaveLines(1);
        textEditor.shouldHaveRowsInLine(0, 1);
    }
}
