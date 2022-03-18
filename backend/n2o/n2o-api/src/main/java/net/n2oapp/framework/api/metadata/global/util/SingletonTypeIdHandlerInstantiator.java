package net.n2oapp.framework.api.metadata.global.util;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Получение singleton бина для TypeIdResolver
 */
public class SingletonTypeIdHandlerInstantiator extends HandlerInstantiator {

    private Map<Class, TypeIdResolver> typeIdResolverMap = new HashMap<>();


    @Override
    public JsonDeserializer<?> deserializerInstance(DeserializationConfig config, Annotated annotated, Class<?> deserClass) {
        return null;
    }

    @Override
    public KeyDeserializer keyDeserializerInstance(DeserializationConfig config, Annotated annotated, Class<?> keyDeserClass) {
        return null;
    }

    @Override
    public JsonSerializer<?> serializerInstance(SerializationConfig config, Annotated annotated, Class<?> serClass) {
        return null;
    }

    @Override
    public TypeResolverBuilder<?> typeResolverBuilderInstance(MapperConfig<?> config, Annotated annotated, Class<?> builderClass) {
        return null;
    }

    @Override
    public TypeIdResolver typeIdResolverInstance(MapperConfig<?> config, Annotated annotated, Class<?> resolverClass) {
        if (!typeIdResolverMap.containsKey(resolverClass))
            typeIdResolverMap.put(resolverClass, (TypeIdResolver) ClassUtil.createInstance(resolverClass, true));
        return typeIdResolverMap.get(resolverClass);
    }

    public void addTypeIdResolver(Class<?> resolverClass, TypeIdResolver typeIdResolver) {
        typeIdResolverMap.put(resolverClass, typeIdResolver);
    }
}
