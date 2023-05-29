package net.n2oapp.framework.api.metadata.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Component;

/**
 * Клиентская модель подвала (footer)
 */
@Getter
@Setter
public class Footer extends Component {
    @JsonProperty
    private String textRight;
    @JsonProperty
    private String textLeft;
}
