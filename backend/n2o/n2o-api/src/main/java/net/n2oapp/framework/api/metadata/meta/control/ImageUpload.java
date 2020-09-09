package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель компонента загрузки изображений
 */
@Getter
@Setter
public class ImageUpload extends FileUpload {
    @JsonProperty
    private ListType listType;
    @JsonProperty
    private Boolean lightbox;
    @JsonProperty
    private Boolean showName;

    public enum ListType {
        image,
        card
    }
}
