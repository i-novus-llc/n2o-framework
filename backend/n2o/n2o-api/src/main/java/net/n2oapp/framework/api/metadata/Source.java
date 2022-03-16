package net.n2oapp.framework.api.metadata;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import net.n2oapp.framework.api.metadata.global.util.ComponentTypeResolver;

import java.io.Serializable;

/**
 * Метка исходных метаданных
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "componentType")
@JsonTypeIdResolver(ComponentTypeResolver.class)
public interface Source extends Serializable {
}
