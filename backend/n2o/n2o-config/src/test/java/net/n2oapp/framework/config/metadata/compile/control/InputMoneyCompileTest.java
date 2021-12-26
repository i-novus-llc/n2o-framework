package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.meta.control.InputMoney;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oControlsV2IOPack;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тестирование компиляции компонента ввода денежных единиц
 */
public class InputMoneyCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsV2IOPack());
        builder.compilers(new InputMoneyCompiler());
    }

    @Test
    public void testInputMoney() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/mapping/testInputMoney.page.xml")
                .get(new PageContext("testInputMoney"));
        Form form = (Form) page.getWidget();
        InputMoney inputMoney = (InputMoney) ((StandardField) form.getComponent().getFieldsets().get(0).getRows()
                .get(0).getCols().get(0).getFields().get(0)).getControl();

        assertThat(inputMoney.getSrc(), is("InputMoney"));
        assertThat(inputMoney.getPrefix(), is("*"));
        assertThat(inputMoney.getSuffix(), is("`suffix`"));
        assertThat(inputMoney.getThousandsSeparatorSymbol(), is(" "));
        assertThat(inputMoney.getDecimalSymbol(), is(","));
        assertThat(inputMoney.getRequireDecimal(), is(false));
        assertThat(inputMoney.getAllowDecimal(), is(true));
        assertThat(inputMoney.getIntegerLimit(), is(100));

        inputMoney = (InputMoney) ((StandardField) form.getComponent().getFieldsets().get(0).getRows()
                .get(1).getCols().get(0).getFields().get(0)).getControl();

        assertThat(inputMoney.getPrefix(), is(""));
        assertThat(inputMoney.getSuffix(), is(""));
    }
}

