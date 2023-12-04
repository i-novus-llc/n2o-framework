package net.n2oapp.framework.api.metadata.meta.action.confirm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ConfirmType;
import net.n2oapp.framework.api.metadata.meta.action.ActionPayload;

import java.util.Map;

@Getter
@Setter
public class ConfirmActionPayload implements ActionPayload {

    @JsonProperty
    private String title;

    @JsonProperty
    private String text;

    @JsonProperty
    private String className;

    @JsonProperty
    private Map<String, String> style;

    @JsonProperty
    private ConfirmType mode;

    @JsonProperty
    private Boolean closeButton;

    @JsonProperty
    private Boolean reverseButtons;

    @JsonProperty
    private ConfirmButton ok;

    @JsonProperty
    private ConfirmButton cancel;

    @Getter
    @Setter
    public static class ConfirmButton implements Compiled {

        @JsonProperty
        private String label;

        @JsonProperty
        private String color;

        @JsonProperty
        private String className;

        @JsonProperty
        private Map<String, String> style;
    }

}
