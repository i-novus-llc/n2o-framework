package net.n2oapp.criteria.api;

/**
 * User: iryabov
 * Date: 25.10.13
 * Time: 16:12
 */
public interface CollectionPageService<C extends Criteria, E> {
    CollectionPage<E> getCollectionPage(C criteria);
}
