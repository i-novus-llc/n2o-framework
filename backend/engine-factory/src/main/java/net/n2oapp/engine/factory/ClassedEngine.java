package net.n2oapp.engine.factory;

/**
 * Типизированный по классу движок
 * @param <A> Класс
 */
public interface ClassedEngine<A> extends TypicalEngine<Class<? extends A>> {
}
