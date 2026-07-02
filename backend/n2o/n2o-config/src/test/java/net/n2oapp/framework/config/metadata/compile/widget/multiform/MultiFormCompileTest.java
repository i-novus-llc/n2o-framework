package net.n2oapp.framework.config.metadata.compile.widget.multiform;

import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.meta.action.alert.AlertAction;
import net.n2oapp.framework.api.metadata.meta.action.alert.AlertActionPayload;
import net.n2oapp.framework.api.metadata.meta.action.clear.ClearAction;
import net.n2oapp.framework.api.metadata.meta.action.copy.CopyAction;
import net.n2oapp.framework.api.metadata.meta.action.editlist.EditListAction;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.action.set_value.SetValueAction;
import net.n2oapp.framework.api.metadata.meta.action.switchaction.SwitchAction;
import net.n2oapp.framework.api.metadata.meta.action.condition.ConditionAction;
import net.n2oapp.framework.api.metadata.meta.action.validate.ValidateAction;
import net.n2oapp.framework.api.metadata.meta.action.confirm.ConfirmAction;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkActionImpl;
import net.n2oapp.framework.api.metadata.meta.control.ButtonField;
import net.n2oapp.framework.api.metadata.meta.control.ValidationTypeEnum;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.form.FormWidgetComponent;
import net.n2oapp.framework.api.metadata.meta.widget.multiform.MultiForm;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Condition;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

class MultiFormCompileTest extends SourceCompileTestBase {

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
                new N2oRegionsPack(),
                new N2oPagesPack(),
                new N2oFieldSetsPack(),
                new N2oControlsPack(),
                new N2oActionsPack(),
                new N2oWidgetsPack());
    }

    @Test
    void testMultiFormCompile() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testMultiFormCompile.page.xml",
                "net/n2oapp/framework/config/metadata/compile/widgets/testFormValidations.object.xml")
                .get(new PageContext("testMultiFormCompile"));

        MultiForm multiForm = (MultiForm) page.getRegions().get("single").getFirst().getContent().get(1);
        assertThat(page.getDatasources().get("testMultiFormCompile_ds").getValidations(ReduxModelEnum.RESOLVE).size(), is(0));
        assertThat(page.getDatasources().get("testMultiFormCompile_ds").getValidations(ReduxModelEnum.DATASOURCE).size(), is(1));
        List<Validation> validations = page.getDatasources().get("testMultiFormCompile_ds").getValidations(ReduxModelEnum.DATASOURCE).get("testField");
        assertThat(validations.size(), is(2));

        assertThat(multiForm.getId(), is("testMultiFormCompile_w2"));
        assertThat(multiForm.getSrc(), is("MultiFormWidget"));
        assertThat(multiForm.getFetchOnInit(), is(true));
        assertThat(multiForm.getFetchOnVisibility(), is(true));
        assertThat(multiForm.getDatasource(), is("testMultiFormCompile_ds"));

        FormWidgetComponent formComponent = multiForm.getForm();
        assertThat(formComponent.getAutoFocus(), is(true));
        assertThat(formComponent.getPrompt(), is(true));

        List<FieldSet.Row> rowList = formComponent.getFieldsets().getFirst().getRows();
        assertThat(rowList.size(), is(15));
        assertThat(rowList.getFirst().getCols().getFirst().getFields().getFirst().getLabel(), is("Имя"));
        assertThat(rowList.get(1).getCols().getFirst().getFields().getFirst().getLabel(), is("Фамилия"));

        // кнопка с open-page внутри fields: path-param ссылается через [index]
        ButtonField openPageFieldButton = (ButtonField) rowList.get(4).getCols().getFirst().getFields().getFirst();
        LinkActionImpl openPageFieldAction = (LinkActionImpl) openPageFieldButton.getAction();
        assertThat(openPageFieldAction.getPathMapping().get("lastName").getLink(),
                is("models.datasource['testMultiFormCompile_ds'][index]"));

        assertThat(multiForm.getPaging().getNext(), is(true));
        assertThat(multiForm.getPaging().getPrev(), is(true));

        assertThat(multiForm.getToolbar().get("bottomRight"), notNullValue());
        List<AbstractButton> buttons = multiForm.getToolbar().get("bottomRight").getFirst().getButtons();
        assertThat(buttons.size(), is(14));

        AbstractButton openButton = buttons.getFirst();
        assertThat(openButton.getLabel(), is("Открыть"));
        assertThat(openButton.getAction(), notNullValue());

        List<Condition> enabledConditions = openButton.getConditions().get(ValidationTypeEnum.ENABLED);
        assertThat(enabledConditions, notNullValue());
        assertThat(enabledConditions.size(), is(2));

        Condition emptyModelCondition = enabledConditions.getFirst();
        assertThat(emptyModelCondition.getModelLink(), is("models.datasource['testMultiFormCompile_ds']"));
        assertThat(emptyModelCondition.getExpression(), is("!$.isEmptyModel(this)"));

        Condition userCondition = enabledConditions.get(1);
        assertThat(userCondition.getModelLink(), is("models.datasource['testMultiFormCompile_ds']"));
        assertThat(userCondition.getExpression(), is("firstName != null"));

        AbstractButton actionButton = buttons.get(1);
        assertThat(actionButton.getLabel(), is("Кнопка с action"));
        assertThat(actionButton.getIcon(), is("fa fa-plus"));
        assertThat(actionButton.getId(), is("action1_5"));

        AbstractButton secondOpenButton = buttons.get(3);
        List<Condition> conditions = secondOpenButton.getConditions().get(ValidationTypeEnum.ENABLED);
        assertThat(conditions, notNullValue());
        assertThat(conditions.size(), is(1));

        Condition enableCondition = conditions.getFirst();
        assertThat(enableCondition.getModelLink(), is("models.filter['testMultiFormCompile_ds']"));
        assertThat(enableCondition.getExpression(), is("firstName != null"));
        assertThat(secondOpenButton.getAction(), notNullValue());
        LinkActionImpl action = (LinkActionImpl) secondOpenButton.getAction();
        assertThat(action.getUrl(), is("/testMultiFormCompile/open2"));

        // кнопка с confirm внутри fields: model=datasource, field=[index]
        ButtonField confirmFieldButton = (ButtonField) rowList.get(3).getCols().getFirst().getFields().getFirst();
        ConfirmAction confirmFieldAction = (ConfirmAction) confirmFieldButton.getAction();
        assertThat(confirmFieldAction.getPayload().getModel(), is(ReduxModelEnum.DATASOURCE));
        assertThat(confirmFieldAction.getPayload().getField(), is("[index]"));

        // кнопка с confirm в toolbar: model=datasource, field=null
        ConfirmAction confirmToolbarAction = (ConfirmAction) buttons.get(2).getAction();
        assertThat(confirmToolbarAction.getPayload().getModel(), is(ReduxModelEnum.DATASOURCE));
        assertThat(confirmToolbarAction.getPayload().getField(), nullValue());

        // кнопка с alert внутри fields: modelLink содержит [index]
        ButtonField alertFieldButton = (ButtonField) rowList.get(6).getCols().getFirst().getFields().getFirst();
        AlertAction alertFieldAction = (AlertAction) alertFieldButton.getAction();
        assertThat(((AlertActionPayload) alertFieldAction.getPayload()).getAlerts().getFirst().getModelLink(),
                is("models.datasource['testMultiFormCompile_ds'][index]"));

        // кнопка с alert в toolbar: modelLink не содержит [index]
        AlertAction alertToolbarAction = (AlertAction) buttons.get(5).getAction();
        assertThat(((AlertActionPayload) alertToolbarAction.getPayload()).getAlerts().getFirst().getModelLink(),
                is("models.datasource['testMultiFormCompile_ds']"));

        // кнопка с clear внутри fields: field=[index]
        ButtonField clearFieldButton = (ButtonField) rowList.get(7).getCols().getFirst().getFields().getFirst();
        ClearAction clearFieldAction = (ClearAction) clearFieldButton.getAction();
        assertThat(clearFieldAction.getPayload().getField(), is("[index]"));

        // кнопка с clear в toolbar: field=null
        ClearAction clearToolbarAction = (ClearAction) buttons.get(6).getAction();
        assertThat(clearToolbarAction.getPayload().getField(), nullValue());

        // кнопка с copy внутри fields: sourceFieldId и targetFieldId содержат [index].
        ButtonField copyFieldButton = (ButtonField) rowList.get(8).getCols().getFirst().getFields().getFirst();
        CopyAction copyFieldAction = (CopyAction) copyFieldButton.getAction();
        assertThat(copyFieldAction.getPayload().getSource().getField(), is("[index].firstName"));
        assertThat(copyFieldAction.getPayload().getTarget().getField(), is("[index].lastName"));

        // кнопка с copy в toolbar: sourceFieldId и targetFieldId без [index]
        CopyAction copyToolbarAction = (CopyAction) buttons.get(7).getAction();
        assertThat(copyToolbarAction.getPayload().getSource().getField(), is("firstName"));
        assertThat(copyToolbarAction.getPayload().getTarget().getField(), is("lastName"));

        // кнопка с edit-list внутри fields: list.field и item.field содержат [index].
        ButtonField editListFieldButton = (ButtonField) rowList.get(9).getCols().getFirst().getFields().getFirst();
        EditListAction editListFieldAction = (EditListAction) editListFieldButton.getAction();
        assertThat(editListFieldAction.getPayload().getList().getField(), is("[index].groups"));
        assertThat(editListFieldAction.getPayload().getItem().getField(), is("[index].items"));

        // кнопка с edit-list в toolbar: list.field и item.field без [index]
        EditListAction editListToolbarAction = (EditListAction) buttons.get(8).getAction();
        assertThat(editListToolbarAction.getPayload().getList().getField(), is("groups"));
        assertThat(editListToolbarAction.getPayload().getItem().getField(), is("items"));

        // кнопка с invoke внутри fields: field=[index]
        ButtonField invokeFieldButton = (ButtonField) rowList.get(10).getCols().getFirst().getFields().getFirst();
        InvokeAction invokeFieldAction = (InvokeAction) invokeFieldButton.getAction();
        assertThat(invokeFieldAction.getPayload().getField(), is("[index]"));

        // кнопка с invoke в toolbar: field=null
        InvokeAction invokeToolbarAction = (InvokeAction) buttons.get(9).getAction();
        assertThat(invokeToolbarAction.getPayload().getField(), nullValue());

        // кнопка с set-value внутри fields: target.field содержит [index]
        ButtonField setValueFieldButton = (ButtonField) rowList.get(11).getCols().getFirst().getFields().getFirst();
        SetValueAction setValueFieldAction = (SetValueAction) setValueFieldButton.getAction();
        assertThat(setValueFieldAction.getPayload().getTarget().getField(), is("[index].lastName"));

        // кнопка с set-value в toolbar: target.field без [index]
        SetValueAction setValueToolbarAction = (SetValueAction) buttons.get(10).getAction();
        assertThat(setValueToolbarAction.getPayload().getTarget().getField(), is("lastName"));

        // кнопка с switch внутри fields: valueFieldId содержит [index]
        ButtonField switchFieldButton = (ButtonField) rowList.get(12).getCols().getFirst().getFields().getFirst();
        SwitchAction switchFieldAction = (SwitchAction) switchFieldButton.getAction();
        assertThat(switchFieldAction.getPayload().getValueFieldId(), is("[index].status"));

        // кнопка с switch в toolbar: valueFieldId без [index]
        SwitchAction switchToolbarAction = (SwitchAction) buttons.get(11).getAction();
        assertThat(switchToolbarAction.getPayload().getValueFieldId(), is("status"));

        // кнопка с if-else внутри fields: field=[index]
        ButtonField ifElseFieldButton = (ButtonField) rowList.get(13).getCols().getFirst().getFields().getFirst();
        ConditionAction ifElseFieldAction = (ConditionAction) ifElseFieldButton.getAction();
        assertThat(ifElseFieldAction.getPayload().getField(), is("[index]"));

        // кнопка с if-else в toolbar: field=null
        ConditionAction ifElseToolbarAction = (ConditionAction) buttons.get(12).getAction();
        assertThat(ifElseToolbarAction.getPayload().getField(), nullValue());

        // кнопка с validate внутри fields: field=[index]
        ButtonField validateFieldButton = (ButtonField) rowList.get(14).getCols().getFirst().getFields().getFirst();
        ValidateAction validateFieldAction = (ValidateAction) validateFieldButton.getAction();
        assertThat(validateFieldAction.getPayload().getField(), is("[index]"));

        // кнопка с validate в toolbar: field=null
        ValidateAction validateToolbarAction = (ValidateAction) buttons.get(13).getAction();
        assertThat(validateToolbarAction.getPayload().getField(), nullValue());
    }
}
