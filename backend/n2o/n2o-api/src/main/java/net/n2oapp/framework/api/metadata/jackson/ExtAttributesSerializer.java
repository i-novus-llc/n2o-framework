package net.n2oapp.framework.api.metadata.jackson;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@JsonDeserialize(keyUsing = N2oNamespaceDeserializer.class)
@JsonSerialize(keyUsing = N2oNamespaceSerializer.class, contentUsing = N2oMapSerializer.class)
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
public @interface ExtAttributesSerializer {
}
