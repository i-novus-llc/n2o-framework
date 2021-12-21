package net.n2oapp.framework.boot.stomp;

public class Message { //fixme удалить после дебага на фронте

    private Integer count;

    public Integer getCount() {
        count = (int) (Math.random() * 10);
        return count;
    }
}
