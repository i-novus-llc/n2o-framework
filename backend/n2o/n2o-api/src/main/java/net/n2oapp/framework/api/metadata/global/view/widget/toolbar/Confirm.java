package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;


/**
 * Модель Предупреждения при выполнении операций
 */

@Getter
@Setter
public class Confirm implements Compiled {
    @JsonProperty
    private String text;
    @JsonProperty
    private String title;
    @JsonProperty
    private String modelLink;
    @JsonProperty
    private String condition;
    @JsonProperty
    private Boolean closeButton;
    @JsonProperty
    private Boolean reverseButtons;
    @JsonProperty
    private ConfirmType mode;
    @JsonProperty
    private Button ok;
    @JsonProperty
    private Button cancel;

    @Getter
    @Setter
    public static class Button implements Compiled {
        @JsonProperty
        private String label;
        @JsonProperty
        private String color;

        public Button() {
        }

        public Button(String label, String color) {
            this.label = label;
            this.color = color;
        }
    }
}
