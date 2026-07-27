package net.n2oapp.framework.config.register.route;

public class IncorrectRouteException extends RuntimeException {

    public IncorrectRouteException(String url) {
        super(String.format("Некорректный URL: '%s'. URL должен начинаться с '/'", url));
    }
}
