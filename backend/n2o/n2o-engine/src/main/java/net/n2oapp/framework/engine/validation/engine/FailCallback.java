package net.n2oapp.framework.engine.validation.engine;

import java.util.function.Consumer;

/**
 * @author operehod
 * @since 02.04.2015
 */
@FunctionalInterface
public interface FailCallback extends Consumer<FailInfo> {

}
