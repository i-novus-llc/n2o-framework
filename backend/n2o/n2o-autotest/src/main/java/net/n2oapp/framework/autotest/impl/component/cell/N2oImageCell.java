package net.n2oapp.framework.autotest.impl.component.cell;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.cell.ImageCell;

public class N2oImageCell extends N2oCell implements ImageCell {
    @Override
    public void srcShouldBe(String src) {
        element().$("img").shouldBe(Condition.attribute("src", src));
    }
}
