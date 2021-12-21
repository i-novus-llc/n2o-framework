package net.n2oapp.framework.boot.stomp;

public class Message { //fixme удалить после дебага на фронте

    private Integer count;

    public Integer getCount() {
        return (int) (Math.random() * 10);
    }
}
