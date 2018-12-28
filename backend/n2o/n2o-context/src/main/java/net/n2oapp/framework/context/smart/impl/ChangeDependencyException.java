package net.n2oapp.framework.context.smart.impl;

/**
 * User: operehod
 * Date: 27.01.2015
 * Time: 16:42
 */
public class ChangeDependencyException extends ContextException {

    public ChangeDependencyException(String key) {
        super(String.format("changed parents for key '%s'", key));
    }

}
