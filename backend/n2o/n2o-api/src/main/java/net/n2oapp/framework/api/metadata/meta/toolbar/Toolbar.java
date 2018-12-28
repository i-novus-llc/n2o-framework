package net.n2oapp.framework.api.metadata.meta.toolbar;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Button;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Group;

import java.util.List;
import java.util.Optional;

/**
 * Клиентская модель тулбара.
 *
 */
public class Toolbar extends StrictMap<String, List<Group>> implements Compiled {
    /**
     * Найти кнопку в тулбаре по идентификатору
     * @param id Идентификатор
     * @return Кнопка
     */
    public Button getButton(String id) {
        Optional<Button> first = values().stream().flatMap(List::stream).flatMap(g -> g.getButtons().stream()).filter(b -> id.equals(b.getId())).findFirst();
        return first.orElse(null);
    }
}
