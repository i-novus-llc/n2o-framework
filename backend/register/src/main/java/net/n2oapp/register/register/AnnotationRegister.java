package net.n2oapp.register.register;

import net.n2oapp.register.extractor.BaseExtractor;
import net.n2oapp.register.scanner.AnnotatedClassScanner;

import java.lang.annotation.Annotation;

/**
 * User: IRyabov
 * Date: 10.05.12
 * Time: 18:35
 */
public class AnnotationRegister<A extends Annotation> extends BaseRegister<Class, A> {
    protected Class<? extends Annotation> annotationClass;

    public AnnotationRegister(final Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
        extractor = new BaseExtractor<Class, A>() {
            @Override
            public A extract(Class source) {
                return (A) source.getAnnotation(annotationClass);
            }

        };
        scanner = new AnnotatedClassScanner(annotationClass);
    }
}
