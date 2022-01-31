package net.n2oapp.framework.api.metadata.meta.page;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;

import java.io.Serializable;

/**
 * Клиентская модель страницы с поисковой строкой
 */
@Getter
@Setter
public class SearchablePage extends StandardPage {
    @JsonProperty
    private SearchBar searchBar;


    /**
     * Поисковая строка с кнопкой
     */
    @Getter
    @Setter
    public static class SearchBar implements Compiled {
        @JsonProperty
        private String fieldId;
        @JsonProperty
        private String datasource;
        @JsonProperty
        private String className;
        @JsonProperty
        private TriggerType trigger;
        @JsonProperty
        private String placeholder;
        @JsonProperty
        private Button button;
        @JsonProperty
        private String icon;
        @JsonProperty
        private Integer throttleDelay;

        /**
         * Тригер вызова поиска
         */
        public enum TriggerType {
            CHANGE,
            ENTER,
            BUTTON
        }

        public static class Button implements Serializable {
        }
    }
}
