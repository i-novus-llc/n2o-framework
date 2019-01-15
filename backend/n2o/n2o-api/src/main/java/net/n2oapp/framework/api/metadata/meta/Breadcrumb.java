package net.n2oapp.framework.api.metadata.meta;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;

import java.util.Map;

/**
 * Клиентская модель навигационной цепочки
 */
@Getter
@Setter
public class Breadcrumb implements Compiled {
    @JsonProperty
    private String label;
    @JsonProperty
    private String path;
    private ModelLink modelLink;
    @JsonProperty
    private Map<String, BindLink> pathMapping = new StrictMap<>();

    public Breadcrumb() {
    }

    public Breadcrumb(String label, String path) {
        this.label = label;
        this.path = path;
    }

    public Breadcrumb(Breadcrumb parent) {
        this.label = parent.getLabel();
        this.path = parent.getPath();
    }
}
