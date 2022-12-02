package net.n2oapp.framework.autotest.api.component;

/**
 * Тултип компонента страницы
 */
public interface Tooltip extends Component {

    void shouldBeEmpty();

    void shouldHaveText(String... text);
}
