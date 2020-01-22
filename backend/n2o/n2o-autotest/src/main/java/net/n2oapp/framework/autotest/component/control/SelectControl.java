package net.n2oapp.framework.autotest.component.control;

import net.n2oapp.framework.autotest.N2oMatcher;

public interface SelectControl extends Control {
    void openOptions();
    void closeOptions();
    void find(String query);
    void select(int index);
    void select(N2oMatcher which);
}
