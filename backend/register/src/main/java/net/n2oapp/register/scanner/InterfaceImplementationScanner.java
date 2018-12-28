package net.n2oapp.register.scanner;

import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

/**
 * User: IRyabov
 * Date: 26.01.12
 * Time: 10:34
 */
public class InterfaceImplementationScanner<T> extends ClassScanner<T> {
    private Class<T> interfaceClass;

    protected InterfaceImplementationScanner(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    @Override
    protected boolean isItemSuitable(Class<?> item) {
        return interfaceClass.isAssignableFrom(item);
    }

}
