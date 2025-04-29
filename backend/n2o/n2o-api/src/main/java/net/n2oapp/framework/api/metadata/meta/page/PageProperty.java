package net.n2oapp.framework.api.metadata.meta.page;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.meta.ModelLink;

import java.util.List;

/**
 * Свойства страницы
 */
@Getter
@Setter
public class PageProperty implements Compiled {
    @JsonProperty
    private String title;
    @JsonProperty
    private String datasource;
    @JsonProperty
    private ReduxModelEnum model;
    @JsonProperty
    private String modalHeaderTitle;
    @JsonProperty
    private String htmlTitle;
    private List<ModelLink> modelLinks;
}
