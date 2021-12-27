package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.exception.SeverityType;
import net.n2oapp.framework.api.metadata.control.plain.N2oInputText;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.control.Field;
import net.n2oapp.framework.api.metadata.meta.control.InputText;
import net.n2oapp.framework.api.metadata.meta.control.IntervalField;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.ActionContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class IntervalFieldCompileTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oActionsPack(), new N2oAllDataPack(), new N2oControlsPack());
    }

    @Test
    public void testIntervalField() {

        PageContext pageContext = new PageContext("testIntervalField");
        pageContext.setSubmitOperationId("update");
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/control/testIntervalField.page.xml",
                "net/n2oapp/framework/config/metadata/compile/stub/utBlank.object.xml")
                .get(pageContext);
        Form form = (Form) page.getWidget();
        Field field = form.getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0);
        IntervalField<InputText> intervalField = (IntervalField<InputText>) field;
        InputText beginControl = intervalField.getBeginControl();
        InputText endControl = intervalField.getEndControl();
        assertThat(beginControl.getId(), is("beginTest"));
        assertThat(beginControl.getMin(), is(0));

        assertThat(endControl.getId(), is("endTest"));
        assertThat(endControl.getMax(), is(10));

        ActionContext actionContext = (ActionContext)route("/testIntervalField/main", CompiledObject.class);
        List<Validation> serverValidations = actionContext.getValidations();
        assertThat(serverValidations.get(0).getSeverity(), is(SeverityType.danger));
        assertThat(serverValidations.get(0).getFieldId(), is("range"));

        assertThat(page.getModels().get("resolve['testIntervalField_main'].range.begin").getValue(), is(5));
        assertThat(page.getModels().get("resolve['testIntervalField_main'].range.end").getValue(), is(7));
    }
}
