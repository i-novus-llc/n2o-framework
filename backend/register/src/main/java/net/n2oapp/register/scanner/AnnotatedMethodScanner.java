package net.n2oapp.register.scanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * User: Igor
 * Date: 09.03.2012
 * Time: 10:08:32
 */
public class AnnotatedMethodScanner extends MethodScanner {
    private Class<Annotation> annotationClass;

    public void setAnnotationClass(Class<Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    @Override
    protected boolean isItemSuitable(Method item) {
        return item.isAnnotationPresent(annotationClass);
    }

}
