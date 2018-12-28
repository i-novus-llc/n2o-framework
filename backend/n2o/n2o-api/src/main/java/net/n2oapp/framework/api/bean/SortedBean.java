package net.n2oapp.framework.api.bean;

/**
 * @author iryabov
 * @since 31.10.2014
 */
public abstract class SortedBean implements LocatedBean {

    private LocatedBean[] prevBeans;
    private LocatedBean[] nextBeans;
    private boolean beforeAll;
    private boolean afterAll;

    @Override
    public LocatedBean[] getPrevBeans() {
        return prevBeans;
    }

    public void setPrevBeans(LocatedBean[] prevBeans) {
        this.prevBeans = prevBeans;
    }

    @Override
    public LocatedBean[] getNextBeans() {
        return nextBeans;
    }

    public void setNextBeans(LocatedBean[] nextBeans) {
        this.nextBeans = nextBeans;
    }

    @Override
    public boolean isBeforeAll() {
        return beforeAll;
    }

    public void setBeforeAll(boolean beforeAll) {
        this.beforeAll = beforeAll;
    }

    @Override
    public boolean isAfterAll() {
        return afterAll;
    }

    public void setAfterAll(boolean afterAll) {
        this.afterAll = afterAll;
    }
}
