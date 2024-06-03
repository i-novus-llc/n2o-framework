package net.n2oapp.framework.autotest.impl.component.region;

import com.codeborne.selenide.*;
import net.n2oapp.framework.autotest.api.component.region.Region;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import java.util.stream.StreamSupport;

/**
 * Регион для автотестирования
 */
public class N2oRegion extends N2oComponent implements Region {

    @Override
    public void shouldHaveStyle(String style) {
        element().shouldHave(Condition.attribute("style", style));
    }

    protected ElementsCollection firstLevelElements(String rootSelector, String childSelector) {
        String elementSelector = String.format("%s > %s", rootSelector, childSelector);
        ElementsCollection nestingElements = element().$$(String.format("%s %s", rootSelector, elementSelector));

        return element().$$(elementSelector)
                .filter(new WebElementCondition("shouldBeFirstLevelElement") {
                    @Nonnull
                    @Override
                    public CheckResult check(@Nonnull Driver driver, @Nonnull WebElement element) {
                        boolean result = StreamSupport.stream(nestingElements.spliterator(), false).noneMatch(element::equals);
                        return new CheckResult(result ? CheckResult.Verdict.ACCEPT : CheckResult.Verdict.REJECT, null);
                    }
                });
    }
}
