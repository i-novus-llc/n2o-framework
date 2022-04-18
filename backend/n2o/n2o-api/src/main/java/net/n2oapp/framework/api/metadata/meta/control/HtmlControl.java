package net.n2oapp.framework.api.metadata.meta.control;

import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель компонента вывода html
 */
@Getter
@Setter
public class HtmlControl extends Control {
    private String html;
}
