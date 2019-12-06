package net.n2oapp.framework.config.metadata.compile.object;

import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.pack.N2oObjectsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

public class ActionValidationCompileTest extends SourceCompileTestBase {
    private CompiledObject object;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oObjectsPack());
        object = compile("net/n2oapp/framework/config/metadata/compile/action_validation_compile/utActionValidation.object.xml")
                .get(new ObjectContext("utActionValidation"));
    }

    @Test
    public void testAll() throws Exception {
        CompiledObject.Operation operation = object.getOperations().get("action_all");
        assert operation.isValidationEnable();
        assert operation.getValidationsMap().size() == 2;
        assert operation.getValidationsMap().containsKey("v1");
        assert operation.getValidationsMap().containsKey("v2");
    }

    @Test
    public void testWhiteList() throws Exception {
        CompiledObject.Operation operation = object.getOperations().get("action_whiteList_v1");
        assert operation.isValidationEnable();
        assert operation.getValidationsMap().size() == 1;
        assert operation.getValidationsMap().containsKey("v1");
    }


    @Test
    public void testBlackList() throws Exception {
        CompiledObject.Operation action = object.getOperations().get("action_blackList_v2");
        assert action.isValidationEnable();
        assert action.getValidationsMap().size() == 1;
        assert action.getValidationsMap().containsKey("v1");
    }


    @Test
    public void testNothing() throws Exception {
        CompiledObject.Operation operation = object.getOperations().get("action_nothing");
        assert !operation.isValidationEnable();
        assert operation.getValidationsMap().size() == 0;
    }

    @Test
    public void testDefault() throws Exception {
        CompiledObject.Operation operation = object.getOperations().get("action_default");
        assert operation.isValidationEnable();
        assert operation.getValidationsMap().size() == 2;
        assert operation.getValidationsMap().containsKey("v1");
        assert operation.getValidationsMap().containsKey("v2");
    }

}
