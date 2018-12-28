package net.n2oapp.framework.api.metadata.local.util;

/**
 * User: operehod
 * Date: 10.03.2015
 * Time: 11:52
 */
@FunctionalInterface
public interface MetadataSupplier<C> {

    C get(String id);

}
