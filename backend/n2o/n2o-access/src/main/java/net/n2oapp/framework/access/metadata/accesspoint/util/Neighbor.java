package net.n2oapp.framework.access.metadata.accesspoint.util;

/**
* User: operehod
* Date: 20.02.2015
* Time: 12:49
*/
public interface Neighbor<V extends Neighbor> {

    public void acceptNeighbor(V v);

}
