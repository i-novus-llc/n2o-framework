package net.n2oapp.framework.config.register.route;

public class IncorrectRouteException extends RuntimeException {

    public IncorrectRouteException(String url) {
        super("Url '" + url + "' incorrect! Should start with '/'! ");
    }
}
