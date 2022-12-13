package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualComponent;

/**
 * Компонент редактирования текста
 */
@Getter
@Setter
@VisualComponent
public class N2oTextEditor extends N2oPlainText {
    private String toolbarUrl;
}
