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
    private String okLabel;
    @JsonProperty
    private String cancelLabel;
    @JsonProperty
    private String modelLink;
    @JsonProperty
    private String confirmCondition;
    @JsonProperty
    private ConfirmType mode;
}
