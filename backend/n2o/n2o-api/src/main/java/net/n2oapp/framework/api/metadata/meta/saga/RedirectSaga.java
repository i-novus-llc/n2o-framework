package net.n2oapp.framework.api.metadata.meta.saga;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.meta.BindLink;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Информация о редиректе после действия
 */
@Getter
@Setter
public class RedirectSaga implements Compiled {
    @JsonProperty
    private String path;
    @JsonProperty
    private Map<String, BindLink> pathMapping = new HashMap<>();
    @JsonProperty
    private Map<String, BindLink> queryMapping = new LinkedHashMap<>();
    @JsonProperty
    private Target target;
    /**
     * Признак, что ссылки в редиректе требуется разрещать на сервере
     */
    private boolean server;
}