package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.meta.control.InputMoney;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class InputMoneyCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oActionsPack(), new N2oAllDataPack(), new N2oControlsV2IOPack());
        builder.compilers(new InputMoneyCompiler());
    }

    @Test
    public void testInputMoney() {
        Form form = (Form) compile("net/n2oapp/framework/config/mapping/testInputMoney.widget.xml")
                .get(new WidgetContext("testInputMoney"));
        InputMoney inputMoney = (InputMoney) ((StandardField) form.getComponent().getFieldsets().get(0).getRows()
                .get(0).getCols().get(0).getFields().get(0)).getControl();

        assertThat(inputMoney.getSrc(), is("InputMoney"));
        assertThat(inputMoney.getPrefix(), is("*"));
        assertThat(inputMoney.getSuffix(), is("^"));
        assertThat(inputMoney.getThousandsSeparatorSymbol(), is(" "));
        assertThat(inputMoney.getDecimalSymbol(), is(","));
        assertThat(inputMoney.getRequireDecimal(), is(false));
        assertThat(inputMoney.getAllowDecimal(), is(true));
        assertThat(inputMoney.getIntegerLimit(), is(100));
    }
}

