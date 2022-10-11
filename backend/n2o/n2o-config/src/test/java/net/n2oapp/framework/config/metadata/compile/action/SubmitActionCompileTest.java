package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oActionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;

/**
 * Тестирование компиляции действия сохранения источника данных
 */
public class SubmitActionCompileTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack());
    }

//    @Test FIXME
//    public void testSubmit() {
//        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/testSubmitAction.page.xml")
//                .get(new PageContext("testSubmitAction"));
//
//        List<AbstractButton> buttons = ((Form) page.getRegions().get("single").get(0).getContent().get(0))
//                .getToolbar().get("topLeft").get(0).getButtons();
//
//        assertThat(buttons.size(), is(2));
//        SubmitAction submitAction = (SubmitAction) buttons.get(0).getAction();
//        assertThat(submitAction.getPayload().getDatasource(), is("testSubmitAction_ds1"));
//        assertThat(submitAction.getType(), is("n2o/actionImpl/SUBMIT"));
//
//        submitAction = (SubmitAction) buttons.get(1).getAction();
//        assertThat(submitAction.getPayload().getDatasource(), is("testSubmitAction_ds2"));
//    }
}
