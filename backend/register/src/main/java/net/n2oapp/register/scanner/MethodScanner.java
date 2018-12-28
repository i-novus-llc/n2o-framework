package net.n2oapp.register.scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * User: Igor
 * Date: 09.03.2012
 * Time: 10:00:35
 */
public class MethodScanner extends ScannerImpl<Method> {
    protected Logger log = LoggerFactory.getLogger(this.getClass());

    private String scanPackage;

    public void setScanPackage(String scanPackage) {
        this.scanPackage = scanPackage;
    }

    @Override
    protected boolean isClassSuitable(Class clazz) {
        return clazz.getName().startsWith(scanPackage);
    }

    @Override
    protected Method[] getItemsFromClass(Class clazz) {
        return clazz.getDeclaredMethods();
    }

    @Override
    protected boolean isItemSuitable(Method item) {
        return true;
    }

}
