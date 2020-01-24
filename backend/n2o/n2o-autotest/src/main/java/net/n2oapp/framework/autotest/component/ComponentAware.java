package net.n2oapp.framework.autotest.component;

public interface ComponentAware {
    <T extends Component> void setComponent(T component);
}
