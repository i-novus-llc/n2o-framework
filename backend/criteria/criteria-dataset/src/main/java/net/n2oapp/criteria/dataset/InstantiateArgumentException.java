package net.n2oapp.criteria.dataset;

/**
 * User: iryabov
 * Date: 27.08.13
 * Time: 17:37
 */
public class InstantiateArgumentException extends RuntimeException {
    private String argument;

    public InstantiateArgumentException(String argument, Throwable cause) {
        super(cause);
        this.argument = argument;
    }

    public String getArgument() {
        return argument;
    }
}
