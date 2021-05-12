package net.n2oapp.framework.api.processing;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.bean.LocatedBean;

/**
 * Абстрактная реализация обработки вызовов действий и выборок N2O
 */
@Getter
@Setter
@Deprecated
public abstract class N2oModule implements DataProcessing, LocatedBean {

    protected String id;
    protected boolean disable;

    //логика для задания положения модуля среди других
    private N2oModule[] prevBeans;
    private N2oModule[] nextBeans;
    private boolean beforeAll;
    private boolean afterAll;

    @Override
    public boolean isBeforeAll() {
        return beforeAll;
    }

    @Override
    public boolean isAfterAll() {
        return afterAll;
    }

    public void setBefore(N2oModule... n2oModule) {
        this.nextBeans = n2oModule;
    }

    public void setAfter(N2oModule... n2oModule) {
        this.prevBeans = n2oModule;
    }
}
