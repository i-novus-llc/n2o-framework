package net.n2oapp.framework.api.bean;

import java.util.List;

/**
 * @author operehod
 * @since 16.06.2015
 */
public class LocatedBeanPack<T extends LocatedBean> implements LocatedBean {

    private Class<T> beanClass;

    public Class<T> getBeanClass() {
        return beanClass;
    }

    public LocatedBeanPack(Class<T> beanClass) {
        this.beanClass = beanClass;
    }

    public LocatedBeanPack(Class<T> beanClass, List<T> pack) {
        this.beanClass = beanClass;
        this.pack = pack;
    }

    private List<T> pack;

    public List<T> getPack() {
        return pack;
    }

    public void setPack(List<T> pack) {
        this.pack = pack;
    }

    //логика для задания положения модуля среди других
    private LocatedBean[] prevBeans;
    private LocatedBean[] nextBeans;
    private boolean beforeAll;
    private boolean afterAll;

    @Override
    public LocatedBean[] getNextBeans() {
        return nextBeans;
    }

    @Override
    public LocatedBean[] getPrevBeans() {
        return prevBeans;
    }

    @Override
    public boolean isBeforeAll() {
        return beforeAll;
    }

    @Override
    public boolean isAfterAll() {
        return afterAll;
    }


    public void setBeforeAll(boolean beforeAll) {
        this.beforeAll = beforeAll;
    }

    public void setAfterAll(boolean afterAll) {
        this.afterAll = afterAll;
    }

    public final void setBefore(LocatedBean... n2oModule) {
        this.nextBeans = n2oModule;
    }

    public final void setAfter(LocatedBean... n2oModule) {
        this.prevBeans = n2oModule;
    }
}
