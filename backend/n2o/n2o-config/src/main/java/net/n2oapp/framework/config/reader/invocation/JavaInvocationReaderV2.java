package net.n2oapp.framework.config.reader.invocation;

import net.n2oapp.framework.api.metadata.global.dao.invocation.java.EjbInvocation;
import net.n2oapp.framework.api.metadata.global.dao.invocation.java.JavaInvocation;
import net.n2oapp.framework.api.metadata.global.dao.invocation.java.SpringInvocation;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.Argument;
import net.n2oapp.framework.api.metadata.reader.TypedElementReader;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getChild;
import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getChildren;

/**
 * Считывает атрибуты действий согласно n2o-invocations-2.0.xsd
 */
@Component
public class JavaInvocationReaderV2 extends AbstractInvocationReaderV2<JavaInvocation> {

    @Override
    public JavaInvocation read(Element element, Namespace namespace) {
        JavaInvocation invocation;
        invocation = getChild(element, "spring", new SpringInvocationReader());
        if (invocation == null)
            invocation = getChild(element, "ejb", new EjbInvocationReader());
        if (invocation == null)
            invocation = new JavaInvocation();
        invocation.setClassName(ReaderJdomUtil.getAttributeString(element, "class"));
        invocation.setMethodName(ReaderJdomUtil.getAttributeString(element, "method"));
        invocation.setArguments(getChildren(element, "arguments", "argument", ArgumentElementReader.getInstance()));
        return invocation;
    }

    @Override
    public Class<JavaInvocation> getElementClass() {
        return JavaInvocation.class;
    }

    @Override
    public String getElementName() {
        return "java";
    }

    /**
     * Считывает атрибуты аргументов
     */
    public static class ArgumentElementReader implements TypedElementReader<Argument> {
        private static final ArgumentElementReader instance = new ArgumentElementReader();

        public static ArgumentElementReader getInstance() {
            return instance;
        }

        @Override
        public Argument read(Element element) {
            Argument argument = new Argument();
            argument.setName(ReaderJdomUtil.getAttributeString(element, "name"));
            argument.setClassName(ReaderJdomUtil.getAttributeString(element, "class"));
            argument.setType(ReaderJdomUtil.getAttributeEnum(element, "type", Argument.Type.class));
            return argument;
        }

        @Override
        public Class<Argument> getElementClass() {
            return Argument.class;
        }

        @Override
        public String getElementName() {
            return "argument";
        }
    }

    /**
     * Считывает атрибуты spring вызовов
     */
    private class SpringInvocationReader implements TypedElementReader<SpringInvocation> {

        @Override
        public Class<SpringInvocation> getElementClass() {
            return SpringInvocation.class;
        }

        @Override
        public SpringInvocation read(Element element) {
            SpringInvocation invocation = new SpringInvocation();
            invocation.setBeanId(ReaderJdomUtil.getAttributeString(element, "bean"));
            return invocation;
        }

        @Override
        public String getElementName() {
            return "spring";
        }
    }

    /**
     * Считывает атрибуты ejb вызовов
     */
    private class EjbInvocationReader implements TypedElementReader<EjbInvocation> {

        @Override
        public Class<EjbInvocation> getElementClass() {
            return EjbInvocation.class;
        }

        @Override
        public EjbInvocation read(Element element) {
            EjbInvocation invocation = new EjbInvocation();
            invocation.setBeanId(ReaderJdomUtil.getAttributeString(element, "bean"));
            invocation.setUri(ReaderJdomUtil.getAttributeString(element, "uri"));
            invocation.setProtocol(ReaderJdomUtil.getAttributeString(element, "protocol"));
            invocation.setApplication(ReaderJdomUtil.getAttributeString(element, "application"));
            invocation.setModule(ReaderJdomUtil.getAttributeString(element, "module"));
            invocation.setDistinct(ReaderJdomUtil.getAttributeString(element, "distinct"));
            invocation.setStatefull(ReaderJdomUtil.getAttributeBoolean(element, "statefull"));
            return invocation;
        }

        @Override
        public String getElementName() {
            return "ejb";
        }
    }
}
