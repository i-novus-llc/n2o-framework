package net.n2oapp.framework.api.metadata.meta.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;

import java.util.Objects;


/**
 * Redux действие
 *
 * @param <M> Постпроцессинг действия (saga)
 * @param <P> Полезная нагрузка
 */
@Getter
@Setter
public class ReduxActionOptions<P extends ActionPayload, M extends MetaSaga> implements ActionOptions {
    @JsonProperty
    private String type;
    @JsonProperty
    private P payload;
    @JsonProperty
    private M meta;

    public ReduxActionOptions() {
    }

    public ReduxActionOptions(String type) {
        this.type = type;
    }

    public ReduxActionOptions(String type, P payload) {
        this.type = type;
        this.payload = payload;
    }

    public ReduxActionOptions(String type, P payload, M meta) {
        this.type = type;
        this.payload = payload;
        this.meta = meta;
    }

    public ReduxActionOptions(P payload) {
        this.payload = payload;
    }

    public ReduxActionOptions(P payload, M meta) {
        this.payload = payload;
        this.meta = meta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReduxActionOptions)) return false;
        ReduxActionOptions<?, ?> that = (ReduxActionOptions<?, ?>) o;
        return Objects.equals(type, that.type) &&
                Objects.equals(payload, that.payload) &&
                Objects.equals(meta, that.meta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, payload, meta);
    }
}