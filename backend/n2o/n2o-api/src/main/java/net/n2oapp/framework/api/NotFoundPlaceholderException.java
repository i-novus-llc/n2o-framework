package net.n2oapp.framework.api;

public class NotFoundPlaceholderException extends RuntimeException {
    private String placeholder;

    public NotFoundPlaceholderException(String placeholder) {
        super();
        this.placeholder = placeholder;
    }

    public String getPlaceholder() {
        return placeholder;
    }
}
