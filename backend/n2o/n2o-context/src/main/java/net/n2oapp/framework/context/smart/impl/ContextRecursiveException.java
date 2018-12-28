package net.n2oapp.framework.context.smart.impl;

/**
 * User: operehod
 * Date: 27.01.2015
 * Time: 16:42
 */
public class ContextRecursiveException extends ContextException {

    public ContextRecursiveException(String key) {
        super(String.format("recursive call for key '%s'", key));
    }

}
