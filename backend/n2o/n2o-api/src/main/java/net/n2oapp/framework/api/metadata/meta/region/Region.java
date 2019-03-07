package net.n2oapp.framework.api.metadata.meta.region;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.NameAware;
import net.n2oapp.framework.api.metadata.aware.PropertiesAware;
import net.n2oapp.framework.api.metadata.aware.SrcAware;

import java.util.List;
import java.util.Map;

/**
 * Клиентская модель региона n2o
 */
@Getter
@Setter
public abstract class Region implements Compiled, SrcAware, NameAware, IdAware {
    private String id;
    private String name;
    private String place;
    @JsonProperty
    private String src;
    private List<? extends Item> items;
    private Map<String, Object> properties;

    @JsonAnyGetter
    public Map<String, Object> getJsonProperties() {
        return properties;
    }

    @Getter
    @Setter
    public static class Item implements Compiled, PropertiesAware {
        @JsonProperty
        private String id = "item";
        @JsonProperty
        private String label;
        @JsonProperty
        private Boolean opened;
        @JsonProperty
        private Boolean fetchOnInit;
        @JsonProperty
        private String widgetId;
        private Map<String, Object> properties;
        private String objectId;
        @JsonProperty
        private RegionDependency dependency;

        @JsonAnyGetter
        public Map<String, Object> getJsonProperties() {
            return properties;
        }

    }

}
