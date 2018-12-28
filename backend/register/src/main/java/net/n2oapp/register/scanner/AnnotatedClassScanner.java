package net.n2oapp.register.scanner;

import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;

/**
 * User: IRyabov
 * Date: 10.05.12
 * Time: 17:06
 */
public class AnnotatedClassScanner<T> extends ClassScanner<T> {
    private Class<? extends Annotation> annotationClass;


    public AnnotatedClassScanner(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    @Override
    protected boolean isItemSuitable(Class<?> item) {
        return item.isAnnotationPresent(annotationClass);
    }

}
