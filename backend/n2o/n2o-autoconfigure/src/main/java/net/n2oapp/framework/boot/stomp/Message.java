package net.n2oapp.framework.boot.stomp;

import lombok.Getter;

@Getter
public class Message { //fixme удалить после дебага на фронте

    private Integer count;

    public void setCount() {
        count = (int) (Math.random() * 10);
    }
}
