package net.n2oapp.framework.engine.data.normalize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для обозначения класса, содержащего нормализующие функции,
 * и обозначения нормализующих функций непосредственно.
 * В качестве нормализующих функций могут быть использованы только статические методы
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Normalizer {
}
