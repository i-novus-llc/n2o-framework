package net.n2oapp.framework.api.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация-метка поля компонента для использования в визуальном редакторе
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface N2oAttribute {
    /**
     * @return Описание атрибута
     */
    String label() default "";
}
