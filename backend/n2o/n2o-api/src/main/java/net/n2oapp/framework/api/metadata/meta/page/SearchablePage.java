package net.n2oapp.framework.api.metadata.meta.page;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;

/**
 * Клиентская модель страницы с поисковой строкой
 */
@Getter
@Setter
public class SearchablePage extends StandardPage {
    @JsonProperty
    private String searchModelPrefix;
    @JsonProperty
    private String searchWidgetId;
    @JsonProperty
    private String searchModelKey;
    @JsonProperty
    private SearchBar searchBar;


    /**
     * Поисковая строка с кнопкой
     */
    @Getter
    @Setter
    public static class SearchBar {
        @JsonProperty
        private String className;
        @JsonProperty
        private Trigger trigger;
        @JsonProperty
        private String placeholder;
        @JsonProperty
        private AbstractButton button;
        @JsonProperty
        private String icon;
        @JsonProperty
        private int throttleDelay;

        public enum Trigger {
            CHANGE,
            ENTER,
            BUTTON
        }

        public void setTrigger(String trigger) {
            this.trigger = Trigger.valueOf(trigger.toUpperCase());
        }
    }

}
