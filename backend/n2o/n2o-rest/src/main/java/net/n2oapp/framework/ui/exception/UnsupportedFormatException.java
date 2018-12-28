package net.n2oapp.framework.ui.exception;

import net.n2oapp.framework.api.exception.N2oException;

/**
 * Created with IntelliJ IDEA.
 * User: operhod
 * Date: 20.09.13
 * Time: 16:23
 * To change this template use File | Settings | File Templates.
 */
public class UnsupportedFormatException extends N2oException {
    public UnsupportedFormatException(String message) {
        super(message);
    }
}
