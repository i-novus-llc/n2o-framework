package net.n2oapp.criteria.api.constructor;

/**
 * Date: 10.12.11
 * Time: 13:32
 */
public interface CriteriaConstructor<T, Init> {

    CriteriaConstructorResult construct(T source, Init init);

}
