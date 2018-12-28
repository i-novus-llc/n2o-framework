package net.n2oapp.register.scanner;

/**
 * User: IRyabov
 * Date: 24.01.12
 * Time: 19:01
 */
public class ClassScanner<T> extends ScannerImpl<Class<?>> {

    protected String scanPackage;

    public void setScanPackage(String scanPackage) {
        this.scanPackage = scanPackage;
    }


    @Override
    protected boolean isClassSuitable(Class clazz) {
        return clazz.getName().startsWith(scanPackage);
    }

    @Override
    protected Class<?>[] getItemsFromClass(Class clazz) {
        return new Class[]{clazz};
    }

    @Override
    protected boolean isItemSuitable(Class<?> item) {
        return true;
    }

}
