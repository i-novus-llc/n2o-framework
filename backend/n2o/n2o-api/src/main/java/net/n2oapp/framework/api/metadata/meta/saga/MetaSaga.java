package net.n2oapp.framework.api.metadata.meta.saga;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;

/**
 * Постпроцессинг действия
 */
@Getter
@Setter
public class MetaSaga implements Compiled {
    @JsonProperty
    private AlertSaga alert;
    @JsonProperty
    private RefreshSaga refresh;
    @JsonProperty
    private RedirectSaga redirect;
    @JsonProperty
    private Integer modalsToClose;
    @JsonProperty
    private MessageSaga messages;
    @JsonProperty
    private CloseSaga onClose;
    @JsonProperty
    private PollingSaga polling;
    @JsonProperty
    private LoadingSaga loading;
    @JsonProperty
    private String clear;
    private String messageWidgetId;
}
