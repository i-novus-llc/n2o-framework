package net.n2oapp.framework.export.streaming;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.data.streaming.reader.Reader;

import java.util.Iterator;

public class ClientCollectionPageReader implements Reader<DataSet> {

    private Iterator<DataSet> iterator;

    public ClientCollectionPageReader(CollectionPage<DataSet> collectionPage) {
        this.iterator = collectionPage.getCollection().iterator();
    }

    @Override
    public void close() {
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public DataSet next() {
        return iterator.next();
    }


}
