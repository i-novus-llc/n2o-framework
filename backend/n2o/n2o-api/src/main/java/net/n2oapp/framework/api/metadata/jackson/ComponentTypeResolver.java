package net.n2oapp.framework.api.metadata.jackson;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.register.ComponentTypeRegister;

/**
 * Соотношение объектов json с типами N2O
 */
public class ComponentTypeResolver implements TypeIdResolver {

    private ComponentTypeRegister register;

    @Override
    public void init(JavaType javaType) {
    }

    @Override
    public String idFromValue(Object o) {
        return idFromValueAndType(o, o.getClass());
    }

    @SuppressWarnings("unchecked")
    @Override
    public String idFromValueAndType(Object o, Class<?> aClass) {
        if (Source.class.isAssignableFrom(aClass)) {
            String type = register.getByClass((Class<? extends Source>) aClass);
            if (type != null)
                return type;
        }
        throw new IllegalStateException("Class " + aClass + " is not assignable a Source class");
    }

    @Override
    public String idFromBaseType() {
        return null;
    }

    @Override
    public JavaType typeFromId(DatabindContext databindContext, String type) {
        Class<? extends Source> clazz = register.getByType(type);
        if (clazz == null)
            throw new IllegalStateException("Class for type " + type + " not found");
        return TypeFactory.defaultInstance().constructType(clazz);
    }

    @Override
    public String getDescForKnownTypeIds() {
        return null;
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CUSTOM;
    }

    public void setRegister(ComponentTypeRegister register) {
        this.register = register;
    }
}
