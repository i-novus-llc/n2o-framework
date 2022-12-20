package net.n2oapp.framework.api.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация-метка компонента для отображения в визуальном редакторе
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface N2oComponent {
    /**
     * @return Имя класса
     */
    String value() default "";
}
