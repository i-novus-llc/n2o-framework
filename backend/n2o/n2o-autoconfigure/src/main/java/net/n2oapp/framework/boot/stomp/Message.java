package net.n2oapp.framework.boot.stomp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {

    private Integer count;

    public void generateCount() {
        count = (int) (Math.random() * 10);
    }
}
