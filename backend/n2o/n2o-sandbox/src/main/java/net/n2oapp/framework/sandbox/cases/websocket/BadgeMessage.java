package net.n2oapp.framework.sandbox.cases.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadgeMessage {
    @JsonProperty
    private Integer count;
    @JsonProperty
    private String color;
}
