package net.n2oapp.framework.autotest.api.component;

public interface Badge extends Component{
    void shouldHaveImage();

    void shouldBeShape(String shape);

    void imageShouldBeShape(String shape);

    void imageShouldBePosition(String position);
}
