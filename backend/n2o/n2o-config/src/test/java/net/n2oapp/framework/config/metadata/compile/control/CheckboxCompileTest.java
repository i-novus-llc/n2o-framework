package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.TriggerEnum;
import net.n2oapp.framework.api.metadata.meta.Models;
import net.n2oapp.framework.api.metadata.meta.control.Checkbox;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class CheckboxCompileTest extends SourceCompileTestBase {
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
    void testCheckboxDefault() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/field/testCheckbox.page.xml")
                .get(new PageContext("testCheckbox"));

        Models models = page.getModels();
        assertThat(models.get("resolve['testCheckbox_main'].test").getValue(), is(false));

        Form form = (Form) page.getWidget();
        List<FieldSet.Row> rowList = form.getComponent().getFieldsets().getFirst().getRows();

        StandardField<?> field = (StandardField<?>) rowList.getFirst().getCols().getFirst().getFields().getFirst();
        Checkbox checkbox = (Checkbox) field.getControl();
        assertThat(checkbox.getHelp(), is("testHelp"));
        assertThat(checkbox.getHelpTrigger(), is(TriggerEnum.HOVER));

        field = (StandardField<?>) rowList.get(1).getCols().getFirst().getFields().getFirst();
        checkbox = (Checkbox) field.getControl();
        assertThat(checkbox.getHelp(), is("testHelp2"));
        assertThat(checkbox.getHelpTrigger(), is(TriggerEnum.CLICK));
    }

}