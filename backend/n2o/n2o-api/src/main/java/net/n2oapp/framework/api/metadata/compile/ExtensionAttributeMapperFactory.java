package net.n2oapp.framework.api.metadata.compile;

import net.n2oapp.framework.api.factory.MetadataFactory;

import java.util.Map;

/**
 * Фабрика преобразователей дополнительных аттрибутов
 */
public interface ExtensionAttributeMapperFactory extends MetadataFactory<ExtensionAttributeMapper> {

    /**
     * Собрать дополнительные атрибуты
     * Если по namespace нашли mapper, то возвращается результат его работы, иначе возвращаются исходные аттрибуты
     *
     * @param attributes   исходные атрибуты
     * @param namespaceUri путь к namespace, по которому определяется как надо собирать атрибуты
     * @return собранные атрибуты
     */
    Map<String, Object> mapAttributes(Map<String, String> attributes, String namespaceUri, CompileProcessor p);
}
