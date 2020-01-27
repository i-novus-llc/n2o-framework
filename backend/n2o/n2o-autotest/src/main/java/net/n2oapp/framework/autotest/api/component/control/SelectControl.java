package net.n2oapp.framework.autotest.api.component.control;

import com.codeborne.selenide.Condition;

public interface SelectControl extends Control {
    void openOptions();
    void closeOptions();
    void find(String query);
    void select(int index);
    void select(Condition by);
}
