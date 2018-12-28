package net.n2oapp.framework.config.io.invocation;

import net.n2oapp.framework.api.metadata.global.dao.invocation.java.EjbInvocation;
import net.n2oapp.framework.api.metadata.global.dao.invocation.java.JavaInvocation;
import net.n2oapp.framework.api.metadata.global.dao.invocation.java.SpringInvocation;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.Argument;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.config.persister.invocation.EjbInvocationPersister;
import net.n2oapp.framework.config.persister.invocation.N2oJavaInvocationPersister;
import net.n2oapp.framework.config.persister.invocation.SpringInvocationPersister;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectivePersister;
import net.n2oapp.framework.config.selective.persister.SelectiveStandardPersister;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;

/**
 * Test reading and persisting invocation-2.0
 *
 * @author igafurov
 * @since 05.05.2017
 */
public class JavaMethodInvocationIOTestV2 {
    private SelectiveStandardReader reader = new SelectiveStandardReader()
            .addObjectReader().addInvocationsReader2();

    private SelectivePersister persister = new SelectiveStandardPersister()
            .addObjectPersister()
            .addPersister(new N2oJavaInvocationPersister())
            .addPersister(new SpringInvocationPersister())
            .addPersister(new EjbInvocationPersister());

    private ION2oMetadataTester tester = new ION2oMetadataTester()
            .addReader(reader)
            .addPersister(persister);

    /**
     * Test reading and persisting static method attributes
     * by JavaMethodInvocationReaderV2 and JavaInvocationPersister
     *
     * @result Action will be reading and persisting without any errors
     */
    @Test
    public void testStaticMethodInvocationIO() {
        assert tester.check("net/n2oapp/framework/config/io/invocation/testMethodInvocationReaderV2.object.xml",
                (N2oObject object) -> {
                    assert object.getOperations().length == 1;
                    JavaInvocation method = (JavaInvocation) object.getOperations()[0].getInvocation();
                    assert method.getClassName().equals("test");
                    assert method.getMethodName().equals("test");
                    assert method.getArguments().length == 2;

                    Argument firstArgument = method.getArguments()[0];
                    assert firstArgument.getClassName().equals("test");
                    assert firstArgument.getName().equals("first");
                    assert firstArgument.getType().equals(Argument.Type.PRIMITIVE);

                    Argument secondArgument = method.getArguments()[1];
                    assert secondArgument.getClassName().equals("test");
                    assert secondArgument.getName().equals("second");
                    assert secondArgument.getType().equals(Argument.Type.ENTITY);
                });
    }

    /**
     * Test reading and persisting spring method attributes
     * by JavaMethodInvocationReaderV2 and SpringInvocationPersister
     *
     * @result Action will be reading and persisting without any errors
     */
    @Test
    public void testSpringMethodInvocationIO() {
        assert tester.check("net/n2oapp/framework/config/io/invocation/testSpringMethodInvocationReader.object.xml",
                (N2oObject object) -> {
                    assert object.getOperations().length == 1;
                    SpringInvocation method = (SpringInvocation) object.getOperations()[0].getInvocation();
                    assert method.getMethodName().equals("calc");
                    assert method.getBeanId().equals("calculator");
                });
    }

    /**
     * Test reading and persisting ejb method attributes
     * by JavaMethodInvocationReaderV2 and EjbInvocationPersister
     *
     * @result Action will be reading and persisting without any errors
     */
    @Test
    public void testEjbMethodInvocationIO() {
        assert tester.check("net/n2oapp/framework/config/io/invocation/testEjbMethodInvocationReader.object.xml",
                (N2oObject object) -> {
                    assert object.getOperations().length == 1;
                    EjbInvocation method = (EjbInvocation) object.getOperations()[0].getInvocation();
                    assert method.getClassName().equals("com.example.MyClass");
                    assert method.getMethodName().equals("mycall");
                    assert method.getBeanId().equals("mybean");
                    assert method.getProtocol().equals("myprotocol");
                    assert method.getApplication().equals("myapplication");
                    assert method.getModule().equals("mymodule");
                    assert method.getDistinct().equals("mydistinct");
                    assert method.getStatefull().equals(true);
                });
    }
}
