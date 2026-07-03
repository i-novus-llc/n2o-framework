package net.n2oapp.framework.config.metadata.compile.widget.multiform;

import net.n2oapp.framework.api.metadata.meta.action.condition.ConditionAction;
import net.n2oapp.framework.api.metadata.meta.action.confirm.ConfirmAction;
import net.n2oapp.framework.api.metadata.meta.action.editlist.EditListAction;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.action.switchaction.SwitchAction;
import net.n2oapp.framework.api.metadata.meta.control.Field;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.multiform.MultiForm;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование подстановки поля [index] для действий и кнопок,
 * скомпилированных внутри полей виджета MultiForm (inner scope)
 */
class MultiFormInnerActionsCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(
                new N2oAllDataPack(),
                new N2oPagesPack(),
                new N2oFieldSetsPack(),
                new N2oControlsPack(),
                new N2oActionsPack(),
                new N2oWidgetsPack());
    }

    @Test
    void testInnerActionsField() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testMultiFormInnerActions.page.xml",
                "net/n2oapp/framework/config/metadata/compile/widgets/testFormValidations.object.xml")
                .get(new PageContext("testMultiFormInnerActions"));

        MultiForm multiForm = (MultiForm) page.getWidget();
        Field field = multiForm.getForm().getFieldsets().getFirst().getRows().getFirst()
                .getCols().getFirst().getFields().getFirst();
        List<AbstractButton> buttons = field.getToolbar()[0].getButtons();

        // у каждой кнопки внутри поля MultiForm field = [index]
        for (AbstractButton button : buttons) {
            assertThat(button.getField(), is("[index]"));
        }

        // invoke
        InvokeAction invokeAction = (InvokeAction) getButton(buttons, "invokeBtn").getAction();
        assertThat(invokeAction.getPayload().getField(), is("[index]"));

        // confirm
        ConfirmAction confirmAction = (ConfirmAction) getButton(buttons, "confirmBtn").getAction();
        assertThat(confirmAction.getPayload().getField(), is("[index]"));

        // if-else (condition)
        ConditionAction conditionAction = (ConditionAction) getButton(buttons, "ifBtn").getAction();
        assertThat(conditionAction.getPayload().getField(), is("[index]"));

        // switch — комбинация с value-field-id
        SwitchAction switchAction = (SwitchAction) getButton(buttons, "switchBtn").getAction();
        assertThat(switchAction.getPayload().getValueFieldId(), is("[index].type"));

        // edit-list — комбинация с field-id для list и item
        EditListAction editListAction = (EditListAction) getButton(buttons, "editBtn").getAction();
        assertThat(editListAction.getPayload().getList().getField(), is("[index].groups"));
        assertThat(editListAction.getPayload().getItem().getField(), is("[index].test2"));
    }

    private AbstractButton getButton(List<AbstractButton> buttons, String id) {
        return buttons.stream().filter(b -> id.equals(b.getId())).findFirst().orElseThrow();
    }
}
