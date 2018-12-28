package net.n2oapp.framework.api.metadata.meta.action;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Кастомизированное действие
 */
@Getter
@Setter
public class CustomAction extends AbstractAction<CustomOptions> {

    public CustomAction(Map<String, Object> options) {
        super(new CustomOptions(options));
    }
}
