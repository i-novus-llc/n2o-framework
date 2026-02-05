package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.control.TextArea;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тестирование компиляции компонента ввода многострочного текста
 */
class TextAreaCompileTest extends SourceCompileTestBase {
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
    void testTextArea() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/mapping/testTextArea.page.xml")
                .get(new PageContext("testTextArea"));
        Form form = (Form) page.getWidget();
        List<FieldSet.Row> rowList = form.getComponent().getFieldsets().getFirst().getRows();
        TextArea textArea = (TextArea) ((StandardField<?>) rowList.getFirst().getCols().getFirst().getFields().getFirst()).getControl();
        assertThat(textArea.getSrc(), is("TextArea"));
        assertThat(textArea.getMinRows(), is(10));
        assertThat(textArea.getMaxRows(), is(20));
        assertThat(textArea.getPlaceholder(), is("test"));

        textArea = (TextArea) ((StandardField<?>) rowList.get(1).getCols().getFirst().getFields().getFirst()).getControl();
        assertThat(textArea.getSrc(), is("TextArea"));
        assertThat(textArea.getMinRows(), is(3));
        assertThat(textArea.getMaxRows(), is(3));
        assertThat(textArea.getId(), is("test2"));
    }
}