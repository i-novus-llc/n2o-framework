package net.n2oapp.framework.autotest.api.collection;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.region.Region;

/**
 * Регионы для автотестирования
 */
public interface Regions extends ComponentsCollection {
    /**
     * <p>
     *     Возвращает регион по индексу
     * </p>
     *
     * <p>For example: {@code
     *     regions().region(0, PanelRegion.class)
     * }</p>
     *
     * @param index порядковый номер требуемого региона на странице
     * @param componentClass возвращаемый тип региона
     * @return Компонент регион для автотестирования
     */
    <T extends Region> T region(int index, Class<T> componentClass);

    /**
     * <p>
     *     Возвращает регион типа, наследуемого от базового региона, по условию
     * </p>
     *
     * <p>For example: {@code
     *     regions().region(Condition.visible, PanelRegion.class)
     * }</p>
     *
     * @param findBy условию поиска
     * @param componentClass возвращаемый тип региона
     * @return Компонент регион для автотестирования
     */
    <T extends Region> T region(Condition findBy, Class<T> componentClass);
}
