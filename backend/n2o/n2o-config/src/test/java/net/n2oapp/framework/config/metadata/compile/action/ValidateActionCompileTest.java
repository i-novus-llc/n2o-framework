package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.action.ValidateBreakOnEnum;
import net.n2oapp.framework.api.metadata.meta.action.multi.MultiAction;
import net.n2oapp.framework.api.metadata.meta.action.validate.ValidateAction;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

/**
 * Проверка компиляции validate действия
 */
class ValidateActionCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(
                new N2oAllPagesPack(),
                new N2oAllDataPack()
        );
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.object.xml"));
    }

    @Test
    void simple() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/action/testValidateAction.page.xml")
                .get(new PageContext("testValidateAction", "/w"));
        Form form = (Form) page.getWidget();
        //filter model
        MultiAction testAction = (MultiAction) form.getToolbar().get("topLeft").getFirst().getButtons().getFirst().getAction();
        ValidateAction validateAction = (ValidateAction) testAction.getPayload().getActions().getFirst();
        assertThat(validateAction.getType(), is("n2o/api/datasource/validate"));
        assertThat(validateAction.getPayload().getId(), is("w_w1"));
        assertThat(validateAction.getPayload().getModel(), is(ReduxModelEnum.RESOLVE));
        assertThat(validateAction.getPayload().getBreakOn(), is(ValidateBreakOnEnum.DANGER));

        assertThat(validateAction.getPayload().getFields(), contains("country"));
    }
}
