package net.n2oapp.framework.config.metadata.compile.object.validation;

import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.data.validation.ValidationDialog;
import net.n2oapp.framework.api.exception.SeverityType;
import net.n2oapp.framework.api.metadata.dataprovider.N2oTestDataProvider;
import net.n2oapp.framework.api.metadata.event.action.N2oCloseAction;
import net.n2oapp.framework.api.metadata.event.action.N2oInvokeAction;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSimpleField;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDialog;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.dataprovider.TestDataProviderIOv1;
import net.n2oapp.framework.config.io.toolbar.ButtonIO;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.pack.N2oActionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oObjectsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Тестирование компиляции валидации с диалогом выбора
 */
public class ValidationDialogCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oObjectsPack(), new N2oActionsPack());
        builder.ios(new TestDataProviderIOv1(), new ButtonIO());
    }

    @Test
    public void testValidationDialog() {
        CompiledObject object = compile("net/n2oapp/framework/config/metadata/compile/object/validation/testValidationDialog.object.xml")
                .get(new ObjectContext("testValidationDialog"));

        List<Validation> validations = object.getValidations();
        assertThat(validations.size(), is(2));

        assertThat(validations.get(0).getId(), is("dialog1"));
        assertThat(validations.get(0).getMoment(), is(N2oValidation.ServerMoment.afterFailOperation));
        assertThat(validations.get(0).getMessage(), is("message"));

        N2oTestDataProvider invocation = (N2oTestDataProvider) ((ValidationDialog) validations.get(0)).getInvocation();
        assertThat(invocation.getOperation(), is(N2oTestDataProvider.Operation.create));
        assertThat(invocation.getFile(), is("test.json"));

        List<AbstractParameter> inParametersList = ((ValidationDialog) validations.get(0)).getInParametersList();
        assertThat(inParametersList.size(), is(3));
        List<ObjectSimpleField> outParametersList = ((ValidationDialog) validations.get(0)).getOutParametersList();
        assertThat(outParametersList.size(), is(2));
        assertThat(outParametersList.get(0).getId(), is("name"));
        assertThat(outParametersList.get(1).getId(), is("validation"));
        assertThat(outParametersList.get(1).getMapping(), is("name=='test'"));

        N2oDialog dialog = ((ValidationDialog) validations.get(0)).getDialog();
        assertThat(dialog.getId(), is("dialog1"));
        assertThat(dialog.getTitle(), is("message"));
        assertThat(dialog.getSize(), is("sm"));
        N2oToolbar toolbar = dialog.getToolbar();
        assertThat(toolbar.getItems().length, is(2));
        assertThat(((N2oButton) toolbar.getItems()[0]).getLabel(), is("Yes"));
        assertThat(((N2oButton) toolbar.getItems()[0]).getAction(), instanceOf(N2oInvokeAction.class));
        assertThat(((N2oButton) toolbar.getItems()[1]).getAction(), instanceOf(N2oCloseAction.class));


        assertThat(validations.get(1).getId(), is("dialog2"));
        assertThat(validations.get(1).getSeverity(), is(SeverityType.danger));
        assertThat(validations.get(1).getMoment(), is(N2oValidation.ServerMoment.afterSuccessOperation));
        outParametersList = ((ValidationDialog) validations.get(1)).getOutParametersList();
        assertThat(outParametersList.size(), is(1));
        assertThat(outParametersList.get(0).getId(), is("validation"));
        assertThat(outParametersList.get(0).getMapping(), nullValue());
    }
}
