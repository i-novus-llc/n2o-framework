package net.n2oapp.framework.api.metadata.global.view.page;


import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.ActionsBar;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;

/**
 * "Исходная" модель страницы
 */
@Getter
@Setter
public abstract class N2oPage extends N2oBasePage {
    private ActionsBar[] actions;
    private GenerateType actionGenerate;
    private N2oToolbar[] toolbars;
}
