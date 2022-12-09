package net.n2oapp.framework.api.metadata.datasource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.meta.ModelLink;

import java.util.List;

/**
 * Клиентская модель источника данных, получающего данные из другого источника данных
 */
@Getter
@Setter
@NoArgsConstructor
public class InheritedDatasource extends AbstractDatasource {

    @JsonProperty
    private Provider provider;
    @JsonProperty
    private Submit submit;

    @Getter
    @Setter
    public static class Provider implements Compiled {
        @JsonProperty
        private String type = "inherited";
        @JsonProperty
        private String sourceDs;
        @JsonProperty
        private ReduxModel sourceModel;
        @JsonProperty
        private String sourceField;
        @JsonProperty
        private List<Filter> filters;
    }

    @Getter
    @Setter
    public static class Submit implements Compiled {
        @JsonProperty
        private String type = "inherited";
        @JsonProperty
        private ReduxModel model;
        @JsonProperty
        private String targetDs;
        @JsonProperty
        private ReduxModel targetModel;
        @JsonProperty
        private String targetField;
        @JsonProperty
        private Boolean auto;
    }

    @Getter
    @Setter
    public static class Filter implements Compiled {
        @JsonProperty
        private FilterType type;
        @JsonProperty
        private String fieldId;
        @JsonProperty
        private Boolean required;
        private ModelLink modelLink;

        @JsonProperty
        public Object getValue() {
            return modelLink.getValue();
        }

        @JsonProperty
        public Object getLink() {
            return modelLink.getBindLink();
        }
    }
}
