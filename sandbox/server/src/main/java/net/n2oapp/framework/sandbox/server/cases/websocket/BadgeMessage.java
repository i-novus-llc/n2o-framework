package net.n2oapp.framework.sandbox.server.cases.websocket;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BadgeMessage {

    private Integer count;
    private String color;
}
