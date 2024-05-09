package net.n2oapp.framework.autotest.impl.component.region;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.ElementsCollection;
import net.n2oapp.framework.autotest.api.component.region.Region;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;

/**
 * Регион для автотестирования
 */
public class N2oRegion extends N2oComponent implements Region {

    protected ElementsCollection firstLevelElements(String rootSelector, String childSelector) {
        String elementSelector = rootSelector + " > " + childSelector;
        ElementsCollection nestingElements = element().$$(rootSelector + " " + elementSelector);
        return element().$$(elementSelector)
                .filter(new Condition("shouldBeFirstLevelElement") {
                    @Nonnull
                    @Override
                    public CheckResult check(Driver driver, WebElement element) {
                        boolean result = !nestingElements.contains(element);
                        return new CheckResult(result ? CheckResult.Verdict.ACCEPT : CheckResult.Verdict.REJECT, null);
                    }
                });
    }


    @Override
    public void shouldHaveCssClass(String classname) {
        element().shouldHave(Condition.cssClass(classname));
    }

    @Override
    public void shouldHaveStyle(String style) {
        element().shouldHave(Condition.attribute("style", style));
    }
}
