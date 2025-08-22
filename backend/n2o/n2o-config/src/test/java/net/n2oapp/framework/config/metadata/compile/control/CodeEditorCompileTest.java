package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.control.plain.CodeLanguageEnum;
import net.n2oapp.framework.api.metadata.meta.control.CodeEditor;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тестирование компиляции компонента редактирования кода
 */
class CodeEditorCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oAllDataPack());
    }

    @Test
    void testCodeEditor() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/mapping/testCodeEditor.page.xml")
                .get(new PageContext("testCodeEditor"));
        Form form = (Form) page.getWidget();
        CodeEditor codeEditor = (CodeEditor) ((StandardField) form.getComponent().getFieldsets().get(0).getRows()
                .get(0).getCols().get(0).getFields().get(0)).getControl();
        assertThat(codeEditor.getSrc(), is("CodeEditor"));
        assertThat(codeEditor.getName(), is("test"));
        assertThat(codeEditor.getLang(), is(CodeLanguageEnum.HTML));
        assertThat(codeEditor.getAutocomplete(), is(true));
        assertThat(codeEditor.getMinLines(), is(10));
        assertThat(codeEditor.getMaxLines(), is(20));

        CodeEditor codeEditor2 = (CodeEditor) ((StandardField) form.getComponent().getFieldsets().get(0).getRows()
                .get(1).getCols().get(0).getFields().get(0)).getControl();
        assertThat(codeEditor2.getMinLines(), is(5));
    }
}