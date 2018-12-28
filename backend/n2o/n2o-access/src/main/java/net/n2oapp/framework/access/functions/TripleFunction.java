package net.n2oapp.framework.access.functions;

/**
 * @author V. Alexeev.
 */
@FunctionalInterface
public interface TripleFunction<A, B, C, D> {

    D apply(A a, B b, C c);

}
