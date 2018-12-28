package net.n2oapp.engine.factory;

/**
 * User: iryabov
 * Date: 09.09.13
 * Time: 11:16
 */
public interface ClassedEngineFactory<A, G extends ClassedEngine<A>> extends EngineFactory<Class<? extends A>, G> {
}
