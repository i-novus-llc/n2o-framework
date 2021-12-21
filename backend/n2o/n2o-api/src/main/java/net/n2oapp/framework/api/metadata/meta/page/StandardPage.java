package net.n2oapp.framework.api.metadata.meta.page;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.global.view.page.BasePageUtil;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.PerformButton;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Клиентская модель страницы с регионами
 */
@Getter
@Setter
public class StandardPage extends Page {
    @JsonProperty
    private Map<String, List<Region>> regions;
    @JsonProperty
    private RegionWidth width;

    /**
     * Найти виджет по клиентскому идентификатору
     *
     * @param clientWidgetId Идентификатор
     * @param clazz          Класс виджета
     * @param <T>            Тип виджета
     * @return Найденный виджет
     * @throws NoSuchElementException Если виджет не был найден
     */
    public <T extends Widget<?>> T findWidget(String clientWidgetId, Class<T> clazz) {
        return BasePageUtil.getCompiledWidgets(this).stream()
                .filter(w -> w.getId().equals(clientWidgetId))
                .map(clazz::cast).findAny().orElseThrow();
    }

    /**
     * Найти кнопку на странице
     *
     * @param buttonId Идентификатор кнопки
     * @return Кнопка
     * @throws NoSuchElementException Если кнопка не найдена
     */
    public PerformButton findButton(String buttonId) {
        AbstractButton button = getToolbar().getButton(buttonId);
        if (button != null)
            return (PerformButton) button;
        List<Toolbar> widgetToolbars = BasePageUtil.getCompiledWidgets(this).stream()
                .map(Widget::getToolbar).collect(Collectors.toList());
        for (Toolbar toolbar : widgetToolbars) {
            button = toolbar.getButton(buttonId);
            if (button != null)
                return (PerformButton) button;
        }
        throw new NoSuchElementException();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public class RegionWidth implements Compiled {
        @JsonProperty
        private String left;
        @JsonProperty
        private String right;

        public RegionWidth(String left, String right) {
            this.left = left;
            this.right = right;
        }
    }
}
