package net.n2oapp.framework.config.metadata.transform;

import net.n2oapp.framework.api.metadata.global.dao.invocation.java.JavaInvocation;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.Argument;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oObjectsPack;
import net.n2oapp.framework.config.metadata.transformer.JavaInvocationTransformer;
import net.n2oapp.framework.config.test.SourceTransformTestBase;
import org.junit.Before;
import org.junit.Test;

public class JavaInvocationTransformerTest extends SourceTransformTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oObjectsPack());
        builder.transformers(new JavaInvocationTransformer());
    }

    /**
     * Проверка проставления класса метода в соответствии с service-class из объекта
     */
    @Test
    public void testDefineArgumentClassByEntity() throws Exception {
        N2oObject object = transform("net/n2oapp/framework/config/metadata/transformer/defineArgumentClassByEntity.object.xml")
                .get("defineArgumentClassByEntity", N2oObject.class);
        JavaInvocation invocation = (JavaInvocation) object.getOperations()[0].getInvocation();
        assert "net.ServiceClassFromObject".equals(invocation.getClassName());
    }

    /**
     * Проверка проставления класса аргумента в соответствии с entity-class
     */
    @Test
    public void testdefineMethodClassByObject() throws Exception {
        N2oObject object = transform("net/n2oapp/framework/config/metadata/transformer/defineArgumentClassByEntity.object.xml")
                .get("defineArgumentClassByEntity", N2oObject.class);

        String entityClass = object.getEntityClass();
        JavaInvocation invocation = (JavaInvocation) object.getOperations()[0].getInvocation();
        assert entityClass.equals(invocation.getArguments()[0].getClassName());
    }

    /**
     * Проверка проставления типа аргумента по дефолту
     */
    @Test
    public void testSetDefaultArgumentType() throws Exception {
        N2oObject object = transform("net/n2oapp/framework/config/metadata/transformer/setDefaultArgumentType.object.xml")
                .get("setDefaultArgumentType", N2oObject.class);

        JavaInvocation invocation = (JavaInvocation) object.getOperations()[0].getInvocation();
        assert Argument.Type.PRIMITIVE == invocation.getArguments()[0].getType();
        assert Argument.Type.CLASS == invocation.getArguments()[1].getType();
    }
}
