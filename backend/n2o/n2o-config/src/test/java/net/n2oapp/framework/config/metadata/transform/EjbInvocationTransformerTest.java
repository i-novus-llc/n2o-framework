package net.n2oapp.framework.config.metadata.transform;

import net.n2oapp.framework.api.data.validation.ConstraintValidation;
import net.n2oapp.framework.api.metadata.global.dao.invocation.java.EjbInvocation;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oConstraint;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oObjectsPack;
import net.n2oapp.framework.config.metadata.transformer.EjbInvocationTransformer;
import net.n2oapp.framework.config.test.SourceTransformTestBase;
import org.junit.Before;
import org.junit.Test;

public class EjbInvocationTransformerTest extends SourceTransformTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oObjectsPack());
        builder.transformers(new EjbInvocationTransformer());
    }

    /**
     * Проверка копирования параметров из объекта в действия
     */
    @Test
    public void testCopyInActions() throws Exception {
        N2oObject object = transform("net/n2oapp/framework/config/metadata/compile/invocation/testCopyInActions.object.xml")
                .get("testCopyInActions", N2oObject.class);
        assert "testId".equals(object.getOperations()[0].getId());
        String appName = object.getAppName();
        String moduleName = object.getModuleName();
        EjbInvocation invocation = (EjbInvocation) object.getOperations()[0].getInvocation();
        assert appName.equals(invocation.getApplication());
        assert moduleName.equals(invocation.getModule());
    }

    /**
     * Проверка копирования параметров из объекта в валидации
     */
    @Test
    public void testCopyInValidations() throws Exception {
        N2oObject object = transform("net/n2oapp/framework/config/metadata/compile/invocation/testCopyInValidations.object.xml")
                .get("testCopyInValidations", N2oObject.class);
        String appName = object.getAppName();
        String moduleName = object.getModuleName();
        N2oConstraint validation = (N2oConstraint) object.getN2oValidations()[1];
        EjbInvocation invocation = (EjbInvocation) validation.getN2oInvocation();
        assert appName.equals(invocation.getApplication());
        assert moduleName.equals(invocation.getModule());
    }

    /**
     * Проверка того, что параметры не переписались, если они уже были в действиях и валидациях
     */
    @Test
    public void testReplaceProperties() throws Exception {
        N2oObject object = transform("net/n2oapp/framework/config/metadata/compile/invocation/testReplaceProperties.object.xml")
                .get("testReplaceProperties", N2oObject.class);

        EjbInvocation actionInvocation = (EjbInvocation) object.getOperations()[0].getInvocation();
        assert !"appInObject".equals(actionInvocation.getApplication());

        N2oConstraint validation = (N2oConstraint) object.getN2oValidations()[0];
        EjbInvocation validationaInvocation = (EjbInvocation) validation.getN2oInvocation();
        assert !"moduleInObject".equals(validationaInvocation.getModule());
    }
}
