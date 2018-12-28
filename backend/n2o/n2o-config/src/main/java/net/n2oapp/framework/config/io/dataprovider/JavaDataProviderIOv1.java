package net.n2oapp.framework.config.io.dataprovider;

import net.n2oapp.framework.api.metadata.dataprovider.EjbProvider;
import net.n2oapp.framework.api.metadata.dataprovider.N2oJavaDataProvider;
import net.n2oapp.framework.api.metadata.dataprovider.DIProvider;
import net.n2oapp.framework.api.metadata.dataprovider.SpringProvider;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.Argument;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import org.jdom.Element;
import org.springframework.stereotype.Component;

@Component
public class JavaDataProviderIOv1 implements NamespaceIO<N2oJavaDataProvider>, DataProviderIOv1 {

    @Override
    public Class<N2oJavaDataProvider> getElementClass() {
        return N2oJavaDataProvider.class;
    }

    @Override
    public String getElementName() {
        return "java";
    }

    @Override
    public void io(Element e, N2oJavaDataProvider m, IOProcessor p) {
        p.attribute(e, "class", m::getClassName, m::setClassName);
        p.attribute(e, "method", m::getMethod, m::setMethod);
        p.children(e, "arguments", "argument", m::getArguments, m::setArguments, Argument::new, this::arguments);
        p.child(e, null, "spring", m::getSpringProvider, m::setSpringProvider, SpringProvider::new, this::spring);
        p.child(e, null, "ejb", m::getEjbProvider, m::setEjbProvider, EjbProvider::new, this::ejb);
//        p.anyChild(e, null, m::getDiProvider, m::setDiProvider, p.oneOf(ObjectLocatorProps.class)
//                .add("spring", SpringObjectLocatorProps.class, this::spring)
//                .add("ejb", EjbObjectLocatorProps.class, this::ejb));
    }

    private void arguments(Element e, Argument t, IOProcessor p) {
        p.attribute(e, "name", t::getName, t::setName);
        p.attributeEnum(e, "type", t::getType, t::setType, Argument.Type.class);
        p.attribute(e, "class", t::getClassName, t::setClassName);
    }

    public void spring(Element e, DIProvider m, IOProcessor p) {
        SpringProvider s = (SpringProvider) m;
        p.attribute(e, "bean", s::getSpringBean, s::setSpringBean);
    }

    public void ejb(Element e, DIProvider m, IOProcessor p) {
        EjbProvider ejb = (EjbProvider) m;
        p.attribute(e, "protocol", ejb::getEjbProtocol, ejb::setEjbProtocol);
        p.attribute(e, "application", ejb::getEjbApplication, ejb::setEjbApplication);
        p.attribute(e, "module", ejb::getEjbModule, ejb::setEjbModule);
        p.attribute(e, "bean", ejb::getEjbBean, ejb::setEjbBean);
        p.attribute(e, "distinct", ejb::getEjbDistinct, ejb::setEjbDistinct);
        p.attribute(e, "uri", ejb::getEjbUri, ejb::setEjbUri);
        p.attributeBoolean(e, "stateful", ejb::getEjbStateful, ejb::setEjbStateful);
    }

}
