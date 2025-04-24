package net.n2oapp.framework.api.metadata.dataprovider;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.dao.invocation.Argument;
import net.n2oapp.framework.api.metadata.global.dao.invocation.N2oArgumentsInvocation;

/**
 * Модель вызова java кода (статичные методы класса, spring бины, ejb бины)
 */
@Getter
@Setter
public class N2oJavaDataProvider extends AbstractDataProvider implements N2oArgumentsInvocation {
    private String className;
    private String method;
    private Argument[] arguments;
    private DIProvider diProvider;

    public void setSpringProvider(SpringProvider spring) {
        if (getDiProvider() == null || spring != null)
            setDiProvider(spring);
    }

    public SpringProvider getSpringProvider() {
        if (getDiProvider() instanceof SpringProvider springProvider)
            return springProvider;
        return null;
    }

    public void setEjbProvider(EjbProvider ejb) {
        if (getDiProvider() == null || ejb != null)
            setDiProvider(ejb);
    }

    public EjbProvider getEjbProvider() {
        if (getDiProvider() instanceof EjbProvider ejbProvider)
            return ejbProvider;
        return null;
    }
}
