package net.n2oapp.framework.autotest.websocket;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BadgeMessage {

    @JsonProperty
    private Integer count;
    @JsonProperty
    private String color;
}
