package net.n2oapp.framework.config.metadata.compile.widget;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.action.UploadType;

/**
 * Используется для передачи UploadType в компиляцию поля
 */
@Getter
@Setter
public class UploadScope {
    private UploadType upload;
}
