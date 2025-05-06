package net.n2oapp.framework.api.metadata.datasource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.n2oapp.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
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
        private ReduxModelEnum sourceModel;
        @JsonProperty
        private String sourceField;
        @JsonProperty
        private String fetchValueExpression;
        @JsonProperty
        private List<Filter> filters;
    }

    @Getter
    @Setter
    public static class Submit implements Compiled {
        @JsonProperty
        private String type = "inherited";
        @JsonProperty
        private ReduxModelEnum model;
        @JsonProperty
        private String targetDs;
        @JsonProperty
        private ReduxModelEnum targetModel;
        @JsonProperty
        private String targetField;
        @JsonProperty
        private Boolean auto;
        @JsonProperty
        private String submitValueExpression;
    }

    @Getter
    @Setter
    public static class Filter implements Compiled {
        @JsonProperty
        private FilterTypeEnum type;
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
            return modelLink.getLink();
        }
    }
}
