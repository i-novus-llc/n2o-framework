package net.n2oapp.framework.api.metadata.compile;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;

import java.util.Map;

/**
 * Преобразование дополнительных аттрибутов
 */
public interface ExtensionAttributeMapper extends NamespaceUriAware {

    /**
     * Из дополнительных атрибутов сформировать список объектов
     *
     * @param attributes список дополнительных атрибутов
     * @return список объектов, построенных на основе дополнительных атрибутов
     */
    Map<String, Object> mapAttributes(Map<String, String> attributes, CompileProcessor p);

}
