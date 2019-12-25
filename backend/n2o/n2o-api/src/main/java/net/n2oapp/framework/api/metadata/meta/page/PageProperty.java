package net.n2oapp.framework.api.metadata.meta.page;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.ModelLink;

import java.io.Serializable;

/**
 * Свойства страницы
 */
@Getter
@Setter
public class PageProperty implements Serializable {
    @JsonProperty
    private String title;
    private ModelLink modelLink;
}
