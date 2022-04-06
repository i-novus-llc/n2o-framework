package net.n2oapp.framework.api.metadata.jackson;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Аннотация для обозначения класса, в котором при сериализации и десериализации проставляется тип класса
 * в свойство componentType. По умолчанию это simpleName класса, но его можно переопределить задав value
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "componentType")
@JsonTypeIdResolver(ComponentTypeResolver.class)
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
public @interface ComponentType {
    String value() default "";
}
