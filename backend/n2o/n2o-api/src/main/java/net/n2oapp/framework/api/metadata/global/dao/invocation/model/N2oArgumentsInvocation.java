package net.n2oapp.framework.api.metadata.global.dao.invocation.model;

/**
 * Модель операции которая на вход принимает аргументы
 */
public interface N2oArgumentsInvocation extends N2oInvocation {

    Argument[] getArguments();

}
