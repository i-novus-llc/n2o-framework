package net.n2oapp.framework.autotest.api.collection;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.region.Region;

/**
 * Регионы для автотестирования
 */
public interface Regions extends ComponentsCollection {
    <T extends Region> T region(int index, Class<T> componentClass);
    <T extends Region> T region(Condition findBy, Class<T> componentClass);
}
