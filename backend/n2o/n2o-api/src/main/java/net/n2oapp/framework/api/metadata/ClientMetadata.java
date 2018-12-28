package net.n2oapp.framework.api.metadata;

import net.n2oapp.framework.api.metadata.local.Processable;

import java.io.Serializable;

/**
 * Маркер, для метаданных готовых к серилизации json или к обработке в jsp
 */
public interface ClientMetadata extends Compiled, Processable {

    /**
     * Возвращает url получения этой метаданной
     * @return  url
     */
    default String getUrl(){
        return "";
    }
}
