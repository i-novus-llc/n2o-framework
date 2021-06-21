package net.n2oapp.framework.api.metadata.application;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;

/**
 * Клиентская модель логотипа и названия
 */
@Getter
@Setter
public class Logo implements Compiled {
    private String title;
    private String className;
    private String style;
    private String href;
    private String src;
}
