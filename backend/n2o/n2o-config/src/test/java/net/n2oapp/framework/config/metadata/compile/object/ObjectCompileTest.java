package net.n2oapp.framework.config.metadata.compile.object;

import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.pack.N2oInvocationV2ReadersPack;
import net.n2oapp.framework.config.metadata.pack.N2oObjectsPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

public class ObjectCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oInvocationV2ReadersPack(), new N2oObjectsPack())
                .sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/object/utAction.object.xml"));
    }

    @Test
    public void testCompileActions() throws Exception {
        CompiledObject object = compile("net/n2oapp/framework/config/metadata/compile/object/utAction.object.xml")
                .get(new ObjectContext("utAction"));
        assert object.getOperations().size() == 2;
        assert "create".equals(object.getOperations().get("create").getId());
        assert "delete".equals(object.getOperations().get("delete").getId());
    }

    @Test
    public void testCompileValidations() throws Exception {
        CompiledObject object = compile("net/n2oapp/framework/config/metadata/compile/object/utValidation.object.xml")
                .get(new ObjectContext("utValidation"));
        assert 2 == object.getValidations().size();
        assert "v1".equals(object.getValidationsMap().get("v1").getId());
        assert "v2".equals(object.getValidationsMap().get("v2").getId());
        assert 2 == object.getOperations().get("all").getValidationList().size();
        assert 0 == object.getOperations().get("nothing").getValidationList().size();
        assert "v1".equals(object.getOperations().get("white").getValidationList().get(0).getId());
        assert "v2".equals(object.getOperations().get("black").getValidationList().get(0).getId());
    }

    @Test
    public void testCompileFields() throws Exception {
        CompiledObject object = compile("net/n2oapp/framework/config/metadata/compile/object/utObjectField.object.xml")
                .get(new ObjectContext("utObjectField"));
        assert 2 == object.getObjectFields().size();
        assert "f1".equals(object.getObjectFieldsMap().get("f1").getId());
        assert "f2".equals(object.getObjectFieldsMap().get("f2").getId());
        assert object.getOperations().get("create").getInParametersMap().get("f1").getDomain().equals("integer");
    }
}
