package net.n2oapp.framework.config.reader.event;

import net.n2oapp.framework.api.metadata.aware.ReaderFactoryAware;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.reader.NamespaceReader;

/**
 * @author rgalina
 * @since 01.03.2016
 */
public interface N2oEventXmlReader<E extends N2oAction> extends NamespaceReader<E>, ReaderFactoryAware {

}
